package com.hsj.force.table.service;

import com.hsj.force.domain.Login;
import com.hsj.force.domain.Order;
import com.hsj.force.domain.Table;
import com.hsj.force.domain.form.CommonLayoutForm;
import com.hsj.force.domain.form.TableForm;
import com.hsj.force.table.repository.TableMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableMapper tableMapper;

    public TableForm selectTableInfo(Login loginMember) {

        String storeName = tableMapper.selectStoreName(loginMember.getStoreNo());
        List<Table> tableList = tableMapper.selectTableList(loginMember.getStoreNo());
        List<Order> orderList = tableMapper.selectOrderList(loginMember.getStoreNo());

        CommonLayoutForm commonLayoutForm = new CommonLayoutForm();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        TableForm tableForm = new TableForm();
        tableForm.setTableList(tableList);
        tableForm.setOrderList(orderList);
        tableForm.setCommonLayoutForm(commonLayoutForm);

        return tableForm;
    }
}
