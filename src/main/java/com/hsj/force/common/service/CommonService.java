package com.hsj.force.common.service;

import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.SaleStatus;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CommonService {

    private final CommonMapper commonMapper;

    public List<SaleStatus> selectSaleStatusList() {
        return commonMapper.selectSaleStatusList();
    }

    public CommonLayoutDTO selectHeaderInfo(User loginMember) {
        String storeName = commonMapper.selectStoreName(loginMember.getStoreNo());
        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());
        return commonLayoutForm;
    }
}
