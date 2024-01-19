package com.hsj.force.open.repository;

import com.hsj.force.domain.OpenForm;
import com.hsj.force.domain.OpenSave;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OpenMapper {

    int selectIsOpen();

    OpenForm selectOpenInfo();

    String selectOpenCloseSeq();


    String selectOpenCloseNo();

    int insertOpen(OpenSave openSave);
}
