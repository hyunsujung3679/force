package com.hsj.force.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CloseDTO {

    private Integer sumSalePrice;
    private int sumOrderCnt;
    private Integer sumCancelPrice;
    private int sumCancelCnt;
    private Integer discountPrice;
    private int realOrderCnt;
    private Integer totalSalePrice;
    private LocalDateTime currentDate;

    private String oneHunThousStr;
    private String fiftyThousStr;
    private String tenThousStr;
    private String fiveThousStr;
    private String oneThousStr;
    private String fiveHunStr;
    private String oneHunStr;
    private String fiftyStr;
    private String tenStr;
    private int closeMoney;

    private String storeNo;
    private String modifyId;

}
