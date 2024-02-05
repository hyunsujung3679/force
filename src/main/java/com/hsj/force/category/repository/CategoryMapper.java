package com.hsj.force.category.repository;

import com.hsj.force.domain.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectCategoryList(String storeNo);

}
