package com.janaushadhi.adminservice.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static String convertDateToStringDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDateTime = "null";
        if (date != null) {
            strDateTime = formatter.format(date);
        }
        return strDateTime;
    }


    public static Date convertStringDateToDate(String stringDate) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
    
    public static String convertUtcToIst(Date utcDate) {
        // Set UTC timezone
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");

        // Set IST timezone
        TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");

        // Create Calendar object for UTC time
        Calendar utcCalendar = Calendar.getInstance(utcTimeZone);
        utcCalendar.setTime(utcDate);

        // Convert UTC time to IST time
        Calendar istCalendar = Calendar.getInstance(istTimeZone);
        istCalendar.setTimeInMillis(utcCalendar.getTimeInMillis() + istTimeZone.getRawOffset());
        if (istTimeZone.inDaylightTime(new Date())) {
            istCalendar.add(Calendar.MILLISECOND, istTimeZone.getDSTSavings());
        }
   //     Date date=;
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	String	date =formatter.format(istCalendar.getTime());
        // Return IST date
        return date;
    }
}
