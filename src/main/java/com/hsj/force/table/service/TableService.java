package com.hsj.force.table.service;

import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.common.repository.CommonRepository;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.OrderListDTO;
import com.hsj.force.domain.dto.TableDTO;
import com.hsj.force.domain.dto.TableListDTO;
import com.hsj.force.domain.entity.TOrder;
import com.hsj.force.domain.entity.TTable;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.order.repository.OrderMapper;
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
        List<TOrder> orders = orderRepository.findAllV2(storeNo);

        List<TableListDTO> tableList = new ArrayList<>();
        TableListDTO tableListDTO = null;
        for (TTable table : tables) {
            tableListDTO = new TableListDTO();
            tableListDTO.setTableNo(table.getTableNo());
            tableListDTO.setTableName(table.getTableName());
            tableList.add(tableListDTO);
        }

        List<OrderListDTO> orderList = new ArrayList<>();
        OrderListDTO orderListDTO = null;
        for (TOrder order : orders) {
            orderListDTO = new OrderListDTO();
            orderListDTO.setOrderNo(order.getOrderId().getOrderNo());
            orderListDTO.setOrderSeq(order.getOrderId().getOrderSeq());
            orderListDTO.setStoreNo(order.getOrderId().getStore().getStoreNo());
            orderListDTO.setTableNo(order.getTable().getTableNo());
            orderListDTO.setMenuNo(order.getMenu().getMenuNo());
            orderListDTO.setSalePrice(order.getSalePrice());
            orderListDTO.setQuantity(order.getQuantity());
            orderListDTO.setTotalSalePrice(order.getTotalSalePrice());
            orderListDTO.setOrderStatusNo(order.getOrderStatus().getOrderStatusNo());
            orderListDTO.setMenuName(order.getMenu().getMenuName());
            orderList.add(orderListDTO);
        }

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
        int result = tableMapper.updateTableNoV1(loginMember.getStoreNo(), table.getAfterTableNo(), table.getBeforeTableNo(), loginMember.getUserId());
        if(result > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public int combineTable(User loginMember, TableDTO table) {
        int result = tableMapper.updateTableNoV2(loginMember.getStoreNo(), table.getFirstTableNo(), table.getSecondTableNo(), loginMember.getUserId());
        if(result > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
