package com.hsj.force.category.service;

import com.hsj.force.category.repository.CategoryMapper;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryListDTO;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CommonService commonService;
    @Autowired
    private CategoryMapper categoryMapper;
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
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);
        List<CategoryListDTO> categoryList = categoryMapper.selectCategoryList(loginMember.getStoreNo());

        for(int i = 0; i < categoryList.size(); i++) {
            CategoryListDTO category = categoryList.get(i);
            category.setNo(i + 1);
        }

        Assertions.assertThat(commonLayoutForm).isNotNull();
        Assertions.assertThat(categoryList).isNotNull();
    }

    @Test
    void insertCategory() {
    }

    @Test
    void updateCategory() {
    }

    @Test
    void selectCategoryUpdateInfo() {
    }

    @Test
    void selectCategoryList() {
    }
}