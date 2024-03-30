package com.hsj.force.ingredient.repository;

import com.hsj.force.domain.InDeReason;
import com.hsj.force.domain.Ingredient;
import com.hsj.force.domain.IngredientHis;
import com.hsj.force.domain.dto.IngredientListDTO;
import com.hsj.force.domain.dto.IngredientUpdateDTO;
import com.hsj.force.domain.dto.MenuIngredientListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IngredientMapper {
    String selectIngredientSeq(IngredientHis ingredientHis);

    int insertIngredientHis(IngredientHis ingredientHis);

    int updateIngredient(Ingredient ingredient);

    List<MenuIngredientListDTO> selectMenuIngredientList(String storeNo, String orderNo, String menuNo);

    List<MenuIngredientListDTO> selectMenuIngredientListByMenuForm(String storeNo);

    List<IngredientListDTO> selectIngredientList(String storeNo);

    List<IngredientListDTO> selectIngredientListV2(String storeNo);

    String selectIngredientNo(String storeNo);

    int insertIngredient(Ingredient ingredient);

    IngredientUpdateDTO selectIngredient(String storeNo, String ingredientNo);

    List<InDeReason> selectInDeReasonList();

    double selectQuantity(Ingredient ingredient);
}
