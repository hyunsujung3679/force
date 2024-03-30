package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class OrderDTO {

    private int totalQuantity;
    private int totalDiscountPrice;
    private int totalSalePrice;

}
