package com.hsj.force.ingredient.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.common.repository.CommonRepository;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.InDeReason;
import com.hsj.force.domain.Ingredient;
import com.hsj.force.domain.IngredientHis;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.*;
import com.hsj.force.domain.entity.TInDeReason;
import com.hsj.force.domain.entity.TIngredient;
import com.hsj.force.domain.entity.TIngredientHis;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.domain.entity.embedded.CommonData;
import com.hsj.force.domain.entity.embedded.TIngredientHisId;
import com.hsj.force.domain.entity.embedded.TIngredientId;
import com.hsj.force.ingredient.repository.IngredientMapper;
import com.hsj.force.ingredient.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final CommonRepository commonRepository;

    private final CommonService commonService;
    private final IngredientMapper ingredientMapper;
    private final CommonMapper commonMapper;

    public List<IngredientListDTO> selectIngredientList(String storeNo) {
        List<TIngredient> ingredients = ingredientRepository.findIngredientV2(storeNo);
        return ingredients.stream()
                .map(i -> new IngredientListDTO(i))
                .collect(Collectors.toList());
    }

    public Map<String, Object> selectIngredientInfo(TUser loginMember) {

        String storeNo = loginMember.getStore().getStoreNo();
        String userName = loginMember.getUserName();

        Map<String, Object> map = new HashMap<>();

        String storeName = "";
        if(commonRepository.findStoreName(storeNo).isPresent()) {
            storeName = commonRepository.findStoreName(storeNo).get();
        }

        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(userName);
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        List<TIngredient> ingredients = ingredientRepository.findIngredientV2(storeNo);
        List<IngredientListDTO> ingredientList = ingredients.stream()
                .map(i -> new IngredientListDTO(i))
                .collect(Collectors.toList());
        for(int i = 0; i < ingredientList.size(); i++) {
            IngredientListDTO ingredient = ingredientList.get(i);
            ingredient.setNo(i + 1);
        }

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("ingredientList", ingredientList);

        return map;
    }

    public int insertIngredient(TUser loginMember, IngredientInsertDTO ingredientInsertDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String ingredientName = ingredientInsertDTO.getIngredientName();
        double quantity = ingredientInsertDTO.getQuantity();
        String userId = loginMember.getUserId();

        TIngredient ingredient = new TIngredient();

        TIngredientId ingredientId = new TIngredientId();
        String ingredientNo = "";
        if(ingredientRepository.findIngredientNo(storeNo).isPresent()) {
            ingredientNo = ingredientRepository.findIngredientNo(storeNo).get();
        }
        String nextIngredientNo = ComUtils.getNextNo(ingredientNo, Constants.INGREDIENT_NO_PREFIX);
        ingredientId.setIngredientNo(nextIngredientNo);
        ingredientId.setStoreNo(storeNo);

        ingredient.setIngredientId(ingredientId);
        ingredient.setIngredientName(ingredientName);
        ingredient.setQuantity(quantity);
        ingredient.setInsertId(userId);
        ingredient.setInsertDate(LocalDateTime.now());
        ingredient.setModifyId(userId);
        ingredient.setModifyDate(LocalDateTime.now());

        ingredientRepository.saveIngredient(ingredient);

        TIngredientHis ingredientHis = new TIngredientHis();

        TIngredientHisId ingredientHisId = new TIngredientHisId();
        String ingredientSeq = "";
        if(ingredientRepository.findIngredientSeq(nextIngredientNo, storeNo).isPresent()) {
            ingredientSeq = ingredientRepository.findIngredientSeq(nextIngredientNo, storeNo).get();
        }
        ingredientHisId.setIngredientNo(nextIngredientNo);
        ingredientHisId.setIngredientSeq(ComUtils.getNextSeq(ingredientSeq));
        ingredientHisId.setStoreNo(storeNo);

        ingredientHis.setIngredientHisId(ingredientHisId);
        ingredientHis.setInDeQuantity(quantity);
        ingredientHis.setInDeReason(ingredientRepository.findInDeReason("ID003"));
        ingredientHis.setCommonData(new CommonData(userId, LocalDateTime.now(), userId, LocalDateTime.now()));

        ingredientRepository.saveIngredientHis(ingredientHis);

        return 1;
    }

    public Map<String, Object> selectIngredientUpdateInfo(TUser loginMember, String ingredientNo) {

        String storeNo = loginMember.getStore().getStoreNo();

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);

        TIngredientId ingredientId = new TIngredientId();
        ingredientId.setIngredientNo(ingredientNo);
        ingredientId.setStoreNo(storeNo);
        TIngredient ingredient = ingredientRepository.findIngredient(ingredientId);

        IngredientUpdateDTO ingredientUpdateDTO = new IngredientUpdateDTO();
        ingredientUpdateDTO.setIngredientNo(ingredientNo);
        ingredientUpdateDTO.setIngredientName(ingredient.getIngredientName());

        List<TInDeReason> inDeReasons = ingredientRepository.findInDeReasonV1();
        List<InDeReasonListDTO> inDeReasonList = inDeReasons.stream()
                .map(i -> new InDeReasonListDTO(i))
                .collect(Collectors.toList());

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("ingredient", ingredientUpdateDTO);
        map.put("inDeReasonList", inDeReasonList);

        return map;
    }

    public int updateIngredient(TUser loginMember, IngredientUpdateDTO ingredientUpdateDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String ingredientNo = ingredientUpdateDTO.getIngredientNo();
        double inDeQuantity = ingredientUpdateDTO.getInDeQuantity();
        String userId = loginMember.getUserId();
        String inDeReasonNo = ingredientUpdateDTO.getInDeReasonNo();
        String ingredientName = ingredientUpdateDTO.getIngredientName();

        TIngredientId ingredientId = new TIngredientId();
        ingredientId.setIngredientNo(ingredientNo);
        ingredientId.setStoreNo(storeNo);

        TIngredient ingredient = ingredientRepository.findIngredient(ingredientId);
        double quantity = ingredientRepository.findIngredientQuantity(ingredientNo, storeNo);
        ingredient.setIngredientName(ingredientName);
        ingredient.setQuantity(quantity + inDeQuantity);
        ingredient.setModifyId(userId);
        ingredient.setModifyDate(LocalDateTime.now());

        TIngredientHis ingredientHis = new TIngredientHis();

        TIngredientHisId ingredientHisId = new TIngredientHisId();
        ingredientHisId.setIngredientNo(ingredientNo);
        String ingredientSeq = null;
        if (ingredientRepository.findIngredientSeq(ingredientNo, storeNo).isPresent()) {
            ingredientSeq = ingredientRepository.findIngredientSeq(ingredientNo, storeNo).get();
        }
        ingredientHisId.setIngredientSeq(ComUtils.getNextSeq(ingredientSeq));
        ingredientHisId.setStoreNo(storeNo);

        ingredientHis.setIngredientHisId(ingredientHisId);
        ingredientHis.setInDeReason(ingredientRepository.findInDeReason(inDeReasonNo));
        ingredientHis.setInDeQuantity(ingredientUpdateDTO.getInDeQuantity());
        ingredientHis.setCommonData(new CommonData(userId, LocalDateTime.now(), userId, LocalDateTime.now()));

        ingredientRepository.saveIngredientHis(ingredientHis);

        return 1;

    }
}
