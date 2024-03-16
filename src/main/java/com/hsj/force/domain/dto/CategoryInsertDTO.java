package com.hsj.force.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryInsertDTO {

    private String categoryName;
    private String useYn;
    private String priorityStr;

}