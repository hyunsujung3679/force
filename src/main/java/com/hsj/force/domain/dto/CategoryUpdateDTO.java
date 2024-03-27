package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class CategoryUpdateDTO {

    private String categoryNo;
    private String storeNo;
    private String categoryName;
    private String useYn;
    private Integer priority;

}
