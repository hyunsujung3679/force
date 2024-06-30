package com.hsj.force.close.service;

import com.hsj.force.close.repository.CloseJpaRepository;
import com.hsj.force.domain.dto.OpenCloseUpdateDTO;
import com.hsj.force.domain.entity.TOpenClose;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
@RequiredArgsConstructor
public class CloseService {

    private final CloseJpaRepository closeJpaRepository;

    public OpenCloseUpdateDTO selectCloseInfo() {

        OpenCloseUpdateDTO close = new OpenCloseUpdateDTO();

        OpenCloseUpdateDTO sumInfo = closeJpaRepository.findSumInfo();
        OpenCloseUpdateDTO cancelInfo = closeJpaRepository.findCancelInfo("OS002");
        Integer discountPrice =  closeJpaRepository.findDiscountPrice("OS003").getDiscountPrice();
        OpenCloseUpdateDTO realOrderInfo = closeJpaRepository.findRealOrderInfo("OS003");

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

    public int updateOpenClose(OpenCloseUpdateDTO closeDTO) {

        int oneHunThous = closeDTO.getOneHunThous() * 100000;
        int fiftyThous = closeDTO.getFiftyThous() * 50000;
        int tenThous = closeDTO.getTenThous() * 10000;
        int fiveThous = closeDTO.getFiveThous() * 5000;
        int oneThous = closeDTO.getOneThous() * 1000;
        int fiveHun = closeDTO.getFiveHun() * 500;
        int oneHun = closeDTO.getOneHun() * 100;
        int fifty = closeDTO.getFifty() * 50;
        int ten = closeDTO.getTen() * 10;
        int closeMoney = oneHunThous + fiftyThous + tenThous + fiveThous + oneThous + fiveHun + oneHun + fifty + ten;

        TOpenClose close = closeJpaRepository.findOneByCloseMoneyIsNull();

        close.setOneHunThous(oneHunThous);
        close.setFiftyThous(fiftyThous);
        close.setTenThous(tenThous);
        close.setFiveThous(fiveThous);
        close.setOneThous(oneThous);
        close.setFiveHun(fiveHun);
        close.setOneHun(oneHun);
        close.setFifty(fifty);
        close.setTen(ten);
        close.setCloseMoney(closeMoney);

        return 1;
    }
}
