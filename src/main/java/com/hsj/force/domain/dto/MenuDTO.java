package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class MenuDTO {

    private String menuNo;
    private String menuName;
    private String saleStatusNo;
    private String categoryNo;
    private int salePrice;
    private String imageSaveName;

    public MenuDTO() {
    }

    public MenuDTO(String menuNo, String menuName, String saleStatusNo, String categoryNo, int salePrice, String imageSaveName) {
        this.menuNo = menuNo;
        this.menuName = menuName;
        this.saleStatusNo = saleStatusNo;
        this.categoryNo = categoryNo;
        this.salePrice = salePrice;
        this.imageSaveName = imageSaveName;
    }
}
