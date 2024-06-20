package com.hsj.force.common.service;

import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.common.repository.CommonRepository;
import com.hsj.force.domain.SaleStatus;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.entity.TUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CommonService {

    private final CommonRepository commonRepository;

    private final CommonMapper commonMapper;

    public List<SaleStatus> selectSaleStatusList() {
        return commonMapper.selectSaleStatusList();
    }

    public CommonLayoutDTO selectHeaderInfo(TUser loginMember) {

        String storeNo = loginMember.getStore().getStoreNo();

        String storeName = "";
        if(commonRepository.findStoreName(storeNo).isPresent()) {
            storeName = commonRepository.findStoreName(storeNo).get();
        }

        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());
        return commonLayoutForm;
    }
}
