package com.hsj.force.domain.dto;

import com.hsj.force.domain.Table;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TableDTO {

    private CommonLayoutDTO commonLayoutForm;
    private List<Table> tableList;
    private List<TableTotalPriceDTO> tableTotalPriceList;
    private Map<String, List<OrderDTO>> tableOfOrderMap;
}
