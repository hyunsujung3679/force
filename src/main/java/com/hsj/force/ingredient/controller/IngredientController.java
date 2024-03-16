package com.hsj.force.ingredient.controller;

import com.hsj.force.domain.Ingredient;
import com.hsj.force.domain.User;
import com.hsj.force.ingredient.service.IngredientService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/ingredient")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @ResponseBody
    @GetMapping("/list")
    public List<Ingredient> selectIngredientList(HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        return ingredientService.selectIngredientList(loginMember.getStoreNo());
    }
}
