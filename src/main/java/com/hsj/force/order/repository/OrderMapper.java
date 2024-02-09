package com.hsj.force.order.repository;

import com.hsj.force.domain.Category;
import com.hsj.force.domain.Order;
import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.domain.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

@Mapper
public interface OrderMapper {

    int selectDuplicateMenuCheck(OrderDTO order);

    List<OrderDTO> selectOrderList(String storeNo, String tableNo);

    String selectOrderNo(OrderDTO order);

    String selectOrderSeq(String orderNo);

    int insertOrder(OrderDTO order);

    int selectQuantity(OrderDTO order);

    int updateOrder(OrderDTO order);

    int updateOrderStatus(OrderDTO order);
}
