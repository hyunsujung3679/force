package com.hsj.force.order.service;

import com.hsj.force.category.repository.CategoryMapper;
import com.hsj.force.category.repository.CategoryRepository;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.Ingredient;
import com.hsj.force.domain.IngredientHis;
import com.hsj.force.domain.Order;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.*;
import com.hsj.force.domain.entity.*;
import com.hsj.force.ingredient.repository.IngredientMapper;
import com.hsj.force.menu.repository.MenuMapper;
import com.hsj.force.menu.repository.MenuRepository;
import com.hsj.force.order.repository.OrderMapper;
import com.hsj.force.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    private final CommonMapper commonMapper;
    private final MenuMapper menuMapper;
    private final CategoryMapper categoryMapper;
    private final OrderMapper orderMapper;
    private final IngredientMapper ingredientMapper;
    private final MessageSource messageSource;

    public Map<String, Object> findOrderInfo(TUser loginMember, String tableNo) {

        String storeNo = loginMember.getStore().getStoreNo();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("storeNo", storeNo);
        paramMap.put("tableNo", tableNo);

        List<TCategory> categories = categoryRepository.findCategoryByOrderForm(storeNo);
        List<TOrder> orders = orderRepository.findAll(storeNo, tableNo);

        // TODO: JPA 적용 필요
        List<MenuListDTO> menuList = menuMapper.selectMenuList(storeNo);
        List<MenuIngredientListDTO> menuIngredientList = menuMapper.selectMenuIngredientList(storeNo);

        List<CategoryListDTO> categoryList = categoryMapper.selectCategoryListByOrderForm(storeNo);
        List<OrderListDTO> orderList = orderMapper.selectOrderList(paramMap);

        int totalQuantity = 0;
        int totalDiscountPrice = 0;
        int totalSalePrice = 0;
        for(int i = 0; i < orderList.size(); i++) {
            OrderListDTO order = orderList.get(i);
            order.setNo(String.valueOf(i + 1));

            if("1".equals(order.getFullPriceYn())) {
                order.setEtc(messageSource.getMessage("word.full.price",null, null));
            } else if("1".equals(order.getFullPerYn())) {
                order.setEtc(messageSource.getMessage("word.full.per",null, null));
            } else if("1".equals(order.getSelPriceYn())) {
                order.setEtc(messageSource.getMessage("word.sel.price",null, null));
            } else if("1".equals(order.getSelPerYn())) {
                order.setEtc(messageSource.getMessage("word.sel.per",null, null));
            } else if("1".equals(order.getServiceYn())) {
                order.setEtc(messageSource.getMessage("word.service",null, null));
            } else {
                order.setEtc("");
            }

            totalQuantity += order.getQuantity();
            totalDiscountPrice += order.getDiscountPrice();
            totalSalePrice += order.getTotalSalePrice();
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalQuantity(totalQuantity);
        orderDTO.setTotalDiscountPrice(totalDiscountPrice);
        orderDTO.setTotalSalePrice(totalSalePrice);

        boolean isEnoughStock;
        for(MenuListDTO menu : menuList) {
            isEnoughStock = true;
            for(MenuIngredientListDTO menuIngredient : menuIngredientList) {
                if(menu.getMenuNo().equals(menuIngredient.getMenuNo())) {
                    if(menuIngredient.getNeedQuantity() > menuIngredient.getStockQuantity()) {
                        isEnoughStock = false;
                        break;
                    }
                }
            }
            menu.setEnoughStock(isEnoughStock);
        }

        String storeName = commonMapper.selectStoreName(storeNo);
        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        Map<String, Object> map = new HashMap<>();
        map.put("commonLayoutForm", commonLayoutForm);
        map.put("categoryList", categoryList);
        map.put("menuList", menuList);
        map.put("orderList", orderList);
        map.put("orderTotal", orderDTO);

        return map;
    }

    public int saveOrder(User loginMember, OrderSaveDTO orderSaveDTO) {

        int orderSaveResult = 0;
        int ingredientHisSaveResult = 0;
        int ingredientSaveResult = 0;
        String lastOrderNo = "";

        Order order = new Order();
        order.setMenuNo(orderSaveDTO.getMenuNo());
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setStoreNo(loginMember.getStoreNo());

        String orderNo = orderMapper.selectOrderNo(order);
        if(orderNo == null) {
            lastOrderNo = orderMapper.selectLastOrderNo(order.getStoreNo());
            order.setOrderNo(ComUtils.getNextNo(lastOrderNo, Constants.ORDER_NO_PREFIX));
        } else {
            List<String> orderStatusNoList = orderMapper.selectOrderStatusNoList(orderNo);
            boolean isOld = orderStatusNoList.stream().anyMatch(value -> value.equals("OS001"));
             if(!isOld) {
                 lastOrderNo = orderMapper.selectLastOrderNo(order.getStoreNo());
                 order.setOrderNo(ComUtils.getNextNo(lastOrderNo, Constants.ORDER_NO_PREFIX));
            } else {
                 order.setOrderNo(orderNo);
            }
        }

        order.setInsertId(loginMember.getUserId());
        order.setModifyId(loginMember.getUserId());

        Integer quantity = orderMapper.selectQuantity(order);
        MenuDTO menu = menuMapper.selectMenu(order.getMenuNo(), loginMember.getStoreNo());

        if(quantity == null) {
            String orderSeq = orderMapper.selectOrderSeq(order.getOrderNo());
            order.setOrderSeq(ComUtils.getNextSeq(orderSeq));
            order.setSalePrice(menu.getSalePrice());
            order.setQuantity(1);
            order.setTotalSalePrice(order.getSalePrice() * order.getQuantity());
            order.setFullPriceYn("0");
            order.setFullPerYn("0");
            order.setSelPriceYn("0");
            order.setSelPerYn("0");
            order.setServiceYn("0");
            order.setDiscountPrice(0);
            order.setOrderStatusNo("OS001");

            orderSaveResult = orderMapper.insertOrder(order);
        } else {
            order.setQuantity(quantity + 1);
            order.setTotalSalePrice(menu.getSalePrice() * order.getQuantity());
            orderSaveResult = orderMapper.updateOrder(order);
        }

        IngredientHis ingredientHis = null;
        Ingredient ingredient = null;
        List<MenuIngredientListDTO> menuIngredientList = menuMapper.selectMenuIngredientListByMenuNo(order);
        for(MenuIngredientListDTO menuIngredientDTO : menuIngredientList) {
            ingredient = new Ingredient();
            ingredient.setIngredientNo(menuIngredientDTO.getIngredientNo());
            ingredient.setStoreNo(loginMember.getStoreNo());
            ingredient.setQuantity(menuIngredientDTO.getStockQuantity() - menuIngredientDTO.getNeedQuantity());
            ingredient.setModifyId(loginMember.getUserId());
            ingredientSaveResult += ingredientMapper.updateIngredient(ingredient);

            ingredientHis = new IngredientHis();
            ingredientHis.setStoreNo(loginMember.getStoreNo());
            ingredientHis.setIngredientNo(menuIngredientDTO.getIngredientNo());
            ingredientHis.setIngredientSeq(ComUtils.getNextSeq(ingredientMapper.selectIngredientSeq(ingredientHis)));
            ingredientHis.setInDeQuantity(-(menuIngredientDTO.getNeedQuantity()));
            ingredientHis.setInDeReasonNo("ID001");
            ingredientHis.setInsertId(loginMember.getUserId());
            ingredientHis.setModifyId(loginMember.getUserId());
            ingredientHisSaveResult += ingredientMapper.insertIngredientHis(ingredientHis);
        }

        if(orderSaveResult == 1 && (ingredientHisSaveResult == menuIngredientList.size()) && (ingredientSaveResult == menuIngredientList.size())) {
            return 1;
        } else {
            return 0;
        }
    }

    public List<OrderListDTO> selectOrderList(String storeNo, String tableNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("storeNo", storeNo);
        paramMap.put("tableNo", tableNo);

        List<OrderListDTO> orderList = orderMapper.selectOrderList(paramMap);

        for(int i = 0; i < orderList.size(); i++) {
            OrderListDTO order = orderList.get(i);
            order.setNo(String.valueOf(i + 1));

            if("1".equals(order.getFullPriceYn())) {
                order.setEtc(messageSource.getMessage("word.full.price",null, null));
            } else if("1".equals(order.getFullPerYn())) {
                order.setEtc(messageSource.getMessage("word.full.per",null, null));
            } else if("1".equals(order.getSelPriceYn())) {
                order.setEtc(messageSource.getMessage("word.sel.price",null, null));
            } else if("1".equals(order.getSelPerYn())) {
                order.setEtc(messageSource.getMessage("word.sel.per",null, null));
            } else if("1".equals(order.getServiceYn())) {
                order.setEtc(messageSource.getMessage("word.service",null, null));
            } else {
                order.setEtc("");
            }
        }
        return orderList;
    }

    public int completeOrder(User loginMember, String tableNo) {
        Order order = new Order();
        order.setStoreNo(loginMember.getStoreNo());
        order.setTableNo(tableNo);
        order.setOrderStatusNo("OS003");
        order.setModifyId(loginMember.getUserId());
        int result = orderMapper.updateOrderStatusV1(order);
        if(result > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public int cancelSelection(User loginMember, OrderSaveDTO orderSaveDTO) {

        Ingredient ingredient = null;
        IngredientHis ingredientHis = null;
        int ingredientSaveResult = 0;
        int ingredientHisSaveResult = 0;

        List<MenuIngredientListDTO> menuIngredientDTOList = ingredientMapper.selectMenuIngredientList(loginMember.getStoreNo(), orderSaveDTO.getOrderNo(), orderSaveDTO.getMenuNo());

        for(MenuIngredientListDTO menuIngredient : menuIngredientDTOList) {
            ingredient = new Ingredient();
            ingredient.setIngredientNo(menuIngredient.getIngredientNo());
            ingredient.setStoreNo(loginMember.getStoreNo());
            ingredient.setQuantity(menuIngredient.getIngredientQuantity() + menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity());
            ingredient.setModifyId(loginMember.getUserId());
            ingredientSaveResult += ingredientMapper.updateIngredient(ingredient);

            ingredientHis = new IngredientHis();
            ingredientHis.setStoreNo(loginMember.getStoreNo());
            ingredientHis.setIngredientNo(menuIngredient.getIngredientNo());
            ingredientHis.setIngredientSeq(ComUtils.getNextSeq(ingredientMapper.selectIngredientSeq(ingredientHis)));
            ingredientHis.setInDeQuantity(menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity());
            ingredientHis.setInDeReasonNo("ID002");
            ingredientHis.setInsertId(loginMember.getUserId());
            ingredientHis.setModifyId(loginMember.getUserId());
            ingredientHisSaveResult += ingredientMapper.insertIngredientHis(ingredientHis);
        }

        Order order = new Order();
        order.setMenuNo(orderSaveDTO.getMenuNo());
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setOrderNo(orderSaveDTO.getOrderNo());
        order.setStoreNo(loginMember.getStoreNo());
        order.setOrderStatusNo("OS002");
        order.setModifyId(loginMember.getUserId());
        int orderSaveResult = orderMapper.updateOrderStatusV2(order);

        if((ingredientSaveResult == menuIngredientDTOList.size()) && (ingredientHisSaveResult == menuIngredientDTOList.size()) && orderSaveResult == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    public int cancelWhole(User loginMember, OrderSaveDTO orderSaveDTO) {

        Ingredient ingredient = null;
        IngredientHis ingredientHis = null;
        int ingredientSaveResult = 0;
        int ingredientHisSaveResult = 0;
        List<MenuIngredientListDTO> menuIngredientDTOList = null;

        List<String> menuNoList = orderMapper.selectMenuNoList(loginMember.getStoreNo(), orderSaveDTO.getOrderNo());
        for(String menuNo : menuNoList) {
            menuIngredientDTOList = ingredientMapper.selectMenuIngredientList(loginMember.getStoreNo(), orderSaveDTO.getOrderNo(), menuNo);
            ingredientSaveResult = 0;
            ingredientHisSaveResult = 0;
            for(MenuIngredientListDTO menuIngredient : menuIngredientDTOList) {
                ingredient = new Ingredient();
                ingredient.setIngredientNo(menuIngredient.getIngredientNo());
                ingredient.setStoreNo(loginMember.getStoreNo());
                ingredient.setQuantity(menuIngredient.getIngredientQuantity() + menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity());
                ingredient.setModifyId(loginMember.getUserId());
                ingredientSaveResult += ingredientMapper.updateIngredient(ingredient);

                ingredientHis = new IngredientHis();
                ingredientHis.setStoreNo(loginMember.getStoreNo());
                ingredientHis.setIngredientNo(menuIngredient.getIngredientNo());
                ingredientHis.setIngredientSeq(ComUtils.getNextSeq(ingredientMapper.selectIngredientSeq(ingredientHis)));
                ingredientHis.setInDeQuantity(menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity());
                ingredientHis.setInDeReasonNo("ID002");
                ingredientHis.setInsertId(loginMember.getUserId());
                ingredientHis.setModifyId(loginMember.getUserId());
                ingredientHisSaveResult += ingredientMapper.insertIngredientHis(ingredientHis);
            }

            if(!(ingredientSaveResult == menuIngredientDTOList.size() && ingredientHisSaveResult == menuIngredientDTOList.size())) {
                return 0;
            }

        }
        Order order = new Order();
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setOrderNo(orderSaveDTO.getOrderNo());
        order.setStoreNo(loginMember.getStoreNo());
        order.setOrderStatusNo("OS002");
        order.setModifyId(loginMember.getUserId());
        int orderSaveResult = orderMapper.updateOrderStatusV3(order);
        if(orderSaveResult > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    public int changeQuantity(User loginMember, OrderSaveDTO orderSaveDTO) {

        Order order = new Order();
        order.setMenuNo(orderSaveDTO.getMenuNo());
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setOrderNo(orderSaveDTO.getOrderNo());
        order.setQuantity(Integer.parseInt(orderSaveDTO.getQuantity().replaceAll(",", "")));
        order.setStoreNo(loginMember.getStoreNo());
        order.setModifyId(loginMember.getUserId());

        Order orderInfo = orderMapper.selectOrderInfo(order);
        if("1".equals(orderInfo.getServiceYn())) {
            order.setDiscountPrice(orderInfo.getSalePrice() * order.getQuantity());
            order.setTotalSalePrice(0);
        } else {
            order.setTotalSalePrice(orderInfo.getSalePrice() * order.getQuantity());
        }

        return orderMapper.updateQuantity(order);
    }

    public int changeSalePrice(User loginMember, OrderSaveDTO orderSaveDTO) {

        Order order = new Order();
        order.setMenuNo(orderSaveDTO.getMenuNo());
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setOrderNo(orderSaveDTO.getOrderNo());
        order.setSalePrice(Integer.valueOf(orderSaveDTO.getSalePrice().replaceAll(",", "")));
        order.setStoreNo(loginMember.getStoreNo());
        order.setModifyId(loginMember.getUserId());

        Order orderInfo = orderMapper.selectOrderInfo(order);
        if("1".equals(orderInfo.getServiceYn())) {
            order.setDiscountPrice(order.getSalePrice() * orderInfo.getQuantity());
            order.setTotalSalePrice(0);
        } else {
            order.setTotalSalePrice(order.getSalePrice() * orderInfo.getQuantity());
        }

        return orderMapper.updateSalePrice(order);
    }

    public int service(User loginMember, OrderSaveDTO orderSaveDTO) {
        Order order = new Order();
        order.setMenuNo(orderSaveDTO.getMenuNo());
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setOrderNo(orderSaveDTO.getOrderNo());
        order.setStoreNo(loginMember.getStoreNo());
        order.setModifyId(loginMember.getUserId());

        Order orderInfo = orderMapper.selectOrderInfo(order);
        if(orderInfo == null ) return 0;
        if("0".equals(orderInfo.getServiceYn())) {
            order.setTotalSalePrice(0);
            order.setServiceYn("1");
            order.setDiscountPrice(orderInfo.getSalePrice() * orderInfo.getQuantity());
        } else {
            order.setTotalSalePrice(orderInfo.getSalePrice() * orderInfo.getQuantity());
            order.setServiceYn("0");
            order.setDiscountPrice(0);
        }

        return orderMapper.updateService(order);
    }

    public int discountFullPer(User loginMember, OrderSaveDTO orderSaveDTO) {
        int count = 0;
        int percent = Integer.parseInt(orderSaveDTO.getPercent().replaceAll(",", ""));

        if(percent > 100) {
            percent = 100;
        } else if(percent == 0) {
            return 1;
        }
        Order order = new Order();
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setOrderNo(orderSaveDTO.getOrderNo());
        order.setStoreNo(loginMember.getStoreNo());

        List<Order> orderInfoList = orderMapper.selectOrderInfoList(order);
        for(Order orderInfo : orderInfoList) {
            orderInfo.setOrderNo(order.getOrderNo());
            orderInfo.setStoreNo(order.getStoreNo());
            orderInfo.setTableNo(order.getTableNo());
            orderInfo.setFullPerYn("1");
            orderInfo.setDiscountPrice(orderInfo.getSalePrice() * orderInfo.getQuantity() * percent / 100);
            orderInfo.setTotalSalePrice(orderInfo.getSalePrice() * orderInfo.getQuantity() - orderInfo.getDiscountPrice());
            orderInfo.setModifyId(loginMember.getUserId());
            count += orderMapper.updateDiscountFullPer(orderInfo);
        }

        if(orderInfoList.size() == count) {
            return 1;
        } else {
            return 0;
        }
    }

    public int discountFullPrice(User loginMember, OrderSaveDTO orderSaveDTO) {
        int count = 0;
        int discountSalePrice = Integer.parseInt(orderSaveDTO.getDiscountPrice().replaceAll(",", ""));

        if(discountSalePrice == 0) return 1;

        Order order = new Order();
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setOrderNo(orderSaveDTO.getOrderNo());
        order.setStoreNo(loginMember.getStoreNo());

        List<Order> orderInfoList = orderMapper.selectOrderInfoList(order);
        for(Order orderInfo : orderInfoList) {
            orderInfo.setOrderNo(order.getOrderNo());
            orderInfo.setStoreNo(order.getStoreNo());
            orderInfo.setTableNo(order.getTableNo());
            if(discountSalePrice > orderInfo.getSalePrice() * orderInfo.getQuantity()) {
                discountSalePrice = orderInfo.getSalePrice() * orderInfo.getQuantity();
            }
            orderInfo.setFullPriceYn("1");
            orderInfo.setDiscountPrice(discountSalePrice);
            orderInfo.setTotalSalePrice(orderInfo.getSalePrice() * orderInfo.getQuantity() - orderInfo.getDiscountPrice());
            orderInfo.setModifyId(loginMember.getUserId());
            count += orderMapper.updateDiscountFullPrice(orderInfo);
        }

        if(orderInfoList.size() == count) {
            return 1;
        } else {
            return 0;
        }
    }

    public int discountFullCancel(User loginMember, OrderSaveDTO orderSaveDTO) {
        int count = 0;

        Order order = new Order();
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setOrderNo(orderSaveDTO.getOrderNo());
        order.setStoreNo(loginMember.getStoreNo());

        List<Order> orderInfoList = orderMapper.selectOrderInfoList(order);
        for(Order orderInfo : orderInfoList) {
            if("1".equals(orderInfo.getFullPerYn()) || "1".equals(orderInfo.getFullPriceYn())) {
                orderInfo.setOrderNo(order.getOrderNo());
                orderInfo.setStoreNo(order.getStoreNo());
                orderInfo.setTableNo(order.getTableNo());
                orderInfo.setTotalSalePrice(orderInfo.getSalePrice() * orderInfo.getQuantity());
                orderInfo.setModifyId(loginMember.getUserId());
                count += orderMapper.updateDiscountCancel(orderInfo);
            }
        }
        if(count > 0) {
            count = 1;
        } else {
            count = 0;
        }
        return count;
    }

    public int discountSelPer(User loginMember, OrderSaveDTO orderSaveDTO) {
        int percent = Integer.parseInt(orderSaveDTO.getPercent().replaceAll(",", ""));

        if(percent > 100) {
            percent = 100;
        } else if(percent == 0) {
            return 1;
        }

        Order order = new Order();
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setOrderNo(orderSaveDTO.getOrderNo());
        order.setStoreNo(loginMember.getStoreNo());
        order.setMenuNo(orderSaveDTO.getMenuNo());

        Order orderInfo = orderMapper.selectOrderInfo(order);
        if(orderInfo == null)  return 0;
        orderInfo.setOrderNo(order.getOrderNo());
        orderInfo.setMenuNo(order.getMenuNo());
        orderInfo.setStoreNo(order.getStoreNo());
        orderInfo.setTableNo(order.getTableNo());
        orderInfo.setSelPerYn("1");
        orderInfo.setDiscountPrice(orderInfo.getSalePrice() * orderInfo.getQuantity() * percent / 100);
        orderInfo.setTotalSalePrice(orderInfo.getSalePrice() * orderInfo.getQuantity() - orderInfo.getDiscountPrice());
        orderInfo.setModifyId(loginMember.getUserId());
        return orderMapper.updateDiscountSelPer(orderInfo);
    }

    public int discountSelPrice(User loginMember, OrderSaveDTO orderSaveDTO) {
        int discountSalePrice = Integer.parseInt(orderSaveDTO.getDiscountPrice().replaceAll(",", ""));

        if(discountSalePrice == 0) return 1;

        Order order = new Order();
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setOrderNo(orderSaveDTO.getOrderNo());
        order.setStoreNo(loginMember.getStoreNo());
        order.setMenuNo(orderSaveDTO.getMenuNo());

        Order orderInfo = orderMapper.selectOrderInfo(order);
        orderInfo.setOrderNo(order.getOrderNo());
        orderInfo.setMenuNo(order.getMenuNo());
        orderInfo.setStoreNo(order.getStoreNo());
        orderInfo.setTableNo(order.getTableNo());
        if(discountSalePrice > orderInfo.getSalePrice() * orderInfo.getQuantity()) {
            discountSalePrice = orderInfo.getSalePrice() * orderInfo.getQuantity();
        }
        orderInfo.setSelPriceYn("1");
        orderInfo.setDiscountPrice(discountSalePrice);
        orderInfo.setTotalSalePrice(orderInfo.getSalePrice() * orderInfo.getQuantity() - orderInfo.getDiscountPrice());
        orderInfo.setModifyId(loginMember.getUserId());
        return orderMapper.updateDiscountSelPrice(orderInfo);
    }

    public int discountSelCancel(User loginMember, OrderSaveDTO orderSaveDTO) {
        int count = 0;

        Order order = new Order();
        order.setTableNo(orderSaveDTO.getTableNo());
        order.setOrderNo(orderSaveDTO.getOrderNo());
        order.setStoreNo(loginMember.getStoreNo());
        order.setMenuNo(orderSaveDTO.getMenuNo());

        Order orderInfo = orderMapper.selectOrderInfo(order);
        if("1".equals(orderInfo.getSelPerYn()) || "1".equals(orderInfo.getSelPriceYn())) {
            orderInfo.setOrderNo(order.getOrderNo());
            orderInfo.setMenuNo(order.getMenuNo());
            orderInfo.setStoreNo(order.getStoreNo());
            orderInfo.setTableNo(order.getTableNo());
            orderInfo.setTotalSalePrice(orderInfo.getSalePrice() * orderInfo.getQuantity());
            orderInfo.setModifyId(loginMember.getUserId());
            count = orderMapper.updateDiscountCancel(orderInfo);
        }
        return count;
    }

    public boolean checkStock(String storeNo, String menuNo) {

        List<MenuIngredientListDTO> menuIngredientList = menuMapper.selectMenuIngredientList(storeNo);

        boolean isEnoughStock = true;
        for(MenuIngredientListDTO menuIngredient : menuIngredientList) {
            if(menuNo.equals(menuIngredient.getMenuNo())) {
                if(menuIngredient.getNeedQuantity() > menuIngredient.getStockQuantity()) {
                    isEnoughStock = false;
                    break;
                }
            }
        }
        return isEnoughStock;
    }
}
