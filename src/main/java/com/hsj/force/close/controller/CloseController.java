package com.hsj.force.close.controller;

import com.hsj.force.close.service.CloseService;
import com.hsj.force.common.Constants;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.OpenCloseUpdateDTO;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.open.service.OpenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/close")
@RequiredArgsConstructor
public class CloseController {

    private final OpenService openService;
    private final CloseService closeService;

    @GetMapping
    public String closeForm(HttpSession session, Model model) {

        TUser loginMember = (TUser) session.getAttribute("loginMember");
        String storeNo = loginMember.getStore().getStoreNo();

        if(!openService.findIsOpen(storeNo)) {
            return "redirect:/open";
        }

        OpenCloseUpdateDTO close = closeService.selectCloseInfo(loginMember);

        model.addAttribute("close", close);

        return "close/closeForm";
    }

    @PostMapping
    public String updateOpenClose(@ModelAttribute OpenCloseUpdateDTO close, HttpSession session) {

        if(close.getOneHunThous() == null) {
            close.setOneHunThous(0);
        }
        if(close.getFiftyThous() == null) {
            close.setFiftyThous(0);
        }
        if(close.getTenThous() == null) {
            close.setTenThous(0);
        }
        if(close.getFiveThous() == null) {
            close.setFiveThous(0);
        }
        if(close.getOneThous() == null) {
            close.setOneThous(0);
        }
        if(close.getFiveHun() == null) {
            close.setFiveHun(0);
        }
        if(close.getOneHun() == null) {
            close.setOneHun(0);
        }
        if(close.getFifty() == null) {
            close.setFifty(0);
        }
        if(close.getTen() == null) {
            close.setTen(0);
        }

        TUser loginMember = (TUser) session.getAttribute(Constants.LOGIN_MEMBER);
        closeService.updateOpenClose(loginMember, close);

        return "redirect:/login";
    }
}
