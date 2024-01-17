package com.hsj.force.domain;

import com.hsj.force.domain.CommonData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpenClose extends CommonData {

    private String openCloseNo;
    private String openCloseSeq;
    private String openMoney;
    private Integer fiftyThous;
    private Integer tenThous;
    private Integer fiveThous;
    private Integer oneThous;
    private Integer fiveHun;
    private Integer oneHun;
    private Integer fifty;
    private Integer ten;
    private String closeMoney;
}
