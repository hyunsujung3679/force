package com.hsj.force.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TableFirstForm {

    private String storeName;
    private LocalDateTime businessDate;
    private String salesMan;
    private LocalDateTime currentDate;

    private List<TableSecondForm> tableSecondFormList;

}
