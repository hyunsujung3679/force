package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class CategoryListDTO {

    private CommonLayoutDTO commonLayoutForm;
    private String categoryNo;
    private String storeNo;
    private String categoryName;
    private String useYn;
    private int priority;

}
