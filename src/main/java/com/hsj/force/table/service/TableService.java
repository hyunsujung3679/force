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
        Map<String, List<OrderDTO>> tableOfOrderMap = new HashMap<>();
        List<OrderDTO> tempOrderList = null;

        for(Table table : tableList) {
            tempOrderList = new ArrayList<>();
            for(OrderDTO order : orderList) {
                if(table.getTableNo().equals(order.getTableNo())) {
                    tempOrderList.add(order);
                }
            }
            tableOfOrderMap.put(table.getTableNo(), tempOrderList);
        }

        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        TableDTO tableForm = new TableDTO();
        tableForm.setTableList(tableList);
        tableForm.setCommonLayoutForm(commonLayoutForm);
        tableForm.setTableTotalPriceList(getTableTotalPriceList(tableList, orderList));
        tableForm.setTableOfOrderMap(tableOfOrderMap);

        return tableForm;
    }

    private List<TableTotalPriceDTO> getTableTotalPriceList(List<Table> tableList, List<OrderDTO> orderList) {
        List<TableTotalPriceDTO> tableTotalPriceList = new ArrayList<>();
        TableTotalPriceDTO tableTotalPriceForm = null;
        for(Table table : tableList) {
            tableTotalPriceForm = new TableTotalPriceDTO();
            tableTotalPriceForm.setTableNo(table.getTableNo());
            int totalPrice = 0;
            for(OrderDTO order : orderList) {
                if(table.getTableNo().equals(order.getTableNo())) {
                    totalPrice += order.getTotalSalePrice();
                }
            }
            tableTotalPriceForm.setTableTotalPrice(totalPrice);
            tableTotalPriceList.add(tableTotalPriceForm);
        }
        return tableTotalPriceList;
    }

    public List<Table> selectTableExistOrderList(String storeNo) {
        return tableMapper.selectTableExistOrderList(storeNo);
    }

    public List<Table> selectTableNotExistOrderList(String storeNo) {
        return tableMapper.selectTableNotExistOrderList(storeNo);
    }

    public int moveTable(User loginMember, TableDTO table) {
        return tableMapper.updateTableNo(loginMember.getStoreNo(), table.getAfterTableNo(), table.getBeforeTableNo(), loginMember.getUserId());
    }

    public int combineTable(User loginMember, TableDTO table) {
        return tableMapper.updateTableNo(loginMember.getStoreNo(), table.getFirstTableNo(), table.getSecondTableNo(), loginMember.getUserId());
    }
}
