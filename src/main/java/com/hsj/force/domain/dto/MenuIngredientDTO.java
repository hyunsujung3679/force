package com.hsj.force.domain.dto;

import com.hsj.force.domain.MenuIngredient;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuIngredientDTO extends MenuIngredient {

    private double needQuantity;
    private double stockQuantity;
    private double menuQuantity;
    private double ingredientQuantity;
}
