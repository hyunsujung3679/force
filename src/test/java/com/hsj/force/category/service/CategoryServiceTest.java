package com.hsj.force.category.service;

import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryInsertDTO;
import com.hsj.force.domain.dto.CategoryListDTO;
import com.hsj.force.domain.dto.CategoryUpdateDTO;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    private User loginMember = null;

    @BeforeEach
    public void beforeEach() {
        loginMember = new User();
        loginMember.setStoreNo("S001");
        loginMember.setUserId("SUSU");
        loginMember.setUserNo("U001");
        loginMember.setUserName("정현수");
        loginMember.setPhoneNum("01027287526");
        loginMember.setPassword("1234");
        loginMember.setUseYn("1");
    }

    @Test
    void selectCategoryListInfo() {
        Map<String, Object> map = categoryService.selectCategoryListInfo(loginMember);
        assertThat(map.get("commonLayoutForm")).isNotNull();
        assertThat(map.get("categoryList")).isNotNull();

    }

    @Test
    void insertCategory() {

       CategoryInsertDTO categoryInsertDTO = new CategoryInsertDTO();
       categoryInsertDTO.setCategoryName("0404-1");
       categoryInsertDTO.setPriority(99);
       categoryInsertDTO.setUseYn("1");

       int result = categoryService.insertCategory(loginMember, categoryInsertDTO);
       assertThat(result).isEqualTo(1);
    }

    @Test
    void updateCategory() {

        CategoryUpdateDTO categoryUpdateDTO = new CategoryUpdateDTO();
        categoryUpdateDTO.setCategoryNo("C001");
        categoryUpdateDTO.setCategoryName("0404-1");
        categoryUpdateDTO.setPriority(99);
        categoryUpdateDTO.setUseYn("1");

        int result = categoryService.updateCategory(loginMember, categoryUpdateDTO);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void selectCategoryUpdateInfo() {
        Map<String, Object> map = categoryService.selectCategoryUpdateInfo(loginMember, "C001");
        assertThat(map.get("commonLayoutForm")).isNotNull();
        assertThat(map.get("category")).isNotNull();
    }

    @Test
    void selectCategoryList() {
        List<CategoryListDTO> categoryList = categoryService.selectCategoryList(loginMember.getStoreNo());
        assertThat(categoryList).isNotNull();
    }
}