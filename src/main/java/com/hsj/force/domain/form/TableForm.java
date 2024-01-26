package com.hsj.force.domain.form;

import com.hsj.force.domain.Order;
import com.hsj.force.domain.Table;
import lombok.Data;

import java.util.List;

@Data
public class TableForm {

    private CommonLayoutForm commonLayoutForm;
    private List<Table> tableList;
    private List<Order> orderList;
}
