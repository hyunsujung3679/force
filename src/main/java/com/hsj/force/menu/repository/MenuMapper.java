package com.hsj.force.menu.repository;

import com.hsj.force.domain.Menu;
import com.hsj.force.domain.MenuIngredient;
import com.hsj.force.domain.MenuPrice;
import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.domain.dto.MenuIngredientDTO;
import com.hsj.force.domain.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

    List<MenuDTO> selectMenuList(String storeNo);

    MenuDTO selectMenu(String menuNo, String storeNo);

    List<MenuDTO> selectMenuListByMenuForm(String storeNo);

    List<MenuIngredientDTO> selectMenuIngredientList(String storeNo);

    List<MenuIngredientDTO> selectMenuIngredientListByMenuNo(OrderDTO order);

    String selectMenuNo(String storeNo);

    int insertMenu(Menu menu);

    int insertMenuIngredient(MenuIngredient menuIngredient);

    int insertMenuPrice(MenuPrice menuPrice);

    List<MenuIngredient> selectMenuIngredientListByMenuNoV2(String menuNo, String storeNo);
}
