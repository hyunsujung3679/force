package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class MenuInsertDTO {

    private String menuName;
    private String categoryNo;
    private String saleStatusNo;
    private String salePriceStr;
    private String ingredientNo;
    private String quantityStr;

}
