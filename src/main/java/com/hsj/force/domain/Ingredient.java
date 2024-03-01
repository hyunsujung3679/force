package com.hsj.force.domain;

import lombok.Data;

@Data
public class Ingredient {

    private String ingredientNo;
    private String storeNo;
    private String ingredientName;
    private double quantity;
    private String insertId;
    private String insertDate;
    private String modifyId;
    private String modifyDate;

}
