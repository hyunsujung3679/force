package com.hsj.force.menu.repository;

import com.hsj.force.domain.Menu;
import com.hsj.force.domain.MenuIngredient;
import com.hsj.force.domain.MenuPrice;
import com.hsj.force.domain.Order;
import com.hsj.force.domain.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MenuMapper {

    List<MenuListDTO> selectMenuList(String storeNo);

    List<MenuListDTO> selectMenuListV2(Map<String, Object> paramMap);

    MenuDTO selectMenu(String menuNo, String storeNo);

    List<MenuListDTO> selectMenuListByMenuForm(String storeNo);

    List<MenuIngredientListDTO> selectMenuIngredientList(String storeNo);

    List<MenuIngredientListDTO> selectMenuIngredientListByMenuNo(String menuNo, String storeNo);

    String selectMenuNo(String storeNo);

    int insertMenu(Menu menu);

    int insertMenuIngredient(MenuIngredient menuIngredient);

    int insertMenuPrice(MenuPrice menuPrice);

    List<MenuIngredientListDTO> selectMenuIngredientListByMenuNoV2(String menuNo, String storeNo);

    int updateMenuV1(Menu menu);

    int updateMenuV2(Menu menu);

    int deleteMenuIngredient(MenuUpdateDTO menuUpdateDTO);

    String selectMenuSeq(MenuPrice menuPrice);

    int selectSalePrice(MenuUpdateDTO menuUpdateDTO);
}
