package com.hsj.force.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OpenCloseUpdateDTO {

    private Integer sumSalePrice;
    private int sumOrderCnt;
    private Integer sumCancelPrice;
    private int sumCancelCnt;
    private Integer discountPrice;
    private int realOrderCnt;
    private Integer totalSalePrice;
    private LocalDateTime currentDate;

    private Integer oneHunThous;
    private Integer fiftyThous;
    private Integer tenThous;
    private Integer fiveThous;
    private Integer oneThous;
    private Integer fiveHun;
    private Integer oneHun;
    private Integer fifty;
    private Integer ten;
    private int closeMoney;

    private String storeNo;
    private String modifyId;

}
