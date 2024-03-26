package com.hsj.force.close.repository;

import com.hsj.force.domain.dto.CloseDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CloseMapper {
    CloseDTO selectSumInfo(String storeNo);

    CloseDTO selectCancelInfo(String storeNo);

    Integer selectDiscountPrice(String storeNo);

    CloseDTO selectRealOrderInfo(String storeNo);

    int updateOpenClose(CloseDTO close);
}
