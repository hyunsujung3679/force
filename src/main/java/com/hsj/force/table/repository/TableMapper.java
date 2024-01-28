package com.hsj.force.table.repository;

import com.hsj.force.domain.Table;
import com.hsj.force.domain.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TableMapper {

    String selectStoreName(String storeNo);

    List<Table> selectTableList(String storeNo);

    List<OrderDTO> selectOrderList(String storeNo);

}
