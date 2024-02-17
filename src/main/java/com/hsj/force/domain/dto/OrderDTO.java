package com.hsj.force.domain.dto;

import com.hsj.force.domain.Category;
import com.hsj.force.domain.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderDTO extends Order {

    private String menuNo;
    private String menuName;
    private CommonLayoutDTO commonLayoutForm;
    private List<Category> categoryList;
    private List<MenuDTO> menuList;
    private List<OrderDTO> orderList;
    private String no;
    private String etc;
    private OrderTotalDTO orderTotal;
    private String salePriceStr;
    private String quantityStr;
    private String percentStr;
    private int percent;
    private String discountPriceStr;

}
