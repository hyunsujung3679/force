package com.hsj.force.order.repository;

import com.hsj.force.domain.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    Integer selectQuantity(OrderDTO order);

    List<OrderDTO> selectOrderList(String storeNo, String tableNo);

    String selectOrderNo(OrderDTO order);

    String selectOrderSeq(String orderNo);

    int insertOrder(OrderDTO order);

    int updateOrder(OrderDTO order);

    int updateOrderStatusV1(OrderDTO order);

    int updateOrderStatusV2(OrderDTO order);

    int updateOrderStatusV3(OrderDTO order);

    int updateQuantity(OrderDTO order);

    int updateSalePrice(OrderDTO order);

    int updateService(OrderDTO order);

    OrderDTO selectOrderInfo(OrderDTO order);

    List<OrderDTO> selectOrderInfoList(OrderDTO order);

    int updateDiscountFullPer(OrderDTO orderInfo);

    int updateDiscountFullPrice(OrderDTO orderInfo);

    int updateDiscountCancel(OrderDTO orderInfo);

    int updateDiscountSelPer(OrderDTO orderInfo);

    int updateDiscountSelPrice(OrderDTO orderInfo);

    List<String> selectOrderStatusNoList(String orderNo);

    String selectLastOrderNo(String storeNo);

    List<String> selectMenuNoList(String storeNo, String orderNo);
}
