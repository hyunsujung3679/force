package com.hsj.force.menu.service;

import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.MenuInsertDTO;
import com.hsj.force.domain.dto.MenuListDTO;
import com.hsj.force.domain.dto.MenuUpdateDTO;
import com.hsj.force.login.service.LoginService;
import com.hsj.force.open.service.OpenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Value("${file.dir}")
    private String fileDir;
    @Autowired
    private MenuService menuService;
    private User loginMember = null;

    @BeforeEach
    public void beforeEach() {
        loginMember = new User();
        loginMember.setStoreNo("S001");
        loginMember.setUserId("SUSU");
        loginMember.setUserNo("U001");
        loginMember.setUserName("정현수");
        loginMember.setPhoneNum("01027287526");
        loginMember.setPassword("1234");
        loginMember.setUseYn("1");
    }

    @Test
    void selectMenuListByCategoryNo() {
        List<MenuListDTO> menuListList =  menuService.selectMenuListByCategoryNo(loginMember.getStoreNo(), "C001");
        assertThat(menuListList).isNotNull();
    }

    @Test
    void selectMenuInfo() {
        Map<String, Object> map = menuService.selectMenuInfo(loginMember);
        assertThat(map.get("commonLayoutForm")).isNotNull();
        assertThat(map.get("menuList")).isNotNull();
    }

    @Test
    void insertMenu() {

        String imageOriginName = "광어.PNG";
        String imageExt = imageOriginName.substring(imageOriginName.lastIndexOf("."));
        String imageSaveName = UUID.randomUUID().toString().replace("-", "") + imageExt;

        MenuInsertDTO menu = new MenuInsertDTO();
        menu.setMenuName("TEST");
        menu.setSaleStatusNo("SS001");
        menu.setCategoryNo("C001");
        menu.setSalePrice(10000);
        menu.setIngredientNo1("I001");
        menu.setIngredientNo2("");
        menu.setIngredientNo3("");
        menu.setIngredientNo4("");
        menu.setQuantity1(1.0);
        menu.setImageOriginName(imageOriginName);
        menu.setImageSaveName(imageSaveName);
        menu.setImagePath(fileDir);
        menu.setImageExt(imageExt);

        int result = menuService.insertMenu(loginMember, menu);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void selectMenuUpdateInfo() {
        Map<String, Object> map = menuService.selectMenuUpdateInfo(loginMember, "M001");
        assertThat(map.get("commonLayoutForm")).isNotNull();
        assertThat(map.get("menu")).isNotNull();
        assertThat(map.get("ingredientQuantityList")).isNotNull();
    }

    @Test
    void updateMenu() {

        String imageOriginName = "광어.PNG";
        String imageExt = imageOriginName.substring(imageOriginName.lastIndexOf("."));
        String imageSaveName = UUID.randomUUID().toString().replace("-", "") + imageExt;

        MenuUpdateDTO menu = new MenuUpdateDTO();
        menu.setMenuNo("M001");
        menu.setMenuName("TEST");
        menu.setSaleStatusNo("SS001");
        menu.setCategoryNo("C001");
        menu.setSalePrice(10000);
        menu.setIngredientNo1("I001");
        menu.setIngredientNo2("");
        menu.setIngredientNo3("");
        menu.setIngredientNo4("");
        menu.setQuantity1(1.0);
        menu.setImageOriginName(imageOriginName);
        menu.setImageSaveName(imageSaveName);
        menu.setImagePath(fileDir);
        menu.setImageExt(imageExt);

        int result = menuService.updateMenu(loginMember, menu);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void selectMenuListByFirstCategory() {
        List<MenuListDTO> menuListList = menuService.selectMenuListByFirstCategory(loginMember.getStoreNo());
        assertThat(menuListList).isNotNull();
    }
}