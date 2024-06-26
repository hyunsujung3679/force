package com.hsj.force.domain.dto;

import com.hsj.force.domain.entity.TCategory;
import lombok.Data;

@Data
public class CategoryListDTO {

    private CommonLayoutDTO commonLayoutForm;
    private int no;
    private String categoryNo;
    private String categoryName;
    private String useYn;
    private int priority;

    public CategoryListDTO() {
    }

    public CategoryListDTO(TCategory category) {
        categoryNo = category.getCategoryNo();
        categoryName = category.getCategoryName();
        useYn = category.getUseYn();
        priority = category.getPriority();
    }

}
