package com.hsj.force.category.service;

import com.hsj.force.category.repository.CategoryMapper;
import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.Category;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryDTO;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CommonMapper commonMapper;
    private final CategoryMapper categoryMapper;

    public CategoryDTO selectCategoryInfo(User loginMember) {

        String storeName = commonMapper.selectStoreName(loginMember.getStoreNo());
        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        List<Category> categoryList = categoryMapper.selectCategoryList(loginMember.getStoreNo());

        CategoryDTO categoryForm = new CategoryDTO();
        categoryForm.setCommonLayoutForm(commonLayoutForm);
        categoryForm.setCategoryList(categoryList);

        return categoryForm;
    }
}
