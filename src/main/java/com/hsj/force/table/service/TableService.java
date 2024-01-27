package com.hsj.force.table.service;

import com.hsj.force.domain.Login;
import com.hsj.force.domain.Table;
import com.hsj.force.domain.form.CommonLayoutForm;
import com.hsj.force.domain.form.OrderForm;
import com.hsj.force.domain.form.TableForm;
import com.hsj.force.domain.form.TableTotalPriceForm;
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
        List<OrderForm> orderList = tableMapper.selectOrderList(loginMember.getStoreNo());

        List<TableTotalPriceForm> tableTotalPriceList = new ArrayList<>();
        TableTotalPriceForm tableTotalPriceForm = new TableTotalPriceForm();
        for(Table table : tableList) {
            int totalPrice = 0;
            tableTotalPriceForm.setTableNo(table.getTableNo());
            for(OrderForm order : orderList) {
                if(table.getTableNo().equals(order.getTableNo())) {
                    totalPrice += Integer.parseInt(order.getTotalSalePrice());
                }
            }
            tableTotalPriceForm.setTableTotalPrice(totalPrice);
            tableTotalPriceList.add(tableTotalPriceForm);
        }


        CommonLayoutForm commonLayoutForm = new CommonLayoutForm();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        TableForm tableForm = new TableForm();
        tableForm.setTableList(tableList);
        tableForm.setOrderList(orderList);
        tableForm.setCommonLayoutForm(commonLayoutForm);
        tableForm.setTableTotalPriceList(tableTotalPriceList);

        return tableForm;
    }
}
