package com.hsj.force.common;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ComUtils {

    /**
     * 다음 기본키(NO) 조회
     * @param previousNo
     * @param prefix
     * @return String
     */
    public static String getNextNo(String previousNo, String prefix) {
        String nextNo = "";
        if(!StringUtils.hasText(previousNo)) {
            nextNo = prefix + "001";
        } else {
            int previousNoInt = 0;
            if(previousNo.length() == 4) {
                previousNoInt = Integer.parseInt(previousNo.substring(1,4));
            } else {
                previousNoInt = Integer.parseInt(previousNo.substring(2,5));
            }
            nextNo = prefix + (String.format("%03d", previousNoInt + 1));
        }
        return nextNo;
    }

    /**
     * 다음 기본키(SEQ) 조회
     * @param previousSeq
     * @return String
     */
    public static String getNextSeq(String previousSeq) {
        String nextSeq = "";
        if(!StringUtils.hasText(previousSeq)) {
            nextSeq = "001";
        } else {
            nextSeq = String.format("%03d", Integer.parseInt(previousSeq) + 1);
        }
        return nextSeq;
    }
}
