package com.hsj.force.order.service;

import com.hsj.force.category.repository.CategoryJpaRepository;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.domain.dto.*;
import com.hsj.force.domain.entity.*;
import com.hsj.force.ingredient.repository.InDeReasonJpaRepository;
import com.hsj.force.ingredient.repository.IngredientHisJpaRepository;
import com.hsj.force.ingredient.repository.IngredientJpaRepository;
import com.hsj.force.menu.repository.MenuIngredientJpaRepository;
import com.hsj.force.menu.repository.MenuJpaRepository;
import com.hsj.force.menu.repository.MenuMapper;
import com.hsj.force.menu.repository.MenuPriceJpaRepository;
import com.hsj.force.order.repository.OrderJpaRepository;
import com.hsj.force.order.repository.OrderStatusJpaRepository;
import com.hsj.force.table.repository.TableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CategoryJpaRepository categoryJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final MenuJpaRepository menuJpaRepository;
    private final TableJpaRepository tableJpaRepository;
    private final OrderStatusJpaRepository orderStatusJpaRepository;
    private final IngredientJpaRepository ingredientJpaRepository;
    private final InDeReasonJpaRepository inDeReasonJpaRepository;
    private final IngredientHisJpaRepository ingredientHisJpaRepository;
    private final MenuIngredientJpaRepository menuIngredientJpaRepository;
    private final MenuPriceJpaRepository menuPriceJpaRepository;

    private final MenuMapper menuMapper;
    private final MessageSource messageSource;

    public Map<String, Object> selectOrderInfo(TUser loginMember, String tableNo) {

        List<TCategory> categories = categoryJpaRepository.findAllByUseYnOrderByPriority("1");
        List<CategoryListDTO> categoryList = categories.stream()
                .map(c -> new CategoryListDTO(c))
                .collect(Collectors.toList());
        List<TOrder> orders = orderJpaRepository.findAllByOrderStatusAndTable(new TOrderStatus("OS001"), new TTable(tableNo));
        List<OrderListDTO> orderList = orders.stream()
                .map(o -> new OrderListDTO(o))
                .collect(Collectors.toList());

        List<MenuListDTO> menuList = menuMapper.selectMenuListV1();
        List<MenuIngredientListDTO> menuIngredientList = menuIngredientJpaRepository.findMenuIngredientListDTOV1(null);

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

        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(messageSource.getMessage("word.store.name",null, null));
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

    public int insertOrder(OrderSaveDTO orderSaveDTO) {

        String tableNo = orderSaveDTO.getTableNo();
        String menuNo = orderSaveDTO.getMenuNo();

        TOrder orderNoOrder = orderJpaRepository.findFirstByTableOrderByOrderNoDesc(new TTable(tableNo));
        String orderNo = orderNoOrder == null ? null : orderNoOrder.getOrderNo();

        String newOrderNo = null;
        TOrder newOrderNoOrder = null;
        if(orderNo == null) {
            newOrderNoOrder  = orderJpaRepository.findFirstByOrderByOrderNoDesc();
            newOrderNo = ComUtils.getNextNo(newOrderNoOrder == null? null : newOrderNoOrder.getOrderNo(), Constants.ORDER_NO_PREFIX);
        } else {
            List<String> orderStatusNoList = orderJpaRepository.findAllByOrderNo(orderNo).stream().
                    map(TOrder::getOrderStatus).toList().stream().
                    map(TOrderStatus::getOrderStatusNo).toList();
            boolean isOld = orderStatusNoList.stream().anyMatch(value -> value.equals("OS001"));
            if(!isOld) {
                newOrderNoOrder  = orderJpaRepository.findFirstByOrderByOrderNoDesc();
                newOrderNo = ComUtils.getNextNo(newOrderNoOrder == null? null : newOrderNoOrder.getOrderNo(), Constants.ORDER_NO_PREFIX);
            } else {
                newOrderNo = orderNo;
            }
        }

        TOrder quantityOrder = orderJpaRepository.findOneCustom(null, tableNo, menuNo, "OS001");
        Integer quantity = quantityOrder == null? null : quantityOrder.getQuantity();
        MenuPriceDTO menuPriceDTO = menuPriceJpaRepository.findMenuPriceDTO(menuNo);
        TOrder orderSeqOrder = orderJpaRepository.findFirstByOrderNoOrderByOrderSeqDesc(newOrderNo);
        String orderSeq = orderSeqOrder == null? null : orderSeqOrder.getOrderSeq();

        TOrder order = null;
        if(quantity == null) {

            TTable table = null;;
            Optional<TTable> optionalTable = tableJpaRepository.findById(tableNo);
            if(optionalTable.isPresent()) {
                table = optionalTable.get();
            }

            TMenu menu = null;
            Optional<TMenu> optionalMenu = menuJpaRepository.findById(menuNo);
            if(optionalMenu.isPresent()) {
                menu = optionalMenu.get();
            }

            TOrderStatus orderStatus = null;
            Optional<TOrderStatus> optionalOrderStatus = orderStatusJpaRepository.findById("OS001");
            if(optionalOrderStatus.isPresent()) {
                orderStatus = optionalOrderStatus.get();
            }

            order = new TOrder(
                    newOrderNo,
                    ComUtils.getNextSeq(orderSeq),
                    table,
                    menu,
                    orderStatus,
                    menuPriceDTO.getSalePrice(),
                    1,
                    0,
                    menuPriceDTO.getSalePrice(),
                    "0",
                    "0",
                    "0",
                    "0",
                    "0",
                    LocalDateTime.now(),
                    null
            );

            orderJpaRepository.save(order);

        } else {
            order = orderJpaRepository.findOneCustom(orderNo, null, menuNo, "OS001");
            order.setQuantity(quantity + 1);
            order.setTotalSalePrice(menuPriceDTO.getSalePrice() * order.getQuantity());
        }

        TIngredient ingredient = null;
        List<MenuIngredientListDTO> menuIngredientList = menuIngredientJpaRepository.findMenuIngredientListDTOV1(menuNo);
        for(MenuIngredientListDTO menuIngredientDTO : menuIngredientList) {
            Optional<TIngredient> optionalIngredient = ingredientJpaRepository.findById(menuIngredientDTO.getIngredientNo());
            if(optionalIngredient.isPresent()) {
                ingredient = optionalIngredient.get();
            }
            ingredient.setQuantity(menuIngredientDTO.getStockQuantity() - menuIngredientDTO.getNeedQuantity());

            TIngredientHis ingredientHis = ingredientHisJpaRepository.findFirstByIngredientNoOrderByIngredientSeqDesc(menuIngredientDTO.getIngredientNo());

            TInDeReason inDeReason = null;
            Optional<TInDeReason> optionalInDeReason = inDeReasonJpaRepository.findById("ID001");
            if(optionalInDeReason.isPresent()) {
                inDeReason = optionalInDeReason.get();
            }

            ingredientHisJpaRepository.save(new TIngredientHis(menuIngredientDTO.getIngredientNo(), ComUtils.getNextSeq(ingredientHis == null? "" : ingredientHis.getIngredientSeq()), -(menuIngredientDTO.getNeedQuantity()), inDeReason));

        }

        return 1;

    }

    public List<OrderListDTO> selectOrderList(String tableNo) {

        List<TOrder> orders = orderJpaRepository.findAllByOrderStatusAndTable(new TOrderStatus("OS001"), new TTable(tableNo));
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

    public int completeOrder(String tableNo) {

        List<TOrder> orders = orderJpaRepository.findAllByOrderStatusAndTable(new TOrderStatus("OS001"), new TTable(tableNo));

        TOrderStatus orderStatus = null;
        Optional<TOrderStatus> optionalOrderStatus = orderStatusJpaRepository.findById("OS003");
        if(optionalOrderStatus.isPresent()) {
            orderStatus = optionalOrderStatus.get();
        }
        for(TOrder order : orders) {
            order.setOrderStatus(orderStatus);

        }

        return 1;
    }

    public int cancelSelection(OrderSaveDTO orderSaveDTO) {

        String orderNo = orderSaveDTO.getOrderNo();
        String menuNo = orderSaveDTO.getMenuNo();
        String tableNo = orderSaveDTO.getTableNo();
        TIngredient ingredient = null;

        List<MenuIngredientListDTO> menuIngredientDTOList = menuIngredientJpaRepository.findMenuIngredientListDTOV2(menuNo, orderNo, "OS001");
        for(MenuIngredientListDTO menuIngredient : menuIngredientDTOList) {

            Optional<TIngredient> optionalIngredient = ingredientJpaRepository.findById(menuIngredient.getIngredientNo());
            if(optionalIngredient.isPresent()) {
                ingredient = optionalIngredient.get();
            }
            ingredient.setQuantity(menuIngredient.getIngredientQuantity() + menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity());

            TIngredientHis ingredientHis = ingredientHisJpaRepository.findFirstByIngredientNoOrderByIngredientSeqDesc(menuIngredient.getIngredientNo());

            TInDeReason inDeReason = null;
            Optional<TInDeReason> optionalInDeReason = inDeReasonJpaRepository.findById("ID002");
            if(optionalInDeReason.isPresent()) {
                inDeReason = optionalInDeReason.get();
            }

            ingredientHisJpaRepository.save(
                    new TIngredientHis(
                            menuIngredient.getIngredientNo(),
                            ComUtils.getNextSeq(ingredientHis == null? "" : ingredientHis.getIngredientSeq()),
                            menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity(),
                            inDeReason));
        }

        TOrderStatus orderStatus = null;
        Optional<TOrderStatus> optionalOrderStatus = orderStatusJpaRepository.findById("OS002");
        if(optionalOrderStatus.isPresent()) {
            orderStatus = optionalOrderStatus.get();
        }
        TOrder order = orderJpaRepository.findOneCustom(orderNo, tableNo, menuNo, "OS001");
        order.setOrderStatus(orderStatus);
        order.setCancelDate(LocalDateTime.now());

        return 1;
    }

    public int cancelWhole(OrderSaveDTO orderSaveDTO) {

        String orderNo = orderSaveDTO.getOrderNo();
        String tableNo = orderSaveDTO.getTableNo();

        TIngredient ingredient = null;
        List<MenuIngredientListDTO> menuIngredientDTOList = null;
        List<String> menuNos = orderJpaRepository.findAllByOrderNoAndOrderStatus(orderNo, new TOrderStatus("OS001"))
                .stream().map(TOrder::getMenu).toList()
                .stream().map(TMenu::getMenuNo).toList();
        for(String menuNo : menuNos) {
            menuIngredientDTOList = menuIngredientJpaRepository.findMenuIngredientListDTOV2(menuNo, orderNo, "OS001");
            for(MenuIngredientListDTO menuIngredient : menuIngredientDTOList) {

                Optional<TIngredient> optionalIngredient = ingredientJpaRepository.findById(menuIngredient.getIngredientNo());
                if(optionalIngredient.isPresent()) {
                    ingredient = optionalIngredient.get();
                } else {
                    return 0;
                }
                ingredient.setQuantity(menuIngredient.getIngredientQuantity() + menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity());

                TIngredientHis ingredientHis = ingredientHisJpaRepository.findFirstByIngredientNoOrderByIngredientSeqDesc(menuIngredient.getIngredientNo());

                TInDeReason inDeReason = null;
                Optional<TInDeReason> optionalInDeReason = inDeReasonJpaRepository.findById("ID002");
                if(optionalInDeReason.isPresent()) {
                    inDeReason = optionalInDeReason.get();
                }

                ingredientHisJpaRepository.save(
                        new TIngredientHis(
                                menuIngredient.getIngredientNo(),
                                ComUtils.getNextSeq(ingredientHis == null? "" : ingredientHis.getIngredientSeq()),
                                menuIngredient.getNeedQuantity() * menuIngredient.getMenuQuantity(),
                                inDeReason));
            }

        }

        List<TOrder> orders =  orderJpaRepository.findAllCustomV1(orderNo, tableNo, "OS001");

        TOrderStatus orderStatus = null;
        Optional<TOrderStatus> optionalOrderStatus = orderStatusJpaRepository.findById("OS002");
        if(optionalOrderStatus.isPresent()) {
            orderStatus = optionalOrderStatus.get();
        }

        for (TOrder order : orders) {
            order.setOrderStatus(orderStatus);
            order.setCancelDate(LocalDateTime.now());
        }

        return 1;

    }

    public int changeQuantity(OrderSaveDTO orderSaveDTO) {

        String orderNo = orderSaveDTO.getOrderNo();
        String tableNo = orderSaveDTO.getTableNo();
        String menuNo = orderSaveDTO.getMenuNo();
        String quantity = orderSaveDTO.getQuantity();

        TOrder order = orderJpaRepository.findOneCustom(orderNo, tableNo, menuNo, "OS001");
        order.setQuantity(Integer.parseInt(quantity.replaceAll(",", "")));

        if("1".equals(order.getServiceYn())) {
            order.setDiscountPrice(order.getSalePrice() * order.getQuantity());
            order.setTotalSalePrice(0);
        } else {
            order.setTotalSalePrice(order.getSalePrice() * order.getQuantity());
        }

        return 1;
    }

    public int changeSalePrice(OrderSaveDTO orderSaveDTO) {

        String tableNo = orderSaveDTO.getTableNo();
        String menuNo = orderSaveDTO.getMenuNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String salePrice = orderSaveDTO.getSalePrice();

        TOrder order = orderJpaRepository.findOneCustom(orderNo, tableNo, menuNo, "OS001");
        order.setSalePrice(Integer.parseInt(salePrice.replaceAll(",", "")));

        if("1".equals(order.getServiceYn())) {
            order.setDiscountPrice(order.getSalePrice() * order.getQuantity());
            order.setTotalSalePrice(0);
        } else {
            order.setTotalSalePrice(order.getSalePrice() * order.getQuantity());
        }

        return 1;
    }

    public int service(OrderSaveDTO orderSaveDTO) {

        String menuNo = orderSaveDTO.getMenuNo();
        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();

        TOrder order = orderJpaRepository.findOneCustom(orderNo, tableNo, menuNo, "OS001");
        order.setFullPerYn("0");
        order.setFullPriceYn("0");
        order.setSelPerYn("0");
        order.setSelPriceYn("0");

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

    public int discountFullPer(OrderSaveDTO orderSaveDTO) {

        String orderNo = orderSaveDTO.getOrderNo();
        String tableNo = orderSaveDTO.getTableNo();

        int percent = Integer.parseInt(orderSaveDTO.getPercent().replaceAll(",", ""));

        if(percent > 100) {
            percent = 100;
        } else if(percent == 0) {
            return 1;
        }

        List<TOrder> orders = orderJpaRepository.findAllCustomV1(orderNo, tableNo, "OS001");
        for(TOrder order : orders) {
            order.setDiscountPrice(order.getSalePrice() * order.getQuantity() * percent / 100);
            order.setTotalSalePrice(order.getSalePrice() * order.getQuantity() - order.getDiscountPrice());
            order.setFullPriceYn("0");
            order.setFullPerYn("1");
            order.setSelPriceYn("0");
            order.setSelPerYn("0");
            order.setServiceYn("0");

        }

        return 1;
    }

    public int discountFullPrice(OrderSaveDTO orderSaveDTO) {

        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();

        int discountSalePrice = Integer.parseInt(orderSaveDTO.getDiscountPrice().replaceAll(",", ""));

        if(discountSalePrice == 0) return 1;

        List<TOrder> orders = orderJpaRepository.findAllCustomV1(orderNo, tableNo, "OS001");
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

        }

        return 1;
    }

    public int discountFullCancel(OrderSaveDTO orderSaveDTO) {

        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();

        List<TOrder> orders = orderJpaRepository.findAllCustomV1(orderNo, tableNo, "OS001");
        for(TOrder order : orders) {
            if("1".equals(order.getFullPerYn()) || "1".equals(order.getFullPriceYn())) {

                order.setDiscountPrice(0);
                order.setTotalSalePrice(order.getSalePrice() * order.getQuantity());
                order.setFullPerYn("0");
                order.setFullPriceYn("0");
                order.setSelPerYn("0");
                order.setSelPriceYn("0");
                order.setServiceYn("0");

            }
        }

        return 1;
    }

    public int discountSelPer(OrderSaveDTO orderSaveDTO) {

        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String menuNo = orderSaveDTO.getMenuNo();

        int percent = Integer.parseInt(orderSaveDTO.getPercent().replaceAll(",", ""));

        if(percent > 100) {
            percent = 100;
        } else if(percent == 0) {
            return 1;
        }

        TOrder order = orderJpaRepository.findOneCustom(orderNo, tableNo, menuNo, "OS001");
        order.setDiscountPrice(order.getSalePrice() * order.getQuantity() * percent / 100);
        order.setTotalSalePrice(order.getSalePrice() * order.getQuantity() - order.getDiscountPrice());
        order.setFullPriceYn("0");
        order.setFullPerYn("0");
        order.setSelPriceYn("0");
        order.setSelPerYn("1");
        order.setServiceYn("0");

       return 1;
    }

    public int discountSelPrice(OrderSaveDTO orderSaveDTO) {

        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String menuNo = orderSaveDTO.getMenuNo();

        int discountSalePrice = Integer.parseInt(orderSaveDTO.getDiscountPrice().replaceAll(",", ""));

        if(discountSalePrice == 0) return 1;

        TOrder order = orderJpaRepository.findOneCustom(orderNo, tableNo, menuNo, "OS001");

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

        return 1;
    }

    public int discountSelCancel(OrderSaveDTO orderSaveDTO) {
        String tableNo = orderSaveDTO.getTableNo();
        String orderNo = orderSaveDTO.getOrderNo();
        String menuNo = orderSaveDTO.getMenuNo();

        TOrder order = orderJpaRepository.findOneCustom(orderNo, tableNo, menuNo, "OS001");

        if("1".equals(order.getSelPerYn()) || "1".equals(order.getSelPriceYn())) {

            order.setDiscountPrice(0);
            order.setTotalSalePrice(order.getSalePrice() * order.getQuantity());
            order.setFullPriceYn("0");
            order.setFullPerYn("0");
            order.setSelPriceYn("0");
            order.setSelPerYn("0");
            order.setServiceYn("0");

        }
        return 1;
    }

    public boolean checkStock(String menuNo) {

        List<MenuIngredientListDTO> menuIngredientList = menuIngredientJpaRepository.findMenuIngredientListDTOV1(null);

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
