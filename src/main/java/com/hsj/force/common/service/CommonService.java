package com.hsj.force.common.service;

import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.SaleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final CommonMapper commonMapper;

    public List<SaleStatus> selectSaleStatusList() {
        return commonMapper.selectSaleStatusList();
    }
}
