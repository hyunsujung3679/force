package com.hsj.force.open.controller;

import com.hsj.force.common.Constants;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.OpenDTO;
import com.hsj.force.domain.dto.OpenSaveDTO;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/open")
@RequiredArgsConstructor
public class OpenController {

    private final OpenService openService;

    @GetMapping
    public String openForm(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute(Constants.LOGIN_MEMBER);
          if(openService.selectIsOpen(loginMember.getStoreNo()) > 0) {
            return "redirect:/table";
        }

        OpenDTO open = openService.selectOpenInfo(loginMember.getStoreNo());
        open.setOpener(loginMember.getUserId() + " - " + loginMember.getUserName());
        open.setCurrentDate(LocalDateTime.now());
        open.setCurrentTime(LocalDateTime.now());

        model.addAttribute("openSaveDTO", open);

        return "open/openForm";
    }

    @PostMapping
    public String insertOpenClose(@ModelAttribute OpenSaveDTO open, HttpSession session) {
        User loginMember = (User) session.getAttribute(Constants.LOGIN_MEMBER);
        try {
            open.setOpenMoney(Integer.parseInt(open.getOpenMoneyStr().replaceAll(",", "")));
        } catch (NumberFormatException e) {
            return "redirect:/open";
        }
        open.setStoreNo(loginMember.getStoreNo());
        open.setInsertId(loginMember.getUserId());
        open.setModifyId(loginMember.getUserId());
        openService.insertOpen(open);

        return "redirect:/table";
    }
}
