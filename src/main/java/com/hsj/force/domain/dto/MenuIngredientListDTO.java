package com.hsj.force.domain.dto;

import com.hsj.force.domain.entity.TMenuIngredient;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MenuIngredientListDTO {

    private String menuNo;
    private String ingredientNo;
    private double quantity;
    private double stockQuantity;
    private int menuQuantity;
    private double ingredientQuantity;
    private double needQuantity;

    public MenuIngredientListDTO() {}

    @QueryProjection
    public MenuIngredientListDTO(String menuNo, String ingredientNo, double quantity, double stockQuantity, int menuQuantity, double ingredientQuantity, double needQuantity) {
        this.menuNo = menuNo;
        this.ingredientNo = ingredientNo;
        this.quantity = quantity;
        this.stockQuantity = stockQuantity;
        this.menuQuantity = menuQuantity;
        this.ingredientQuantity = ingredientQuantity;
        this.needQuantity = needQuantity;
    }

    public MenuIngredientListDTO(TMenuIngredient menuIngredient) {
        this.ingredientNo = menuIngredient.getIngredientNo();
        this.quantity = menuIngredient.getQuantity();
    }
}
