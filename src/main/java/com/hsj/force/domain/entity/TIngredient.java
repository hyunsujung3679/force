package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TINGREDIENT")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TIngredient extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "INGREDIENT_NO")
    private String ingredientNo;

    private String ingredientName;
    private double quantity;

    public TIngredient(String ingredientNo) {
        this.ingredientNo = ingredientNo;
    }

    public TIngredient(String ingredientNo, String ingredientName, double quantity) {
        this.ingredientNo = ingredientNo;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
    }

    @Override
    public String getId() {
        return ingredientNo;
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
