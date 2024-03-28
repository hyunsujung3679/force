package com.hsj.force.close.service;

import com.hsj.force.close.repository.CloseMapper;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.OpenCloseUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
@RequiredArgsConstructor
public class CloseService {

    private final CloseMapper closeMapper;

    public OpenCloseUpdateDTO selectCloseInfo(User loginMember) {

        OpenCloseUpdateDTO close = new OpenCloseUpdateDTO();

        OpenCloseUpdateDTO sumInfo = closeMapper.selectSumInfo(loginMember.getStoreNo());
        OpenCloseUpdateDTO cancelInfo = closeMapper.selectCancelInfo(loginMember.getStoreNo());
        Integer discountPrice = closeMapper.selectDiscountPrice(loginMember.getStoreNo());
        OpenCloseUpdateDTO realOrderInfo = closeMapper.selectRealOrderInfo(loginMember.getStoreNo());

        close.setSumSalePrice(sumInfo.getSumSalePrice() == null ? 0 : sumInfo.getSumSalePrice());
        close.setSumOrderCnt(sumInfo.getSumOrderCnt());
        close.setSumCancelPrice(cancelInfo.getSumCancelPrice() == null ? 0 : cancelInfo.getSumCancelPrice());
        close.setSumCancelCnt(cancelInfo.getSumCancelCnt());
        close.setDiscountPrice(discountPrice == null ? 0 : discountPrice);
        close.setRealOrderCnt(realOrderInfo.getRealOrderCnt());
        close.setTotalSalePrice(realOrderInfo.getTotalSalePrice() == null ? 0 : realOrderInfo.getTotalSalePrice());
        close.setCurrentDate(LocalDateTime.now());

        return close;
    }

    public int updateOpenClose(OpenCloseUpdateDTO close) {

        int oneHunThous = close.getOneHunThous() * 100000;
        int fiftyThous = close.getFiftyThous() * 50000;
        int tenThous = close.getTenThous() * 10000;
        int fiveThous = close.getFiveThous() * 5000;
        int oneThous = close.getOneThous() * 1000;
        int fiveHun = close.getFiveHun() * 500;
        int oneHun = close.getOneHun() * 100;
        int fifty = close.getFifty() * 50;
        int ten = close.getTen() * 10;
        int closeMoney = oneHunThous + fiftyThous + tenThous + fiveThous + oneThous + fiveHun + oneHun + fifty + ten;
        close.setCloseMoney(closeMoney);

        return closeMapper.updateOpenClose(close);
    }
}
