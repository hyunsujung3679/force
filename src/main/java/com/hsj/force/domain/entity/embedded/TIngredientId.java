package com.hsj.force.domain.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class TIngredientId implements Serializable {

    @Column(name = "INGREDIENT_NO")
    private String ingredientNo;

    @Column(name = "STORE_NO")
    private String storeNo;

}
