package com.hsj.force.close.repository;

import com.hsj.force.domain.dto.OpenCloseUpdateDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CloseMapper {
    OpenCloseUpdateDTO selectSumInfo(String storeNo);

    OpenCloseUpdateDTO selectCancelInfo(String storeNo);

    Integer selectDiscountPrice(String storeNo);

    OpenCloseUpdateDTO selectRealOrderInfo(String storeNo);

    int updateOpenClose(OpenCloseUpdateDTO close);
}
