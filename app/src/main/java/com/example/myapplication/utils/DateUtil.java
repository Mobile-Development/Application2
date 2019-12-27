package com.example.myapplication.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {
    public static final String DATE_JFP_STR = "yyyyMM";
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SMALL_STR = "yyyy-MM-dd";
    public static final String DATE_TIME_STR = "HH:mm:ss";
    public static final String DATE_KEY_STR = "yyMMddHHmmss";
    private static final long MILLIS_PER_DAY = 86400000L;
    private static final long INTERVAL_IN_MILLISECONDS = 30000L;

    /**
     * 获取当前时间的string类型值"yyyy-MM-dd"
     */
    public static String getTodayDate() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_SMALL_STR);
        return df.format(new Date(System.currentTimeMillis()));
    }

    public static String getTodayTime(String dateformat) {
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        return df.format(new Date(System.currentTimeMillis()));
    }


    /**
     * @param strTime    要转换的String类型的时间
     * @param formatType 时间格式
     * @return
     * @throws ParseException
     */
    /**
     * @param date
     * @return
     */
    /**
     * @param formatType
     * @return
     * @throws ParseException
     */

    public static String DateToString(Date date, String formatType)
            {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        String dateString = formatter.format(date);
        return dateString;
    }

    public static Date StringToDate(String paramString1, String paramString2) {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(paramString2);
        Date localDate = null;
        try {
            localDate = localSimpleDateFormat.parse(paramString1);
        } catch (ParseException localParseException) {
            localParseException.printStackTrace();
        }
        return localDate;
    }
}
