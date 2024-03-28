package com.hsj.force.menu.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.Menu;
import com.hsj.force.domain.MenuIngredient;
import com.hsj.force.domain.MenuPrice;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.*;
import com.hsj.force.ingredient.repository.IngredientMapper;
import com.hsj.force.menu.repository.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Transactional
@Service
@RequiredArgsConstructor
public class MenuService {

    private final CommonService commonService;
    private final CommonMapper commonMapper;
    private final MenuMapper menuMapper;
    private final IngredientMapper ingredientMapper;

    public List<MenuListDTO> selectMenuListByCategoryNo(String storeNo, String categoryNo) {
        List<MenuListDTO> menuList = menuMapper.selectMenuList(storeNo);
        List<MenuIngredientDTO> menuIngredientList = menuMapper.selectMenuIngredientList(storeNo);
        List<MenuListDTO> menuListByCategoryNo = new ArrayList<>();
        for(MenuListDTO menu : menuList) {
            if(categoryNo.equals(menu.getCategoryNo())) {
                menuListByCategoryNo.add(menu);
            }
        }

        boolean isEnoughStock;
        for(MenuListDTO menu : menuListByCategoryNo) {
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

        List<MenuListDTO> menuList = menuMapper.selectMenuListByMenuForm(loginMember.getStoreNo());
        List<MenuIngredientDTO> menuIngredientList = ingredientMapper.selectMenuIngredientListByMenuForm(loginMember.getStoreNo());

        for(int i = 0; i < menuList.size(); i++) {
            MenuListDTO menu = menuList.get(i);
            menu.setNo(i + 1);
            menu.setStock(getStock(menu, menuIngredientList));
        }

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("menuList", menuList);

        return map;
    }

    private int getStock(MenuListDTO menu, List<MenuIngredientDTO> menuIngredientList) {
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

        String ingredientNo1 = menuInsertDTO.getIngredientNo1();
        String ingredientNo2 = menuInsertDTO.getIngredientNo2();
        String ingredientNo3 = menuInsertDTO.getIngredientNo3();
        String ingredientNo4 = menuInsertDTO.getIngredientNo4();
        String[] ingredientNoArr = new String[] {ingredientNo1, ingredientNo2, ingredientNo3, ingredientNo4};
        ingredientNoArr = Arrays.stream(ingredientNoArr)
                .filter(a -> !"".equals(a))
                .toArray(String[]::new);

        double quantity1 = 0;
        double quantity2 = 0;
        double quantity3 = 0;
        double quantity4 = 0;

        if(menuInsertDTO.getQuantity1() != null) {
            quantity1 = menuInsertDTO.getQuantity1();
        }
        if(menuInsertDTO.getQuantity2() != null) {
            quantity2 = menuInsertDTO.getQuantity2();
        }
        if(menuInsertDTO.getQuantity3() != null) {
            quantity3 = menuInsertDTO.getQuantity3();
        }
        if(menuInsertDTO.getQuantity4() != null) {
            quantity4 = menuInsertDTO.getQuantity4();
        }

        double[] quantityArr = new double[] {quantity1, quantity2, quantity3, quantity4};
        quantityArr = Arrays.stream(quantityArr)
                .filter(a -> a != 0)
                .toArray();

        MenuIngredient menuIngredient = null;
        for(int i = 0; i < ingredientNoArr.length; i++) {
            for(int j = 0; j < quantityArr.length; j++) {
                if(i == j) {
                    menuIngredient = new MenuIngredient();
                    menuIngredient.setMenuNo(nextMenuNo);
                    menuIngredient.setIngredientNo(ingredientNoArr[i]);
                    menuIngredient.setStoreNo(loginMember.getStoreNo());
                    menuIngredient.setQuantity(quantityArr[i]);
                    menuIngredient.setInsertId(loginMember.getUserId());
                    menuIngredient.setModifyId(loginMember.getUserId());
                    menuIngredientSaveResult += menuMapper.insertMenuIngredient(menuIngredient);
                }
            }
        }

        MenuPrice menuPrice = new MenuPrice();
        menuPrice.setMenuNo(nextMenuNo);
        menuPrice.setMenuSeq("001");
        menuPrice.setSalePrice(menuInsertDTO.getSalePrice());
        menuPrice.setInsertId(loginMember.getUserId());
        menuPrice.setModifyId(loginMember.getUserId());
        menuPriceSaveResult = menuMapper.insertMenuPrice(menuPrice);

        if(menuSaveResult > 0 &&
          (ingredientNoArr.length == menuIngredientSaveResult) &&
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
        menuUpdateDTO.setSalePrice(menuDTO.getSalePrice());
        menuUpdateDTO.setImageSaveName(menuDTO.getImageSaveName());
        menuUpdateDTO.setIngredientQuantityList(menuIngredientList);

        MenuIngredient menuIngredient = null;

        if(menuIngredientList.size() == 0) {
            for(int i = 0; i < 4; i++) {
                menuIngredient = new MenuIngredient();
                menuIngredient.setIngredientNo("");
                menuIngredient.setQuantity(0.0);
                menuIngredientList.add(menuIngredient);
            }
        }

        if(menuIngredientList.size() == 1) {
            for(int i = 1; i < 4; i++) {
                menuIngredient = new MenuIngredient();
                menuIngredient.setIngredientNo("");
                menuIngredient.setQuantity(0.0);
                menuIngredientList.add(menuIngredient);
            }
        }

        if(menuIngredientList.size() == 2) {
            for(int i = 2; i < 4; i++) {
                menuIngredient = new MenuIngredient();
                menuIngredient.setIngredientNo("");
                menuIngredient.setQuantity(0.0);
                menuIngredientList.add(menuIngredient);
            }
        }

        if(menuIngredientList.size() == 3) {
            for(int i = 3; i < 4; i++) {
                menuIngredient = new MenuIngredient();
                menuIngredient.setIngredientNo("");
                menuIngredient.setQuantity(0.0);
                menuIngredientList.add(menuIngredient);
            }
        }

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("menu", menuUpdateDTO);
        map.put("ingredientQuantityList", menuIngredientList);

        return map;
    }

    public int updateMenu(User loginMember, MenuUpdateDTO menuUpdateDTO) {

        menuUpdateDTO.setStoreNo(loginMember.getStoreNo());
        int menuSaveResult = 0;
        int menuIngredientSaveResult = 0;
        int menuPriceSaveResult = 0;
        int menuIngredientDeleteResult = 0;

        Menu menu = new Menu();
        menu.setMenuNo(menuUpdateDTO.getMenuNo());
        menu.setMenuName(menuUpdateDTO.getMenuName());
        menu.setSaleStatusNo(menuUpdateDTO.getSaleStatusNo());
        menu.setCategoryNo(menuUpdateDTO.getCategoryNo());
        menu.setModifyId(loginMember.getUserId());
        if(menuUpdateDTO.getImageOriginName() != null) {
            menu.setImageOriginName(menuUpdateDTO.getImageOriginName());
            menu.setImageSaveName(menuUpdateDTO.getImageSaveName());
            menu.setImageExt(menuUpdateDTO.getImageExt());
            menu.setImagePath(menuUpdateDTO.getImagePath());
            menuSaveResult = menuMapper.updateMenuV1(menu);
        } else {
            menuSaveResult = menuMapper.updateMenuV2(menu);
        }

        String ingredientNo1 = menuUpdateDTO.getIngredientNo1();
        String ingredientNo2 = menuUpdateDTO.getIngredientNo2();
        String ingredientNo3 = menuUpdateDTO.getIngredientNo3();
        String ingredientNo4 = menuUpdateDTO.getIngredientNo4();
        String[] ingredientNoArr = new String[] {ingredientNo1, ingredientNo2, ingredientNo3, ingredientNo4};
        ingredientNoArr = Arrays.stream(ingredientNoArr)
                .filter(a -> !"".equals(a))
                .toArray(String[]::new);

        double quantity1 = 0;
        double quantity2 = 0;
        double quantity3 = 0;
        double quantity4 = 0;

        if(menuUpdateDTO.getQuantity1() != null) {
            quantity1 = menuUpdateDTO.getQuantity1();
        }
        if(menuUpdateDTO.getQuantity2() != null) {
            quantity2 = menuUpdateDTO.getQuantity2();
        }
        if(menuUpdateDTO.getQuantity3() != null) {
            quantity3 = menuUpdateDTO.getQuantity3();
        }
        if(menuUpdateDTO.getQuantity4() != null) {
            quantity4 = menuUpdateDTO.getQuantity4();
        }

        double[] quantityArr = new double[] {quantity1, quantity2, quantity3, quantity4};
        quantityArr = Arrays.stream(quantityArr)
                .filter(a -> a != 0)
                .toArray();

        menuIngredientDeleteResult = menuMapper.deleteMenuIngredient(menuUpdateDTO);
        if(menuIngredientDeleteResult > 0) {
            MenuIngredient menuIngredient = null;
            for(int i = 0; i < ingredientNoArr.length; i++) {
                for(int j = 0; j < quantityArr.length; j++) {
                    if(i == j) {
                        menuIngredient = new MenuIngredient();
                        menuIngredient.setMenuNo(menuUpdateDTO.getMenuNo());
                        menuIngredient.setIngredientNo(ingredientNoArr[i]);
                        menuIngredient.setStoreNo(loginMember.getStoreNo());
                        menuIngredient.setQuantity(quantityArr[i]);
                        menuIngredient.setInsertId(loginMember.getUserId());
                        menuIngredient.setModifyId(loginMember.getUserId());
                        menuIngredientSaveResult += menuMapper.insertMenuIngredient(menuIngredient);
                    }
                }
            }
        }

        int salePrice = menuMapper.selectSalePrice(menuUpdateDTO);
        int nextSalePrice = menuUpdateDTO.getSalePrice();
        if(salePrice != nextSalePrice) {
            MenuPrice menuPrice = new MenuPrice();
            menuPrice.setMenuNo(menuUpdateDTO.getMenuNo());
            String nextMenuSeq = menuMapper.selectMenuSeq(menuPrice);
            menuPrice.setMenuSeq(ComUtils.getNextSeq(nextMenuSeq));
            menuPrice.setSalePrice(nextSalePrice);
            menuPrice.setInsertId(loginMember.getUserId());
            menuPrice.setModifyId(loginMember.getUserId());
            menuPriceSaveResult = menuMapper.insertMenuPrice(menuPrice);
        } else {
            menuPriceSaveResult = 1;
        }

        if(menuSaveResult > 0 && (ingredientNoArr.length == menuIngredientSaveResult) && menuPriceSaveResult > 0) {
            return 1;
        } else {
            return 0;
        }

    }
}
