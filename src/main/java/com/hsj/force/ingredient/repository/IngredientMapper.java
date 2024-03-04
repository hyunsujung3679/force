package com.hsj.force.ingredient.repository;

import com.hsj.force.domain.Ingredient;
import com.hsj.force.domain.IngredientHis;
import com.hsj.force.domain.dto.MenuIngredientDTO;
import com.hsj.force.domain.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IngredientMapper {
    String selectIngredientSeq(IngredientHis ingredientHis);

    int insertIngredientHis(IngredientHis ingredientHis);

    int updateIngredient(Ingredient ingredient);

    List<MenuIngredientDTO> selectMenuIngredientList(String storeNo, String orderNo, String menuNo);

}
