package com.hsj.force.domain.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class TOpenCloseId implements Serializable {

    @Column(name = "OPEN_CLOSE_NO")
    private String openCloseNo;

    @Column(name = "STORE_NO")
    private String storeNo;
}
