package com.hsj.force.table.repository;

import com.hsj.force.domain.dto.TableListDTO;

import java.util.List;

public interface TableJpaRepositoryCustom {

    List<TableListDTO> findTableListDTOV1(String orderStatus);

    List<TableListDTO> findTableListDTOV2(String orderStatus);
}
