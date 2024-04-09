package com.hsj.force.table.repository;

import com.hsj.force.domain.dto.OrderListDTO;
import com.hsj.force.domain.dto.TableListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TableMapper {

    List<TableListDTO> selectTableList(String storeNo);

    List<OrderListDTO> selectOrderList(String storeNo);

    List<TableListDTO> selectTableExistOrderList(String storeNo);

    List<TableListDTO> selectTableNotExistOrderList(String storeNo);

    int updateTableNoV1(String storeNo, String afterTableNo, String beforeTableNo, String modifyId);

    int updateTableNoV2(String storeNo, String firstTableNo, String secondTableNo, String modifyId);
}
