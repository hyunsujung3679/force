package com.hsj.force.table.controller;

import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.TableDTO;
import com.hsj.force.domain.dto.TableListDTO;
import com.hsj.force.open.service.OpenService;
import com.hsj.force.table.service.TableService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

        Map<String, Object> map = tableService.selectTableInfo(loginMember);
        model.addAttribute("header", map.get("commonLayoutForm"));
        model.addAttribute("tableList", map.get("tableList"));
        model.addAttribute("tableTotalPriceList", map.get("tableTotalPriceList"));
        model.addAttribute("tableOfOrderMap", map.get("tableOfOrderMap"));

        return "table/" + loginMember.getStoreNo() + "/tableForm";
    }

    @GetMapping("/exist-order/list")
    @ResponseBody
    public List<TableListDTO> selectTableExistOrderList(HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        return tableService.selectTableExistOrderList(loginMember.getStoreNo());
    }

    @GetMapping("/not-exist-order/list")
    @ResponseBody
    public List<TableListDTO> selectTableNotExistOrderList(HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        return tableService.selectTableNotExistOrderList(loginMember.getStoreNo());
    }

    @PostMapping("/move")
    @ResponseBody
    public int moveTable(HttpSession session, @RequestBody TableDTO table) {
        User loginMember = (User) session.getAttribute("loginMember");
        return tableService.moveTable(loginMember, table);
    }

    @PostMapping("/combine")
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
