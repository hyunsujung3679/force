package com.hsj.force.domain.entity.embedded;

import lombok.Data;

import java.io.Serializable;

@Data
public class TOrderId implements Serializable {

    private String orderNo;
    private String orderSeq;

    public TOrderId() {}

    public TOrderId(String orderNo, String orderSeq) {
        this.orderNo = orderNo;
        this.orderSeq = orderSeq;
    }
}
