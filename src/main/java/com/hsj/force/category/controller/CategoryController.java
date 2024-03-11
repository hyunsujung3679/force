package com.hsj.force.category.controller;

import com.hsj.force.category.service.CategoryService;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.Category;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryListDTO;
import com.hsj.force.domain.dto.CategoryInsertDTO;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final OpenService openService;
    private final CategoryService categoryService;
    private final CommonService commonService;

    @GetMapping
    public String categoryListForm(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute("loginMember");
        if(openService.selectIsOpen(loginMember.getStoreNo()) == 0) {
            return "redirect:/open";
        }

        CategoryListDTO category = categoryService.selectCategoryInfo(loginMember);

        model.addAttribute("header", category.getCommonLayoutForm());
        model.addAttribute("categoryList", category.getCategoryList());

        return "category/categoryList";
    }

    @GetMapping("/insert")
    public String categoryInsertForm(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute("loginMember");
        CommonLayoutDTO commonLayoutDTO = commonService.selectHeaderInfo(loginMember);

        model.addAttribute("header", commonLayoutDTO);
        model.addAttribute("category", new CategoryInsertDTO());

        return "category/categoryInsert";
    }


    @PostMapping("/insert")
    @ResponseBody
    public int insertCategory(HttpSession session, @RequestBody Category category) {

        if(category.getPriority() < 1) {
            return category.getPriority();
        }

        User loginMember = (User) session.getAttribute("loginMember");
        return categoryService.insertCategory(loginMember, category);
    }

    @PostMapping("/update")
    @ResponseBody
    public int updateCategory(HttpSession session, @RequestBody Category category) {

        if(category.getPriority() < 1) {
            return category.getPriority();
        }

        User loginMember = (User) session.getAttribute("loginMember");
        return categoryService.updateCategory(loginMember, category);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<Category> selectCategoryList(HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        return categoryService.selectCategoryList(loginMember.getStoreNo());
    }
}
