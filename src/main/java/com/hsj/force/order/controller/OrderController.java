package com.hsj.force.order.controller;

import com.hsj.force.domain.dto.OrderListDTO;
import com.hsj.force.domain.dto.OrderSaveDTO;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.open.service.OpenService;
import com.hsj.force.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

        TUser loginMember = (TUser) session.getAttribute("loginMember");
        if(!openService.selectIsOpen()) {
            return "redirect:/open";
        }

        Map<String, Object> map = orderService.selectOrderInfo(loginMember, tableNo);
        model.addAttribute("header", map.get("commonLayoutForm"));
        model.addAttribute("categoryList", map.get("categoryList"));
        model.addAttribute("menuList", map.get("menuList"));
        model.addAttribute("orderList", map.get("orderList"));
        model.addAttribute("orderTotal", map.get("orderTotal"));
        model.addAttribute("tableNo", tableNo);

        return "order/orderForm";
    }

    @PostMapping("/complete")
    public String completeOrder(String tableNo) {
        orderService.completeOrder(tableNo);
        return "redirect:/table";
    }

    @PostMapping("/save")
    @ResponseBody
    public int insertOrder(@RequestBody OrderSaveDTO order) {
        boolean isEnoughStock = orderService.checkStock(order.getMenuNo());
        if(!isEnoughStock) return 0;
        return orderService.insertOrder(order);
    }

    @GetMapping
    @ResponseBody
    public List<OrderListDTO> selectOrderList(String tableNo) {
        return orderService.selectOrderList(tableNo);
    }

    @PostMapping("/cancel-selection")
    @ResponseBody
    public int cancelSelection(@RequestBody OrderSaveDTO order) {
        return orderService.cancelSelection(order);
    }

    @PostMapping("/cancel-whole")
    @ResponseBody
    public int cancelWhole(@RequestBody OrderSaveDTO order) {
        return orderService.cancelWhole(order);
    }

    @PostMapping("/change-quantity")
    @ResponseBody
    public int changeQuantity(@RequestBody OrderSaveDTO order) {
        return orderService.changeQuantity(order);
    }

    @PostMapping("/change-salePrice")
    @ResponseBody
    public int changeSalePrice(@RequestBody OrderSaveDTO order) {
        return orderService.changeSalePrice(order);
    }

    @PostMapping("/service")
    @ResponseBody
    public int service(@RequestBody OrderSaveDTO order) {
        return orderService.service(order);
    }

    @PostMapping("/discount-full-per")
    @ResponseBody
    public int discountFullPer(@RequestBody OrderSaveDTO order) {
        return orderService.discountFullPer(order);
    }

    @PostMapping("/discount-full-price")
    @ResponseBody
    public int discountFullPrice(@RequestBody OrderSaveDTO order) {
        return orderService.discountFullPrice(order);
    }

    @PostMapping("/discount-full-cancel")
    @ResponseBody
    public int discountFullCancel(@RequestBody OrderSaveDTO order) {
        return orderService.discountFullCancel(order);
    }

    @PostMapping("/discount-sel-per")
    @ResponseBody
    public int discountSelPer(@RequestBody OrderSaveDTO order) {
        return orderService.discountSelPer(order);
    }

    @PostMapping("/discount-sel-price")
    @ResponseBody
    public int discountSelPrice(@RequestBody OrderSaveDTO order) {
        return orderService.discountSelPrice(order);
    }

    @PostMapping("/discount-sel-cancel")
    @ResponseBody
    public int discountSelCancel(@RequestBody OrderSaveDTO order) {
        return orderService.discountSelCancel(order);
    }
}
