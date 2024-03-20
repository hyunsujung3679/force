package com.hsj.force.ingredient.controller;

import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.Ingredient;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.IngredientInsertDTO;
import com.hsj.force.ingredient.service.IngredientService;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/ingredient")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;
    private final CommonService commonService;
    private final OpenService openService;

    @GetMapping
    public String ingredientListForm(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute("loginMember");
        if(openService.selectIsOpen(loginMember.getStoreNo()) == 0) {
            return "redirect:/open";
        }

        Map<String, Object> map = ingredientService.selectIngredientInfo(loginMember);

        model.addAttribute("header", map.get("commonLayoutForm"));
        model.addAttribute("ingredientList", map.get("ingredientList"));

        return "ingredient/ingredientList";
    }

    @GetMapping("/insert")
    public String ingredientInsertForm(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute("loginMember");
        CommonLayoutDTO commonLayoutDTO = commonService.selectHeaderInfo(loginMember);

        model.addAttribute("header", commonLayoutDTO);
        model.addAttribute("ingredient", new IngredientInsertDTO());

        return "ingredient/ingredientInsert";
    }

    @ResponseBody
    @GetMapping("/list")
    public List<Ingredient> selectIngredientList(HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        return ingredientService.selectIngredientList(loginMember.getStoreNo());
    }
}
