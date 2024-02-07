package com.hsj.force.menu.repository;

import com.hsj.force.domain.dto.MenuDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

    List<MenuDTO> selectMenuList(String storeNo);

    MenuDTO selectMenu(String menuNo);
}
