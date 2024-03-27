package com.hsj.force.close.service;

import com.hsj.force.close.repository.CloseMapper;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CloseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
@RequiredArgsConstructor
public class CloseService {

    private final CloseMapper closeMapper;

    public CloseDTO selectCloseInfo(User loginMember) {

        CloseDTO close = new CloseDTO();

        CloseDTO sumInfo = closeMapper.selectSumInfo(loginMember.getStoreNo());
        CloseDTO cancelInfo = closeMapper.selectCancelInfo(loginMember.getStoreNo());
        Integer discountPrice = closeMapper.selectDiscountPrice(loginMember.getStoreNo());
        CloseDTO realOrderInfo = closeMapper.selectRealOrderInfo(loginMember.getStoreNo());

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

    public int updateOpenClose(CloseDTO close) {

        int oneHunThous = Integer.parseInt(close.getOneHunThousStr()) * 100000;
        int fiftyThous = Integer.parseInt(close.getFiftyThousStr()) * 50000;
        int tenThous = Integer.parseInt(close.getTenThousStr()) * 10000;
        int fiveThous = Integer.parseInt(close.getFiveThousStr()) * 5000;
        int oneThous = Integer.parseInt(close.getOneThousStr()) * 1000;
        int fiveHun = Integer.parseInt(close.getFiveHunStr()) * 500;
        int oneHun = Integer.parseInt(close.getOneHunStr()) * 100;
        int fifty = Integer.parseInt(close.getFiftyStr()) * 50;
        int ten = Integer.parseInt(close.getTenStr()) * 10;
        int closeMoney = oneHunThous + fiftyThous + tenThous + fiveThous + oneThous + fiveHun + oneHun + fifty + ten;
        close.setCloseMoney(closeMoney);

        return closeMapper.updateOpenClose(close);
    }
}
