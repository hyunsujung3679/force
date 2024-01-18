package com.hsj.force.openclose.repository;

import com.hsj.force.domain.response.OpenCloseRes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OpenCloseMapper {

    int selectIsOpen();

    OpenCloseRes selectOpenCloseInfo();

    String selectOpenCloseSeq();


}
