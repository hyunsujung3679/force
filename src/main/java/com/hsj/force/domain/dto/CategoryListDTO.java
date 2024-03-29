package com.hsj.force.domain.dto;

import com.hsj.force.domain.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryListDTO extends Category {

    private CommonLayoutDTO commonLayoutForm;
    private List<Category> categoryList;
    private int maxPriority;
}
