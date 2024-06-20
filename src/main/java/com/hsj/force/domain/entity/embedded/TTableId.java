package com.hsj.force.domain.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
public class TTableId implements Serializable {

//    @Column(name = "TABLE_NO")
    private String tableNo;

//    @Column(name = "STORE_NO")
    private String storeNo;

}
