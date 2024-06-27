package com.hsj.force.table.repository;

import com.hsj.force.domain.dto.TableListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TableMapper {

    List<TableListDTO> selectTableNotExistOrderList();

}
