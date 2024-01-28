package com.hsj.force.login.controller;

import com.hsj.force.common.Constants;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.UserDTO;
import com.hsj.force.login.service.LoginService;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class LoginController {

    public final MessageSource messageSource;

    public final LoginService loginService;

    public final OpenService openService;
    @GetMapping(value = {"/", "/login"})
    public String loginForm(@ModelAttribute UserDTO userDTO) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute UserDTO userDTO,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        User loginMember = loginService.findUser(userDTO);
        if(loginMember == null) {
            bindingResult.reject("loginFail", messageSource.getMessage("message.id.password.wrong", null, Locale.KOREA));
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(Constants.LOGIN_MEMBER, loginMember);

        if("/".equals(redirectURL)) {
            return "redirect:/open";
        } else {
            return "redirect:" + redirectURL;
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}
