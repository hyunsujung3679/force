package com.hsj.force.close.controller;

import com.hsj.force.close.service.CloseService;
import com.hsj.force.common.Constants;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CloseDTO;
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

        User loginMember = (User) session.getAttribute(Constants.LOGIN_MEMBER);
        if(openService.selectIsOpen(loginMember.getStoreNo()) == 0) {
            return "redirect:/open";
        }

        CloseDTO close = closeService.selectCloseInfo(loginMember);

        model.addAttribute("close", close);

        return "close/closeForm";
    }

    @PostMapping
    public String updateOpenClose(@ModelAttribute CloseDTO close, HttpSession session) {

        if("".equals(close.getOneHunThousStr())) {
            close.setOneHunThousStr("0");
        }
        if("".equals(close.getFiftyThousStr())) {
            close.setFiftyThousStr("0");
        }
        if("".equals(close.getTenThousStr())) {
            close.setTenThousStr("0");
        }
        if("".equals(close.getFiveThousStr())) {
            close.setFiveThousStr("0");
        }
        if("".equals(close.getOneThousStr())) {
            close.setOneThousStr("0");
        }
        if("".equals(close.getFiveHunStr())) {
            close.setFiveHunStr("0");
        }
        if("".equals(close.getOneHunStr())) {
            close.setOneHunStr("0");
        }
        if("".equals(close.getFiftyStr())) {
            close.setFiftyStr("0");
        }
        if("".equals(close.getTenStr())) {
            close.setTenStr("0");
        }

        User loginMember = (User) session.getAttribute(Constants.LOGIN_MEMBER);
        close.setStoreNo(loginMember.getStoreNo());
        close.setModifyId(loginMember.getUserId());

        closeService.updateOpenClose(close);

        return "redirect:/login";
    }
}
