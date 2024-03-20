package com.hsj.force.ingredient.service;

import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.Ingredient;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.IngredientListDTO;
import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.domain.dto.MenuIngredientDTO;
import com.hsj.force.ingredient.repository.IngredientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientMapper ingredientMapper;
    private final CommonMapper commonMapper;

    public List<Ingredient> selectIngredientList(String storeNo) {
        return ingredientMapper.selectIngredientList(storeNo);
    }

    public Map<String, Object> selectIngredientInfo(User loginMember) {

        Map<String, Object> map = new HashMap<>();
        String storeName = commonMapper.selectStoreName(loginMember.getStoreNo());
        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        List<IngredientListDTO> ingredientList = ingredientMapper.selectIngredientListV2(loginMember.getStoreNo());
        for(int i = 0; i < ingredientList.size(); i++) {
            IngredientListDTO ingredient = ingredientList.get(i);
            ingredient.setNo(i + 1);
        }

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("ingredientList", ingredientList);

        return map;
    }
}
