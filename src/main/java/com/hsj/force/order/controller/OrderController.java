package com.hsj.force.order.controller;

import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.OrderDTO;
import com.hsj.force.open.service.OpenService;
import com.hsj.force.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OpenService openService;

    private final OrderService orderService;

    @GetMapping("/{tableNo}")
    public String orderForm(@PathVariable String tableNo,
                            HttpSession session,
                            Model model) {

        if(openService.selectIsOpen() == 0) {
            return "redirect:/open";
        }

        User loginMember = (User) session.getAttribute("loginMember");
        OrderDTO order = orderService.selectOrderInfo(loginMember, tableNo);
        model.addAttribute("header", order.getCommonLayoutForm());
        model.addAttribute("categoryList", order.getCategoryList());
        model.addAttribute("menuList", order.getMenuList());
        model.addAttribute("orderList", order.getOrderList());
        model.addAttribute("orderTotal", order.getOrderTotal());
        model.addAttribute("tableNo", tableNo);

        return "order/orderForm";
    }

    @PostMapping("/complete")
    public String completeOrder(String tableNo,
                                HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        orderService.completeOrder(loginMember, tableNo);
        return "redirect:/table";
    }

    @PostMapping("/save")
    @ResponseBody
    public int saveOrder(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.saveOrder(loginMember, order);
    }

    @GetMapping
    @ResponseBody
    public List<OrderDTO> selectOrderList(String tableNo,
                                          HttpSession session) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.selectOrderList(loginMember.getStoreNo(), tableNo);
    }

    @PostMapping("/cancel/selection")
    @ResponseBody
    public int cancelSelection(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.cancelSelection(loginMember, order);
    }

}
