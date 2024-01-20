package com.hsj.force.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
public class OpenSave {

    private String openCloseNo;
    private String openCloseSeq;
    private Integer openMoney;
    private String insertId;
    private String modifyId;

}
