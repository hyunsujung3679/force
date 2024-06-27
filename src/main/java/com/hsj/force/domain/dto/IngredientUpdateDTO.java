package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class IngredientUpdateDTO {

    private String ingredientNo;
    private String ingredientName;
    private Double inDeQuantity;
    private String inDeReasonNo;

}
