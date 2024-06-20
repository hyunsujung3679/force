package com.hsj.force.menu.service;

import com.hsj.force.category.repository.CategoryMapper;
import com.hsj.force.category.repository.CategoryRepository;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.common.repository.CommonRepository;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.Menu;
import com.hsj.force.domain.MenuIngredient;
import com.hsj.force.domain.MenuPrice;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.*;
import com.hsj.force.domain.entity.*;
import com.hsj.force.domain.entity.embedded.CommonData;
import com.hsj.force.domain.entity.embedded.TMenuIngredientId;
import com.hsj.force.domain.entity.embedded.TMenuPriceId;
import com.hsj.force.ingredient.repository.IngredientMapper;
import com.hsj.force.ingredient.repository.IngredientRepository;
import com.hsj.force.menu.repository.MenuMapper;
import com.hsj.force.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final CommonRepository commonRepository;
    private final IngredientRepository ingredientRepository;

    private final CommonService commonService;
    private final CommonMapper commonMapper;
    private final MenuMapper menuMapper;
    private final IngredientMapper ingredientMapper;
    private final CategoryMapper categoryMapper;

    public List<MenuListDTO> selectMenuListByCategoryNo(String storeNo, String categoryNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("storeNo", storeNo);
        paramMap.put("categoryNo", categoryNo);

        // TODO: JPA 변경 필요
        List<MenuListDTO> menuList = menuMapper.selectMenuListV2(paramMap);
        List<MenuIngredientListDTO> menuIngredientList = menuMapper.selectMenuIngredientList(storeNo);

        boolean isEnoughStock;
        for(MenuListDTO menu : menuList) {
            isEnoughStock = true;
            for(MenuIngredientListDTO menuIngredient : menuIngredientList) {
                if(menu.getMenuNo().equals(menuIngredient.getMenuNo())) {
                    if(menuIngredient.getNeedQuantity() > menuIngredient.getStockQuantity()) {
                        isEnoughStock = false;
                        break;
                    }
                }
            }
            menu.setEnoughStock(isEnoughStock);
        }

        return menuList;
    }

    public Map<String, Object> selectMenuInfo(TUser loginMember) {

        String storeNo = loginMember.getStore().getStoreNo();
        String userName = loginMember.getUserName();

        String storeName = "";
        if(commonRepository.findStoreName(storeNo).isPresent()) {
            storeName = commonRepository.findStoreName(storeNo).get();
        }

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(userName);
        commonLayoutForm.setStoreName(storeName);
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        //TODO: JPA 변경 필요
        List<MenuListDTO> menuList = menuMapper.selectMenuListByMenuForm(storeNo);

        List<MenuIngredientListDTO> menuIngredientList = ingredientRepository.findMenuIngredientListV2(storeNo);

        for(int i = 0; i < menuList.size(); i++) {
            MenuListDTO menu = menuList.get(i);
            menu.setNo(i + 1);
            menu.setStock(getStock(menu, menuIngredientList));
        }

        map.put("commonLayoutForm", commonLayoutForm);
        map.put("menuList", menuList);

        return map;
    }

    private int getStock(MenuListDTO menu, List<MenuIngredientListDTO> menuIngredientList) {
        int stock = 0;
        for(MenuIngredientListDTO menuIngredient : menuIngredientList) {
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

    public int insertMenu(TUser loginMember, MenuInsertDTO menuInsertDTO) {

        String menuNo = null;
        if(menuRepository.findMenuNo(loginMember.getStore().getStoreNo()).isPresent()) {
            menuNo = menuRepository.findMenuNo(loginMember.getStore().getStoreNo()).get();
        }
        String nextMenuNo = ComUtils.getNextNo(menuNo, Constants.MENU_NO_PREFIX);
        TSaleStatus saleStatus = menuRepository.findSaleStatus(menuInsertDTO.getSaleStatusNo());
        TCategory category = categoryRepository.findCategory(menuInsertDTO.getCategoryNo());

        TMenu menu = new TMenu();
        menu.setMenuNo(nextMenuNo);
        menu.setMenuName(menuInsertDTO.getMenuName());
        menu.setSaleStatus(saleStatus);
        menu.setCategory(category);
        menu.setImageOriginName(menuInsertDTO.getImageOriginName());
        menu.setImageSaveName(menuInsertDTO.getImageSaveName());
        menu.setImageExt(menuInsertDTO.getImageExt());
        menu.setImagePath(menuInsertDTO.getImagePath());
        menu.setInsertId(loginMember.getUserId());
        menu.setInsertDate(LocalDateTime.now());
        menu.setModifyId(loginMember.getUserId());
        menu.setModifyDate(LocalDateTime.now());

        menuRepository.saveMenu(menu);

//        Menu menu = new Menu();
//        menu.setMenuNo(nextMenuNo);
//        menu.setMenuName(menuInsertDTO.getMenuName());
//        menu.setSaleStatusNo(menuInsertDTO.getSaleStatusNo());
//        menu.setCategoryNo(menuInsertDTO.getCategoryNo());
//        menu.setImageOriginName(menuInsertDTO.getImageOriginName());
//        menu.setImageSaveName(menuInsertDTO.getImageSaveName());
//        menu.setImageExt(menuInsertDTO.getImageExt());
//        menu.setImagePath(menuInsertDTO.getImagePath());
//        menu.setInsertId(loginMember.getUserId());
//        menu.setModifyId(loginMember.getUserId());
//        menuSaveResult = menuMapper.insertMenu(menu);

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

        TMenuIngredient menuIngredient1 = null;
//        MenuIngredient menuIngredient = null;
        for(int i = 0; i < ingredientNoArr.length; i++) {
            for(int j = 0; j < quantityArr.length; j++) {
                if(i == j) {

                    menuIngredient1 = new TMenuIngredient();

                    TMenuIngredientId menuIngredientId = new TMenuIngredientId();
                    menuIngredientId.setIngredientNo(ingredientNoArr[i]);
                    menuIngredientId.setMenuNo(nextMenuNo);
                    menuIngredientId.setStoreNo(loginMember.getStore().getStoreNo());

                    menuIngredient1.setMenuIngredientId(menuIngredientId);
                    menuIngredient1.setQuantity(quantityArr[i]);
                    menuIngredient1.setInsertId(loginMember.getUserId());
                    menuIngredient1.setInsertDate(LocalDateTime.now());
                    menuIngredient1.setModifyId(loginMember.getUserId());
                    menuIngredient1.setModifyDate(LocalDateTime.now());

                    menuRepository.saveMenuIngredient(menuIngredient1);

//                    menuIngredient = new MenuIngredient();
//                    menuIngredient.setMenuNo(nextMenuNo);
//                    menuIngredient.setIngredientNo(ingredientNoArr[i]);
//                    menuIngredient.setStoreNo(loginMember.getStoreNo());
//                    menuIngredient.setQuantity(quantityArr[i]);
//                    menuIngredient.setInsertId(loginMember.getUserId());
//                    menuIngredient.setModifyId(loginMember.getUserId());
//                    menuIngredientSaveResult += menuMapper.insertMenuIngredient(menuIngredient);
                }
            }
        }

        TMenuPriceId menuPriceId = new TMenuPriceId();
        menuPriceId.setMenuNo(nextMenuNo);
        menuPriceId.setMenuSeq("001");

        TMenuPrice menuPrice1 = new TMenuPrice();
        menuPrice1.setMenuPriceId(menuPriceId);
        menuPrice1.setSalePrice(menuInsertDTO.getSalePrice());
        menuPrice1.setCommonData(new CommonData(loginMember.getUserId(), LocalDateTime.now(), loginMember.getUserId(), LocalDateTime.now()));

        menuRepository.saveMenuPrice(menuPrice1);

//        MenuPrice menuPrice = new MenuPrice();
//        menuPrice.setMenuNo(nextMenuNo);
//        menuPrice.setMenuSeq("001");
//        menuPrice.setSalePrice(menuInsertDTO.getSalePrice());
//        menuPrice.setInsertId(loginMember.getUserId());
//        menuPrice.setModifyId(loginMember.getUserId());
//        menuPriceSaveResult = menuMapper.insertMenuPrice(menuPrice);

        return 1;

    }

    public Map<String, Object> selectMenuUpdateInfo(TUser loginMember, String menuNo) {

        String storeNo = loginMember.getStore().getStoreNo();

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);

        MenuDTO menuDTO = menuRepository.findMenuV3(storeNo, menuNo);

        List<TMenuIngredient> menuIngredients= menuRepository.findMenuIngredient(menuNo, storeNo);
        List<MenuIngredientListDTO> menuIngredientList = menuIngredients.stream()
                .map(mi -> new MenuIngredientListDTO(mi))
                .collect(Collectors.toList());

        MenuUpdateDTO menuUpdateDTO = new MenuUpdateDTO();
        menuUpdateDTO.setMenuNo(menuDTO.getMenuNo());
        menuUpdateDTO.setMenuName(menuDTO.getMenuName());
        menuUpdateDTO.setSaleStatusNo(menuDTO.getSaleStatusNo());
        menuUpdateDTO.setCategoryNo(menuDTO.getCategoryNo());
        menuUpdateDTO.setSalePrice(menuDTO.getSalePrice());
        menuUpdateDTO.setImageSaveName(menuDTO.getImageSaveName());
        menuUpdateDTO.setIngredientQuantityList(menuIngredientList);

        MenuIngredientListDTO menuIngredient = null;

        if(menuIngredientList.size() == 0) {
            for(int i = 0; i < 4; i++) {
                menuIngredient = new MenuIngredientListDTO();
                menuIngredient.setIngredientNo("");
                menuIngredient.setQuantity(0.0);
                menuIngredientList.add(menuIngredient);
            }
        }

        if(menuIngredientList.size() == 1) {
            for(int i = 1; i < 4; i++) {
                menuIngredient = new MenuIngredientListDTO();
                menuIngredient.setIngredientNo("");
                menuIngredient.setQuantity(0.0);
                menuIngredientList.add(menuIngredient);
            }
        }

        if(menuIngredientList.size() == 2) {
            for(int i = 2; i < 4; i++) {
                menuIngredient = new MenuIngredientListDTO();
                menuIngredient.setIngredientNo("");
                menuIngredient.setQuantity(0.0);
                menuIngredientList.add(menuIngredient);
            }
        }

        if(menuIngredientList.size() == 3) {
            for(int i = 3; i < 4; i++) {
                menuIngredient = new MenuIngredientListDTO();
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

    public int updateMenu(TUser loginMember, MenuUpdateDTO menuUpdateDTO) {

        String storeNo = loginMember.getStore().getStoreNo();
        String userId = loginMember.getUserId();

        TCategory category = categoryRepository.findCategory(menuUpdateDTO.getCategoryNo());
        TSaleStatus saleStatus = menuRepository.findSaleStatus(menuUpdateDTO.getSaleStatusNo());

        TMenu menu = menuRepository.findMenu(menuUpdateDTO.getMenuNo());
        menu.setMenuName(menuUpdateDTO.getMenuName());
        menu.setCategory(category);
        menu.setSaleStatus(saleStatus);
        menu.setModifyId(userId);
        menu.setModifyDate(LocalDateTime.now());
        if(menuUpdateDTO.getImageOriginName() != null) {
            menu.setImageOriginName(menuUpdateDTO.getImageOriginName());
            menu.setImageSaveName(menuUpdateDTO.getImageSaveName());
            menu.setImageExt(menuUpdateDTO.getImageExt());
            menu.setImagePath(menuUpdateDTO.getImagePath());
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

        int result = menuRepository.deleteMenuIngredient(storeNo, menuUpdateDTO.getMenuNo());
        if(result > 0) {
            TMenuIngredient menuIngredient1 = null;
            for(int i = 0; i < ingredientNoArr.length; i++) {
                for(int j = 0; j < quantityArr.length; j++) {
                    if(i == j) {
                        menuIngredient1 = new TMenuIngredient();

                        TMenuIngredientId menuIngredientId = new TMenuIngredientId();
                        menuIngredientId.setMenuNo(menuUpdateDTO.getMenuNo());
                        menuIngredientId.setIngredientNo(ingredientNoArr[i]);
                        menuIngredientId.setStoreNo(storeNo);

                        menuIngredient1.setMenuIngredientId(menuIngredientId);
                        menuIngredient1.setQuantity(quantityArr[i]);
                        menuIngredient1.setInsertId(loginMember.getUserId());
                        menuIngredient1.setInsertDate(LocalDateTime.now());
                        menuIngredient1.setModifyId(loginMember.getUserId());
                        menuIngredient1.setModifyDate(LocalDateTime.now());

                        menuRepository.saveMenuIngredient(menuIngredient1);
                    }
                }
            }
        }


        int salePrice = menuRepository.findSalePrice(menuUpdateDTO.getMenuNo());
        int nextSalePrice = menuUpdateDTO.getSalePrice();
        if(salePrice != nextSalePrice) {

            TMenuPrice menuPrice1 = new TMenuPrice();

            TMenuPriceId menuPriceId = new TMenuPriceId();
            menuPriceId.setMenuNo(menuUpdateDTO.getMenuNo());

            String nextMenuSeq = null;
            if(menuRepository.findMenuSeq(menuUpdateDTO.getMenuNo()).isPresent()) {
                nextMenuSeq = menuRepository.findMenuSeq(menuUpdateDTO.getMenuNo()).get();
            }
            menuPriceId.setMenuSeq(ComUtils.getNextSeq(nextMenuSeq));
            menuPrice1.setMenuPriceId(menuPriceId);
            menuPrice1.setSalePrice(nextSalePrice);
            menuPrice1.setCommonData(new CommonData(userId, LocalDateTime.now(), userId, LocalDateTime.now()));

            menuRepository.saveMenuPrice(menuPrice1);

        }

        return 1;

    }

    public List<MenuListDTO> selectMenuListByFirstCategory(String storeNo) {

        String categoryNo = null;
        if(categoryRepository.findFirstCategoryNo(storeNo).isPresent()) {
            categoryNo = categoryRepository.findFirstCategoryNo(storeNo).get();
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("storeNo", storeNo);
        paramMap.put("categoryNo", categoryNo);

        //TODO : JPA 적용 필요
        List<MenuListDTO> menuList = menuMapper.selectMenuListV2(paramMap);
        List<MenuIngredientListDTO> menuIngredientList = menuMapper.selectMenuIngredientList(storeNo);

        boolean isEnoughStock;
        for(MenuListDTO menu : menuList) {
            isEnoughStock = true;
            for(MenuIngredientListDTO menuIngredient : menuIngredientList) {
                if(menu.getMenuNo().equals(menuIngredient.getMenuNo())) {
                    if(menuIngredient.getNeedQuantity() > menuIngredient.getStockQuantity()) {
                        isEnoughStock = false;
                        break;
                    }
                }
            }
            menu.setEnoughStock(isEnoughStock);
        }

        return menuList;
    }
}
