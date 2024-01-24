package com.hsj.force.table.controller;

import com.hsj.force.domain.Login;
import com.hsj.force.domain.TableFirstForm;
import com.hsj.force.table.service.TableService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/table")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @GetMapping
    public String tableForm(HttpSession session, Model model) {

        Login loginMember = (Login) session.getAttribute("loginMember");

        TableFirstForm tableForm = tableService.selectTableInfo(loginMember);

        return "table/tableForm";
    }

}
