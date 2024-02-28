package com.hsj.force.menu.service;

import com.hsj.force.common.repository.CommonMapper;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryDTO;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.menu.repository.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final CommonMapper commonMapper;
    private final MenuMapper menuMapper;

    public List<MenuDTO> selectMenuListByCategoryNo(String storeNo, String categoryNo) {
        List<MenuDTO> menuList = menuMapper.selectMenuList(storeNo);
        List<MenuDTO> menuListByCategoryNo = new ArrayList<>();
        for(MenuDTO menu : menuList) {
            if (categoryNo.equals(menu.getCategoryNo())) {
                menuListByCategoryNo.add(menu);
            }
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

        List<MenuDTO> menuList = menuMapper.selectMenuList();

        MenuDTO menuForm = new MenuDTO();
        menuForm.setCommonLayoutForm(commonLayoutForm);

        return menuForm;
    }
}
