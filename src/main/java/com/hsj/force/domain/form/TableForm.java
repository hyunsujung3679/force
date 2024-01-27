package com.hsj.force.domain.form;

import com.hsj.force.domain.Table;
import lombok.Data;

import java.util.List;

@Data
public class TableForm {

    private CommonLayoutForm commonLayoutForm;
    private List<Table> tableList;
    private List<OrderForm> orderList;
    private List<TableTotalPriceForm> tableTotalPriceList;
}
