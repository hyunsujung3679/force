package com.hsj.force.table.service;

import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.*;
import com.hsj.force.order.repository.OrderMapper;
import com.hsj.force.table.repository.TableMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class TableService {

    private final TableMapper tableMapper;

    private final CommonMapper commonMapper;
    private final OrderMapper orderMapper;

    public Map<String, Object> selectTableInfo(User loginMember) {

        String storeName = commonMapper.selectStoreName(loginMember.getStoreNo());
        List<TableListDTO> tableList = tableMapper.selectTableList(loginMember.getStoreNo());
        List<OrderListDTO> orderList = orderMapper.selectOrderListV2(loginMember.getStoreNo());
        Map<String, List<OrderListDTO>> tableOfOrderMap = new HashMap<>();
        List<OrderListDTO> tempOrderList = null;

        for(TableListDTO table : tableList) {
            tempOrderList = new ArrayList<>();
            for(OrderListDTO order : orderList) {
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

        Map<String, Object> map = new HashMap<>();
        map.put("commonLayoutForm", commonLayoutForm);
        map.put("tableList", tableList);
        map.put("tableTotalPriceList", getTableTotalPriceList(tableList, orderList));
        map.put("tableOfOrderMap", tableOfOrderMap);

        return map;
    }

    private List<TableListDTO> getTableTotalPriceList(List<TableListDTO> tableList, List<OrderListDTO> orderList) {

        for(TableListDTO table : tableList) {
            int totalPrice = 0;
            for(OrderListDTO order : orderList) {
                if(table.getTableNo().equals(order.getTableNo())) {
                    totalPrice += order.getTotalSalePrice();
                }
            }
            table.setTableTotalPrice(totalPrice);
        }

        return tableList;
    }

    public List<TableListDTO> selectTableExistOrderList(String storeNo) {
        return tableMapper.selectTableExistOrderList(storeNo);
    }

    public List<TableListDTO> selectTableNotExistOrderList(String storeNo) {
        return tableMapper.selectTableNotExistOrderList(storeNo);
    }

    public int moveTable(User loginMember, TableDTO table) {
        return tableMapper.updateTableNo(loginMember.getStoreNo(), table.getAfterTableNo(), table.getBeforeTableNo(), loginMember.getUserId());
    }

    public int combineTable(User loginMember, TableDTO table) {
        return tableMapper.updateTableNo(loginMember.getStoreNo(), table.getFirstTableNo(), table.getSecondTableNo(), loginMember.getUserId());
    }
}
