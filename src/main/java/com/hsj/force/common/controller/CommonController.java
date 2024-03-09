package com.hsj.force.common.controller;

import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.SaleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @ResponseBody
    @GetMapping("/sale-status")
    public List<SaleStatus> selectSaleStatusList() {
        return commonService.selectSaleStatusList();
    }
}
