package com.hsj.force.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.hsj.force.common.Constants.OPEN_CLOSE_NO_PRIFIX;

public class ComUtils {

    /**
     * 날짜 변환
     * @param dateStr
     * @return LocalDateTime
     */
    public static LocalDateTime stringTolocalDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateStr, formatter);
    }

    public static String getNextNo(String previousNo, String prefix) {
        String nextNo = "";

        if(previousNo == null) {
            nextNo = prefix + "001";
        } else {
            nextNo = prefix + (String.format("%03d", Integer.parseInt(previousNo.substring(2,5)) + 1));
        }
        return nextNo;
    }

    public static String getNextSeq(String previousSeq) {
        String nextSeq = "";

        if(previousSeq == null) {
            nextSeq = "001";
        } else {
            nextSeq = String.format("%03d", Integer.parseInt(previousSeq) + 1);
        }
        return nextSeq;
    }
}
