package com.hsj.force.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ComUtils {

    /**
     * 날짜 변환
     * @param oldDateStr, beforeFormat, afterFormat
     * @return String
     */
    public static String simpleDataFormat(String oldDateStr, String beforeFormat, String afterFormat) throws ParseException {
        SimpleDateFormat beforeFormatter = new SimpleDateFormat(beforeFormat);
        SimpleDateFormat afterFormatter = new SimpleDateFormat(afterFormat);
        Date beforeDate = beforeFormatter.parse(oldDateStr);
        return afterFormatter.format(beforeDate);
    }
}
