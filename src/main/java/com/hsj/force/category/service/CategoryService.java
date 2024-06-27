package com.hsj.force.category.service;

import com.hsj.force.category.repository.CategoryJpaRepository;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.dto.CategoryInsertDTO;
import com.hsj.force.domain.dto.CategoryListDTO;
import com.hsj.force.domain.dto.CategoryUpdateDTO;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.entity.TCategory;
import com.hsj.force.domain.entity.TUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;

    private final CommonService commonService;

    public Map<String, Object> selectCategoryListInfo(TUser loginMember) {

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);
        List<TCategory> categories = categoryJpaRepository.findAllByOrderByPriority();
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

    public int insertCategory(CategoryInsertDTO categoryInsertDTO) {

        String categoryName = categoryInsertDTO.getCategoryName();
        int priority = categoryInsertDTO.getPriority();
        String useYn = categoryInsertDTO.getUseYn();

        String categoryNo = categoryJpaRepository.findFirstByOrderByCategoryNoDesc().getCategoryNo();

        TCategory category = new TCategory(
                ComUtils.getNextNo(categoryNo, Constants.CATEGORY_NO_PREFIX),
                categoryName,
                priority,
                useYn);

        checkPriority(category, category.getPriority());

        categoryJpaRepository.save(category);

        return 1;
    }

    public int updateCategory(CategoryUpdateDTO categoryUpdateDTO) {

        String categoryNo = categoryUpdateDTO.getCategoryNo();
        String categoryName = categoryUpdateDTO.getCategoryName();
        String useYn = categoryUpdateDTO.getUseYn();
        int priority = categoryUpdateDTO.getPriority();

        TCategory category = null;
        Optional<TCategory> optionalCategory = categoryJpaRepository.findById(categoryNo);
        if(optionalCategory.isPresent()) {
            category = optionalCategory.get();
        }

        checkPriority(category, priority);

        category.setCategoryName(categoryName);
        category.setUseYn(useYn);
        category.setPriority(priority);

        return 1;
    }

    public Map<String, Object> selectCategoryUpdateInfo(TUser loginMember, String categoryNo) {

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);

        TCategory category = null;
        Optional<TCategory> optionalCategory = categoryJpaRepository.findById(categoryNo);
        if(optionalCategory.isPresent()) {
            category = optionalCategory.get();
        }

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("category", category);

        return map;
    }

    public List<CategoryListDTO> selectCategoryList() {
        List<TCategory> categories = categoryJpaRepository.findAllByOrderByPriority();
        return categories.stream()
                .map(c -> new CategoryListDTO(c))
                .collect(Collectors.toList());
    }

    private void checkPriority(TCategory category, int priority) {

        TCategory checkCategory = categoryJpaRepository.findOneByPriority(priority);
        if(checkCategory != null) {

            if(checkCategory.getCategoryNo().equals(category.getCategoryNo())) return;

            int maxPriority = categoryJpaRepository.findFirstByOrderByPriorityDesc().getPriority();

            checkCategory.setPriority((maxPriority + 1));
        }

    }
}
