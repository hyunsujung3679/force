package com.hsj.force.domain.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class TOrderId implements Serializable {

    @Column(name = "ORDER_NO")
    private String orderNo;

    @Column(name = "ORDER_SEQ")
    private String orderSeq;

}
