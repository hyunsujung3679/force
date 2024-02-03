package com.hsj.force.common.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {

    String selectStoreName(String storeNo);

}
