package com.hsj.force.domain.dto;

import com.hsj.force.domain.entity.TIngredient;
import lombok.Data;

@Data
public class IngredientListDTO {

    private int no;
    private String ingredientNo;
    private String ingredientName;
    private double quantity;

    public IngredientListDTO() {
    }

    public IngredientListDTO(TIngredient ingredient) {
        this.ingredientNo = ingredient.getIngredientId().getIngredientNo();
        this.ingredientName = ingredient.getIngredientName();
        this.quantity = ingredient.getQuantity();
    }
}
