package com.hsj.force.ingredient.controller;

import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.Ingredient;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.*;
import com.hsj.force.ingredient.service.IngredientService;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/ingredient")
@RequiredArgsConstructor
public class IngredientController {

    private final MessageSource messageSource;
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

    @PostMapping("/insert")
    public String insertIngredient(@ModelAttribute IngredientInsertDTO ingredient,
                                   HttpSession session,
                                   Model model) {

        Map<String, String> errors = new HashMap<>();
        User loginMember = (User) session.getAttribute("loginMember");
        CommonLayoutDTO commonLayoutDTO = commonService.selectHeaderInfo(loginMember);

        if(!StringUtils.hasText(ingredient.getIngredientName())) {
            errors.put("ingredientName", messageSource.getMessage("message.input.ingredient.name", null, Locale.KOREA));
        }
        if(!StringUtils.hasText(ingredient.getQuantityStr())) {
            errors.put("quantityStr", messageSource.getMessage("message.input.quantityStr", null, Locale.KOREA));
        } else {
            try {
                Double.parseDouble(ingredient.getQuantityStr().replaceAll(",", ""));
            } catch (NumberFormatException e) {
                errors.put("quantityStr", messageSource.getMessage("message.rewrite", null, Locale.KOREA));
            }
        }
        if(!errors.isEmpty()) {
            model.addAttribute("header", commonLayoutDTO);
            model.addAttribute("ingredient", new IngredientInsertDTO());
            model.addAttribute("errors", errors);
            return "ingredient/ingredientInsert";
        }

        ingredientService.insertIngredient(loginMember, ingredient);
        return "redirect:/ingredient";
    }

    @GetMapping("/{ingredientNo}/update")
    public String categoryUpdateForm(@PathVariable String ingredientNo,
                                     HttpSession session,
                                     Model model) {

        User loginMember = (User) session.getAttribute("loginMember");

        Map<String, Object> map = ingredientService.selectIngredientUpdateInfo(loginMember, ingredientNo);

        model.addAttribute("header", map.get("commonLayoutForm"));
        model.addAttribute("ingredient", map.get("ingredient"));
        model.addAttribute("inDeReasonList", map.get("inDeReasonList"));

        return "ingredient/ingredientUpdate";
    }

    @PostMapping("/{ingredientNo}/update")
    public String updateIngredient(@ModelAttribute IngredientUpdateDTO ingredient,
                                   HttpSession session,
                                   Model model) {

        Map<String, String> errors = new HashMap<>();
        User loginMember = (User) session.getAttribute("loginMember");
        CommonLayoutDTO commonLayoutDTO = commonService.selectHeaderInfo(loginMember);

        if(!StringUtils.hasText(ingredient.getIngredientName())) {
            errors.put("ingredientName", messageSource.getMessage("message.input.ingredient.name", null, Locale.KOREA));
        }
        if(!StringUtils.hasText(ingredient.getInDeQuantity())) {
            errors.put("inDeQuantity", messageSource.getMessage("message.input.in.de.quantity", null, Locale.KOREA));
        } else {
            try {
                Double.parseDouble(ingredient.getInDeQuantity().replaceAll(",", ""));
            } catch (NumberFormatException e) {
                errors.put("inDeQuantity", messageSource.getMessage("message.rewrite", null, Locale.KOREA));
            }
        }
        if(!StringUtils.hasText(ingredient.getInDeReasonNo())) {
            errors.put("inDeReasonNo", messageSource.getMessage("message.input.in.de.reason", null, Locale.KOREA));
        }

        if(!errors.isEmpty()) {
            model.addAttribute("header", commonLayoutDTO);
            model.addAttribute("ingredient", new IngredientUpdateDTO());
            model.addAttribute("errors", errors);
            return "ingredient/ingredientUpdate";
        }
        ingredientService.updateIngredient(loginMember, ingredient);
        return "redirect:/ingredient";
    }

    @ResponseBody
    @GetMapping("/list")
    public List<Ingredient> selectIngredientList(HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        return ingredientService.selectIngredientList(loginMember.getStoreNo());
    }
}
