package com.hsj.force.domain;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Data
public class Order {

    private String orderNo;
    private String orderSeq;
    private String storeNo;
    private String tableNo;
    private String menuNo;
    private Integer salePrice;
    private int quantity;
    private int totalSalePrice;
    private String fullPriceYn;
    private String fullPerYn;
    private String selPriceYn;
    private String selPerYn;
    private String serviceYn;
    private int discountPrice;
    private String OrderDate;
    private String orderStatusNo;
    private String cancelDate;
    private String insertId;
    private String insertDate;
    private String modifyId;
    private String modifyDate;

}
