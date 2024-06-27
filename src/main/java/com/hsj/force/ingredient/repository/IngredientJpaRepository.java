package com.hsj.force.ingredient.repository;

import com.hsj.force.domain.entity.TIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientJpaRepository extends JpaRepository<TIngredient, String> {

    List<TIngredient> findAllByOrderByIngredientNo();

    TIngredient findFirstByOrderByIngredientNoDesc();

}
