package com.hsj.force.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class commonData {

    @NotNull
    private String insertId;
    @NotNull
    private String insertDate;
    @NotNull
    private String modifyId;
    @NotNull
    private String modifyDate;

}
