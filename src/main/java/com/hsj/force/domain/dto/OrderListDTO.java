package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class OrderListDTO {

    private String orderNo;
    private String orderSeq;
    private String menuNo;
    private String menuName;
    private Integer salePrice;
    private Integer quantity;
    private Integer discountPrice;
    private Integer totalSalePrice;
    private String fullPriceYn;
    private String fullPerYn;
    private String selPriceYn;
    private String selPerYn;
    private String serviceYn;
    private String no;
    private String etc;
    private String storeNo;
    private String tableNo;
    private String orderStatusNo;

}
