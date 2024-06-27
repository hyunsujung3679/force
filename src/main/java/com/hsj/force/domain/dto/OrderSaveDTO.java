package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class OrderSaveDTO {

    private String menuNo;
    private String tableNo;
    private String orderNo;
    private String quantity;
    private String salePrice;
    private String percent;
    private String discountPrice;

}
