package com.hsj.force.table.controller;

import com.hsj.force.domain.Login;
import com.hsj.force.domain.form.TableForm;
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

        TableForm tableForm = tableService.selectTableInfo(loginMember);
        model.addAttribute("header", tableForm.getCommonLayoutForm());
        model.addAttribute("tableList", tableForm.getTableList());
        model.addAttribute("orderList", tableForm.getOrderList());
        model.addAttribute("tableTotalPriceList", tableForm.getTableTotalPriceList());

        return "table/tableForm";
    }

}
