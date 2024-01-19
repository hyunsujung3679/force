package com.hsj.force.login.controller;

import com.hsj.force.domain.LoginForm;
import com.hsj.force.domain.Login;
import com.hsj.force.login.service.LoginService;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    public final LoginService loginService;

    public final OpenService openCloseService;
    @GetMapping
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "login/loginForm";
    }

    @PostMapping
    public String login(@ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Login loginMember = loginService.findUser(loginForm);
        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginMember", loginMember);

        if(openCloseService.selectIsOpen() > 0) {
            return "redirect:/table";
        } else {
            return "redirect:/open";
        }
    }
}
