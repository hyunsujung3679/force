package com.hsj.force.domain.entity.embedded;

import lombok.Data;

import java.io.Serializable;

@Data
public class TMenuIngredientId implements Serializable {

    private String ingredientNo;
    private String menuNo;

    public TMenuIngredientId() {}

    public TMenuIngredientId(String ingredientNo, String menuNo) {
        this.ingredientNo = ingredientNo;
        this.menuNo = menuNo;
    }
}
