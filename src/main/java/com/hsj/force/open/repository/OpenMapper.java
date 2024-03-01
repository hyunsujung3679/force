package com.hsj.force.open.repository;

import com.hsj.force.domain.dto.OpenDTO;
import com.hsj.force.domain.dto.OpenSaveDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OpenMapper {

    int selectIsOpen(String storeNo);

    OpenDTO selectOpenInfo(String storeNo);

    String selectOpenCloseSeq(String storeNo);


    String selectOpenCloseNo(String storeNo);

    int insertOpen(OpenSaveDTO openSave);
}
