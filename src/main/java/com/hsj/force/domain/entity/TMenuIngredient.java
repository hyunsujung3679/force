package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.TMenuIngredientId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TMENUINGREDIENT")
@Getter
@Setter
public class TMenuIngredient {

    @EmbeddedId
    private TMenuIngredientId menuIngredientId;

    private int quantity;
    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;
}
