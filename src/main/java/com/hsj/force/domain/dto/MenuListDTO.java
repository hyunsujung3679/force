package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class MenuListDTO {

    private int no;
    private String menuNo;
    private String menuName;
    private String saleStatus;
    private String categoryName;
    private int salePrice;
    private int stock;
}
