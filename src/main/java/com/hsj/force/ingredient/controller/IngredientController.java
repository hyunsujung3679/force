package com.hsj.force.ingredient.controller;

import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.IngredientInsertDTO;
import com.hsj.force.domain.dto.IngredientListDTO;
import com.hsj.force.domain.dto.IngredientUpdateDTO;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.ingredient.service.IngredientService;
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
@RequestMapping("/ingredient")
@RequiredArgsConstructor
public class IngredientController {

    private final MessageSource messageSource;
    private final IngredientService ingredientService;
    private final CommonService commonService;
    private final OpenService openService;

    @GetMapping
    public String ingredientListForm(HttpSession session, Model model) {

        TUser loginMember = (TUser) session.getAttribute("loginMember");
        String storeNo = loginMember.getStore().getStoreNo();

        if(!openService.findIsOpen(storeNo)) {
            return "redirect:/open";
        }

        Map<String, Object> map = ingredientService.selectIngredientInfo(loginMember);

        model.addAttribute("header", map.get("commonLayoutForm"));
        model.addAttribute("ingredientList", map.get("ingredientList"));

        return "ingredient/ingredientList";
    }

    @GetMapping("/insert")
    public String ingredientInsertForm(HttpSession session, Model model) {

        TUser loginMember = (TUser) session.getAttribute("loginMember");
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
        TUser loginMember = (TUser) session.getAttribute("loginMember");
        CommonLayoutDTO commonLayoutDTO = commonService.selectHeaderInfo(loginMember);

        if(!StringUtils.hasText(ingredient.getIngredientName())) {
            errors.put("ingredientName", messageSource.getMessage("message.input.ingredient.name", null, Locale.KOREA));
        }
        if(ingredient.getQuantity() == null) {
            errors.put("quantity", messageSource.getMessage("message.input.quantity", null, Locale.KOREA));
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

        TUser loginMember = (TUser) session.getAttribute("loginMember");

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
        TUser loginMember = (TUser) session.getAttribute("loginMember");

        if(!StringUtils.hasText(ingredient.getIngredientName())) {
            errors.put("ingredientName", messageSource.getMessage("message.input.ingredient.name", null, Locale.KOREA));
        }
        if(ingredient.getInDeQuantity() == null) {
            errors.put("inDeQuantity", messageSource.getMessage("message.input.in.de.quantity", null, Locale.KOREA));
        }
        if(!StringUtils.hasText(ingredient.getInDeReasonNo())) {
            errors.put("inDeReasonNo", messageSource.getMessage("message.input.in.de.reason", null, Locale.KOREA));
        }

        if(!errors.isEmpty()) {
            Map<String, Object> map = ingredientService.selectIngredientUpdateInfo(loginMember, ingredient.getIngredientNo());
            model.addAttribute("header", map.get("commonLayoutForm"));
            model.addAttribute("ingredient", map.get("ingredient"));
            model.addAttribute("inDeReasonList", map.get("inDeReasonList"));
            model.addAttribute("errors", errors);
            return "ingredient/ingredientUpdate";
        }
        ingredientService.updateIngredient(loginMember, ingredient);
        return "redirect:/ingredient";
    }

    @GetMapping("/list")
    @ResponseBody
    public List<IngredientListDTO> selectIngredientList(HttpSession session) {
        TUser loginMember = (TUser) session.getAttribute("loginMember");
        return ingredientService.selectIngredientList(loginMember.getStore().getStoreNo());
    }
}
