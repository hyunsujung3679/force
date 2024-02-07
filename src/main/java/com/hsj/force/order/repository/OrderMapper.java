package com.hsj.force.order.repository;

import com.hsj.force.domain.Category;
import com.hsj.force.domain.Order;
import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.domain.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

@Mapper
public interface OrderMapper {

    List<OrderDTO> selectOrderList(String storeNo, String tableNo);

    Order selectOrderNoSeq();

    void insertOrder(OrderDTO order);

}
