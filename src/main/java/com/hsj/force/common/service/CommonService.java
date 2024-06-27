package com.hsj.force.common.service;

import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.SaleStatusListDTO;
import com.hsj.force.domain.entity.TSaleStatus;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.menu.repository.SaleStatusJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class CommonService {

    private final MessageSource messageSource;
    private final SaleStatusJpaRepository saleStatusJpaRepository;

    public List<SaleStatusListDTO> selectSaleStatusList() {
        List<TSaleStatus> saleStatuses = saleStatusJpaRepository.findAllByOrderBySaleStatusNo();
        return saleStatuses.stream()
                .map(s -> new SaleStatusListDTO(s))
                .collect(Collectors.toList());
    }

    public CommonLayoutDTO selectHeaderInfo(TUser loginMember) {
        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(messageSource.getMessage("word.store.name",null, null));
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());
        return commonLayoutForm;
    }
}
