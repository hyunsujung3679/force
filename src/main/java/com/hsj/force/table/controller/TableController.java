package com.hsj.force.table.controller;

import com.hsj.force.domain.Table;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.OrderDTO;
import com.hsj.force.domain.dto.TableDTO;
import com.hsj.force.open.service.OpenService;
import com.hsj.force.table.service.TableService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@Controller
@RequestMapping("/table")
@RequiredArgsConstructor
public class TableController {

    private final OpenService openService;

    private final TableService tableService;

    @GetMapping
    public String tableForm(HttpSession session, Model model) {

        User loginMember = (User) session.getAttribute("loginMember");
        if(openService.selectIsOpen(loginMember.getStoreNo()) == 0) {
            return "redirect:/open";
        }

        TableDTO tableForm = tableService.selectTableInfo(loginMember);
        model.addAttribute("header", tableForm.getCommonLayoutForm());
        model.addAttribute("tableList", tableForm.getTableList());
        model.addAttribute("tableTotalPriceList", tableForm.getTableTotalPriceList());
        model.addAttribute("tableOfOrderMap", tableForm.getTableOfOrderMap());

        return "table/" + loginMember.getStoreNo() + "/tableForm";
    }

    @GetMapping("/exist/order/list")
    @ResponseBody
    public List<Table> selectTableExistOrderList(HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        return tableService.selectTableExistOrderList(loginMember.getStoreNo());
    }

    @GetMapping("/not/exist/order/list")
    @ResponseBody
    public List<Table> selectTableNotExistOrderList(HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        return tableService.selectTableNotExistOrderList(loginMember.getStoreNo());
    }

    @PostMapping("/update/V1")
    @ResponseBody
    public int moveTable(HttpSession session, @RequestBody TableDTO table) {
        User loginMember = (User) session.getAttribute("loginMember");
        return tableService.moveTable(loginMember, table);
    }

    @PostMapping("/update/V2")
    @ResponseBody
    public int combineTable(HttpSession session, @RequestBody TableDTO table) {
        if(table.getFirstTableNo().equals(table.getSecondTableNo())) {
            return 0;
        } else {
            User loginMember = (User) session.getAttribute("loginMember");
            return tableService.combineTable(loginMember, table);
        }
    }

}
