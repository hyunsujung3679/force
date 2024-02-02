package com.hsj.force.order.service;

import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.OrderDTO;
import com.hsj.force.table.repository.TableMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final TableMapper tableMapper;

    public OrderDTO selectOrderInfo(User loginMember) {

        String storeName = tableMapper.selectStoreName(loginMember.getStoreNo());

        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCommonLayoutForm(commonLayoutForm);

        return orderDTO;
    }
}
