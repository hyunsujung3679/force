package com.hsj.force.openclose.controller;

import com.hsj.force.domain.User;
import com.hsj.force.domain.response.OpenCloseRes;
import com.hsj.force.openclose.service.OpenCloseService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class OpenCloseController {

    private final OpenCloseService openCloseService;

    @GetMapping("/open")
    public String open(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute("loginMember");

        OpenCloseRes openClose = openCloseService.selectOpenCloseInfo();
        openClose.setOpener(loginMember.getUserId() + " - " + loginMember.getUserName());
        openClose.setCurrentDate(LocalDateTime.now());
        openClose.setCurrentTime(LocalDateTime.now());

        model.addAttribute("openClose", openClose);

        return "openclose/open";
    }

}
