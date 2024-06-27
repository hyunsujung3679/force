package com.hsj.force.domain.entity.embedded;

import lombok.Data;

import java.io.Serializable;

@Data
public class TMenuPriceId implements Serializable {

    private String menuNo;
    private String menuSeq;

    public TMenuPriceId() {}

    public TMenuPriceId(String menuNo, String menuSeq) {
        this.menuNo = menuNo;
        this.menuSeq = menuSeq;
    }
}
