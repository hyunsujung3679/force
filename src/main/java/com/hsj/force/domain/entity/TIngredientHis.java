package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.TIngredientHisId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "TINGREDIENTHIS")
@Getter
@Setter
public class TIngredientHis {

    @EmbeddedId
    private TIngredientHisId ingredientHisId;

    private int inDeQuantity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "IN_DE_REASON_NO")
    private TInDeReason inDeReason;

    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;

    //==연관관계 메서드==//
    public void setInDeReason(TInDeReason inDeReason) {
        this.inDeReason = inDeReason;
        inDeReason.getIngredientHiss().add(this);
    }

}
