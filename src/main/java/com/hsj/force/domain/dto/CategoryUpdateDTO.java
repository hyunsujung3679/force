package com.hsj.force.domain.dto;

import com.hsj.force.domain.Category;
import lombok.Data;

@Data
public class CategoryUpdateDTO {

    private String categoryNo;
    private String storeNo;
    private String categoryName;
    private String useYn;
    private String priorityStr;
    private int priority;

}
