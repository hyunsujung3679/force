package com.hsj.force.menu.service;

import com.hsj.force.category.repository.CategoryMapper;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.*;
import com.hsj.force.domain.dto.*;
import com.hsj.force.ingredient.repository.IngredientMapper;
import com.hsj.force.menu.repository.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class MenuService {

    private final CommonService commonService;
    private final CommonMapper commonMapper;
    private final MenuMapper menuMapper;
    private final IngredientMapper ingredientMapper;
    private final CategoryMapper categoryMapper;

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

    public Map<String, Object> selectMenuInfo(User loginMember) {

        Map<String, Object> map = new HashMap<>();
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

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("menuList", menuList);

        return map;
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

    public int insertMenu(User loginMember, MenuInsertDTO menuInsertDTO) {

        int menuSaveResult = 0;
        int menuIngredientSaveResult = 0;
        int menuPriceSaveResult = 0;

        String menuNo = menuMapper.selectMenuNo(loginMember.getStoreNo());
        String nextMenuNo = ComUtils.getNextNo(menuNo, Constants.MENU_NO_PREFIX);

        Menu menu = new Menu();
        menu.setMenuNo(nextMenuNo);
        menu.setMenuName(menuInsertDTO.getMenuName());
        menu.setSaleStatusNo(menuInsertDTO.getSaleStatusNo());
        menu.setCategoryNo(menuInsertDTO.getCategoryNo());
        menu.setImageOriginName(menuInsertDTO.getImageOriginName());
        menu.setImageSaveName(menuInsertDTO.getImageSaveName());
        menu.setImageExt(menuInsertDTO.getImageExt());
        menu.setImagePath(menuInsertDTO.getImagePath());
        menu.setInsertId(loginMember.getUserId());
        menu.setModifyId(loginMember.getUserId());
        menuSaveResult = menuMapper.insertMenu(menu);

        String[] ingredientArr = menuInsertDTO.getIngredientNo();
        String[] quantityArr = menuInsertDTO.getQuantityStr();
        MenuIngredient menuIngredient = null;
        for(int i = 0; i < ingredientArr.length; i++) {
            for(int j = 0; j < quantityArr.length; j++) {
                if(i == j) {
                    menuIngredient = new MenuIngredient();
                    menuIngredient.setMenuNo(nextMenuNo);
                    menuIngredient.setIngredientNo(ingredientArr[i]);
                    menuIngredient.setStoreNo(loginMember.getStoreNo());
                    menuIngredient.setQuantity(Double.parseDouble(quantityArr[i]));
                    menuIngredient.setInsertId(loginMember.getUserId());
                    menuIngredient.setModifyId(loginMember.getUserId());
                    menuIngredientSaveResult += menuMapper.insertMenuIngredient(menuIngredient);
                }
            }
        }

        int salePrice = Integer.parseInt(menuInsertDTO.getSalePriceStr().replaceAll(",", ""));
        MenuPrice menuPrice = new MenuPrice();
        menuPrice.setMenuNo(nextMenuNo);
        menuPrice.setMenuSeq("001");
        menuPrice.setSalePrice(salePrice);
        menuPrice.setInsertId(loginMember.getUserId());
        menuPrice.setModifyId(loginMember.getUserId());
        menuPriceSaveResult = menuMapper.insertMenuPrice(menuPrice);

        if(menuSaveResult > 0 &&
          (ingredientArr.length == menuIngredientSaveResult) &&
          menuPriceSaveResult > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    public Map<String, Object> selectMenuUpdateInfo(User loginMember, String menuNo) {

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);
        MenuDTO menuDTO = menuMapper.selectMenu(menuNo, loginMember.getStoreNo());
        List<MenuIngredient> menuIngredientList = menuMapper.selectMenuIngredientListByMenuNoV2(menuNo, loginMember.getStoreNo());

        MenuUpdateDTO menuUpdateDTO = new MenuUpdateDTO();
        menuUpdateDTO.setMenuNo(menuDTO.getMenuNo());
        menuUpdateDTO.setMenuName(menuDTO.getMenuName());
        menuUpdateDTO.setSaleStatusNo(menuDTO.getSaleStatusNo());
        menuUpdateDTO.setCategoryNo(menuDTO.getCategoryNo());
        menuUpdateDTO.setSalePriceStr(String.valueOf(menuDTO.getSalePrice()));
        menuUpdateDTO.setImageSaveName(menuDTO.getImageSaveName());
        menuUpdateDTO.setIngredientQuantityList(menuIngredientList);

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("menu", menuUpdateDTO);
        map.put("ingredientQuantityList", menuIngredientList);

        return map;
    }
}
