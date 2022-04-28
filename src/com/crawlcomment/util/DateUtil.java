package com.crawlcomment.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    private static SimpleDateFormat sdf;
    public final String timeformat = "HH:mm:ss";
    private Logger logger = LogManager.getLogger();

    public boolean isBetween(String from, String to, String parttern) {
        boolean result = false;

        Date now = parseDate(parseString(new Date(), parttern), timeformat);

        Date fromDate = parseDate(from, parttern);
        Date toDate = parseDate(to, parttern);

        if (fromDate.compareTo(now) <= 0 && now.compareTo(toDate) <= 0) {
            result = true;
        }

        logger.debug("isBetween: {} and {} ==> {}", from, to, result);

        return result;
    }

    public static Date startOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date endOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date parseDate(String dateStr, String parttern) {
        Date result = null;

        sdf = new SimpleDateFormat(parttern);
        try {
            result = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String parseString(Date date, String parttern) {
        String result = "";

        sdf = new SimpleDateFormat(parttern);
        result = sdf.format(date);

        return result;
    }

}
