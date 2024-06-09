package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.TIngredientId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TINGREDIENT")
@Getter
@Setter
public class TIngredient {

    @EmbeddedId
    private TIngredientId ingredientId;

    private String ingredientName;
    private int quantity;
    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;

}
