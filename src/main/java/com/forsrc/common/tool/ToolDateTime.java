package com.forsrc.common.tool;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@Slf4j
public class ToolDateTime {

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  public static Date toDateTime(LocalDateTime dateTime) {
    return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date toDateTime(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }

  public static LocalDate toLocalDate(Date date) {
    return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static LocalDateTime toLocalDateTime(Date date) {
    return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static Date now() {
    return new Date();
    //    LocalDateTime dateTime = LocalDateTime.now();
    //    return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  // >>>----------------------- normal -----------------------

  // <<<----------------------- plus LocalDateTime -----------------------

  public static Date plusSeconds(LocalDateTime localDateTime, int seconds) {
    LocalDateTime newDatetime = localDateTime.plusSeconds(seconds);
    return toDateTime(newDatetime);
  }

  public static Date minusSeconds(LocalDateTime localDateTime, int seconds) {
    LocalDateTime newDatetime = localDateTime.minusSeconds(seconds);
    return toDateTime(newDatetime);
  }

  public static Date plusMinutes(LocalDateTime localDateTime, int minutes) {
    LocalDateTime newDatetime = localDateTime.plusMinutes(minutes);
    return toDateTime(newDatetime);
  }

  public static Date minusMinutes(LocalDateTime localDateTime, int minutes) {
    LocalDateTime newDatetime = localDateTime.minusMinutes(minutes);
    return toDateTime(newDatetime);
  }

  public static Date plusHours(LocalDateTime localDateTime, int hours) {
    LocalDateTime newDatetime = localDateTime.plusHours(hours);
    return toDateTime(newDatetime);
  }

  public static Date minusHours(LocalDateTime localDateTime, int hours) {
    LocalDateTime newDatetime = localDateTime.minusHours(hours);
    return toDateTime(newDatetime);
  }

  public static Date plusDays(LocalDateTime localDateTime, int days) {
    LocalDateTime newDatetime = localDateTime.plusDays(days);
    return toDateTime(newDatetime);
  }

  public static Date minusDays(LocalDateTime localDateTime, int days) {
    LocalDateTime newDatetime = localDateTime.minusDays(days);
    return toDateTime(newDatetime);
  }

  public static Date plusWeeks(LocalDateTime localDateTime, int weeks) {
    LocalDateTime newDatetime = localDateTime.plusWeeks(weeks);
    return toDateTime(newDatetime);
  }

  public static Date minusWeeks(LocalDateTime localDateTime, int weeks) {
    LocalDateTime newDatetime = localDateTime.minusWeeks(weeks);
    return toDateTime(newDatetime);
  }

  public static Date plusMonths(LocalDateTime localDateTime, int months) {
    LocalDateTime newDatetime = localDateTime.plusMonths(months);
    return toDateTime(newDatetime);
  }

  public static Date minusMonths(LocalDateTime localDateTime, int months) {
    LocalDateTime newDatetime = localDateTime.minusMonths(months);
    return toDateTime(newDatetime);
  }

  public static Date plusYears(LocalDateTime localDateTime, int years) {
    LocalDateTime newDatetime = localDateTime.plusYears(years);
    return toDateTime(newDatetime);
  }

  public static Date minusYears(LocalDateTime localDateTime, int years) {
    LocalDateTime newDatetime = localDateTime.minusYears(years);
    return toDateTime(newDatetime);
  }

  // >>>----------------------- plus LocalDateTime -----------------------

  // <<<----------------------- plus LocalDate -----------------------

  public static Date plusDays(LocalDate localDate, int days) {
    LocalDate newDate = localDate.plusDays(days);
    return toDateTime(newDate);
  }

  public static Date minusDays(LocalDate localDate, int days) {
    LocalDate newDate = localDate.minusDays(days);
    return toDateTime(newDate);
  }

  public static Date plusWeeks(LocalDate localDate, int weeks) {
    LocalDate newDate = localDate.plusWeeks(weeks);
    return toDateTime(newDate);
  }

  public static Date minusWeeks(LocalDate localDate, int weeks) {
    LocalDate newDate = localDate.minusWeeks(weeks);
    return toDateTime(newDate);
  }

  public static Date plusMonths(LocalDate localDate, int months) {
    LocalDate newDate = localDate.plusMonths(months);
    return toDateTime(newDate);
  }

  public static Date minusMonths(LocalDate localDate, int months) {
    LocalDate newDate = localDate.minusMonths(months);
    return toDateTime(newDate);
  }

  public static Date plusYears(LocalDate localDate, int years) {
    LocalDate newDate = localDate.plusYears(years);
    return toDateTime(newDate);
  }

  public static Date minusYears(LocalDate localDate, int years) {
    LocalDate newDate = localDate.minusYears(years);
    return toDateTime(newDate);
  }

  // >>>----------------------- plus LocalDate -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  // >>>----------------------- normal -----------------------

  // <<<----------------------- tool -----------------------

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}