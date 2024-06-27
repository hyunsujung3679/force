package com.hsj.force.table.controller;

import com.hsj.force.domain.dto.TableDTO;
import com.hsj.force.domain.dto.TableListDTO;
import com.hsj.force.domain.entity.TUser;
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

        TUser loginMember = (TUser) session.getAttribute("loginMember");
        if(!openService.selectIsOpen()) {
            return "redirect:/table";
        }

        Map<String, Object> map = tableService.selectTableInfo(loginMember);
        model.addAttribute("header", map.get("commonLayoutForm"));
        model.addAttribute("tableList", map.get("tableList"));
        model.addAttribute("tableTotalPriceList", map.get("tableTotalPriceList"));
        model.addAttribute("tableOfOrderMap", map.get("tableOfOrderMap"));

        return "table/" + "S001" + "/tableForm";
    }

    @GetMapping("/exist-order/list")
    @ResponseBody
    public List<TableListDTO> selectTableExistOrderList() {
        return tableService.selectTableExistOrderList();
    }

    @GetMapping("/not-exist-order/list")
    @ResponseBody
    public List<TableListDTO> selectTableNotExistOrderList() {
        return tableService.selectTableNotExistOrderList();
    }

    @PostMapping("/move")
    @ResponseBody
    public int moveTable(@RequestBody TableDTO table) {
        return tableService.moveTable(table);
    }

    @PostMapping("/combine")
    @ResponseBody
    public int combineTable(@RequestBody TableDTO table) {
        if(table.getFirstTableNo().equals(table.getSecondTableNo())) {
            return 0;
        } else {
            return tableService.combineTable(table);
        }
    }

}
