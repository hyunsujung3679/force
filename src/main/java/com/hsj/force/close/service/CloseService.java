package com.hsj.force.close.service;

import com.hsj.force.close.repository.CloseMapper;
import com.hsj.force.close.repository.CloseRepository;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.OpenCloseUpdateDTO;
import com.hsj.force.domain.entity.TOpenClose;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.domain.entity.embedded.TOpenCloseId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
@RequiredArgsConstructor
public class CloseService {

    private final CloseRepository closeRepository;

    private final CloseMapper closeMapper;

    public OpenCloseUpdateDTO selectCloseInfo(TUser loginMember) {

        OpenCloseUpdateDTO close = new OpenCloseUpdateDTO();

        OpenCloseUpdateDTO sumInfo = closeMapper.selectSumInfo(loginMember.getStore().getStoreNo());
        OpenCloseUpdateDTO cancelInfo = closeMapper.selectCancelInfo(loginMember.getStore().getStoreNo());
        Integer discountPrice = closeMapper.selectDiscountPrice(loginMember.getStore().getStoreNo());
        OpenCloseUpdateDTO realOrderInfo = closeMapper.selectRealOrderInfo(loginMember.getStore().getStoreNo());

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

    public int updateOpenClose(TUser loginMember, OpenCloseUpdateDTO closeDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String userId = loginMember.getUserId();

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

        TOpenClose close = new TOpenClose();
        if(closeRepository.findOpenCloseV1(storeNo).isPresent()) {
            close = closeRepository.findOpenCloseV1(storeNo).get();
        }
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
        close.setModifyId(userId);
        close.setModifyDate(LocalDateTime.now());

        return 1;
    }
}
