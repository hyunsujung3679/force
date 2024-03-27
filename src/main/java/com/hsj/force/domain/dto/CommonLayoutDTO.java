package com.hsj.force.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CommonLayoutDTO {

    private String storeName;
    private String salesMan;
    private LocalDateTime businessDate;
    private LocalDateTime currentDate;

}
