package com.hsj.force.ingredient.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.InDeReason;
import com.hsj.force.domain.Ingredient;
import com.hsj.force.domain.IngredientHis;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.IngredientInsertDTO;
import com.hsj.force.domain.dto.IngredientListDTO;
import com.hsj.force.domain.dto.IngredientUpdateDTO;
import com.hsj.force.ingredient.repository.IngredientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class IngredientService {

    private final CommonService commonService;
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

    public int insertIngredient(User loginMember, IngredientInsertDTO ingredientInsertDTO) {

        int ingredientSaveResult = 0;
        int ingredientHisSaveResult = 0;

        Ingredient ingredient = new Ingredient();
        String ingredientNo = ingredientMapper.selectIngredientNo(loginMember.getStoreNo());
        String nextIngredientNo = ComUtils.getNextNo(ingredientNo, Constants.INGREDIENT_NO_PREFIX);
        ingredient.setIngredientNo(nextIngredientNo);
        ingredient.setStoreNo(loginMember.getStoreNo());
        ingredient.setIngredientName(ingredientInsertDTO.getIngredientName());
        double quantity = ingredientInsertDTO.getQuantity();
        ingredient.setQuantity(quantity);
        ingredient.setInsertId(loginMember.getUserId());
        ingredient.setModifyId(loginMember.getUserId());
        ingredientSaveResult = ingredientMapper.insertIngredient(ingredient);

        IngredientHis ingredientHis = new IngredientHis();
        ingredientHis.setIngredientNo(nextIngredientNo);
        ingredientHis.setStoreNo(loginMember.getStoreNo());
        String ingredientSeq = ingredientMapper.selectIngredientSeq(ingredientHis);
        ingredientHis.setIngredientSeq(ComUtils.getNextSeq(ingredientSeq));
        ingredientHis.setInDeQuantity(quantity);
        ingredientHis.setInDeReasonNo("ID003");
        ingredientHis.setInsertId(loginMember.getUserId());
        ingredientHis.setModifyId(loginMember.getUserId());
        ingredientHisSaveResult = ingredientMapper.insertIngredientHis(ingredientHis);

        if(ingredientSaveResult > 0 && ingredientHisSaveResult > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public Map<String, Object> selectIngredientUpdateInfo(User loginMember, String ingredientNo) {

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);
        IngredientUpdateDTO ingredientUpdateDTO = ingredientMapper.selectIngredient(loginMember.getStoreNo(), ingredientNo);
        List<InDeReason> inDeReasonList = ingredientMapper.selectInDeReasonList();

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("ingredient", ingredientUpdateDTO);
        map.put("inDeReasonList", inDeReasonList);

        return map;
    }

    public int updateIngredient(User loginMember, IngredientUpdateDTO ingredientUpdateDTO) {

        int ingredientUpdateResult = 0;
        int ingredientHisSaveResult = 0;

        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientNo(ingredientUpdateDTO.getIngredientNo());
        ingredient.setStoreNo(loginMember.getStoreNo());
        double quantity = ingredientMapper.selectQuantity(ingredient);
        ingredient.setQuantity(quantity + ingredientUpdateDTO.getInDeQuantity());
        ingredient.setModifyId(loginMember.getUserId());

        ingredientUpdateResult = ingredientMapper.updateIngredient(ingredient);

        IngredientHis ingredientHis = new IngredientHis();
        ingredientHis.setIngredientNo(ingredientUpdateDTO.getIngredientNo());
        ingredientHis.setStoreNo(loginMember.getStoreNo());
        String ingredientSeq = ingredientMapper.selectIngredientSeq(ingredientHis);
        ingredientHis.setIngredientSeq(ComUtils.getNextSeq(ingredientSeq));
        ingredientHis.setInDeQuantity(ingredientUpdateDTO.getInDeQuantity());
        ingredientHis.setInDeReasonNo(ingredientUpdateDTO.getInDeReasonNo());
        ingredientHis.setInsertId(loginMember.getUserId());
        ingredientHis.setModifyId(loginMember.getUserId());
        ingredientHisSaveResult = ingredientMapper.insertIngredientHis(ingredientHis);

        if(ingredientUpdateResult > 0 && ingredientHisSaveResult > 0) {
            return 1;
        } else {
            return 0;
        }

    }
}
