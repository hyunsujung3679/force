package com.hsj.force.close.repository;

import com.hsj.force.domain.dto.OpenCloseUpdateDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CloseMapper {
    OpenCloseUpdateDTO selectSumInfo();

    OpenCloseUpdateDTO selectCancelInfo();

    Integer selectDiscountPrice();

    OpenCloseUpdateDTO selectRealOrderInfo();

}
