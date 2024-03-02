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

        User loginMember = (User) session.getAttribute("loginMember");
        if(openService.selectIsOpen(loginMember.getStoreNo()) == 0) {
            return "redirect:/open";
        }

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

         boolean isEnoughStock = orderService.checkStock(loginMember.getStoreNo(), order.getMenuNo());
         if(!isEnoughStock) {
            return 0;
        }

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

    @PostMapping("/cancel/whole")
    @ResponseBody
    public int cancelWhole(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.cancelWhole(loginMember, order);
    }

    @PostMapping("/change/quantity")
    @ResponseBody
    public int changeQuantity(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.changeQuantity(loginMember, order);
    }

    @PostMapping("/change/salePrice")
    @ResponseBody
    public int changeSalePrice(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.changeSalePrice(loginMember, order);
    }

    @PostMapping("/service")
    @ResponseBody
    public int service(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.service(loginMember, order);
    }

    @PostMapping("/discount/full/per")
    @ResponseBody
    public int discountFullPer(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.discountFullPer(loginMember, order);
    }

    @PostMapping("/discount/full/price")
    @ResponseBody
    public int discountFullPrice(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.discountFullPrice(loginMember, order);
    }

    @PostMapping("/discount/full/cancel")
    @ResponseBody
    public int discountFullCancel(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.discountFullCancel(loginMember, order);
    }

    @PostMapping("/discount/sel/per")
    @ResponseBody
    public int discountSelPer(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.discountSelPer(loginMember, order);
    }

    @PostMapping("/discount/sel/price")
    @ResponseBody
    public int discountSelPrice(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.discountSelPrice(loginMember, order);
    }

    @PostMapping("/discount/sel/cancel")
    @ResponseBody
    public int discountSelCancel(HttpSession session, @RequestBody OrderDTO order) {
        User loginMember = (User) session.getAttribute("loginMember");
        return orderService.discountSelCancel(loginMember, order);
    }
}
