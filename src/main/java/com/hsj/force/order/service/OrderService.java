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
import com.hsj.force.menu.repository.MenuMapper;
import com.hsj.force.order.repository.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CommonMapper commonMapper;
    private final MenuMapper menuMapper;
    private final CategoryMapper categoryMapper;
    private final OrderMapper orderMapper;

    public OrderDTO selectOrderInfo(User loginMember, String tableNo) {

        List<Category> categoryList = categoryMapper.selectCategoryList(loginMember.getStoreNo());
        List<MenuDTO> menuList = menuMapper.selectMenuList(loginMember.getStoreNo());
        List<OrderDTO> orderList = orderMapper.selectOrderList(loginMember.getStoreNo(), tableNo);

        for(OrderDTO order : orderList) {
            order.setOrderSeqInt(Integer.parseInt(order.getOrderSeq()));
        }

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

        return orderDTO;
    }

    public OrderDTO saveOrder(OrderDTO order) {

        int duplicateMenuCheck = orderMapper.selectDuplicateMenuCheck(order);
        if(duplicateMenuCheck == 0) {
            MenuDTO menu = menuMapper.selectMenu(order.getMenuNo());
            String orderNo = orderMapper.selectOrderNo(order);
            String orderSeq = orderMapper.selectOrderSeq(orderNo);

            order.setOrderNo(orderNo);
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

            order.setOrderSeqInt(Integer.parseInt(order.getOrderSeq()));
            order.setMenuName(menu.getMenuName());
            order.setEtc("");

            orderMapper.insertOrder(order);
        } else {
            int quantity = orderMapper.selectQuantity(order);
            order.setQuantity(quantity + 1);
            orderMapper.updateOrder(order);
        }

        return order;
    }
}
