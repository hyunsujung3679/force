package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.BaseEntity;
import com.hsj.force.domain.entity.embedded.TMenuIngredientId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "TMENUINGREDIENT")
@IdClass(TMenuIngredientId.class)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TMenuIngredient extends BaseEntity implements Persistable<TMenuIngredientId> {

    @Id
    @Column(name = "INGREDIENT_NO")
    private String ingredientNo;

    @Id
    @Column(name = "MENU_NO")
    private String menuNo;

    private double quantity;

    public TMenuIngredient(String ingredientNo, String menuNo, double quantity) {
        this.ingredientNo = ingredientNo;
        this.menuNo = menuNo;
        this.quantity = quantity;
    }

    @Override
    public TMenuIngredientId getId() {
        return new TMenuIngredientId(ingredientNo, menuNo);
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
