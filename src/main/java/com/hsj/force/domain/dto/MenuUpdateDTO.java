package com.hsj.force.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

@Data
public class MenuUpdateDTO {

    private String menuNo;
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
    private List<MenuIngredientListDTO> ingredientQuantityList;
    private String imageOriginName;
    private String imageSaveName;
    private String imageExt;
    private String imagePath;
    private String storeNo;

}
