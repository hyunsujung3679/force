package com.hsj.force.domain.entity.embedded;

import lombok.Data;

import java.io.Serializable;

@Data
public class TIngredientHisId implements Serializable {

    private String ingredientNo;
    private String ingredientSeq;

    public TIngredientHisId() {}

    public TIngredientHisId(String ingredientNo, String ingredientSeq) {
        this.ingredientNo = ingredientNo;
        this.ingredientSeq = ingredientSeq;
    }
}
