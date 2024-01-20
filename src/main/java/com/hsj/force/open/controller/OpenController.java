package com.hsj.force.open.controller;

import com.hsj.force.domain.Login;
import com.hsj.force.domain.OpenForm;
import com.hsj.force.domain.OpenSave;
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

        Login loginMember = (Login) session.getAttribute("loginMember");

        OpenForm openForm = openService.selectOpenInfo();
        openForm.setOpener(loginMember.getUserId() + " - " + loginMember.getUserName());
        openForm.setCurrentDate(LocalDateTime.now());
        openForm.setCurrentTime(LocalDateTime.now());

        model.addAttribute("open", openForm);

        return "open/openForm";
    }

    @PostMapping
    public String open(@ModelAttribute OpenSave open,
                       BindingResult bindingResult,
                       HttpSession session) {

        if(bindingResult.hasErrors()) {
            return "open/openForm";
        }

        Login loginMember = (Login) session.getAttribute("loginMember");
        open.setInsertId(loginMember.getUserId());
        open.setModifyId(loginMember.getUserId());

        openService.insertOpen(open);
        return "redirect:/table";
    }
}
