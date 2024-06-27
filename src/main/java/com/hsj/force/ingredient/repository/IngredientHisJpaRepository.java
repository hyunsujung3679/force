package com.hsj.force.ingredient.repository;

import com.hsj.force.domain.entity.TIngredient;
import com.hsj.force.domain.entity.TIngredientHis;
import com.hsj.force.domain.entity.embedded.TIngredientHisId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface IngredientHisJpaRepository extends JpaRepository<TIngredientHis, TIngredientHisId> {

    TIngredientHis findFirstByIngredientNoOrderByIngredientSeqDesc(@Param("ingredientNo") String ingredientNo);
}
