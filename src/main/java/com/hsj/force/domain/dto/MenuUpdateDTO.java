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
    private String ingredientNo;
    private String quantityStr;
    private List<MenuIngredient> ingredientQuantityList;
    private String imageSaveName;

}
