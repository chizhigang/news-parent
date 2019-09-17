package com.business.start.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class DateUtils {

    public static String getYearMonthDay() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH) + 1;
        int DATE = calendar.get(Calendar.DATE);
        return YEAR + "/" + MONTH + "/" + DATE + "/";
    }

    public static Long strToLong(String date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        try {
            now = dateFormat.parse(date);
        } catch (ParseException e) {

        }
        return now.getTime() ;
    }
}
