package com.hsj.force.category.service;

import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryDTO;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.TableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CommonMapper commonMapper;

    public CategoryDTO selectCategoryInfo(User loginMember) {

        String storeName = commonMapper.selectStoreName(loginMember.getStoreNo());

        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        CategoryDTO categoryForm = new CategoryDTO();
        categoryForm.setCommonLayoutForm(commonLayoutForm);

        return categoryForm;
    }
}
