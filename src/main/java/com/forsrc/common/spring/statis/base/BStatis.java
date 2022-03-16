package com.forsrc.common.spring.statis.base;

import com.forsrc.common.spring.bean.param.ParamStatis;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolDateTime;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public class BStatis {

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  protected ParamStatis getParamStatisSecond(LocalDateTime dateTime, int seconds) {
    LocalDateTime startDate = dateTime.minusSeconds(seconds);
    ParamStatis paramStatis = new ParamStatis();
    paramStatis.setStart(ToolDateTime.toDateTime(startDate));
    paramStatis.setEnd(ToolDateTime.toDateTime(dateTime));
    return paramStatis;
  }

  protected ParamStatis getParamStatisMinute(LocalDateTime dateTime, int minutes) {
    LocalDateTime startDate = dateTime.minusMinutes(minutes);
    ParamStatis paramStatis = new ParamStatis();
    paramStatis.setStart(ToolDateTime.toDateTime(startDate));
    paramStatis.setEnd(ToolDateTime.toDateTime(dateTime));
    return paramStatis;
  }

  protected ParamStatis getParamStatisHour(LocalDateTime dateTime, int hours) {
    LocalDateTime startDate = dateTime.minusHours(hours);
    ParamStatis paramStatis = new ParamStatis();
    paramStatis.setStart(ToolDateTime.toDateTime(startDate));
    paramStatis.setEnd(ToolDateTime.toDateTime(dateTime));
    return paramStatis;
  }

  protected ParamStatis getParamStatisDay(LocalDateTime dateTime, int days) {
    LocalDate localDate = dateTime.toLocalDate();
    LocalDate startDate = localDate.minusDays(days);
    ParamStatis paramStatis = new ParamStatis();
    paramStatis.setStart(ToolDateTime.toDateTime(startDate));
    paramStatis.setEnd(ToolDateTime.toDateTime(localDate));
    return paramStatis;
  }

  protected ParamStatis getParamStatisWeek(LocalDateTime dateTime, int weeks) {
    LocalDate localDate = dateTime.toLocalDate();
    LocalDate startDate = localDate.minusWeeks(weeks);
    ParamStatis paramStatis = new ParamStatis();
    paramStatis.setStart(ToolDateTime.toDateTime(startDate));
    paramStatis.setEnd(ToolDateTime.toDateTime(localDate));
    return paramStatis;
  }

  protected ParamStatis getParamStatisMonth(LocalDateTime dateTime, int months) {
    LocalDate localDate = dateTime.toLocalDate();
    LocalDate startDate = localDate.minusMonths(months);
    ParamStatis paramStatis = new ParamStatis();
    paramStatis.setStart(ToolDateTime.toDateTime(startDate));
    paramStatis.setEnd(ToolDateTime.toDateTime(localDate));
    return paramStatis;
  }

  protected ParamStatis getParamStatisYear(LocalDateTime dateTime, int years) {
    LocalDate localDate = dateTime.toLocalDate();
    LocalDate startDate = localDate.minusYears(years);
    ParamStatis paramStatis = new ParamStatis();
    paramStatis.setStart(ToolDateTime.toDateTime(startDate));
    paramStatis.setEnd(ToolDateTime.toDateTime(localDate));
    return paramStatis;
  }

  // >>>----------------------- normal -----------------------

  // <<<----------------------- number -----------------------

  protected Integer toNumberSecond(Date date) {
    return Tool.toInt(Tool.toString(date, "yyyyMMddHHmmss"));
  }

  protected Integer toNumberMinute(Date date) {
    return Tool.toInt(Tool.toString(date, "yyyyMMddHHmm"));
  }

  protected Integer toNumberHour(Date date) {
    return Tool.toInt(Tool.toString(date, "yyyyMMddHH"));
  }

  protected Integer toNumberDay(Date date) {
    return Tool.toInt(Tool.toString(date, "yyyyMMdd"));
  }

  protected Integer toNumberWeek(Date date) {
    return Tool.toInt(Tool.toString(date, "yyyyMMdd"));
  }

  protected Integer toNumberMonth(Date date) {
    return Tool.toInt(Tool.toString(date, "yyyyMM"));
  }

  protected Integer toNumberYear(Date date) {
    return Tool.toInt(Tool.toString(date, "yyyy"));
  }

  protected Integer toNumberManual(Date date) {
    return Tool.toInt(Tool.toString(date, "yyyyMMdd"));
  }

  // >>>----------------------- number -----------------------

  // <<<----------------------- datetime -----------------------

  protected Date toDateTimeSecond(Date date) {
    return Tool.toDatetime(Tool.toString(date, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
  }

  protected Date toDateTimeMinute(Date date) {
    return Tool.toDatetime(Tool.toString(date, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
  }

  protected Date toDateTimeHour(Date date) {
    return Tool.toDatetime(Tool.toString(date, "yyyy-MM-dd HH"), "yyyy-MM-dd HH");
  }

  protected Date toDateTimeDay(Date date) {
    return Tool.toDatetime(Tool.toString(date, "yyyy-MM-dd"), "yyyy-MM-dd");
  }

  protected Date toDateTimeWeek(Date date) {
    return Tool.toDatetime(Tool.toString(date, "yyyy-MM-dd"), "yyyy-MM-dd");
  }

  protected Date toDateTimeMonth(Date date) {
    return Tool.toDatetime(Tool.toString(date, "yyyy-MM"), "yyyy-MM");
  }

  protected Date toDateTimeYear(Date date) {
    return Tool.toDatetime(Tool.toString(date, "yyyy"), "yyyy");
  }

  protected Date toDateTimeManual(Date date) {
    return Tool.toDatetime(Tool.toString(date, "yyyy-MM-dd"), "yyyy-MM-dd");
  }

  // >>>----------------------- datetime -----------------------

  // <<<----------------------- string -----------------------

  protected String toStringSecond(Date date) {
    return Tool.toString(date, "yyyy-MM-dd HH:mm:ss");
  }

  protected String toStringMinute(Date date) {
    return Tool.toString(date, "yyyy-MM-dd HH:mm");
  }

  protected String toStringHour(Date date) {
    return Tool.toString(date, "yyyy-MM-dd HH");
  }

  protected String toStringDay(Date date) {
    return Tool.toString(date, "yyyy-MM-dd");
  }

  protected String toStringWeek(Date date) {
    return Tool.toString(date, "yyyy-MM-dd");
  }

  protected String toStringMonth(Date date) {
    return Tool.toString(date, "yyyy-MM");
  }

  protected String toStringYear(Date date) {
    return Tool.toString(date, "yyyy");
  }

  protected String toStringManual(Date date) {
    return Tool.toString(date, "yyyy-MM-dd");
  }

  // >>>----------------------- string -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  // >>>----------------------- normal -----------------------

  // <<<----------------------- tool -----------------------

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}