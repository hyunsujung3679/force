package com.hsj.force.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MenuInsertDTO {

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
    private String imageSaveName;
    private String imageOriginName;
    private String imagePath;
    private String imageExt;

}
