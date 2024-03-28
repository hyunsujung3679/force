package com.hsj.force.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Data
public class MenuInsertDTO {

    private String menuName;
    private String categoryNo;
    private String saleStatusNo;
    @NumberFormat(pattern = "###,###")
    private Integer salePrice;
    private String ingredientNo1;
    private String ingredientNo2;
    private String ingredientNo3;
    private String ingredientNo4;
    private Double quantity1;
    private Double quantity2;
    private Double quantity3;
    private Double quantity4;
    private String imageSaveName;
    private String imageOriginName;
    private String imagePath;
    private String imageExt;

}
