package com.hsj.force.table.service;

import com.hsj.force.common.repository.CommonRepository;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.OrderListDTO;
import com.hsj.force.domain.dto.TableDTO;
import com.hsj.force.domain.dto.TableListDTO;
import com.hsj.force.domain.entity.TOrder;
import com.hsj.force.domain.entity.TTable;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.domain.entity.embedded.TTableId;
import com.hsj.force.order.repository.OrderRepository;
import com.hsj.force.table.repository.TableMapper;
import com.hsj.force.table.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class TableService {

    private final CommonRepository commonRepository;
    private final TableRepository tableRepository;
    private final OrderRepository orderRepository;

    private final TableMapper tableMapper;

    public Map<String, Object> findTableInfo(TUser loginMember) {
        String storeNo = loginMember.getStore().getStoreNo();
        String storeName = "";

        if(commonRepository.findStoreName(storeNo).isPresent()) {
            storeName = commonRepository.findStoreName(storeNo).get();
        }

        List<TTable> tables = tableRepository.findAll(storeNo);
        List<TableListDTO> tableList = tables.stream()
                .map(t -> new TableListDTO(t))
                .collect(Collectors.toList());

        List<OrderListDTO> orderList = orderRepository.findAllV2(storeNo);

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
        //TODO: JPA 적용 필요
        return tableMapper.selectTableExistOrderList(storeNo);
    }

    public List<TableListDTO> selectTableNotExistOrderList(String storeNo) {
        //TODO: JPA 적용 필요
        return tableMapper.selectTableNotExistOrderList(storeNo);
    }

    public int moveTable(TUser loginMember, TableDTO tableDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String userId = loginMember.getUserId();
        String afterTableNo = tableDTO.getAfterTableNo();
        String beforeTableNo = tableDTO.getBeforeTableNo();

        TTableId tableId = new TTableId();
        tableId.setStoreNo(storeNo);
        tableId.setTableNo(afterTableNo);
        TTable table = tableRepository.findTable(tableId);

        List<TOrder> orders = orderRepository.findOrderV3(storeNo, beforeTableNo);
        for(TOrder order : orders) {
            order.setTable(table);
            order.setModifyId(userId);
            order.setModifyDate(LocalDateTime.now());
        }

        return 1;
    }

    public int combineTable(TUser loginMember, TableDTO tableDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String firstTableNo = tableDTO.getFirstTableNo();
        String secondTableNo = tableDTO.getSecondTableNo();
        String userId = loginMember.getUserId();

        TTableId tableId = new TTableId();
        tableId.setStoreNo(storeNo);
        tableId.setTableNo(secondTableNo);
        TTable table = tableRepository.findTable(tableId);

        List<TOrder> orders = orderRepository.findOrderV3(storeNo, firstTableNo);
        for(TOrder order : orders) {
            order.setTable(table);
            order.setModifyId(userId);
            order.setModifyDate(LocalDateTime.now());
        }

        return 1;
    }
}
