package com.hsj.force.menu.controller;

import com.hsj.force.category.service.CategoryService;
import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.*;
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
    public String menuForm(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute("loginMember");
        if(openService.selectIsOpen(loginMember.getStoreNo()) == 0) {
            return "redirect:/open";
        }

        Map<String, Object> map = menuService.selectMenuInfo(loginMember);

        model.addAttribute("header", map.get("commonLayoutForm"));
        model.addAttribute("menuList", map.get("menuList"));

        return "menu/menuList";
    }

    @GetMapping("/insert")
    public String menuInsertForm(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute("loginMember");
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
        User loginMember = (User) session.getAttribute("loginMember");
        CommonLayoutDTO commonLayoutDTO = commonService.selectHeaderInfo(loginMember);

        if(!StringUtils.hasText(menu.getMenuName())) {
            errors.put("menuName", messageSource.getMessage("message.input.menu.name", null, Locale.KOREA));
        }
        if(!StringUtils.hasText(menu.getSalePriceStr())) {
            errors.put("salePrice", messageSource.getMessage("message.input.sale.price", null, Locale.KOREA));
        } else {
            try {
                Integer.parseInt(menu.getSalePriceStr().replaceAll(",", ""));
            } catch (NumberFormatException e) {
                errors.put("salePriceStr", messageSource.getMessage("message.input.price.number", null, Locale.KOREA));
            }
        }
        String[] ingredientNoArr = new String[]{menu.getIngredientNo1(), menu.getIngredientNo2(), menu.getIngredientNo3(), menu.getIngredientNo4()};
        List<String> ingredientNoList = Arrays.asList(ingredientNoArr);
        int cnt = Collections.frequency(Arrays.asList(ingredientNoArr), "");
        if(cnt == 4) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.ingredientNo", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo1()) && "".equals(menu.getQuantityStr1())) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity1", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo2()) && "".equals(menu.getQuantityStr2())) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity2", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo3()) && "".equals(menu.getQuantityStr3())) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity3", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo4()) && "".equals(menu.getQuantityStr4())) {
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
        int result = menuService.insertMenu(loginMember, menu);
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

        User loginMember = (User) session.getAttribute("loginMember");

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
        User loginMember = (User) session.getAttribute("loginMember");
        CommonLayoutDTO commonLayoutDTO = commonService.selectHeaderInfo(loginMember);

        if(!StringUtils.hasText(menu.getMenuName())) {
            errors.put("menuName", messageSource.getMessage("message.input.menu.name", null, Locale.KOREA));
        }
        if(!StringUtils.hasText(menu.getSalePriceStr())) {
            errors.put("salePrice", messageSource.getMessage("message.input.sale.price", null, Locale.KOREA));
        } else {
            try {
                Integer.parseInt(menu.getSalePriceStr().replaceAll(",", ""));
            } catch (NumberFormatException e) {
                errors.put("salePriceStr", messageSource.getMessage("message.input.price.number", null, Locale.KOREA));
            }
        }
        String[] ingredientNoArr = new String[]{menu.getIngredientNo1(), menu.getIngredientNo2(), menu.getIngredientNo3(), menu.getIngredientNo4()};
        List<String> ingredientNoList = new ArrayList<>(Arrays.asList(ingredientNoArr));
        ingredientNoList.removeAll(Arrays.asList("", null));

//        for(int i = 0; i < ingredientNoList.size(); i++) {
//            if("".equals(ingredientNoList.get(i))) {
//                ingredientNoList.remove(i);
//            }
//        }

        int cnt = Collections.frequency(Arrays.asList(ingredientNoArr), "");
        if(cnt == 4) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.ingredientNo", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo1()) && "".equals(menu.getQuantityStr1())) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity1", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo2()) && "".equals(menu.getQuantityStr2())) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity2", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo3()) && "".equals(menu.getQuantityStr3())) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity3", null, Locale.KOREA));
        } else if(!"".equals(menu.getIngredientNo4()) && "".equals(menu.getQuantityStr4())) {
            errors.put("ingredientNo", messageSource.getMessage("message.input.quantity4", null, Locale.KOREA));
        } else if(ingredientNoList.size() != ingredientNoList.stream().distinct().count()) {
            errors.put("ingredientNo", messageSource.getMessage("message.no.input.same.ingredient.no", null, Locale.KOREA));
        }

        if(!errors.isEmpty()) {
            model.addAttribute("header", commonLayoutDTO);
            model.addAttribute("menu", new MenuInsertDTO());
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

        int result = menuService.updateMenu(loginMember, menu);
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

    @ResponseBody
    @GetMapping("/image/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileDir + fileName);
    }

    @ResponseBody
    @GetMapping("/{categoryNo}")
    public List<MenuDTO> selectMenuListByCategoryNo(HttpSession session, @PathVariable String categoryNo) {
        User loginMember = (User) session.getAttribute("loginMember");
        return menuService.selectMenuListByCategoryNo(loginMember.getStoreNo(), categoryNo);
    }

}
