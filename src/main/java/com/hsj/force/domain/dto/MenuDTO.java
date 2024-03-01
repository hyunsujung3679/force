package com.hsj.force.domain.dto;

import com.hsj.force.domain.Menu;
import lombok.Data;
import java.util.List;

@Data
public class MenuDTO extends Menu {

    private int salePrice;
    private CommonLayoutDTO commonLayoutForm;
    private List<MenuDTO> menuList;
    private boolean isEnoughStock;

}
