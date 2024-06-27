package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.BaseEntity;
import com.hsj.force.domain.entity.embedded.TIngredientHisId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "TINGREDIENTHIS")
@IdClass(TIngredientHisId.class)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TIngredientHis extends BaseEntity implements Persistable<TIngredientHisId> {

    @Id
    @Column(name = "INGREDIENT_NO")
    private String ingredientNo;

    @Id
    @Column(name = "INGREDIENT_SEQ")
    private String ingredientSeq;

    private Double inDeQuantity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "IN_DE_REASON_NO")
    private TInDeReason inDeReason;

    public TIngredientHis(String ingredientNo, String ingredientSeq, Double inDeQuantity, TInDeReason inDeReason) {
        this.ingredientNo = ingredientNo;
        this.ingredientSeq = ingredientSeq;
        this.inDeQuantity = inDeQuantity;
        this.inDeReason = inDeReason;
    }

    //==연관관계 메서드==//
    public void setInDeReason(TInDeReason inDeReason) {
        this.inDeReason = inDeReason;
        inDeReason.getIngredientHiss().add(this);
    }

    @Override
    public TIngredientHisId getId() {
        return new TIngredientHisId(ingredientNo, ingredientSeq);
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
