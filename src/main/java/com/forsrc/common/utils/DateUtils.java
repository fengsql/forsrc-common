package com.forsrc.common.utils;

import com.alibaba.excel.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DateUtils {

  /**
   * Date转换为LocalDateTime
   * @param date
   */
  public static LocalDateTime date2LocalDateTime(Date date) {
    Instant instant = date.toInstant();
    ZoneId zoneId = ZoneId.systemDefault();
    LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

    return localDateTime;

  }

  public static int betweenYears(Date startDateTime, Date endDateTime) {
    return date2LocalDateTime(startDateTime).getYear() - date2LocalDateTime(endDateTime).getYear();
  }

  /**
   * LocalDateTime转换为Date
   * @param localDateTime
   */
  public static Date localDateTime2Date(LocalDateTime localDateTime) {
    ZoneId zoneId = ZoneId.systemDefault();
    ZonedDateTime zdt = localDateTime.atZone(zoneId);
    Date date = Date.from(zdt.toInstant());

    return date;
  }

  /**
   * Date转换为LocalDateTime
   * @param dateStr 201911220001
   */
  public static LocalDateTime dateStr2LocalDateTime(String dateStr) {
    return LocalDateTime.of(Integer.parseInt(dateStr.substring(0, 4)), Integer.parseInt(dateStr.substring(4, 6)), Integer.parseInt(dateStr.substring(6, 8)), Integer.parseInt(dateStr.substring(8, 10)), Integer.parseInt(dateStr.substring(10)));
  }

  // 获得某天最大时间日期格式 2017-10-15 23:59:59
  public static Date getEndOfDay(Date date) {
    LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    ;
    LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
    return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
  }

  // 获得某天最小时间日期格式 2017-10-15 00:00:00
  public static Date getStartOfDay(Date date) {
    LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
    return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static String ofDay(String startTime, String stopTime) {
    String format = null;
    Date date;
    Date ofDay;
    try {
      if (!StringUtils.isEmpty(startTime)) {
        date = com.alibaba.excel.util.DateUtils.parseDate(startTime);
        ofDay = getStartOfDay(date);
      } else {
        date = com.alibaba.excel.util.DateUtils.parseDate(stopTime);
        ofDay = getEndOfDay(date);
      }
      format = com.alibaba.excel.util.DateUtils.format(ofDay);
    } catch (ParseException e) {
      log.error(" DateUtils.format is error: " + e);
    }
    return format;
  }

  // 获得某天最小时间数字格式 201710100000
  public static String getStartOfDay(String date) {
    String format = getFormat(date, "yyyy-MM-dd");
    return format + "0000";
  }

  // 获得某天最大时间数字格式 201710102359
  public static String getEndOfDay(String date) {
    String format = getFormat(date, "yyyy-MM-dd");
    return format + "2359";
  }

  // 数字日期转日期格式 2019-10-10 10:10:00
  public static String getByStringSecond(String time) {
    if (StringUtils.isEmpty(time)) {
      return "";
    }
    String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
    return time.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
  }

  // 数字日期转日期格式 2019-10-10 10:10
  public static String getByStringMinute(String time) {
    if (StringUtils.isEmpty(time)) {
      return "";
    }
    String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
    return time.replaceAll(reg, "$1-$2-$3 $4:$5");
  }

  // 数字日期转日期格式 2019-10-10
  public static String getByStringDay(String time) {
    String reg = "(\\d{4})(\\d{2})(\\d{2})";
    return time.replaceAll(reg, "$1-$2-$3");
  }

  /**
   * 获取指定格式时间
   */
  public static String getFormat(String dateTime, String format) {
    String formatNew = null;
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
      Date parse = simpleDateFormat.parse(dateTime);
      formatNew = simpleDateFormat.format(parse).replaceAll("-", "");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return formatNew;
  }

  /**
   * 当天0点和明天0点
   * @return
   */
  public static Map<String, String> LocalDateTime() {
    String beginDate = LocalDate.now().toString().replace("T", "").replaceAll("-", "");
    Map<String, String> param = new HashMap<>();
    param.put("startTime", beginDate + "0000");
    String dayAfterTimeMinute = LocalDate.of(Integer.parseInt(beginDate.substring(0, 4)), Integer.parseInt(beginDate.substring(4, 6)), Integer.parseInt(beginDate.substring(6))).plusDays(1).toString().replaceAll("T", "").replaceAll("-", "");
    param.put("stopTime", dayAfterTimeMinute + "0000");
    return param;
  }
}
