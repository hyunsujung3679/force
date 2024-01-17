package com.hsj.force.openclose.controller;

import com.hsj.force.domain.User;
import com.hsj.force.domain.out.OpenCloseOutDTO;
import com.hsj.force.openclose.service.OpenCloseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class OpenCloseController {

    private final OpenCloseService openCloseService;

    @GetMapping("/open")
    public String open(HttpServletRequest request, Model model) throws ParseException {

        HttpSession session = request.getSession();
        User loginMember = (User) session.getAttribute("loginMember");

        OpenCloseOutDTO openClose = openCloseService.selectOpenCloseInfo();
        openClose.setOpener(loginMember.getUserId() + " - " + loginMember.getUserName());
        model.addAttribute("openClose", openClose);
        return "openclose/open";
    }

}
