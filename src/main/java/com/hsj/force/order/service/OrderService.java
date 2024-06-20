package com.hsj.force.order.service;

import com.hsj.force.category.repository.CategoryMapper;
import com.hsj.force.category.repository.CategoryRepository;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.common.repository.CommonRepository;
import com.hsj.force.domain.Ingredient;
import com.hsj.force.domain.IngredientHis;
import com.hsj.force.domain.Order;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.*;
import com.hsj.force.domain.entity.*;
import com.hsj.force.domain.entity.embedded.*;
import com.hsj.force.ingredient.repository.IngredientMapper;
import com.hsj.force.ingredient.repository.IngredientRepository;
import com.hsj.force.menu.repository.MenuMapper;
import com.hsj.force.menu.repository.MenuRepository;
import com.hsj.force.order.repository.OrderMapper;
import com.hsj.force.order.repository.OrderRepository;
import com.hsj.force.table.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final CommonRepository commonRepository;
    private final IngredientRepository ingredientRepository;

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
        List<CategoryListDTO> categoryList = categories.stream()
                .map(c -> new CategoryListDTO(c))
                .collect(Collectors.toList());
        List<TOrder> orders = orderRepository.findAll(storeNo, tableNo);
        List<OrderListDTO> orderList = orders.stream()
                .map(o -> new OrderListDTO(o))
                .collect(Collectors.toList());

        // TODO: JPA 적용 필요
        List<MenuListDTO> menuList = menuMapper.selectMenuList(storeNo);
        List<MenuIngredientListDTO> menuIngredientList = menuMapper.selectMenuIngredientList(storeNo);

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

        String storeName = "";
        if(commonRepository.findStoreName(storeNo).isPresent()) {
            storeName = commonRepository.findStoreName(storeNo).get();
        }
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

    public int saveOrder(TUser loginMember, OrderSaveDTO orderSaveDTO) {

        String orderNo = null;
        String lastOrderNo = null;
        String newOrderNo = null;

        String storeNo = loginMember.getStore().getStoreNo();
        String tableNo = orderSaveDTO.getTableNo();
        String menuNo = orderSaveDTO.getMenuNo();
        String userId = loginMember.getUserId();

        if(orderRepository.findOrderNo(storeNo, tableNo).isPresent()) {
            orderNo = orderRepository.findOrderNo(storeNo, tableNo).get();
        }

        if(orderNo == null) {
            if(orderRepository.findLastOrderNo(storeNo).isPresent()) {
                lastOrderNo = orderRepository.findLastOrderNo(storeNo).get();
            }
            newOrderNo = ComUtils.getNextNo(lastOrderNo, Constants.ORDER_NO_PREFIX);
        } else {
            List<String> orderStatusNoList = orderMapper.selectOrderStatusNoList(orderNo);
            boolean isOld = orderStatusNoList.stream().anyMatch(value -> value.equals("OS001"));
            if(!isOld) {
                if(orderRepository.findLastOrderNo(storeNo).isPresent()) {
                    lastOrderNo = orderRepository.findLastOrderNo(storeNo).get();
                }
                newOrderNo = ComUtils.getNextNo(lastOrderNo, Constants.ORDER_NO_PREFIX);
            } else {
                newOrderNo = orderNo;
            }
        }

        Integer quantity = null;
        if(orderRepository.findOrderQuantity(storeNo, tableNo, menuNo).isPresent()) {
            quantity = orderRepository.findOrderQuantity(storeNo, tableNo, menuNo).get();
        }

        MenuDTO menuDTO = new MenuDTO();
        if(menuRepository.findMenuV2(storeNo, menuNo).isPresent()) {
            menuDTO = menuRepository.findMenuV2(storeNo, menuNo).get();
        }

        String orderSeq = null;
        if(orderRepository.findOrderSeq(newOrderNo).isPresent()) {
            orderSeq = orderRepository.findOrderSeq(newOrderNo).get();
        }

        TOrder order = null;
        if(quantity == null) {

            String newOrderSeq = ComUtils.getNextSeq(orderSeq);

            TOrderId orderId = new TOrderId();
            orderId.setOrderNo(newOrderNo);
            orderId.setOrderSeq(newOrderSeq);

            TTableId tableId = new TTableId();
            tableId.setTableNo(tableNo);
            tableId.setStoreNo(storeNo);

            TTable table = tableRepository.findTable(tableId);
            TMenu menu = menuRepository.findMenu(menuNo);
            TOrderStatus orderStatus = orderRepository.findOrderStatus("OS001");

            order = new TOrder();
            order.setOrderId(orderId);
            order.setMenu(menu);
            order.setTable(table);
            order.setOrderStatus(orderStatus);
            order.setSalePrice(menuDTO.getSalePrice());
            order.setQuantity(1);
            order.setDiscountPrice(0);
            order.setTotalSalePrice(menuDTO.getSalePrice() * order.getQuantity());
            order.setFullPriceYn("0");
            order.setFullPerYn("0");
            order.setSelPriceYn("0");
            order.setSelPerYn("0");
            order.setServiceYn("0");
            order.setOrderDate(LocalDateTime.now());
            order.setCancelDate(null);
            order.setInsertId(userId);
            order.setInsertDate(LocalDateTime.now());
            order.setModifyId(userId);
            order.setModifyDate(LocalDateTime.now());

            orderRepository.saveOrder(order);

        } else if(orderRepository.findOrderByOrderNoAndMenuNo(orderNo, menuNo).isPresent()){

            order = orderRepository.findOrderByOrderNoAndMenuNo(orderNo, menuNo).get();
            order.setQuantity(quantity + 1);
            order.setTotalSalePrice(menuDTO.getSalePrice() * order.getQuantity());
            order.setModifyId(userId);
            order.setModifyDate(LocalDateTime.now());

        }

        TIngredient ingredient = null;
        TIngredientHis ingredientHis = null;
        //TODO: JPA 적용 필요
        List<MenuIngredientListDTO> menuIngredientList = menuMapper.selectMenuIngredientListByMenuNo(menuNo, storeNo);
        for(MenuIngredientListDTO menuIngredientDTO : menuIngredientList) {
            TIngredientId ingredientId = new TIngredientId();
            ingredientId.setIngredientNo(menuIngredientDTO.getIngredientNo());
            ingredientId.setStoreNo(storeNo);

            ingredient = ingredientRepository.findIngredient(ingredientId);
            ingredient.setQuantity(menuIngredientDTO.getStockQuantity() - menuIngredientDTO.getNeedQuantity());
            ingredient.setModifyId(userId);
            ingredient.setModifyDate(LocalDateTime.now());

            String ingredientSeq = null;
            if(ingredientRepository.findIngredientSeq(menuIngredientDTO.getIngredientNo(), storeNo).isPresent()) {
                ingredientSeq = ingredientRepository.findIngredientSeq(menuIngredientDTO.getIngredientNo(), storeNo).get();
            }

            TIngredientHisId ingredientHisId = new TIngredientHisId();
            ingredientHisId.setIngredientNo(menuIngredientDTO.getIngredientNo());
            ingredientHisId.setIngredientSeq(ComUtils.getNextSeq(ingredientSeq));
            ingredientHisId.setStoreNo(storeNo);

            TInDeReason inDeReason = ingredientRepository.findInDeReason("ID001");

            ingredientHis = new TIngredientHis();
            ingredientHis.setIngredientHisId(ingredientHisId);
            ingredientHis.setInDeQuantity(-(menuIngredientDTO.getNeedQuantity()));
            ingredientHis.setInDeReason(inDeReason);
            ingredientHis.setCommonData(new CommonData(userId, LocalDateTime.now(), userId, LocalDateTime.now()));

            ingredientRepository.saveIngredientHis(ingredientHis);

        }

        return 1;

    }

    public List<OrderListDTO> selectOrderList(String storeNo, String tableNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("storeNo", storeNo);
        paramMap.put("tableNo", tableNo);

        List<TOrder> orders = orderRepository.findOrderList(storeNo, tableNo);
        List<OrderListDTO> orderList = orders.stream()
                .map(o -> new OrderListDTO(o))
                .collect(Collectors.toList());

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

    public int completeOrder(TUser loginMember, String tableNo) {

        String storeNo = loginMember.getStore().getStoreNo();
        String userId = loginMember.getUserId();

        List<TOrder> orders = orderRepository.findOrderV3(storeNo, tableNo);
        TOrderStatus orderStatus = orderRepository.findOrderStatus("OS003");
        for(TOrder order : orders) {
            order.setOrderStatus(orderStatus);
            order.setModifyId(userId);
            order.setModifyDate(LocalDateTime.now());
        }

        return 1;
    }

    public int cancelSelection(TUser loginMember, OrderSaveDTO orderSaveDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String menuNo = orderSaveDTO.getMenuNo();
        String userId = loginMember.getUserId();
        String tableNo = orderSaveDTO.getTableNo();

        TIngredient ingredient = null;
        TIngredientHis ingredientHis = null;

        List<MenuIngredientListDTO> menuIngredientDTOList = ingredientRepository.findMenuIngredientList(storeNo, orderNo, menuNo);

        for(MenuIngredientListDTO menuIngredient : menuIngredientDTOList) {

            TIngredientId ingredientId = new TIngredientId();
            ingredientId.setIngredientNo(menuIngredient.getIngredientNo());
            ingredientId.setStoreNo(storeNo);

            ingredient = ingredientRepository.findIngredient(ingredientId);
            ingredient.setQuantity(menuIngredient.getIngredientQuantity() + menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity());
            ingredient.setModifyId(userId);
            ingredient.setModifyDate(LocalDateTime.now());

            String ingredientSeq = null;
            if(ingredientRepository.findIngredientSeq(menuIngredient.getIngredientNo(), storeNo).isPresent()) {
                ingredientSeq = ingredientRepository.findIngredientSeq(menuIngredient.getIngredientNo(), storeNo).get();
            }

            TIngredientHisId ingredientHisId = new TIngredientHisId();
            ingredientHisId.setIngredientNo(menuIngredient.getIngredientNo());
            ingredientHisId.setIngredientSeq(ComUtils.getNextSeq(ingredientSeq));
            ingredientHisId.setStoreNo(storeNo);

            TInDeReason inDeReason = ingredientRepository.findInDeReason("ID002");

            ingredientHis = new TIngredientHis();
            ingredientHis.setIngredientHisId(ingredientHisId);
            ingredientHis.setInDeQuantity(menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity());
            ingredientHis.setInDeReason(inDeReason);
            ingredientHis.setCommonData(new CommonData(userId, LocalDateTime.now(), userId, LocalDateTime.now()));

            ingredientRepository.saveIngredientHis(ingredientHis);
        }

        TOrder order = new TOrder();
        if(orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).isPresent()) {
            order = orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).get();
        }
        order.setOrderStatus(orderRepository.findOrderStatus("OS002"));
        order.setCancelDate(LocalDateTime.now());
        order.setModifyId(userId);
        order.setModifyDate(LocalDateTime.now());

        return 1;
    }

    public int cancelWhole(TUser loginMember, OrderSaveDTO orderSaveDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String userId = loginMember.getUserId();
        String orderNo = orderSaveDTO.getOrderNo();
        String tableNo = orderSaveDTO.getTableNo();

        TIngredient ingredient = null;
        TIngredientHis ingredientHis = null;
        List<MenuIngredientListDTO> menuIngredientDTOList = null;
        List<String> menuNoList = orderRepository.findMenuNoList(storeNo, orderNo);

        for(String menuNo : menuNoList) {
            menuIngredientDTOList = ingredientRepository.findMenuIngredientList(storeNo, orderNo, menuNo);
            for(MenuIngredientListDTO menuIngredient : menuIngredientDTOList) {

                TIngredientId ingredientId = new TIngredientId();
                ingredientId.setIngredientNo(menuIngredient.getIngredientNo());
                ingredientId.setStoreNo(storeNo);

                ingredient = ingredientRepository.findIngredient(ingredientId);
                ingredient.setQuantity(menuIngredient.getIngredientQuantity() + menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity());
                ingredient.setModifyId(userId);
                ingredient.setModifyDate(LocalDateTime.now());

                String ingredientSeq = null;
                if(ingredientRepository.findIngredientSeq(menuIngredient.getIngredientNo(), storeNo).isPresent()) {
                    ingredientSeq = ingredientRepository.findIngredientSeq(menuIngredient.getIngredientNo(), storeNo).get();
                }

                TIngredientHisId ingredientHisId = new TIngredientHisId();
                ingredientHisId.setIngredientNo(menuIngredient.getIngredientNo());
                ingredientHisId.setIngredientSeq(ComUtils.getNextSeq(ingredientSeq));
                ingredientHisId.setStoreNo(storeNo);

                TInDeReason inDeReason = ingredientRepository.findInDeReason("ID002");

                ingredientHis = new TIngredientHis();
                ingredientHis.setIngredientHisId(ingredientHisId);
                ingredientHis.setInDeQuantity(menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity());
                ingredientHis.setInDeReason(inDeReason);
                ingredientHis.setCommonData(new CommonData(userId, LocalDateTime.now(), userId, LocalDateTime.now()));

                ingredientRepository.saveIngredientHis(ingredientHis);

            }

        }

        List<TOrder> orders = orderRepository.findOrderV1(storeNo, orderNo, tableNo);
        TOrderStatus orderStatus = orderRepository.findOrderStatus("OS002");
        for (TOrder order : orders) {
            order.setOrderStatus(orderStatus);
            order.setCancelDate(LocalDateTime.now());
            order.setModifyId(userId);
            order.setModifyDate(LocalDateTime.now());
        }

//        TOrder order = new TOrder();
//        if(orderRepository.findOrderV1(storeNo, orderNo, tableNo).isPresent()) {
//            order = orderRepository.findOrderV1(storeNo, orderNo, tableNo).get();
//        }
//        order.setOrderStatus(orderRepository.findOrderStatus("OS002"));
//        order.setCancelDate(LocalDateTime.now());
//        order.setModifyId(userId);
//        order.setModifyDate(LocalDateTime.now());

        return 1;

    }

    public int changeQuantity(TUser loginMember, OrderSaveDTO orderSaveDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String tableNo = orderSaveDTO.getTableNo();
        String menuNo = orderSaveDTO.getMenuNo();
        String userId = loginMember.getUserId();
        String quantity = orderSaveDTO.getQuantity();

        TOrder order = new TOrder();
        if(orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).isPresent()) {
            order = orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).get();
        }
        order.setQuantity(Integer.parseInt(quantity.replaceAll(",", "")));
        order.setModifyId(userId);
        order.setModifyDate(LocalDateTime.now());

        if("1".equals(order.getServiceYn())) {
            order.setDiscountPrice(order.getSalePrice() * order.getQuantity());
            order.setTotalSalePrice(0);
        } else {
            order.setTotalSalePrice(order.getSalePrice() * order.getQuantity());
        }


