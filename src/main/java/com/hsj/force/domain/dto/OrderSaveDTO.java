package com.hsj.force.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Data
public class OrderSaveDTO {

    private String menuNo;
    private String tableNo;
    private String storeNo;
    private String orderNo;
    private String quantity;
    private String salePrice;
    private String percent;
    private String discountPrice;

}
