package com.hsj.force.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OpenCloseUpdateDTO {

    private Integer sumSalePrice;
    private Long sumOrderCnt;
    private Integer sumCancelPrice;
    private Long sumCancelCnt;
    private Integer discountPrice;
    private Integer totalSalePrice;
    private Long realOrderCnt;
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

    private String modifyId;

    public OpenCloseUpdateDTO() {
    }

    @QueryProjection
    public OpenCloseUpdateDTO(Integer sumSalePrice, Long sumOrderCnt, Integer sumCancelPrice, Long sumCancelCnt, Integer discountPrice, Integer totalSalePrice, Long realOrderCnt) {
        this.sumSalePrice = sumSalePrice;
        this.sumOrderCnt = sumOrderCnt;
        this.sumCancelPrice = sumCancelPrice;
        this.sumCancelCnt = sumCancelCnt;
        this.discountPrice = discountPrice;
        this.totalSalePrice = totalSalePrice;
        this.realOrderCnt = realOrderCnt;
    }
}
