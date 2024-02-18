package com.hsj.force.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OpenSaveDTO {

    private String openCloseNo;
    private String openCloseSeq;
    private int openMoney;
    private String insertId;
    private String modifyId;
    private String openMoneyStr;

}
