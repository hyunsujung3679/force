package com.hsj.force.order.repository;

import com.hsj.force.domain.Category;
import com.hsj.force.domain.dto.MenuDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

@Mapper
public interface OrderMapper {
    List<Category> selectCategoryList(String storeNo);

    List<MenuDTO> selectMenuList(String storeNo);
}
