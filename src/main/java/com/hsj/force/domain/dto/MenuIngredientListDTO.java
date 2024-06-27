package com.hsj.force.domain.dto;

import com.hsj.force.domain.entity.TMenuIngredient;
import lombok.Data;

@Data
public class MenuIngredientListDTO {

    private String menuNo;
    private String ingredientNo;
    private double quantity;
    private double stockQuantity;
    private double menuQuantity;
    private double ingredientQuantity;
    private double needQuantity;

    public MenuIngredientListDTO() {}

    public MenuIngredientListDTO(String menuNo, String ingredientNo, double ingredientQuantity, double needQuantity) {
        this.menuNo = menuNo;
        this.ingredientNo = ingredientNo;
        this.ingredientQuantity = ingredientQuantity;
        this.needQuantity = needQuantity;
    }

    public MenuIngredientListDTO(String menuNo, double stockQuantity, String ingredientNo, double needQuantity) {
        this.menuNo = menuNo;
        this.stockQuantity = stockQuantity;
        this.ingredientNo = ingredientNo;
        this.needQuantity = needQuantity;
    }

    public MenuIngredientListDTO(String menuNo, String ingredientNo, double menuQuantity, double ingredientQuantity, double needQuantity) {
        this.menuNo = menuNo;
        this.ingredientNo = ingredientNo;
        this.menuQuantity = menuQuantity;
        this.ingredientQuantity = ingredientQuantity;
        this.needQuantity = needQuantity;
    }

    public MenuIngredientListDTO(TMenuIngredient menuIngredient) {
        this.ingredientNo = menuIngredient.getIngredientNo();
        this.quantity = menuIngredient.getQuantity();
    }
}
