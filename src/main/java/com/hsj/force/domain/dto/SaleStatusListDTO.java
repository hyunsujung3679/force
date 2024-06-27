package com.hsj.force.domain.dto;

import com.hsj.force.domain.entity.TSaleStatus;
import lombok.Data;

@Data
public class SaleStatusListDTO {

    private String saleStatusNo;
    private String saleStatus;

    public SaleStatusListDTO(TSaleStatus saleStatus) {
        this.saleStatusNo = saleStatus.getSaleStatusNo();
        this.saleStatus = saleStatus.getSaleStatus();
    }
}
