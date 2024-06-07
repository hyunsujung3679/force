package com.hsj.force.open.repository;

import com.hsj.force.domain.dto.OpenCloseInsertDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OpenMapper {

    int selectIsOpen(String storeNo);

    String selectOpenCloseSeq(String storeNo);

}
