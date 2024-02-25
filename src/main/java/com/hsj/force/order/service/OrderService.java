package com.hsj.force.order.service;

import com.hsj.force.category.repository.CategoryMapper;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.Category;
import com.hsj.force.domain.Order;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.domain.dto.OrderDTO;
import com.hsj.force.domain.dto.OrderTotalDTO;
import com.hsj.force.menu.repository.MenuMapper;
import com.hsj.force.order.repository.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CommonMapper commonMapper;
    private final MenuMapper menuMapper;
    private final CategoryMapper categoryMapper;
    private final OrderMapper orderMapper;
    private final MessageSource messageSource;

    public OrderDTO selectOrderInfo(User loginMember, String tableNo) {

        List<Category> categoryList = categoryMapper.selectCategoryList(loginMember.getStoreNo());
        List<MenuDTO> menuList = menuMapper.selectMenuList(loginMember.getStoreNo());
        List<OrderDTO> orderList = orderMapper.selectOrderList(loginMember.getStoreNo(), tableNo);

        int totalQuantity = 0;
        int totalDiscountPrice = 0;
        int totalSalePrice = 0;
        for(int i = 0; i < orderList.size(); i++) {
            OrderDTO order = orderList.get(i);
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

        OrderTotalDTO orderTotal = new OrderTotalDTO();
        orderTotal.setTotalQuantity(totalQuantity);
        orderTotal.setTotalDiscountPrice(totalDiscountPrice);
        orderTotal.setTotalSalePrice(totalSalePrice);

        String storeName = commonMapper.selectStoreName(loginMember.getStoreNo());
        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCategoryList(categoryList);
        orderDTO.setMenuList(menuList);
        orderDTO.setOrderList(orderList);
        orderDTO.setCommonLayoutForm(commonLayoutForm);
        orderDTO.setOrderTotal(orderTotal);

        return orderDTO;
    }

    public int saveOrder(User loginMember, OrderDTO order) {

        int result = 0;
        String lastOrderNo = "";

        order.setStoreNo(loginMember.getStoreNo());
        String orderNo = orderMapper.selectOrderNo(order);
        if(orderNo == null) {
            lastOrderNo = orderMapper.selectLastOrderNo();
            order.setOrderNo(ComUtils.getNextNo(lastOrderNo, Constants.ORDER_NO_PREFIX));
        } else {
            List<String> orderStatusNoList = orderMapper.selectOrderStatusNoList(orderNo);
            boolean isOld = orderStatusNoList.stream().anyMatch(value -> value.equals("OS001"));
            if(!isOld) {
                lastOrderNo = orderMapper.selectLastOrderNo();
                order.setOrderNo(ComUtils.getNextNo(lastOrderNo, Constants.ORDER_NO_PREFIX));
            } else {
                order.setOrderNo(orderNo);
            }
        }
        order.setInsertId(loginMember.getUserId());
        order.setModifyId(loginMember.getUserId());

        Integer quantity = orderMapper.selectQuantity(order);
        MenuDTO menu = menuMapper.selectMenu(order.getMenuNo());

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

            result = orderMapper.insertOrder(order);
        } else {
            order.setQuantity(quantity + 1);
            order.setTotalSalePrice(menu.getSalePrice() * order.getQuantity());
            result = orderMapper.updateOrder(order);
        }

        return result;
    }

    public List<OrderDTO> selectOrderList(String storeNo, String tableNo) {
        List<OrderDTO> orderList = orderMapper.selectOrderList(storeNo, tableNo);

        for(int i = 0; i < orderList.size(); i++) {
            OrderDTO order = orderList.get(i);
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
        OrderDTO order = new OrderDTO();
        order.setStoreNo(loginMember.getStoreNo());
        order.setTableNo(tableNo);
        order.setOrderStatusNo("OS003");
        order.setModifyId(loginMember.getUserId());
        return orderMapper.updateOrderStatusV1(order);
    }

    public int cancelSelection(User loginMember, OrderDTO order) {
        order.setStoreNo(loginMember.getStoreNo());
        order.setOrderStatusNo("OS002");
        order.setModifyId(loginMember.getUserId());
        return orderMapper.updateOrderStatusV2(order);
    }

    public int cancelWhole(User loginMember, OrderDTO order) {
        order.setStoreNo(loginMember.getStoreNo());
        order.setOrderStatusNo("OS002");
        order.setModifyId(loginMember.getUserId());
        return orderMapper.updateOrderStatusV3(order);
    }

    public int changeQuantity(User loginMember, OrderDTO order) {
        order.setStoreNo(loginMember.getStoreNo());
        order.setQuantity(Integer.parseInt(order.getQuantityStr().replaceAll(",", "")));

        OrderDTO orderInfo = orderMapper.selectOrderInfo(order);
        if("1".equals(orderInfo.getServiceYn())) {
            order.setDiscountPrice(orderInfo.getSalePrice() * order.getQuantity());
            order.setTotalSalePrice(0);
        } else {
            order.setTotalSalePrice(orderInfo.getSalePrice() * order.getQuantity());
        }
        order.setModifyId(loginMember.getUserId());
        return orderMapper.updateQuantity(order);
    }

    public int changeSalePrice(User loginMember, OrderDTO order) {
        order.setStoreNo(loginMember.getStoreNo());
        order.setSalePrice(Integer.parseInt(order.getSalePriceStr().replaceAll(",", "")));

        OrderDTO orderInfo = orderMapper.selectOrderInfo(order);
        if("1".equals(orderInfo.getServiceYn())) {
            order.setDiscountPrice(order.getSalePrice() * orderInfo.getQuantity());
            order.setTotalSalePrice(0);
        } else {
            order.setTotalSalePrice(order.getSalePrice() * orderInfo.getQuantity());
        }
        order.setModifyId(loginMember.getUserId());
        return orderMapper.updateSalePrice(order);
    }

    public int service(User loginMember, OrderDTO order) {
        order.setStoreNo(loginMember.getStoreNo());

        OrderDTO orderInfo = orderMapper.selectOrderInfo(order);
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
        order.setModifyId(loginMember.getUserId());
        return orderMapper.updateService(order);
    }

    public int discountFullPer(User loginMember, OrderDTO order) {
        int count = 0;
        int percent = 0;
        try {
            percent = Integer.parseInt(order.getPercentStr().replaceAll(",", ""));
        } catch (NumberFormatException e) {
            return 1;
        }
        if(percent > 100) {
            percent = 100;
        } else if(percent == 0) {
            return 1;
        }
        order.setStoreNo(loginMember.getStoreNo());

        List<OrderDTO> orderInfoList = orderMapper.selectOrderInfoList(order);
        for(OrderDTO orderInfo : orderInfoList) {
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

    public int discountFullPrice(User loginMember, OrderDTO order) {
        int count = 0;
        int discountSalePrice = 0;
        try {
            discountSalePrice = Integer.parseInt(order.getDiscountPriceStr().replaceAll(",", ""));
        } catch (NumberFormatException e) {
            return 1;
        }
        if(discountSalePrice == 0) return 1;
        order.setStoreNo(loginMember.getStoreNo());

        List<OrderDTO> orderInfoList = orderMapper.selectOrderInfoList(order);
        for(OrderDTO orderInfo : orderInfoList) {
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

    public int discountFullCancel(User loginMember, OrderDTO order) {
        int count = 0;
        order.setStoreNo(loginMember.getStoreNo());

        List<OrderDTO> orderInfoList = orderMapper.selectOrderInfoList(order);
        for(OrderDTO orderInfo : orderInfoList) {
            if("1".equals(orderInfo.getFullPerYn()) || "1".equals(orderInfo.getFullPriceYn())) {
                orderInfo.setOrderNo(order.getOrderNo());
                orderInfo.setStoreNo(order.getStoreNo());
                orderInfo.setTableNo(order.getTableNo());
                orderInfo.setTotalSalePrice(orderInfo.getSalePrice() * orderInfo.getQuantity());
                orderInfo.setModifyId(loginMember.getUserId());
                count += orderMapper.updateDiscountCancel(orderInfo);
            }
        }
        return count;
    }

    public int discountSelPer(User loginMember, OrderDTO order) {
        int percent = 0;
        try {
            percent = Integer.parseInt(order.getPercentStr().replaceAll(",", ""));
        } catch (NumberFormatException e) {
            return 1;
        }
        if(percent > 100) {
            percent = 100;
        } else if(percent == 0) {
            return 1;
        }
        order.setStoreNo(loginMember.getStoreNo());
        OrderDTO orderInfo = orderMapper.selectOrderInfo(order);
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

    public int discountSelPrice(User loginMember, OrderDTO order) {
        int discountSalePrice = 0;
        try {
            discountSalePrice = Integer.parseInt(order.getDiscountPriceStr().replaceAll(",", ""));
        } catch (NumberFormatException e) {
            return 1;
        }
        if(discountSalePrice == 0) return 1;
        order.setStoreNo(loginMember.getStoreNo());

        OrderDTO orderInfo = orderMapper.selectOrderInfo(order);
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

    public int discountSelCancel(User loginMember, OrderDTO order) {
        int count = 0;
        order.setStoreNo(loginMember.getStoreNo());

        OrderDTO orderInfo = orderMapper.selectOrderInfo(order);
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
}
