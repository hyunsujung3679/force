package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class MenuIngredientListDTO {

    private String menuNo;
    private String ingredientNo;
    private double quantity;
    private double needQuantity;
    private double stockQuantity;
    private double menuQuantity;
    private double ingredientQuantity;

}
