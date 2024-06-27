package com.hsj.force.menu.service;

import com.hsj.force.category.repository.CategoryJpaRepository;
import com.hsj.force.common.ComUtils;
import com.hsj.force.common.Constants;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.dto.*;
import com.hsj.force.domain.entity.*;
import com.hsj.force.ingredient.repository.IngredientJpaRepository;
import com.hsj.force.menu.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuIngredientJpaRepository menuIngredientJpaRepository;
    private final MenuJpaRepository menuJpaRepository;
    private final SaleStatusJpaRepository saleStatusJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final MenuPriceJpaRepository menuPriceJpaRepository;
    private final IngredientJpaRepository ingredientJpaRepository;

    private final CommonService commonService;
    private final MenuMapper menuMapper;

    private final MessageSource messageSource;

    public List<MenuListDTO> selectMenuListByCategoryNo(String categoryNo) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("categoryNo", categoryNo);

        List<MenuListDTO> menuList = menuMapper.selectMenuListV2(paramMap);
        List<MenuIngredientListDTO> menuIngredientList = menuIngredientJpaRepository.findMenuIngredientListDTOV1();

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

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = new CommonLayoutDTO();
        commonLayoutForm.setSalesMan(loginMember.getUserName());
        commonLayoutForm.setStoreName(messageSource.getMessage("word.store.name",null, null));
        commonLayoutForm.setCurrentDate(LocalDateTime.now());
        commonLayoutForm.setBusinessDate(LocalDateTime.now());

        List<MenuListDTO> menuList = menuMapper.selectMenuListByMenuForm();
        List<MenuIngredientListDTO> menuIngredientList = menuIngredientJpaRepository.findMenuIngredientListDTOV3();

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

    public int insertMenu(MenuInsertDTO menuInsertDTO) {

        String nextMenuNo = ComUtils.getNextNo(menuJpaRepository.findFirstByOrderByMenuNoDesc().getMenuNo(), Constants.MENU_NO_PREFIX);

        TSaleStatus saleStatus = null;
        Optional<TSaleStatus> optionalSaleStatus = saleStatusJpaRepository.findById(menuInsertDTO.getSaleStatusNo());
        if(optionalSaleStatus.isPresent()) {
            saleStatus = optionalSaleStatus.get();
        }

        TCategory category = null;
        Optional<TCategory> optionalCategory = categoryJpaRepository.findById(menuInsertDTO.getCategoryNo());
        if(optionalCategory.isPresent()) {
            category = optionalCategory.get();
        }

        TMenu menu = new TMenu(
                nextMenuNo,
                menuInsertDTO.getMenuName(),
                saleStatus,
                category,
                menuInsertDTO.getImageSaveName(),
                menuInsertDTO.getImageOriginName(),
                menuInsertDTO.getImagePath(),
                menuInsertDTO.getImageExt());

        menuJpaRepository.save(menu);

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

        for(int i = 0; i < ingredientNoArr.length; i++) {
            for(int j = 0; j < quantityArr.length; j++) {
                if(i == j) {
                    menuIngredientJpaRepository.save(new TMenuIngredient(ingredientNoArr[i], nextMenuNo, quantityArr[i]));
                }
            }
        }

        menuPriceJpaRepository.save(new TMenuPrice(nextMenuNo, "001", menuInsertDTO.getSalePrice()));

        return 1;
    }

    public Map<String, Object> selectMenuUpdateInfo(TUser loginMember, String menuNo) {

        Map<String, Object> map = new HashMap<>();
        CommonLayoutDTO commonLayoutForm = commonService.selectHeaderInfo(loginMember);

        MenuPriceDTO menuPriceDTO = menuPriceJpaRepository.findMenuPriceDTO(menuNo);

        List<TMenuIngredient> menuIngredients = menuIngredientJpaRepository.findAllByMenuNo(menuNo);
        List<MenuIngredientListDTO> menuIngredientList = menuIngredients.stream()
                .map(mi -> new MenuIngredientListDTO(mi))
                .collect(Collectors.toList());

        MenuUpdateDTO menuUpdateDTO = new MenuUpdateDTO();
        menuUpdateDTO.setMenuNo(menuPriceDTO.getMenuNo());
        menuUpdateDTO.setMenuName(menuPriceDTO.getMenuName());
        menuUpdateDTO.setSaleStatusNo(menuPriceDTO.getSaleStatusNo());
        menuUpdateDTO.setCategoryNo(menuPriceDTO.getCategoryNo());
        menuUpdateDTO.setSalePrice(menuPriceDTO.getSalePrice());
        menuUpdateDTO.setImageSaveName(menuPriceDTO.getImageSaveName());
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

    public int updateMenu(MenuUpdateDTO menuUpdateDTO) {

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

        menuIngredientJpaRepository.deleteByMenuNo(menuUpdateDTO.getMenuNo());
        for(int i = 0; i < ingredientNoArr.length; i++) {
            for(int j = 0; j < quantityArr.length; j++) {
                if(i == j) {
                    menuIngredientJpaRepository.save(new TMenuIngredient(ingredientNoArr[i], menuUpdateDTO.getMenuNo(), quantityArr[i]));
                }
            }
        }

        TCategory category = null;
        Optional<TCategory> optionalCategory = categoryJpaRepository.findById(menuUpdateDTO.getCategoryNo());
        if(optionalCategory.isPresent()) {
            category = optionalCategory.get();
        }

        TSaleStatus saleStatus = null;
        Optional<TSaleStatus> optionalSaleStatus = saleStatusJpaRepository.findById(menuUpdateDTO.getSaleStatusNo());
        if(optionalSaleStatus.isPresent()) {
            saleStatus = optionalSaleStatus.get();
        }

        TMenu menu = null;
        Optional<TMenu> optionalMenu = menuJpaRepository.findById(menuUpdateDTO.getMenuNo());
        if(optionalMenu.isPresent()) {
            menu = optionalMenu.get();
        }

        menu.setMenuName(menuUpdateDTO.getMenuName());
        menu.setCategory(category);
        menu.setSaleStatus(saleStatus);

        if(menuUpdateDTO.getImageOriginName() != null) {
            menu.setImageOriginName(menuUpdateDTO.getImageOriginName());
            menu.setImageSaveName(menuUpdateDTO.getImageSaveName());
            menu.setImageExt(menuUpdateDTO.getImageExt());
            menu.setImagePath(menuUpdateDTO.getImagePath());
        }

        MenuPriceDTO menuPriceDTO = menuPriceJpaRepository.findMenuPriceDTO(menuUpdateDTO.getMenuNo());

        int salePrice = menuPriceDTO.getSalePrice();
        int nextSalePrice = menuUpdateDTO.getSalePrice();
        if(salePrice != nextSalePrice) {
            menuPriceJpaRepository.save(new TMenuPrice(menuUpdateDTO.getMenuNo(), ComUtils.getNextSeq(menuPriceDTO.getMenuSeq()), nextSalePrice));
        }

        return 1;
    }

    public List<MenuListDTO> selectMenuListByFirstCategory() {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("categoryNo",  categoryJpaRepository.findFirstByUseYnOrderByPriority("1").getCategoryNo());

        List<MenuListDTO> menuList = menuMapper.selectMenuListV2(paramMap);
        List<MenuIngredientListDTO> menuIngredientList = menuIngredientJpaRepository.findMenuIngredientListDTOV1();

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
