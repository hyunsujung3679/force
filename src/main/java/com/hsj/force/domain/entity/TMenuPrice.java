package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.TMenuPriceId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TINGREDIENTHIS")
@Getter
@Setter
public class TMenuPrice {

    @EmbeddedId
    private TMenuPriceId menuPriceId;

    private int menuPrice;
    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;

}
