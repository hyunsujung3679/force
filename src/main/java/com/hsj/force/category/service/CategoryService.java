package com.hsj.force.category.service;

import com.hsj.force.category.repository.CategoryMapper;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.Category;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryUpdateDTO;
import com.hsj.force.domain.dto.CategoryInsertDTO;
import com.hsj.force.domain.dto.CategoryListDTO;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CommonService commonService;
    private final CategoryMapper categoryMapper;

    public Map<String, Object> selectCategoryListInfo(User loginMember) {

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);
        List<Category> categoryList = categoryMapper.selectCategoryList(loginMember.getStoreNo());

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("categoryList", categoryList);

        return map;
    }

    public void insertCategory(User loginMember, CategoryInsertDTO categoryInsertDTO) {

        Category category = new Category();
        category.setStoreNo(loginMember.getStoreNo());
        category.setCategoryName(categoryInsertDTO.getCategoryName());
        category.setPriority(Integer.parseInt(categoryInsertDTO.getPriorityStr()));
        category.setUseYn(categoryInsertDTO.getUseYn());
        String categoryNo = categoryMapper.selectCategoryNo(category.getStoreNo());
        category.setCategoryNo(ComUtils.getNextNo(categoryNo, Constants.CATEGORY_NO_PREFIX));
        category.setInsertId(loginMember.getUserId());
        category.setModifyId(loginMember.getUserId());

        checkPriority(loginMember, category);

        categoryMapper.insertCategory(category);
    }


    public int updateCategory(User loginMember, CategoryUpdateDTO categoryUpdateDTO) {

        Category category = new Category();
        category.setCategoryNo(categoryUpdateDTO.getCategoryNo());
        category.setCategoryName(categoryUpdateDTO.getCategoryName());
        category.setUseYn(categoryUpdateDTO.getUseYn());
        category.setPriority(Integer.parseInt(categoryUpdateDTO.getPriorityStr()));
        category.setStoreNo(loginMember.getStoreNo());
        category.setModifyId(loginMember.getUserId());

        checkPriority(loginMember, category);

        return categoryMapper.updateCategory(category);
    }

    public Map<String, Object> selectCategoryUpdateInfo(User loginMember, String categoryNo) {

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);
        CategoryUpdateDTO categoryUpdateDTO = categoryMapper.selectCategory(loginMember.getStoreNo(), categoryNo);
        categoryUpdateDTO.setPriorityStr(String.valueOf(categoryUpdateDTO.getPriority()));

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("category", categoryUpdateDTO);

        return map;
    }

    public List<Category> selectCategoryList(String storeNo) {
        return categoryMapper.selectCategoryList(storeNo);
    }

    private void checkPriority(User loginMember, Category category) {
        Integer priority = categoryMapper.selectPriority(category);
        if(priority != null) {
            int maxPriority = categoryMapper.selectMaxPriority(category);
            CategoryListDTO categoryListDTO = new CategoryListDTO();
            categoryListDTO.setMaxPriority(maxPriority + 1);
            categoryListDTO.setModifyId(loginMember.getUserId());
            categoryListDTO.setStoreNo(loginMember.getStoreNo());
            categoryListDTO.setPriority(category.getPriority());
            categoryMapper.updatePriority(categoryListDTO);
        }
    }
}
