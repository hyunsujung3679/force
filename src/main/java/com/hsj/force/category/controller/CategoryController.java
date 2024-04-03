package com.hsj.force.category.controller;

import com.hsj.force.category.service.CategoryService;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CategoryInsertDTO;
import com.hsj.force.domain.dto.CategoryListDTO;
import com.hsj.force.domain.dto.CategoryUpdateDTO;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final MessageSource messageSource;
    private final OpenService openService;
    private final CategoryService categoryService;
    private final CommonService commonService;

    @GetMapping
    public String categoryListForm(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute("loginMember");
        if(openService.selectIsOpen(loginMember.getStoreNo()) == 0) {
            return "redirect:/open";
        }

        Map<String, Object> map = categoryService.selectCategoryListInfo(loginMember);

        model.addAttribute("header", map.get("commonLayoutForm"));
        model.addAttribute("categoryList", map.get("categoryList"));

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
    public String insertCategory(@ModelAttribute CategoryInsertDTO category,
                                 HttpSession session,
                                 Model model) {

        Map<String, String> errors = new HashMap<>();
        User loginMember = (User) session.getAttribute("loginMember");
        CommonLayoutDTO commonLayoutDTO = commonService.selectHeaderInfo(loginMember);

        if(!StringUtils.hasText(category.getCategoryName())) {
            errors.put("categoryName", messageSource.getMessage("message.input.category.name", null, Locale.KOREA));
        }
        if(category.getPriority() == null) {
            errors.put("priority", messageSource.getMessage("message.input.priority", null, Locale.KOREA));
        }
        if(!errors.isEmpty()) {
            model.addAttribute("header", commonLayoutDTO);
            model.addAttribute("category", new CategoryInsertDTO());
            model.addAttribute("errors", errors);
            return "category/categoryInsert";
        }

        categoryService.insertCategory(loginMember, category);
        return "redirect:/category";
    }

    @GetMapping("/{categoryNo}/update")
    public String categoryUpdateForm(@PathVariable String categoryNo,
                                     HttpSession session,
                                     Model model) {

        User loginMember = (User) session.getAttribute("loginMember");

        Map<String, Object> map = categoryService.selectCategoryUpdateInfo(loginMember, categoryNo);

        model.addAttribute("header", map.get("commonLayoutForm"));
        model.addAttribute("category", map.get("category"));

        return "category/categoryUpdate";
    }

    @PostMapping("/{categoryNo}/update")
    public String updateCategory(@ModelAttribute CategoryUpdateDTO category,
                                 HttpSession session,
                                 Model model) {

        Map<String, String> errors = new HashMap<>();
        User loginMember = (User) session.getAttribute("loginMember");

        if(!StringUtils.hasText(category.getCategoryName())) {
            errors.put("categoryName", messageSource.getMessage("message.input.category.name", null, Locale.KOREA));
        }
        if(category.getPriority() == null) {
            errors.put("priority", messageSource.getMessage("message.input.priority", null, Locale.KOREA));
        }
        if(!errors.isEmpty()) {
            Map<String, Object> map = categoryService.selectCategoryUpdateInfo(loginMember, category.getCategoryNo());
            model.addAttribute("header", map.get("commonLayoutForm"));
            model.addAttribute("category", map.get("category"));
            model.addAttribute("errors", errors);
            return "category/categoryUpdate";
        }
        categoryService.updateCategory(loginMember, category);
        return "redirect:/category";
    }

    @GetMapping("/list")
    @ResponseBody
    public List<CategoryListDTO> selectCategoryList(HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        return categoryService.selectCategoryList(loginMember.getStoreNo());
    }
}
