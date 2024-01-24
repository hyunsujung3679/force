package com.hsj.force.domain;

import lombok.Data;

import java.util.List;

@Data
public class TableSecondForm {

    private String tableNo;
    private String tableName;
    private String orderNo;
    List<TableThirdForm> tableThirdForms;
    private String totalPrice;
    private String useYn;

}
