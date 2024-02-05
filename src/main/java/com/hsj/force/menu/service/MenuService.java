package com.hsj.force.menu.service;

import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.menu.repository.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

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
}
