package com.hsj.force.category.controller;

import com.hsj.force.category.service.CategoryService;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryDTO;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final OpenService openService;
    private final CategoryService categoryService;

    @GetMapping
    public String categoryForm(HttpSession session, Model model) {

        if(openService.selectIsOpen() == 0) {
            return "redirect:/open";
        }

        User loginMember = (User) session.getAttribute("loginMember");
        CategoryDTO categoryForm = categoryService.selectCategoryInfo(loginMember);

        model.addAttribute("header", categoryForm.getCommonLayoutForm());
        model.addAttribute("categoryList", categoryForm.getCategoryList());

        return "category/categoryForm";
    }
}
