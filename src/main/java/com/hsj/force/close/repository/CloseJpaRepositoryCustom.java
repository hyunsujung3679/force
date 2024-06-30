package com.hsj.force.close.repository;

import com.hsj.force.domain.dto.OpenCloseUpdateDTO;

public interface CloseJpaRepositoryCustom {

    OpenCloseUpdateDTO findSumInfo();

    OpenCloseUpdateDTO findCancelInfo(String orderStatusNo);

    OpenCloseUpdateDTO findDiscountPrice(String orderStatusNo);

    OpenCloseUpdateDTO findRealOrderInfo(String orderStatusNo);
}
