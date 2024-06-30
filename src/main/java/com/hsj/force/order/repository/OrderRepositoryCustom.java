package com.hsj.force.order.repository;

import com.hsj.force.domain.dto.OrderListDTO;
import com.hsj.force.domain.entity.TOrder;

import java.util.List;

public interface OrderRepositoryCustom {

    List<OrderListDTO> findOrderListDTO(String orderStatusNo, String saleStatusNo);

    TOrder findOneCustom(String orderNo, String tableNo, String menuNo, String orderStatusNo);

    List<TOrder> findAllCustomV1(String orderNo, String tableNo, String orderStatusNo);



}
