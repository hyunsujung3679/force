package com.hsj.force.order.controller;

import com.hsj.force.open.service.OpenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OpenService openService;

    @GetMapping("/{tableNo}")
    public String orderForm(@PathVariable String tableNo) {

        if(openService.selectIsOpen() == 0) {
            return "redirect:/open";
        }

        return "order/orderForm";
    }

}
