package com.hsj.force.menu.repository;

import com.hsj.force.domain.dto.MenuListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MenuMapper {

    List<MenuListDTO> selectMenuListV1();

    List<MenuListDTO> selectMenuListV2(Map<String, Object> paramMap);

    List<MenuListDTO> selectMenuListV3();

}
