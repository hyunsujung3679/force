package com.hsj.force.domain.form;

import lombok.Data;

@Data
public class OrderForm {

    private String orderNo;
    private String orderSeq;
    private String storeNo;
    private String tableNo;
    private String menuNo;
    private String salePrice;
    private int quantity;
    private String totalSalePrice;
    private String fullPriceYn;
    private String fullPerYn;
    private String selPriceYn;
    private String selPerYn;
    private String serviceYn;
    private String discountPrice;
    private String OrderDate;
    private String orderStatusNo;
    private String cancelDate;
    private String inDeReason;
    private String insertId;
    private String insertDate;
    private String modifyId;
    private String modifyDate;

    private String menuName;

}
