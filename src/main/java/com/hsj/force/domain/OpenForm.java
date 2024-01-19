package com.hsj.force.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OpenForm {

    private LocalDateTime closeDate;    //마감 처리 일자
    private LocalDateTime closeTime;    //마감 처리 시간
    private String closer;              //마감 판매원(closerId - closerName) EX) test - 정현수
    private String closerId;            //마감 판매원 아이디
    private String closerName;          //마감 판매원 이름
    private LocalDateTime currentDate;  //시스템 일자
    private LocalDateTime currentTime;  //개점 영업 일자
    private Integer procedure;          //차수
    private String openMoney;           //개점 준비금
    private String opener;              //개점 판매원
    private String modifyDate;          //수정 일자

}
