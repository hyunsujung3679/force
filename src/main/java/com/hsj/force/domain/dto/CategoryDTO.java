package com.hsj.force.domain.dto;

import com.hsj.force.domain.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryDTO extends Category {

    private CommonLayoutDTO commonLayoutForm;
}
