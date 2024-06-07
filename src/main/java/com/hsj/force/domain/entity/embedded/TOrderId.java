package com.hsj.force.domain.entity.embedded;

import com.hsj.force.domain.entity.TStore;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;

@Data
@Embeddable
public class TOrderId implements Serializable {

    @Column(name = "ORDER_NO")
    private String orderNo;

    @Column(name = "ORDER_SEQ")
    private String orderSeq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "STORE_NO")
    private TStore store;
}
