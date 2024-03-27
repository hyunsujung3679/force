package com.hsj.force.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Data
public class OpenCloseInsertDTO {

    private String openCloseNo;
    private String openCloseSeq;
    private String storeNo;
    private String insertId;
    private String modifyId;
    private String closer;
    private String closerId;
    private String closerName;
    private String modifyDate;
    private Integer procedure;
    private String opener;

    @NumberFormat(pattern = "###,###")
    private int openMoney;
    @DateTimeFormat(pattern = "yyyy-MM-dd (E)")
    private LocalDateTime closeDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime closeTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd (E)")
    private LocalDateTime currentDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime currentTime;

}
