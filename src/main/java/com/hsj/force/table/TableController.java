package com.hsj.force.table;

import com.hsj.force.domain.TableForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/table")
@RequiredArgsConstructor
public class TableController {

    @GetMapping
    public String tableForm(@ModelAttribute TableForm tableForm) {
        return "table/tableForm";
    }

}
