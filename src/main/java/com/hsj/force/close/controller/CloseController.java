package com.hsj.force.close.controller;

import com.hsj.force.common.Constants;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.OpenDTO;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/close")
@RequiredArgsConstructor
public class CloseController {

    private final OpenService openService;

    @GetMapping
    public String closeForm(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute(Constants.LOGIN_MEMBER);
        if(openService.selectIsOpen(loginMember.getStoreNo()) == 0) {
            return "redirect:/open";
        }

//        Close close = closeService.selectCloseInfo(loginMember.getStoreNo());
//
//        model.addAttribute("close", close);

        return "close/closeForm";
    }
}
