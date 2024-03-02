package com.hsj.force.menu.repository;

import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.domain.dto.MenuIngredientDTO;
import com.hsj.force.domain.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

    List<MenuDTO> selectMenuList(String storeNo);

    MenuDTO selectMenu(String menuNo);

    List<MenuDTO> selectMenuListByMenuForm(String storeNo);

    List<MenuIngredientDTO> selectMenuIngredientList(String storeNo);

    List<MenuIngredientDTO> selectMenuIngredientListByMenuNo(OrderDTO order);
}
