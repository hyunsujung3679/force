package com.hsj.force.menu.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.Menu;
import com.hsj.force.domain.MenuIngredient;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryDTO;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.domain.dto.MenuIngredientDTO;
import com.hsj.force.ingredient.repository.IngredientMapper;
import com.hsj.force.menu.repository.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final CommonMapper commonMapper;
    private final MenuMapper menuMapper;
    private final IngredientMapper ingredientMapper;

    public List<MenuDTO> selectMenuListByCategoryNo(String storeNo, String categoryNo) {
        List<MenuDTO> menuList = menuMapper.selectMenuList(storeNo);
        List<MenuIngredientDTO> menuIngredientList = menuMapper.selectMenuIngredientList(storeNo);
        List<MenuDTO> menuListByCategoryNo = new ArrayList<>();
        for(MenuDTO menu : menuList) {
            if(categoryNo.equals(menu.getCategoryNo())) {
                menuListByCategoryNo.add(menu);
            }
        }

        boolean isEnoughStock;
        for(MenuDTO menu : menuListByCategoryNo) {
            isEnoughStock = true;
            for(MenuIngredientDTO menuIngredient : menuIngredientList) {
                if(menu.getMenuNo().equals(menuIngredient.getMenuNo())) {
                    if(menuIngredient.getNeedQuantity() > menuIngredient.getStockQuantity()) {
                        isEnoughStock = false;
                        break;
                    }
                }
            }
            menu.setEnoughStock(isEnoughStock);
        }

        return menuListByCategoryNo;
    }

    public MenuDTO selectMenuInfo(User loginMember) {

        String storeName = commonMapper.selectStoreName(loginMember.getStoreNo());
        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        List<MenuDTO> menuList = menuMapper.selectMenuListByMenuForm(loginMember.getStoreNo());
        List<MenuIngredientDTO> menuIngredientList = ingredientMapper.selectMenuIngredientListByMenuForm(loginMember.getStoreNo());

        for(int i = 0; i < menuList.size(); i++) {
            MenuDTO menu = menuList.get(i);
            menu.setNo(i + 1);
            menu.setStock(getStock(menu, menuIngredientList));
        }

        MenuDTO menuForm = new MenuDTO();
        menuForm.setCommonLayoutForm(commonLayoutForm);
        menuForm.setMenuList(menuList);

        return menuForm;
    }

    private int getStock(MenuDTO menu, List<MenuIngredientDTO> menuIngredientList) {
        int stock = 0;
        for(MenuIngredientDTO menuIngredient : menuIngredientList) {
            if(menu.getMenuNo().equals(menuIngredient.getMenuNo())) {
                if(stock == 0) {
                    stock = (int) (menuIngredient.getIngredientQuantity() / menuIngredient.getNeedQuantity());
                } else if(stock > (int) (menuIngredient.getIngredientQuantity() / menuIngredient.getNeedQuantity())) {
                    stock = (int) (menuIngredient.getIngredientQuantity() / menuIngredient.getNeedQuantity());
                }
            }
        }
        return stock;
    }

    public int insertMenu(User loginMember, Map<String, Object> parameter) {

        int menuSaveResult = 0;
        int menuIngredientSaveResult = 0;

        String menuNo = menuMapper.selectMenuNo(loginMember.getStoreNo());
        String nextMenuNo = ComUtils.getNextNo(menuNo, Constants.MENU_NO_PREFIX);

        Menu menu = new Menu();
        menu.setMenuNo(nextMenuNo);
        menu.setMenuName((String) parameter.get("menuName"));
        menu.setSaleStatusNo((String) parameter.get("saleStatusNo"));
        menu.setCategoryNo((String) parameter.get("categoryNo"));
        menu.setInsertId(loginMember.getUserId());
        menu.setModifyId(loginMember.getUserId());
        menuSaveResult = menuMapper.insertMenu(menu);

        List<String> ingredientNoList = (List<String>) parameter.get("ingredientNoArr");
        List<String> quantityList = (List<String>) parameter.get("quantityArr");
        MenuIngredient menuIngredient = null;
        for(int i = 0; i < ingredientNoList.size(); i++) {
            for(int j = 0; j < quantityList.size(); j++) {
                if(i == j) {
                    menuIngredient = new MenuIngredient();
                    menuIngredient.setMenuNo(nextMenuNo);
                    menuIngredient.setIngredientNo(ingredientNoList.get(i));
                    menuIngredient.setStoreNo(loginMember.getStoreNo());
                    menuIngredient.setQuantity(Double.parseDouble(quantityList.get(i)));
                    menuIngredient.setInsertId(loginMember.getUserId());
                    menuIngredient.setModifyId(loginMember.getUserId());
                    menuIngredientSaveResult += menuMapper.insertMenuIngredient(menuIngredient);
                }
            }
        }

        if(menuSaveResult > 0 && (ingredientNoList.size() == menuIngredientSaveResult)) {
            return 1;
        } else {
            return -1;
        }

    }
}
