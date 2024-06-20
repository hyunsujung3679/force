package com.hsj.force.category.service;

import com.hsj.force.category.repository.CategoryMapper;
import com.hsj.force.category.repository.CategoryRepository;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.Category;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.*;
import com.hsj.force.domain.entity.TCategory;
import com.hsj.force.domain.entity.TUser;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CommonService commonService;
    private final CategoryMapper categoryMapper;

    public Map<String, Object> selectCategoryListInfo(TUser loginMember) {

        String storeNo = loginMember.getStore().getStoreNo();

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);
        List<TCategory> categories = categoryRepository.findCategoryV1(storeNo);
        List<CategoryListDTO> categoryList = categories.stream()
                .map(c -> new CategoryListDTO(c))
                .collect(Collectors.toList());

        for(int i = 0; i < categoryList.size(); i++) {
            CategoryListDTO category = categoryList.get(i);
            category.setNo(i + 1);
        }

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("categoryList", categoryList);

        return map;
    }

    public int insertCategory(TUser loginMember, CategoryInsertDTO categoryInsertDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String categoryName = categoryInsertDTO.getCategoryName();
        int priority = categoryInsertDTO.getPriority();
        String useYn = categoryInsertDTO.getUseYn();
        String categoryNo = "";
        if(categoryRepository.findMaxCategoryNo(storeNo).isPresent()) {
            categoryNo = categoryRepository.findMaxCategoryNo(storeNo).get();
        }
        String userId = loginMember.getUserId();

        TCategory category = new TCategory();
        category.setCategoryNo(ComUtils.getNextNo(categoryNo, Constants.CATEGORY_NO_PREFIX));
        category.setStoreNo(storeNo);
        category.setCategoryName(categoryName);
        category.setPriority(priority);
        category.setUseYn(useYn);
        category.setInsertId(userId);
        category.setInsertDate(LocalDateTime.now());
        category.setModifyId(userId);
        category.setModifyDate(LocalDateTime.now());

        checkPriority(loginMember, category, category.getPriority());

        categoryRepository.saveCategory(category);

        return 1;
    }


    public int updateCategory(TUser loginMember, CategoryUpdateDTO categoryUpdateDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String categoryNo = categoryUpdateDTO.getCategoryNo();
        String categoryName = categoryUpdateDTO.getCategoryName();
        String useYn = categoryUpdateDTO.getUseYn();
        int priority = categoryUpdateDTO.getPriority();
        String userId = loginMember.getUserId();

        TCategory category = null;
        if(categoryRepository.findCategoryV3(storeNo, categoryNo).isPresent()) {
            category = categoryRepository.findCategoryV3(storeNo, categoryNo).get();

            checkPriority(loginMember, category, priority);

            category.setCategoryName(categoryName);
            category.setUseYn(useYn);
            category.setPriority(priority);
            category.setModifyId(userId);
            category.setModifyDate(LocalDateTime.now());
        }

        return 1;
    }

    public Map<String, Object> selectCategoryUpdateInfo(TUser loginMember, String categoryNo) {

        String storeNo = loginMember.getStore().getStoreNo();

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);

        TCategory category = null;
        if(categoryRepository.findCategoryV3(storeNo, categoryNo).isPresent()) {
            category = categoryRepository.findCategoryV3(storeNo, categoryNo).get();
        }

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("category", category);

        return map;
    }

    public List<CategoryListDTO> selectCategoryList(String storeNo) {
        List<TCategory> categories = categoryRepository.findCategoryV1(storeNo);
        return categories.stream()
                .map(c -> new CategoryListDTO(c))
                .collect(Collectors.toList());
    }

    private void checkPriority(TUser loginMember, TCategory category, int priority) {

        String storeNo = loginMember.getStore().getStoreNo();
        String userId = loginMember.getUserId();

        TCategory checkCategory = null;
        if(categoryRepository.findCategoryV2(storeNo, priority).isPresent()) {
            checkCategory = categoryRepository.findCategoryV2(storeNo, priority).get();
        }

        if(checkCategory != null) {

            if(checkCategory.getCategoryNo().equals(category.getCategoryNo())) return;

            int maxPriority = categoryRepository.findMaxPriority(storeNo);

            checkCategory.setPriority((maxPriority + 1));
            checkCategory.setModifyId(userId);
            checkCategory.setModifyDate(LocalDateTime.now());
        }

    }
}
