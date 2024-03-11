package com.hsj.force.category.service;

import com.hsj.force.category.repository.CategoryMapper;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.Category;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryListDTO;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CommonService commonService;
    private final CategoryMapper categoryMapper;

    public CategoryListDTO selectCategoryInfo(User loginMember) {

        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);
        List<Category> categoryList = categoryMapper.selectCategoryList(loginMember.getStoreNo());

        CategoryListDTO categoryForm = new CategoryListDTO();
        categoryForm.setCommonLayoutForm(commonLayoutForm);
        categoryForm.setCategoryList(categoryList);

        return categoryForm;
    }

    public int insertCategory(User loginMember, Category category) {
        category.setStoreNo(loginMember.getStoreNo());
        String categoryNo = categoryMapper.selectCategoryNo(category.getStoreNo());
        String nextCategoryNo = "";
        if(categoryNo == null) {
            nextCategoryNo = "C001";
        } else {
            nextCategoryNo = ComUtils.getNextNo(categoryNo, Constants.CATEGORY_NO_PREFIX);
        }
        category.setCategoryNo(nextCategoryNo);
        category.setInsertId(loginMember.getUserId());
        category.setModifyId(loginMember.getUserId());

        checkPriority(loginMember, category);

        return categoryMapper.insertCategory(category);
    }


    public int updateCategory(User loginMember, Category category) {
        category.setStoreNo(loginMember.getStoreNo());
        category.setInsertId(loginMember.getUserId());
        category.setModifyId(loginMember.getUserId());

        checkPriority(loginMember, category);

        return categoryMapper.updateCategory(category);
    }

    private void checkPriority(User loginMember, Category category) {
        Integer priority = categoryMapper.selectPriority(category);
        if(priority != null) {
            int maxPriority = categoryMapper.selectMaxPriority(category);
            CategoryListDTO categoryDTO = new CategoryListDTO();
            categoryDTO.setMaxPriority(maxPriority + 1);
            categoryDTO.setModifyId(loginMember.getUserId());
            categoryDTO.setStoreNo(loginMember.getStoreNo());
            categoryDTO.setPriority(category.getPriority());
            categoryMapper.updatePriority(categoryDTO);
        }
    }

    public List<Category> selectCategoryList(String storeNo) {
        return categoryMapper.selectCategoryList(storeNo);
    }
}
