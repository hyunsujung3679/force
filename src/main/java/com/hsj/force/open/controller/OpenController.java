package com.hsj.force.open.controller;

import com.hsj.force.domain.Login;
import com.hsj.force.domain.OpenForm;
import com.hsj.force.domain.OpenSave;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

        Login loginMember = (Login) session.getAttribute("loginMember");

        OpenForm openForm = openService.selectOpenInfo();
        openForm.setOpener(loginMember.getUserId() + " - " + loginMember.getUserName());
        openForm.setCurrentDate(LocalDateTime.now());
        openForm.setCurrentTime(LocalDateTime.now());

        model.addAttribute("openForm", openForm);

        return "open/openForm";
    }

    @PostMapping
    public String insertOpen(@ModelAttribute OpenSave openSave,
                             BindingResult bindingResult,
                             HttpSession session) {

        if(bindingResult.hasErrors()) {
            return "open/openForm";
        }

        Login loginMember = (Login) session.getAttribute("loginMember");
        openSave.setInsertId(loginMember.getUserId());
        openSave.setModifyId(loginMember.getUserId());

        int result = openService.insertOpen(openSave);
        if(result > 0) {
            return "redirect:/table";
        } else {
            return "open/openForm";
        }
    }
}
