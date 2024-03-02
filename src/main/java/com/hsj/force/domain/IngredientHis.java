package com.hsj.force.domain;

import lombok.Data;

@Data
public class IngredientHis {

    private String ingredientNo;
    private String ingredientSeq;
    private String storeNo;
    private double inDeQuantity;
    private String inDeReasonNo;
    private String expirationDate;
    private String insertId;
    private String insertDate;
    private String modifyId;
    private String modifyDate;

}
