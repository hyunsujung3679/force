package com.hsj.force.category.repository;

import com.hsj.force.domain.Category;
import com.hsj.force.domain.dto.CategoryListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectCategoryList(String storeNo);

    List<Category> selectCategoryListByOrderForm(String storeNo);

    String selectCategoryNo(String storeNo);

    int insertCategory(Category category);

    Integer selectPriority(Category category);

    int selectMaxPriority(Category category);

    int updatePriority(CategoryListDTO category);

    int updateCategory(Category category);
}
