package com.hsj.force.openclose.repository;

import com.hsj.force.domain.out.OpenCloseOutDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OpenCloseMapper {

    int selectIsOpen();

    OpenCloseOutDTO selectOpenCloseInfo();

    String selectOpenCloseSeq();


}
