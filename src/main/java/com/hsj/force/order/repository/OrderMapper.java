package com.hsj.force.order.repository;

import com.hsj.force.domain.Order;
import com.hsj.force.domain.dto.OrderListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    Integer selectQuantity(Order order);

    List<OrderListDTO> selectOrderList(Map<String, Object> paramMap);

    String selectOrderNo(Order order);

    String selectOrderSeq(String orderNo);

    int insertOrder(Order order);

    int updateOrder(Order order);

    int updateOrderStatusV1(Order order);

    int updateOrderStatusV2(Order order);

    int updateOrderStatusV3(Order order);

    int updateQuantity(Order order);

    int updateSalePrice(Order order);

    int updateService(Order order);

    Order selectOrderInfo(Order order);

    List<Order> selectOrderInfoList(Order order);

    int updateDiscountFullPer(Order orderInfo);

    int updateDiscountFullPrice(Order orderInfo);

    int updateDiscountCancel(Order orderInfo);

    int updateDiscountSelPer(Order orderInfo);

    int updateDiscountSelPrice(Order orderInfo);

    List<String> selectOrderStatusNoList(String orderNo);

    String selectLastOrderNo(String storeNo);

    List<String> selectMenuNoList(String storeNo, String orderNo);
}