//        Order order = new Order();
//        order.setMenuNo(menuNo);
//        order.setTableNo(tableNo);
//        order.setOrderNo(orderNo);
//        order.setQuantity(Integer.parseInt(orderSaveDTO.getQuantity().replaceAll(",", "")));
//        order.setStoreNo(storeNo);
//        order.setModifyId(userId);
//
//        Order orderInfo = orderMapper.selectOrderInfo(order);
//        if("1".equals(orderInfo.getServiceYn())) {
//            order.setDiscountPrice(orderInfo.getSalePrice() * order.getQuantity());
//            order.setTotalSalePrice(0);
//        } else {
//            order.setTotalSalePrice(orderInfo.getSalePrice() * order.getQuantity());
//        }
//
//        return orderMapper.updateQuantity(order);
        return 1;
    }

    public int changeSalePrice(TUser loginMember, OrderSaveDTO orderSaveDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String tableNo = orderSaveDTO.getTableNo();
        String menuNo = orderSaveDTO.getMenuNo();
        String userId = loginMember.getUserId();
        String orderNo = orderSaveDTO.getOrderNo();
        String salePrice = orderSaveDTO.getSalePrice();

        TOrder order = new TOrder();
        if(orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).isPresent()) {
            order = orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).get();
        }

        order.setSalePrice(Integer.parseInt(salePrice.replaceAll(",", "")));
        order.setModifyId(userId);
        order.setModifyDate(LocalDateTime.now());

        if("1".equals(order.getServiceYn())) {
            order.setDiscountPrice(order.getSalePrice() * order.getQuantity());
            order.setTotalSalePrice(0);
        } else {
            order.setTotalSalePrice(order.getSalePrice() * order.getQuantity());
        }

        return 1;
    }

    public int service(TUser loginMember, OrderSaveDTO orderSaveDTO) {

        String menuNo = orderSaveDTO.getMenuNo();
        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String storeNo = loginMember.getStore().getStoreNo();
        String userId = loginMember.getUserId();

        TOrder order = null;
        if(orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).isPresent()) {
            order = orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).get();
        } else {
            return 0;
        }

        order.setFullPerYn("0");
        order.setFullPriceYn("0");
        order.setSelPerYn("0");
        order.setSelPriceYn("0");
        order.setModifyId(userId);
        order.setModifyDate(LocalDateTime.now());

        if("0".equals(order.getServiceYn())) {
            order.setTotalSalePrice(0);
            order.setServiceYn("1");
            order.setDiscountPrice(order.getSalePrice() * order.getQuantity());
        } else {
            order.setTotalSalePrice(order.getSalePrice() * order.getQuantity());
            order.setServiceYn("0");
            order.setDiscountPrice(0);
        }

        return 1;
    }

    public int discountFullPer(TUser loginMember, OrderSaveDTO orderSaveDTO) {

        String orderNo = orderSaveDTO.getOrderNo();
        String tableNo = orderSaveDTO.getTableNo();
        String storeNo = loginMember.getStore().getStoreNo();
        String userId = loginMember.getUserId();

        int percent = Integer.parseInt(orderSaveDTO.getPercent().replaceAll(",", ""));

        if(percent > 100) {
            percent = 100;
        } else if(percent == 0) {
            return 1;
        }

        List<TOrder> orders = orderRepository.findOrderV1(storeNo, orderNo, tableNo);

        for(TOrder order : orders) {
            order.setDiscountPrice(order.getSalePrice() * order.getQuantity() * percent / 100);
            order.setTotalSalePrice(order.getSalePrice() * order.getQuantity() - order.getDiscountPrice());
            order.setFullPriceYn("0");
            order.setFullPerYn("1");
            order.setSelPriceYn("0");
            order.setSelPerYn("0");
            order.setServiceYn("0");
            order.setModifyId(userId);
            order.setModifyDate(LocalDateTime.now());

//            orderInfo.setOrderNo(order.getOrderNo());
//            orderInfo.setStoreNo(order.getStoreNo());
//            orderInfo.setTableNo(order.getTableNo());
//            orderInfo.setFullPerYn("1");
//            orderInfo.setDiscountPrice(orderInfo.getSalePrice() * orderInfo.getQuantity() * percent / 100);
//            orderInfo.setTotalSalePrice(orderInfo.getSalePrice() * orderInfo.getQuantity() - orderInfo.getDiscountPrice());
//            orderInfo.setModifyId(loginMember.getUserId());
//            count += orderMapper.updateDiscountFullPer(orderInfo);
        }

        return 1;
    }

    public int discountFullPrice(TUser loginMember, OrderSaveDTO orderSaveDTO) {

        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String storeNo = loginMember.getStore().getStoreNo();
        String userId = loginMember.getUserId();

        int discountSalePrice = Integer.parseInt(orderSaveDTO.getDiscountPrice().replaceAll(",", ""));

        if(discountSalePrice == 0) return 1;

        List<TOrder> orders = orderRepository.findOrderV1(storeNo, orderNo, tableNo);

        for(TOrder order : orders) {

            if(discountSalePrice > order.getSalePrice() * order.getQuantity()) {
                discountSalePrice = order.getSalePrice() * order.getQuantity();
            }
            order.setDiscountPrice(discountSalePrice);
            order.setTotalSalePrice(order.getSalePrice() * order.getQuantity() - order.getDiscountPrice());
            order.setFullPriceYn("1");
            order.setFullPerYn("0");
            order.setSelPriceYn("0");
            order.setSelPerYn("");
            order.setServiceYn("0");
            order.setModifyId(userId);
            order.setModifyDate(LocalDateTime.now());

        }

        return 1;
    }

    public int discountFullCancel(TUser loginMember, OrderSaveDTO orderSaveDTO) {

        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String storeNo = loginMember.getStore().getStoreNo();
        String userId = loginMember.getUserId();

        List<TOrder> orders = orderRepository.findOrderV1(storeNo, orderNo, tableNo);

        for(TOrder order : orders) {
            if("1".equals(order.getFullPerYn()) || "1".equals(order.getFullPriceYn())) {

                order.setDiscountPrice(0);
                order.setTotalSalePrice(order.getSalePrice() * order.getQuantity());
                order.setFullPerYn("0");
                order.setFullPriceYn("0");
                order.setSelPerYn("0");
                order.setSelPriceYn("0");
                order.setServiceYn("0");
                order.setModifyId(userId);
                order.setModifyDate(LocalDateTime.now());

            }
        }

        return 1;
    }

    public int discountSelPer(TUser loginMember, OrderSaveDTO orderSaveDTO) {

        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String menuNo = orderSaveDTO.getMenuNo();
        String storeNo = loginMember.getStore().getStoreNo();
        String userId = loginMember.getUserId();

        int percent = Integer.parseInt(orderSaveDTO.getPercent().replaceAll(",", ""));

        if(percent > 100) {
            percent = 100;
        } else if(percent == 0) {
            return 1;
        }

        TOrder order = null;
        if(orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).isPresent()) {
            order = orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).get();
        } else {
            return 0;
        }

        order.setDiscountPrice(order.getSalePrice() * order.getQuantity() * percent / 100);
        order.setTotalSalePrice(order.getSalePrice() * order.getQuantity() - order.getDiscountPrice());
        order.setFullPriceYn("0");
        order.setFullPerYn("0");
        order.setSelPriceYn("0");
        order.setSelPerYn("1");
        order.setServiceYn("0");
        order.setModifyId(userId);
        order.setModifyDate(LocalDateTime.now());

       return 1;
    }

    public int discountSelPrice(TUser loginMember, OrderSaveDTO orderSaveDTO) {

        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String storeNo = loginMember.getStore().getStoreNo();
        String menuNo = orderSaveDTO.getMenuNo();
        String userId = loginMember.getUserId();

        int discountSalePrice = Integer.parseInt(orderSaveDTO.getDiscountPrice().replaceAll(",", ""));

        if(discountSalePrice == 0) return 1;

        TOrder order = null;
        if(orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).isPresent()) {
            order = orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).get();
        } else {
            return 0;
        }

        if(discountSalePrice > order.getSalePrice() * order.getQuantity()) {
            discountSalePrice = order.getSalePrice() * order.getQuantity();
        }
        order.setDiscountPrice(discountSalePrice);
        order.setTotalSalePrice(order.getSalePrice() * order.getQuantity() - order.getDiscountPrice());
        order.setFullPriceYn("0");
        order.setFullPerYn("0");
        order.setSelPriceYn("1");
        order.setSelPerYn("0");
        order.setServiceYn("0");
        order.setModifyId(userId);
        order.setModifyDate(LocalDateTime.now());

        return 1;
    }

    public int discountSelCancel(TUser loginMember, OrderSaveDTO orderSaveDTO) {
        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String storeNo = loginMember.getStore().getStoreNo();
        String menuNo = orderSaveDTO.getMenuNo();
        String userId = loginMember.getUserId();

        TOrder order = null;
        if(orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).isPresent()) {
            order = orderRepository.findOrderV2(storeNo, orderNo, tableNo, menuNo).get();
        } else {
            return 0;
        }

        if("1".equals(order.getSelPerYn()) || "1".equals(order.getSelPriceYn())) {

            order.setDiscountPrice(0);
            order.setTotalSalePrice(order.getSalePrice() * order.getQuantity());
            order.setFullPriceYn("0");
            order.setFullPerYn("0");
            order.setSelPriceYn("0");
            order.setSelPerYn("0");
            order.setServiceYn("0");
            order.setModifyId(userId);
            order.setModifyDate(LocalDateTime.now());

        }
        return 1;
    }

    public boolean checkStock(String storeNo, String menuNo) {

        // TODO: JPA 변경 필요
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
