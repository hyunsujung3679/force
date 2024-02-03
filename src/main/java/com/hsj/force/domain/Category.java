package com.hsj.force.domain;

import lombok.Data;

@Data
public class Category {

    private String categoryNo;
    private String storeNo;
    private String categoryName;
    private int priority;
    private String useYn;
    private String insertId;
    private String insertDate;
    private String modifyId;
    private String modifyDate;

}
