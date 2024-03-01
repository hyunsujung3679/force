package com.hsj.force.domain;

import lombok.Data;

@Data
public class MenuIngredient {

    private String menuNo;
    private String ingredientNo;
    private String storeNo;
    private double quantity;
    private String insertId;
    private String insertDate;
    private String modifyId;
    private String modifyDate;

}
