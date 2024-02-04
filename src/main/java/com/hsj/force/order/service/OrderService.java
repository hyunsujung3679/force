package com.hsj.force.order.service;

import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.Category;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.domain.dto.OrderDTO;
import com.hsj.force.order.repository.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CommonMapper commonMapper;

    private final OrderMapper orderMapper;

    public OrderDTO selectOrderInfo(User loginMember) {

        List<Category> categoryList = orderMapper.selectCategoryList(loginMember.getStoreNo());
        List<MenuDTO> menuList = orderMapper.selectMenuList(loginMember.getStoreNo());

        String storeName = commonMapper.selectStoreName(loginMember.getStoreNo());
        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCategoryList(categoryList);
        orderDTO.setMenuList(menuList);
        orderDTO.setCommonLayoutForm(commonLayoutForm);

        return orderDTO;
    }
}
