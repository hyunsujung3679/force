package com.hsj.force.category.service;

import com.hsj.force.category.repository.CategoryMapper;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.Category;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CommonService commonService;
    private final CategoryMapper categoryMapper;

    public Map<String, Object> selectCategoryListInfo(User loginMember) {

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);
        List<CategoryListDTO> categoryList = categoryMapper.selectCategoryList(loginMember.getStoreNo());

        for(int i = 0; i < categoryList.size(); i++) {
            CategoryListDTO category = categoryList.get(i);
            category.setNo(i + 1);
        }

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("categoryList", categoryList);

        return map;
    }

    public int insertCategory(User loginMember, CategoryInsertDTO categoryInsertDTO) {

        Category category = new Category();
        category.setStoreNo(loginMember.getStoreNo());
        category.setCategoryName(categoryInsertDTO.getCategoryName());
        category.setPriority(categoryInsertDTO.getPriority());
        category.setUseYn(categoryInsertDTO.getUseYn());
        String categoryNo = categoryMapper.selectCategoryNo(category.getStoreNo());
        category.setCategoryNo(ComUtils.getNextNo(categoryNo, Constants.CATEGORY_NO_PREFIX));
        category.setInsertId(loginMember.getUserId());
        category.setModifyId(loginMember.getUserId());

        checkPriority(loginMember, category);

        return categoryMapper.insertCategory(category);
    }


    public int updateCategory(User loginMember, CategoryUpdateDTO categoryUpdateDTO) {

        Category category = new Category();
        category.setCategoryNo(categoryUpdateDTO.getCategoryNo());
        category.setCategoryName(categoryUpdateDTO.getCategoryName());
        category.setUseYn(categoryUpdateDTO.getUseYn());
        category.setPriority(categoryUpdateDTO.getPriority());
        category.setStoreNo(loginMember.getStoreNo());
        category.setModifyId(loginMember.getUserId());

        checkPriority(loginMember, category);

        return categoryMapper.updateCategory(category);
    }

    public Map<String, Object> selectCategoryUpdateInfo(User loginMember, String categoryNo) {

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);
        CategoryUpdateDTO categoryUpdateDTO = categoryMapper.selectCategory(loginMember.getStoreNo(), categoryNo);

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("category", categoryUpdateDTO);

        return map;
    }

    public List<CategoryListDTO> selectCategoryList(String storeNo) {
        return categoryMapper.selectCategoryList(storeNo);
    }

    private void checkPriority(User loginMember, Category category) {
        Integer priority = categoryMapper.selectPriority(category);
        if(priority != null) {
            int maxPriority = categoryMapper.selectMaxPriority(category);
            CategoryUpdateDTO categoryUpdateDTO = new CategoryUpdateDTO();
            categoryUpdateDTO.setMaxPriority(maxPriority + 1);
            categoryUpdateDTO.setModifyId(loginMember.getUserId());
            categoryUpdateDTO.setStoreNo(loginMember.getStoreNo());
            categoryUpdateDTO.setPriority(category.getPriority());
            categoryMapper.updatePriority(categoryUpdateDTO);
        }
    }
}
