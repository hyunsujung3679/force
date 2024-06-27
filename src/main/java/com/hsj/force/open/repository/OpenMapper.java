package com.hsj.force.open.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OpenMapper {

    String selectOpenCloseSeq();

}
