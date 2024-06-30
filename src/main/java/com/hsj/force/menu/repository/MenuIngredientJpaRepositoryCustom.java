package com.hsj.force.menu.repository;

import com.hsj.force.domain.dto.MenuIngredientListDTO;

import java.util.List;

public interface MenuIngredientJpaRepositoryCustom {

    List<MenuIngredientListDTO> findMenuIngredientListDTOV1(String menuNo);

    List<MenuIngredientListDTO> findMenuIngredientListDTOV2(String menuNo, String orderNo, String orderStatusNo);

}
