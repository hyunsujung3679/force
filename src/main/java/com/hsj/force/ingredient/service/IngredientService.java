package com.hsj.force.ingredient.service;

import com.hsj.force.domain.Ingredient;
import com.hsj.force.ingredient.repository.IngredientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientMapper ingredientMapper;

    public List<Ingredient> selectIngredientList(String storeNo) {
        return ingredientMapper.selectIngredientList(storeNo);
    }
}
