package com.hsj.force.domain.dto;

import com.hsj.force.domain.Table;
import lombok.Data;

import java.util.List;

@Data
public class TableDTO {

    private CommonLayoutDTO commonLayoutForm;
    private List<Table> tableList;
    private List<OrderDTO> orderList;
    private List<TableTotalPriceDTO> tableTotalPriceList;
}
