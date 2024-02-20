package com.hsj.force.table.controller;

import com.hsj.force.domain.Table;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.TableDTO;
import com.hsj.force.open.service.OpenService;
import com.hsj.force.table.service.TableService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;
@Controller
@RequestMapping("/table")
@RequiredArgsConstructor
public class TableController {

    private final OpenService openService;

    private final TableService tableService;

    @GetMapping
    public String tableForm(HttpSession session, Model model) {

        if(openService.selectIsOpen() == 0) {
            return "redirect:/open";
        }

        User loginMember = (User) session.getAttribute("loginMember");

        TableDTO tableForm = tableService.selectTableInfo(loginMember);
        model.addAttribute("header", tableForm.getCommonLayoutForm());
        model.addAttribute("tableList", tableForm.getTableList());
        model.addAttribute("tableTotalPriceList", tableForm.getTableTotalPriceList());
        model.addAttribute("tableOfOrderMap", tableForm.getTableOfOrderMap());

        return "table/" + loginMember.getStoreNo() + "/tableForm";
    }

    @GetMapping("/move/before/list")
    public List<Table> selectTableMoveBeforeList(HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        return tableService.selectTableMoveBeforeList(loginMember.getStoreNo());
    }

}
