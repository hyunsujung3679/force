package com.hsj.force.domain.dto;

import com.hsj.force.domain.OpenClose;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpenSaveDTO extends OpenClose {

    private String openMoneyStr;

}
