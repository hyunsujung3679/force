package com.hsj.force.menu.controller;

import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryDTO;
import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.menu.service.MenuService;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final OpenService openService;
    private final MenuService menuService;

    @GetMapping
    public String menuForm(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute("loginMember");
        if(openService.selectIsOpen(loginMember.getStoreNo()) == 0) {
            return "redirect:/open";
        }

        MenuDTO menuForm = menuService.selectMenuInfo(loginMember);

        model.addAttribute("header", menuForm.getCommonLayoutForm());
        model.addAttribute("menuList", menuForm.getMenuList());

        return "menu/menuForm";
    }

    @ResponseBody
    @GetMapping("/{categoryNo}")
    public List<MenuDTO> selectMenuListByCategoryNo(HttpSession session, @PathVariable String categoryNo) {
        User loginMember = (User) session.getAttribute("loginMember");
        return menuService.selectMenuListByCategoryNo(loginMember.getStoreNo(), categoryNo);
    }

    @PostMapping("/insert")
    @ResponseBody
    public int insertMenu(HttpSession session, @RequestBody Map<String, Object> parameter) {
        User loginMember = (User) session.getAttribute("loginMember");

        String[] ingredientNoArr = (String[]) parameter.get("ingredientArr");
        String[] quantityArr = (String[]) parameter.get("quantityArr");
        if(ingredientNoArr.length != quantityArr.length) {
            return 0;
        }

        return menuService.insertMenu(loginMember, parameter);
    }
}
