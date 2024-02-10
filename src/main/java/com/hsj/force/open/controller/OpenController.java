package com.hsj.force.open.controller;

import com.hsj.force.common.Constants;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.OpenDTO;
import com.hsj.force.domain.dto.OpenSaveDTO;
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

          if(openService.selectIsOpen() > 0) {
            return "redirect:/table";
        }

        User user = (User) session.getAttribute(Constants.LOGIN_MEMBER);

        OpenDTO open = openService.selectOpenInfo();
        open.setOpener(user.getUserId() + " - " + user.getUserName());
        open.setCurrentDate(LocalDateTime.now());
        open.setCurrentTime(LocalDateTime.now());

        model.addAttribute("openSaveDTO", open);

        return "open/openForm";
    }

    @PostMapping
    public String open(@ModelAttribute OpenSaveDTO open,
                       BindingResult bindingResult,
                       HttpSession session) {

        if(bindingResult.hasErrors()) {
            return "open/openForm";
        }

        User user = (User) session.getAttribute(Constants.LOGIN_MEMBER);
        open.setOpenMoney(open.getOpenMoney().replaceAll(",", ""));
        open.setInsertId(user.getUserId());
        open.setModifyId(user.getUserId());

        openService.insertOpen(open);
        return "redirect:/table";
    }
}
