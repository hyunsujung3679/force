package com.hsj.force.menu.controller;

import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import com.hsj.force.domain.dto.MenuInsertDTO;
import com.hsj.force.domain.dto.MenuListDTO;
import com.hsj.force.domain.dto.MenuUpdateDTO;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.menu.service.MenuService;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    @Value("${file.dir}")
    private String fileDir;
    private final MessageSource messageSource;
    private final OpenService openService;
    private final MenuService menuService;
    private final CommonService commonService;

    @GetMapping
    public String menuListForm(HttpSession session, Model model) {

        TUser loginMember = (TUser) session.getAttribute("loginMember");
        if(!openService.selectIsOpen()) {
            return "redirect:/open";
        }

        Map<String, Object> map = menuService.selectMenuInfo(loginMember);

        model.addAttribute("header", map.get("commonLayoutForm"));
        model.addAttribute("menuList", map.get("menuList"));

        return "menu/menuList";
    }

    @GetMapping("/insert")
    public String menuInsertForm(HttpSession session, Model model) {

        TUser loginMember = (TUser) session.getAttribute("loginMember");
        CommonLayoutDTO commonLayoutDTO = commonService.selectHeaderInfo(loginMember);

        model.addAttribute("header", commonLayoutDTO);
        model.addAttribute("menu", new MenuInsertDTO());

        return "menu/menuInsert";
    }

    @PostMapping("/insert")
    public String insertMenu(@ModelAttribute MenuInsertDTO menu,
                             @RequestParam MultipartFile file,
                             HttpSession session,
                             Model model) throws IOException {

        Map<String, String> errors = new HashMap<>();
        TUser loginMember = (TUser) session.getAttribute("loginMember");
        CommonLayoutDTO commonLayoutDTO = commonService.selectHeaderInfo(loginMember);

        if(!StringUtils.hasText(menu.getMenuName())) {
            errors.put("menuName", messageSource.getMessage("message.input.menu.name", null, Locale.KOREA));
        }
        if(menu.getSalePrice() == null) {
            errors.put("salePrice", messageSource.getMessage("message.input.sale.price", null, Locale.KOREA));
        }

        List<String> ingredientNoList = new ArrayList<>();
        ingredientNoList.add(menu.getIngredientNo1());
        ingredientNoList.add(menu.getIngredientNo2());
        ingredientNoList.add(menu.getIngredientNo3());
        ingredientNoList.add(menu.getIngredientNo4());
        ingredientNoList.removeAll(List.of(""));

        if(!"".equals(menu.getIngredientNo1()) && menu.getQuantity1() == null) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity1", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo2()) && menu.getQuantity2() == null) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity2", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo3()) && menu.getQuantity3() == null) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity3", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo4()) && menu.getQuantity4() == null) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity4", null, Locale.KOREA));
        } else if(ingredientNoList.size() != ingredientNoList.stream().distinct().count()) {
            errors.put("ingredientNo", messageSource.getMessage("message.no.input.same.ingredient.no", null, Locale.KOREA));
        }

        if (file.isEmpty()) {
            errors.put("file", messageSource.getMessage("message.input.file", null, Locale.KOREA));
        }

        if(!errors.isEmpty()) {
            model.addAttribute("header", commonLayoutDTO);
            model.addAttribute("menu", new MenuInsertDTO());
            model.addAttribute("errors", errors);
            return "menu/menuInsert";
        }

        String imageOriginName = file.getOriginalFilename();
        String imageExt = imageOriginName.substring(imageOriginName.lastIndexOf("."));
        String imageSaveName = UUID.randomUUID().toString().replace("-", "") + imageExt;
        String fullPath = fileDir + imageSaveName;

        menu.setImageOriginName(imageOriginName);
        menu.setImageExt(imageExt);
        menu.setImageSaveName(imageSaveName);
        menu.setImagePath(fileDir);
        int result = menuService.insertMenu(menu);
        if(result > 0) {
            File mkdir = new File(fileDir);
            if(!mkdir.exists()) {
                mkdir.mkdirs();
            }
            file.transferTo(new File(fullPath));
            return "redirect:/menu";
        } else {
            return "menu/menuInsert";
        }
    }

    @GetMapping("/{menuNo}/update")
    public String menuUpdateForm(@PathVariable String menuNo,
                                 HttpSession session,
                                 Model model) {

        TUser loginMember = (TUser) session.getAttribute("loginMember");

        Map<String, Object> map = menuService.selectMenuUpdateInfo(loginMember, menuNo);
        MenuUpdateDTO menu = (MenuUpdateDTO) map.get("menu");

        model.addAttribute("header", map.get("commonLayoutForm"));
        model.addAttribute("menu", map.get("menu"));
        model.addAttribute("ingredientQuantityList", map.get("ingredientQuantityList"));
        model.addAttribute("imageSaveName", menu.getImageSaveName());

        return "menu/menuUpdate";
    }

    @PostMapping("/{menuNo}/update")
    public String updateMenu(@PathVariable String menuNo,
                             @ModelAttribute MenuUpdateDTO menu,
                             @RequestParam MultipartFile file,
                             HttpSession session,
                             Model model) throws IOException {

        Map<String, String> errors = new HashMap<>();
        TUser loginMember = (TUser) session.getAttribute("loginMember");

        if(!StringUtils.hasText(menu.getMenuName())) {
            errors.put("menuName", messageSource.getMessage("message.input.menu.name", null, Locale.KOREA));
        }
        if(menu.getSalePrice() == null) {
            errors.put("salePrice", messageSource.getMessage("message.input.sale.price", null, Locale.KOREA));
        }

        List<String> ingredientNoList = new ArrayList<>();
        ingredientNoList.add(menu.getIngredientNo1());
        ingredientNoList.add(menu.getIngredientNo2());
        ingredientNoList.add(menu.getIngredientNo3());
        ingredientNoList.add(menu.getIngredientNo4());
        ingredientNoList.removeAll(List.of(""));

        if(!"".equals(menu.getIngredientNo1()) && menu.getQuantity1() == null) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity1", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo2()) && menu.getQuantity2() == null) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity2", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo3()) && menu.getQuantity3() == null) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity3", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo4()) && menu.getQuantity4() == null) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity4", null, Locale.KOREA));
        } else if(ingredientNoList.size() != ingredientNoList.stream().distinct().count()) {
            errors.put("ingredientNo", messageSource.getMessage("message.no.input.same.ingredient.no", null, Locale.KOREA));
        }

        if(!errors.isEmpty()) {
            Map<String, Object> map = menuService.selectMenuUpdateInfo(loginMember, menuNo);
            MenuUpdateDTO menuUpdateDTO = (MenuUpdateDTO) map.get("menu");
            model.addAttribute("header", map.get("commonLayoutForm"));
            model.addAttribute("menu", map.get("menu"));
            model.addAttribute("ingredientQuantityList", map.get("ingredientQuantityList"));
            model.addAttribute("imageSaveName", menuUpdateDTO.getImageSaveName());
            model.addAttribute("errors", errors);
            return "menu/menuUpdate";
        }

        String fullPath = "";
        if(!file.isEmpty()) {
            String imageOriginName = file.getOriginalFilename();
            String imageExt = imageOriginName.substring(imageOriginName.lastIndexOf("."));
            String imageSaveName = UUID.randomUUID().toString().replace("-", "") + imageExt;
            fullPath = fileDir + imageSaveName;

            menu.setImageOriginName(imageOriginName);
            menu.setImageExt(imageExt);
            menu.setImageSaveName(imageSaveName);
            menu.setImagePath(fileDir);
        }

        int result = menuService.updateMenu(menu);
        if(result > 0) {
            if(!file.isEmpty()) {
                File mkdir = new File(fileDir);
                if(!mkdir.exists()) {
                    mkdir.mkdirs();
                }
                file.transferTo(new File(fullPath));
            }
            return "redirect:/menu";
        } else {
            return "menu/menuUpdate";
        }
    }

    @GetMapping("/image/{fileName}")
    @ResponseBody
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileDir + fileName);
    }

    @GetMapping("/{categoryNo}/list")
    @ResponseBody
    public List<MenuListDTO> selectMenuListByCategoryNo(@PathVariable String categoryNo) {
        return menuService.selectMenuListByCategoryNo(categoryNo);
    }

    @GetMapping("/first-category/list")
    @ResponseBody
    public List<MenuListDTO> selectMenuListByFirstCategory() {
        return menuService.selectMenuListByFirstCategory();
    }

}
