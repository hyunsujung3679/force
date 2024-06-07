package com.hsj.force.domain.entity.embedded;

import com.hsj.force.domain.entity.TIngredient;
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
public class TIngredientHisId implements Serializable {

    @Column(name = "INGREDIENT_NO")
    private String ingredientNo;

    @Column(name = "INGREDIENT_SEQ")
    private String ingredientSeq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "STORE_NO")
    private TStore store;
}
