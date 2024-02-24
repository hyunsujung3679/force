package com.hsj.force.domain.dto;

import com.hsj.force.domain.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class TableDTO extends Table {

    private CommonLayoutDTO commonLayoutForm;
    private List<Table> tableList;
    private List<TableTotalPriceDTO> tableTotalPriceList;
    private Map<String, List<OrderDTO>> tableOfOrderMap;
    private String beforeTableNo;
    private String afterTableNo;
    private String firstTableNo;
    private String secondTableNo;
}
