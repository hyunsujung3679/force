package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.CommonData;
import com.hsj.force.domain.entity.embedded.TMenuPriceId;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TMENUPRICE")
@Getter
@Setter
public class TMenuPrice {

    @EmbeddedId
    private TMenuPriceId menuPriceId;

    private int salePrice;

    @Embedded
    private CommonData commonData;

}
