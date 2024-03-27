package com.hsj.force.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Data
public class CategoryInsertDTO {

    private String categoryName;
    private String useYn;
    private Integer priority;

}
