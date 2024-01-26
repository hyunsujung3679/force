package com.hsj.force.domain.form;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonLayoutForm {

    private String storeName;
    private LocalDateTime businessDate;
    private String salesMan;
    private LocalDateTime currentDate;

}
