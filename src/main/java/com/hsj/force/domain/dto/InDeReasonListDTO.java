package com.hsj.force.domain.dto;

import com.hsj.force.domain.entity.TInDeReason;
import lombok.Data;

@Data
public class InDeReasonListDTO {

    private String inDeReasonNo;
    private String inDeReason;

    public InDeReasonListDTO() {
    }

    public InDeReasonListDTO(TInDeReason inDeReason) {
        this.inDeReasonNo = inDeReason.getInDeReasonNo();
        this.inDeReason = inDeReason.getInDeReason();
    }
}
