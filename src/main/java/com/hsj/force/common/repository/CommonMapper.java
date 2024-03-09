package com.hsj.force.common.repository;

import com.hsj.force.domain.SaleStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonMapper {

    String selectStoreName(String storeNo);

    List<SaleStatus> selectSaleStatusList();
}
