package com.hsj.force.domain.dto;

import lombok.Data;

@Data
public class MenuPriceDTO {

    private String menuNo;
    private String menuSeq;
    private String menuName;
    private String saleStatusNo;
    private String categoryNo;
    private Integer salePrice;
    private String imageSaveName;

    public MenuPriceDTO() {
    }

    public MenuPriceDTO(String menuNo, String menuSeq, String menuName, String saleStatusNo, String categoryNo, Integer salePrice, String imageSaveName) {
        this.menuNo = menuNo;
        this.menuSeq = menuSeq;
        this.menuName = menuName;
        this.saleStatusNo = saleStatusNo;
        this.categoryNo = categoryNo;
        this.salePrice = salePrice;
        this.imageSaveName = imageSaveName;
    }
}
