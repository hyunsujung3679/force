package com.hsj.force.login.controller;

import com.hsj.force.domain.User;
import com.hsj.force.login.service.LoginService;
import com.hsj.force.openclose.service.OpenCloseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class LoginController {

    public final LoginService loginService;

    public final OpenCloseService openCloseService;
    @GetMapping("/login")
    public String loginForm(@ModelAttribute User user) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user,
                        BindingResult bindingResult,
                        HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        User loginMember = loginService.findUser(user);
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
