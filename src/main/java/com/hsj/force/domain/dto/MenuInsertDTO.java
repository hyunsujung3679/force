package com.hsj.force.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MenuInsertDTO {

    private String menuName;
    private String categoryNo;
    private String saleStatusNo;
    private String salePriceStr;
    private String[] ingredientNo;
    private String[] quantityStr;
    private String imageSaveName;
    private String imageOriginName;
    private String imagePath;
    private String imageExt;

}
