package com.hsj.force.login.controller;

import com.hsj.force.domain.vo.User;
import com.hsj.force.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class LoginController {

    public final LoginService loginService;

    @GetMapping("/login")
    public String loginForm() {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute User user,
                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        int result = loginService.findByIdAndPassword(user);

        return null;
    }
}
