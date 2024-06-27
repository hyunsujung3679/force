package com.hsj.force.ingredient.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.dto.*;
import com.hsj.force.domain.entity.TInDeReason;
import com.hsj.force.domain.entity.TIngredient;
import com.hsj.force.domain.entity.TIngredientHis;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.ingredient.repository.InDeReasonJpaRepository;
import com.hsj.force.ingredient.repository.IngredientHisJpaRepository;
import com.hsj.force.ingredient.repository.IngredientJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientJpaRepository ingredientJpaRepository;
    private final IngredientHisJpaRepository ingredientHisJpaRepository;
    private final InDeReasonJpaRepository inDeReasonJpaRepository;

    private final CommonService commonService;

    private final MessageSource messageSource;

    public List<IngredientListDTO> selectIngredientList() {
        List<TIngredient> ingredients = ingredientJpaRepository.findAllByOrderByIngredientNo();
        return ingredients.stream()
                .map(i -> new IngredientListDTO(i))
                .collect(Collectors.toList());
    }

    public Map<String, Object> selectIngredientInfo(TUser loginMember) {

        Map<String, Object> map = new HashMap<>();

        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(messageSource.getMessage("word.store.name",null, null));
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        List<TIngredient> ingredients = ingredientJpaRepository.findAllByOrderByIngredientNo();
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

    public int insertIngredient(IngredientInsertDTO ingredientInsertDTO) {

        String ingredientName = ingredientInsertDTO.getIngredientName();
        double quantity = ingredientInsertDTO.getQuantity();

        TIngredient ingredient = ingredientJpaRepository.findFirstByOrderByIngredientNoDesc();
        String ingredientNo = ingredient.getIngredientNo();
        String nextIngredientNo = ComUtils.getNextNo(ingredientNo, Constants.INGREDIENT_NO_PREFIX);

        ingredientJpaRepository.save(new TIngredient(nextIngredientNo, ingredientName, quantity));

        TIngredientHis ingredientHis = ingredientHisJpaRepository.findFirstByIngredientNoOrderByIngredientSeqDesc(nextIngredientNo);

        TInDeReason inDeReason = null;
        Optional<TInDeReason> optionalInDeReason = inDeReasonJpaRepository.findById("ID003");
        if(optionalInDeReason.isPresent()) {
            inDeReason = optionalInDeReason.get();
        }
        ingredientHisJpaRepository.save(new TIngredientHis(nextIngredientNo, ComUtils.getNextSeq(ingredientHis == null? "" : ingredientHis.getIngredientSeq()), quantity, inDeReason));

        return 1;
    }

    public Map<String, Object> selectIngredientUpdateInfo(TUser loginMember, String ingredientNo) {

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);

        TIngredient ingredient = null;
        Optional<TIngredient> optionalIngredient = ingredientJpaRepository.findById(ingredientNo);
        if(optionalIngredient.isPresent()) {
            ingredient = optionalIngredient.get();
        }

        IngredientUpdateDTO ingredientUpdateDTO = new IngredientUpdateDTO();
        ingredientUpdateDTO.setIngredientNo(ingredientNo);
        ingredientUpdateDTO.setIngredientName(ingredient.getIngredientName());

        List<TInDeReason> inDeReasons = inDeReasonJpaRepository.findAll();
        List<InDeReasonListDTO> inDeReasonList = inDeReasons.stream()
                .map(i -> new InDeReasonListDTO(i))
                .collect(Collectors.toList());

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("ingredient", ingredientUpdateDTO);
        map.put("inDeReasonList", inDeReasonList);

        return map;
    }

    public int updateIngredient(IngredientUpdateDTO ingredientUpdateDTO) {

        String ingredientNo = ingredientUpdateDTO.getIngredientNo();
        double inDeQuantity = ingredientUpdateDTO.getInDeQuantity();
        String inDeReasonNo = ingredientUpdateDTO.getInDeReasonNo();
        String ingredientName = ingredientUpdateDTO.getIngredientName();

        TIngredient ingredient = null;
        Optional<TIngredient> optionalIngredient = ingredientJpaRepository.findById(ingredientNo);
        if(optionalIngredient.isPresent()) {
            ingredient = optionalIngredient.get();
        }
        ingredient.setIngredientName(ingredientName);
        ingredient.setQuantity(ingredient.getQuantity() + inDeQuantity);

        TIngredientHis ingredientHis = ingredientHisJpaRepository.findFirstByIngredientNoOrderByIngredientSeqDesc(ingredientNo);

        TInDeReason inDeReason = null;
        Optional<TInDeReason> optionalInDeReason = inDeReasonJpaRepository.findById(inDeReasonNo);
        if(optionalInDeReason.isPresent()) {
            inDeReason = optionalInDeReason.get();
        }
        ingredientHisJpaRepository.save(
                new TIngredientHis(
                        ingredientNo,
                        ComUtils.getNextSeq(ingredientHis == null? "" : ingredientHis.getIngredientSeq()),
                        ingredientUpdateDTO.getInDeQuantity(),
                        inDeReason));
        return 1;

    }
}
