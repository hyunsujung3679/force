package com.hsj.force.open.repository;

import com.hsj.force.domain.dto.OpenDTO;
import com.hsj.force.domain.dto.OpenSaveDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OpenMapper {

    int selectIsOpen();

    OpenDTO selectOpenInfo();

    String selectOpenCloseSeq();


    String selectOpenCloseNo();

    int insertOpen(OpenSaveDTO openSave);
}
