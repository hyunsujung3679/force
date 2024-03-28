package com.hsj.force.domain.dto;

import com.hsj.force.domain.MenuIngredient;
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
    private Integer quantity1;
    private Integer quantity2;
    private Integer quantity3;
    private Integer quantity4;
    private List<MenuIngredient> ingredientQuantityList;
    private String imageOriginName;
    private String imageSaveName;
    private String imageExt;
    private String imagePath;
    private String storeNo;

}
