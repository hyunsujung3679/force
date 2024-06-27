//package com.hsj.force.ingredient.service;
//
//import com.hsj.force.domain.dto.IngredientInsertDTO;
//import com.hsj.force.domain.dto.IngredientListDTO;
//import com.hsj.force.domain.dto.IngredientUpdateDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class IngredientServiceTest {
//
//    @Autowired
//    private IngredientService ingredientService;
//    private User loginMember = null;
//
//    @BeforeEach
//    public void beforeEach() {
//        loginMember = new User();
//        loginMember.setStoreNo("S001");
//        loginMember.setUserId("SUSU");
//        loginMember.setUserNo("U001");
//        loginMember.setUserName("정현수");
//        loginMember.setPhoneNum("01027287526");
//        loginMember.setPassword("1234");
//        loginMember.setUseYn("1");
//    }
//
//    @Test
//    void selectIngredientList() {
//        List<IngredientListDTO> ingredientListList =  ingredientService.selectIngredientList(loginMember.getStoreNo());
//        assertThat(ingredientListList).isNotNull();
//    }
//
//    @Test
//    void selectIngredientInfo() {
//        Map<String, Object> map = ingredientService.selectIngredientInfo(loginMember);
//        assertThat(map.get("commonLayoutForm")).isNotNull();
//        assertThat(map.get("ingredientList")).isNotNull();
//    }
//
//    @Test
//    void insertIngredient() {
//        IngredientInsertDTO ingredient = new IngredientInsertDTO();
//        ingredient.setIngredientName("TEST");
//        ingredient.setQuantity(10.0);
//
//        int result = ingredientService.insertIngredient(loginMember, ingredient);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void selectIngredientUpdateInfo() {
//        Map<String, Object> map = ingredientService.selectIngredientUpdateInfo(loginMember, "I001");
//        assertThat(map.get("commonLayoutForm")).isNotNull();
//        assertThat(map.get("ingredient")).isNotNull();
//        assertThat(map.get("inDeReasonList")).isNotNull();
//    }
//
//    @Test
//    void updateIngredient() {
//        IngredientUpdateDTO ingredient = new IngredientUpdateDTO();
//        ingredient.setIngredientNo("I001");
//        ingredient.setIngredientName("TEST-UPDATE");
//        ingredient.setInDeQuantity(5.0);
//        ingredient.setInDeReasonNo("ID003");
//
//        int result = ingredientService.updateIngredient(loginMember, ingredient);
//        assertThat(result).isEqualTo(1);
//    }
//}