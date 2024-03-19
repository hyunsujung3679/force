package com.hsj.force.domain.dto;

import com.hsj.force.domain.MenuIngredient;
import lombok.Data;
import java.util.*;

@Data
public class MenuUpdateDTO {

    private String menuNo;
    private String menuName;
    private String categoryNo;
    private String saleStatusNo;
    private String salePriceStr;
    private String ingredientNo1;
    private String ingredientNo2;
    private String ingredientNo3;
    private String ingredientNo4;
    private String quantityStr1;
    private String quantityStr2;
    private String quantityStr3;
    private String quantityStr4;
    private List<MenuIngredient> ingredientQuantityList;
    private String imageOriginName;
    private String imageSaveName;
    private String imageExt;
    private String imagePath;
    private String storeNo;

}
