package com.hsj.force.table.service;

import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.OrderListDTO;
import com.hsj.force.domain.dto.TableDTO;
import com.hsj.force.domain.dto.TableListDTO;
import com.hsj.force.domain.entity.TOrder;
import com.hsj.force.domain.entity.TOrderStatus;
import com.hsj.force.domain.entity.TTable;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.order.repository.OrderJpaRepository;
import com.hsj.force.table.repository.TableJpaRepository;
import com.hsj.force.table.repository.TableMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class TableService {

    private final TableJpaRepository tableJpaRepository;
    private final OrderJpaRepository orderJpaRepository;

    private final TableMapper tableMapper;

    private final MessageSource messageSource;

    public Map<String, Object> selectTableInfo(TUser loginMember) {

        List<TTable> tables = tableJpaRepository.findAllByOrderByTableNo();
        List<TableListDTO> tableList = tables.stream()
                .map(t -> new TableListDTO(t))
                .collect(Collectors.toList());

        List<OrderListDTO> orderList = orderJpaRepository.findOrderListDTO("OS001", "SS001");

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
        commonLayoutForm.setStoreName(messageSource.getMessage("word.store.name",null, null));
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

    public List<TableListDTO> selectTableExistOrderList() {
        return orderJpaRepository.findAllV2("OS001");
    }

    public List<TableListDTO> selectTableNotExistOrderList() {
        return tableMapper.selectTableNotExistOrderList();
    }

    public int moveTable(TableDTO tableDTO) {

        String afterTableNo = tableDTO.getAfterTableNo();
        String beforeTableNo = tableDTO.getBeforeTableNo();

        TTable table = null;
        Optional<TTable> optionalTable = tableJpaRepository.findById(afterTableNo);
        if(optionalTable.isPresent()) {
            table = optionalTable.get();
        }

        List<TOrder> orders = orderJpaRepository.findAllByOrderStatusAndTable(new TOrderStatus("OS001"), new TTable(beforeTableNo));
        for(TOrder order : orders) {
            order.setTable(table);
        }

        return 1;
    }

    public int combineTable(TableDTO tableDTO) {

        String firstTableNo = tableDTO.getFirstTableNo();
        String secondTableNo = tableDTO.getSecondTableNo();

        TTable table = null;
        Optional<TTable> optionalTable = tableJpaRepository.findById(secondTableNo);
        if(optionalTable.isPresent()) {
            table = optionalTable.get();
        }

        List<TOrder> orders = orderJpaRepository.findAllByOrderStatusAndTable(new TOrderStatus("OS001"), new TTable(firstTableNo));
        for(TOrder order : orders) {
            order.setTable(table);
        }

        return 1;
    }
}
