package com.hsj.force.common.controller;

import com.hsj.force.common.service.CommonService;
import com.hsj.force.domain.dto.SaleStatusListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @GetMapping("/sale-status")
    @ResponseBody
    public List<SaleStatusListDTO> selectSaleStatusList() {
        return commonService.selectSaleStatusList();
    }
}
