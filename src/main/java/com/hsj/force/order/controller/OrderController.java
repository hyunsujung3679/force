package com.hsj.force.order.controller;

import com.hsj.force.domain.Order;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.OrderDTO;
import com.hsj.force.open.service.OpenService;
import com.hsj.force.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        OrderDTO orderForm = orderService.selectOrderInfo(loginMember, tableNo);
        model.addAttribute("header", orderForm.getCommonLayoutForm());
        model.addAttribute("categoryList", orderForm.getCategoryList());
        model.addAttribute("menuList", orderForm.getMenuList());
        model.addAttribute("orderList", orderForm.getOrderList());
        model.addAttribute("tableNo", tableNo);

        return "order/orderForm";
    }

    @PostMapping
    public OrderDTO insertOrder(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        order.setStoreNo(loginMember.getStoreNo());
        order.setInsertId(loginMember.getUserId());
        order.setModifyId(loginMember.getUserId());
        return orderService.insertOrder(order);
    }

}
