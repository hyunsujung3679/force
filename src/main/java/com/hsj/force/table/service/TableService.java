package com.hsj.force.table.service;

import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.User;
import com.hsj.force.domain.Table;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.OrderDTO;
import com.hsj.force.domain.dto.TableDTO;
import com.hsj.force.domain.dto.TableTotalPriceDTO;
import com.hsj.force.table.repository.TableMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableMapper tableMapper;

    private final CommonMapper commonMapper;

    public TableDTO selectTableInfo(User loginMember) {

        String storeName = commonMapper.selectStoreName(loginMember.getStoreNo());
        List<Table> tableList = tableMapper.selectTableList(loginMember.getStoreNo());
        List<OrderDTO> orderList = tableMapper.selectOrderList(loginMember.getStoreNo());

        List<TableTotalPriceDTO> tableTotalPriceList = new ArrayList<>();
        TableTotalPriceDTO tableTotalPriceForm = null;
        for(Table table : tableList) {
            tableTotalPriceForm = new TableTotalPriceDTO();
            tableTotalPriceForm.setTableNo(table.getTableNo());
            int totalPrice = 0;
            for(OrderDTO order : orderList) {
                if(table.getTableNo().equals(order.getTableNo())) {
                    totalPrice += Integer.parseInt(order.getTotalSalePrice());
                }
            }
            tableTotalPriceForm.setTableTotalPrice(totalPrice);
            tableTotalPriceList.add(tableTotalPriceForm);
        }

        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        TableDTO tableForm = new TableDTO();
        tableForm.setTableList(tableList);
        tableForm.setOrderList(orderList);
        tableForm.setCommonLayoutForm(commonLayoutForm);
        tableForm.setTableTotalPriceList(tableTotalPriceList);

        return tableForm;
    }
}
