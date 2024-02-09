package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class OrderTotalDTO {

    private int totalQuantity;
    private int totalDiscountPrice;
    private int totalSalePrice;

}
