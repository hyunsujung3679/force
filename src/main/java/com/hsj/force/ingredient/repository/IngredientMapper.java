package com.hsj.force.ingredient.repository;

import com.hsj.force.domain.Ingredient;
import com.hsj.force.domain.IngredientHis;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IngredientMapper {
    String selectIngredientSeq(IngredientHis ingredientHis);

    int insertIngredientHis(IngredientHis ingredientHis);

    int updateIngredient(Ingredient ingredient);
}
