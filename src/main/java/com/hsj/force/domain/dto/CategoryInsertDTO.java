package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class CategoryInsertDTO {

    private String categoryName;
    private String useYn;
    private int priority;

}
