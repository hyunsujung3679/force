package com.hsj.force.category.repository;

import com.hsj.force.domain.Category;
import com.hsj.force.domain.dto.CategoryListDTO;
import com.hsj.force.domain.dto.CategoryUpdateDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryListDTO> selectCategoryList(String storeNo);

    List<CategoryListDTO> selectCategoryListByOrderForm(String storeNo);

    String selectCategoryNo(String storeNo);

    int insertCategory(Category category);

    Integer selectPriority(Category category);

    int selectMaxPriority(Category category);

    int updatePriority(CategoryUpdateDTO category);

    int updateCategory(Category category);

    CategoryUpdateDTO selectCategory(String storeNo, String categoryNo);

    String selectFirstCategoryNo(String storeNo);
}
