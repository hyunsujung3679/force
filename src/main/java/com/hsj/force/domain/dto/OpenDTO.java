package com.hsj.force.domain.dto;

import com.hsj.force.domain.OpenClose;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpenDTO extends OpenClose {

    private LocalDateTime closeDate;    //마감 처리 일자
    private LocalDateTime closeTime;    //마감 처리 시간
    private String closer;              //마감 판매원(closerId - closerName) EX) test - 정현수
    private String closerId;            //마감 판매원 아이디
    private String closerName;          //마감 판매원 이름
    private LocalDateTime currentDate;  //시스템 일자
    private LocalDateTime currentTime;  //개점 영업 일자
    private Integer procedure;          //차수
    private String openMoneyStr;        //개점 준비금 String
    private String opener;              //개점 판매원

}
