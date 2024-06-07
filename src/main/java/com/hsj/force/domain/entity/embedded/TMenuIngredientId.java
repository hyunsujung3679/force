package com.hsj.force.domain.entity.embedded;

import com.hsj.force.domain.entity.TIngredient;
import com.hsj.force.domain.entity.TMenu;
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
public class TMenuIngredientId implements Serializable {

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "INGREDIENT_NO")
//    private TIngredient ingredient;

    @Column(name = "INGREDIENT_NO")
    private String ingredientNo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MENU_NO")
    private TMenu menu;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "STORE_NO")
    private TStore store;
}
