package com.forsrc.common.tool;

import com.forsrc.common.constant.Code;
import com.forsrc.common.constant.Const;
import com.forsrc.common.exception.CommonException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class Tool {
  private static final String charset_default = "UTF-8";
  private static final String datetime_format = "yyyy-MM-dd HH:mm:ss";
  private static final String datetime_sss_format = "yyyy-MM-dd HH:mm:ss.SSS"; // 长日期格式
  private static final String date_format = "yyyy-MM-dd"; // 短日期格式
  private static final String time_format = "HH:mm:ss"; // 时间格式
  private static final String time_sss_format = "HH:mm:ss.SSS"; // 时间格式
  private static final String null_datetime = "1970-01-01 08:00:00"; //
  private static final String prefix_hex = "0X"; //
  //时间单位
  private static final String unit_second = "s";
  private static final String unit_minute = "m";
  private static final String unit_hour = "h";
  private static final String unit_day = "d";

  //  private static long randomSeedOffset = 0; // 随机数的种子，每次取随机数自动加1

  private static final char offsetCase = 1 << 5;
  //  private static final char offsetUpper = (char) (Character.MAX_VALUE - (1 << 5));
  private static final char minUpperChar = 'A';
  private static final char maxUpperChar = 'Z';
  private static final char minLowerChar = 'a';
  private static final char maxLowerChar = 'z';
  private static final char minDigit = '0';
  private static final char maxDigit = '9';

  private static final int length_short_text = 500; //
  private static final int protected_length = 5120000;// 输入流保护 5000KB

  private static final String upperStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String lowerStr = "abcdefghijklmnopqrstuvwxyz";
  private static final String numStr = "1234567890";

  // <<----------------------- public -----------------------

  // <<----------------------- String -----------------------

  public static boolean isNull(String value) {
    return value == null || value.trim().length() <= 0;
  }

  public static boolean equal(String value1, String value2, boolean ignoreCase) {
    if (value1 == null && value2 == null) {
      return true;
    }
    if (value1 == null || value2 == null) {
      return false;
    }
    if (ignoreCase) {
      return value1.equalsIgnoreCase(value2);
    } else {
      return value1.equals(value2);
    }
  }

  public static boolean equal(String value1, String value2) {
    return equal(value1, value2, false);
  }

  public static boolean equalIgnore(String value1, String value2) {
    return equal(value1, value2, true);
  }

  public static boolean isLetter(char ch) {
    return isLower(ch) || isUpper(ch);
  }

  public static boolean isDigit(char ch) {
    return ch >= minDigit && ch <= maxDigit;
  }

  public static boolean isLetterOrDigit(char ch) {
    return isLetter(ch) || isDigit(ch);
  }

  public static boolean isLower(char ch) {
    return ch >= minLowerChar && ch <= maxLowerChar;
  }

  public static boolean isUpper(char ch) {
    return ch >= minUpperChar && ch <= maxUpperChar;
  }

  public static char toLower(char ch) {
    return ch >= minUpperChar && ch <= maxUpperChar ? (char) (ch | offsetCase) : ch;
  }

  public static char toUpper(char ch) {
    return ch >= minLowerChar && ch <= maxLowerChar ? (char) (ch - offsetCase) : ch;
  }

  public static String toUpper(String str) {
    if (isNull(str)) {
      return str;
    }
    char[] chars = str.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char temp = chars[i];
      chars[i] = toUpper(temp);
    }
    return new String(chars);
  }

  public static String toLower(String str) {
    if (isNull(str)) {
      return str;
    }
    char[] chars = str.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char temp = chars[i];
      chars[i] = toLower(temp);
    }
    return new String(chars);
  }

  public static String toLowerFirst(String value) {
    if (isNull(value)) {
      return value;
    }
    if (value.length() == 1) {
      return String.valueOf(toLower(value.charAt(0)));
    }
    char[] chars = value.toCharArray();
    chars[0] = toLower(chars[0]);
    return new String(chars);
  }

  public static String toUpperFirst(String value) {
    if (isNull(value)) {
      return value;
    }
    if (value.length() == 1) {
      return String.valueOf(toUpper(value.charAt(0)));
    }
    char[] chars = value.toCharArray();
    chars[0] = toUpper(chars[0]);
    return new String(chars);
  }

  public static String toCamel(String source) {
    if (isNull(source)) {
      return null;
    }
    boolean isFind = false;
    int size = source.length();
    StringBuilder stringBuilder = new StringBuilder(size);
    for (int i = 0; i < size; i++) {
      char ch = source.charAt(i);
      if (ch == Const._underline) {
        isFind = true;
        continue;
      }
      if (isFind) {
        stringBuilder.append(toUpper(ch));
        isFind = false;
      } else {
        stringBuilder.append(ch);
      }
    }
    return stringBuilder.toString();
  }

  public static String toUnderline(String source) {
    if (isNull(source)) {
      return null;
    }
    int size = source.length();
    StringBuilder stringBuilder = new StringBuilder(size * 2);
    for (int i = 0; i < size; i++) {
      char ch = source.charAt(i);
      if (isUpper(ch)) {
        stringBuilder.append(Const._underline).append(toLower(ch));
      } else {
        stringBuilder.append(ch);
      }
    }
    return stringBuilder.toString();
  }

  public static String[] split(String source, String delimiter) {
    if (source == null) {
      return null;
    }
    String[] result = null;
    StringTokenizer stringTokenizer = new StringTokenizer(source, delimiter);
    result = new String[stringTokenizer.countTokens()];
    int i = 0;
    while (stringTokenizer.hasMoreTokens()) {
      result[i] = stringTokenizer.nextToken().trim();
      i++;
    }
    return result;
  }

  public static String trimRight(String value) {
    if (value == null || value.length() == 0) {
      return value;
    }
    char[] chars = value.toCharArray();
    int size = chars.length;
    int index = size - 1;
    while (index >= 0 && chars[index] <= ' ') {
      index--;
    }
    if (index == size - 1) {
      return value;
    }
    if (index < 0) {
      return "";
    }
    return new String(chars, 0, index + 1);
  }

  public static String trimLeft(String value) {
    if (value == null || value.length() == 0) {
      return value;
    }
    char[] chars = value.toCharArray();
    int size = chars.length;
    int index = 0;
    while (index < size && chars[index] <= ' ') {
      index++;
    }
    if (index == 0) {
      return value;
    }
    if (index == size) {
      return "";
    }
    return new String(chars, index, size - index);
  }

  public static byte[] toBytes(String value, String charset) {
    if (value == null) {
      return null;
    } else {
      try {
        return value.getBytes(charset);
      } catch (UnsupportedEncodingException e) {
        log.error("toBytes fail!", e);
        return null;
      }
    }
  }

  public static byte[] toBytes(String value) {
    return toBytes(value, charset_default);
  }

  /**
   * 从索引位置截取指定长度的字符串。
   * @param source     源字符串。
   * @param beginIndex 开始索引位置，从 0 开始，包含这个位置的字符。
   * @param count      截取的指定长度，小于 0 时截取到结尾。
   * @return null 出现错误；非null 返回结果字符串。
   */
  public static String subString(String source, int beginIndex, int count) {
    if ((source == null) || (source.length() == 0)) {
      return "";
    }
    int size = source.length();
    if (beginIndex < 0) {
      beginIndex = 0;
    }
    if (beginIndex >= size) {
      return "";
    }
    if (count < 0) {
      return source.substring(beginIndex);
    }
    int endIndex = beginIndex + count;
    if (endIndex > size) {
      endIndex = size;
    }
    return source.substring(beginIndex, endIndex);
  }

  /**
   * 从索引位置截取后面所有字符串
   * @param source 源字符串
   * @param index  索引位置，从 0 开始，包含这个位置的字符
   * @return null 出现错误；非null 返回结果字符串
   */
  public static String subString(String source, int index) {
    return subString(source, index, -1);
  }

  public static int lastIndexOf(String subString, String source) {
    return source == null ? -1 : source.lastIndexOf(subString);
  }

  public static String getSimpleText(String source) {
    if (source == null || source.length() <= length_short_text + 10) {
      return source;
    }
    int size = source.length();
    return subString(source, 0, length_short_text / 5) + " ... (" + size + " size)";
  }

  /**
   * 字符串替代。如：System.out.println(strReplace("this is a test","is","was")); 输出：thwas was a test
   * @param text         : 要进行替换操作的字符串
   * @param searchString : 被替换的子字符串
   * @param replacement  : 替换成的子字符串
   * @return 返回替换后的字符串。
   */
  public static String replace(String text, String searchString, String replacement) {
    if (isNull(text)) {
      return text;
    }
    return StringUtils.replace(text, searchString, replacement);
  }

  public static String toSqlStr(String s) {
    s = toString(s);
    s = replace(s, "'", "''");
    return s.trim();
  }

  /**
   * 是否以结尾字符串结束
   * @param source 源字符串；
   * @param suffix 结尾字符串
   * @return true: 以结尾字符串结尾； false: 不是以结尾字符串结尾。
   */
  public static boolean isSuffix(String source, String suffix) {
    if (isNull(source) || isNull(suffix)) {
      return false;
    }
    source = toLower(source);
    suffix = toLower(suffix);
    if (source.endsWith(suffix)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 去掉结尾字符串，如果不是结尾字符串结尾，返回源字符串，否则去掉结尾字符串。
   * @param source 源字符串；
   * @param suffix 结尾字符串；
   * @return 去掉结尾的字符串。
   */
  public static String ridSuffix(String source, String suffix) {
    if (isNull(suffix)) {
      return source;
    }
    if (!isSuffix(source, suffix)) {
      return source;
    }
    int length = source.length() - suffix.length();
    return subString(source, 0, length);
  }

  /**
   * 添加结尾字符串，如果不是结尾字符串结尾，添加，否则返回源字符串。
   * @param source 源字符串；
   * @param suffix 结尾字符串；
   * @return 添加结尾字符串后的字符串。
   */
  public static String addSuffix(String source, String suffix) {
    if (isNull(suffix)) {
      return source;
    }
    if (isSuffix(source, suffix)) {
      return source;
    }
    return source + suffix;
  }

  /**
   * 是否以开头字符串结束
   * @param source 源字符串；
   * @param header 开头字符串
   * @return true: 以开头字符串开头； false: 不是以开头字符串开头。
   */
  public static boolean isHeader(String source, String header) {
    if (isNull(source) || isNull(header)) {
      return false;
    }
    source = toLower(source);
    header = toLower(header);
    if (source.startsWith(header)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 去掉开头字符串，如果不是开头字符串开头，返回源字符串，否则去掉开头字符串。
   * @param source 源字符串；
   * @param header 开头字符串；
   * @return 去掉开头的字符串。
   */
  public static String ridHeader(String source, String header) {
    if (isNull(header)) {
      return source;
    }
    if (!isHeader(source, header)) {
      return source;
    }
    int length = header.length();
    return subString(source, length);
  }

  /**
   * 添加开头字符串，如果不是开头字符串开头，添加，否则返回源字符串。
   * @param source 源字符串；
   * @param header 开头字符串；
   * @return 添加开头字符串后的字符串。
   */
  public static String addHeader(String source, String header) {
    if (isNull(header)) {
      return source;
    }
    if (isHeader(source, header)) {
      return source;
    }
    return header + source;
  }

  // >>----------------------- String -----------------------

  // <<----------------------- toString -----------------------

  public static String toString(String value) {
    return value == null ? "" : value.trim();
  }

  public static String toString(Boolean value) {
    return value == null ? "" : String.valueOf(value);
  }

  public static String toString(Byte value) {
    return value == null ? "" : String.valueOf(value);
  }

  public static String toString(Short value) {
    return value == null ? "" : String.valueOf(value);
  }

  public static String toString(Integer value) {
    return value == null ? "" : String.valueOf(value);
  }

  public static String toString(Long value) {
    return value == null ? "" : String.valueOf(value);
  }

  public static String toString(Float value) {
    return value == null ? "" : String.valueOf(value);
  }

  public static String toString(Double value) {
    return value == null ? "" : String.valueOf(value);
  }

  public static String toString(char value) {
    return String.valueOf(value);
  }

  public static String toString(Object value) {
    return value == null ? "" : String.valueOf(value).trim();
  }

  public static String toString(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    try {
      return new String(bytes, charset_default);
    } catch (Exception e) {
      log.error("toString fail!", e);
      return null;
    }
  }

  public static String toString(byte[] bytes, String charset) {
    if (bytes == null) {
      return null;
    }
    try {
      return new String(bytes, charset);
    } catch (Exception e) {
      log.error("toString fail!", e);
      return null;
    }
  }

  public static Object toObject(String value) {
    return value;
  }

  // >>----------------------- toString -----------------------

  // <<----------------------- hex -----------------------

  public static String bytesToHex(byte[] src) {
    if (isNull(src)) {
      return null;
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < src.length; i++) {
      int v = src[i] & 0xFF;
      String hex = Integer.toHexString(v);
      if (hex.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(hex);
    }
    return stringBuilder.toString().toUpperCase();
  }

  public static String bytesToHex(byte[] src, int from, int length) {
    if (isNull(src)) {
      return null;
    }
    if (from < 0) {
      from = 0;
    }
    if (length <= 0) {
      return null;
    }
    StringBuilder stringBuilder = new StringBuilder();
    int count = from + length;
    if (count > src.length) {
      count = src.length;
    }
    for (int i = from; i < count; i++) {
      int v = src[i] & 0xFF;
      String hex = Integer.toHexString(v);
      if (hex.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(hex);
    }
    return stringBuilder.toString().toUpperCase();
  }

  /**
   * 将hex字符串转换为字节数组。
   * @param hexString hex 字符串。
   * @param size      长度。
   * @return 返回字节数组。
   */
  public static byte[] hexToBytes(String hexString, int size) {
    if (isNull(hexString)) {
      return null;
    }
    try {
      hexString = hexString.toUpperCase();
      int length = hexString.length() / 2;
      if (size <= 0) {
        size = length;
      }
      char[] hexChars = hexString.toCharArray();
      byte[] dest = new byte[size];
      for (int i = 0; i < length; i++) {
        int pos = i * 2;
        dest[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
      }
      return dest;
    } catch (Exception e) {
      log.error("hexToBytes error!", e);
      return null;
    }
  }

  public static byte[] hexToBytes(String hexString) {
    return hexToBytes(hexString, 0);
  }

  public static boolean isHex(String value) {
    if (isNull(value)) {
      return false;
    }
    value = toUpper(value);
    if (value.startsWith(prefix_hex)) {
      value = subString(value, prefix_hex.length());
    }
    if (isNull(value)) {
      return false;
    }
    for (char ch : value.toCharArray()) {
      if (charToByte(ch) < 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Convert char to byte
   * @param c char
   * @return byte
   */
  private static byte charToByte(char c) {
    return (byte) "0123456789ABCDEF".indexOf(c);
  }

  // >>----------------------- hex -----------------------

  // <<----------------------- boolean -----------------------

  public static boolean toBoolean(Boolean value) {
    return value == null ? false : value;
  }

  public static boolean toBoolean(String value) {
    return value != null && value.equalsIgnoreCase("true");
  }

  // >>----------------------- boolean -----------------------

  // <<----------------------- char -----------------------

  public static char toChar(String value) {
    return isNull(value) ? 0 : value.charAt(0);
  }

  // >>----------------------- char -----------------------

  // <<----------------------- byte -----------------------

  public static byte toByte(String value) {
    try {
      return isNull(value) ? 0 : Byte.parseByte(value);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  // >>----------------------- byte -----------------------

  // <<----------------------- short -----------------------

  public static short toShort(String value) {
    try {
      return isNull(value) ? 0 : Short.parseShort(value);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  // >>----------------------- short -----------------------

  // <<<----------------------- int -----------------------

  public static boolean isNull(int value) {
    return value <= 0;
  }

  public static boolean isNull(Integer value) {
    return value == null || value <= 0;
  }

  public static boolean equal(Integer value1, Integer value2) {
    if (value1 == null && value2 == null) {
      return true;
    }
    if (value1 == null || value2 == null) {
      return false;
    }
    return value1.intValue() == value2.intValue();
  }

  public static boolean equal(int value1, int value2) {
    return value1 == value2;
  }

  public static int toInt(Integer value) {
    return value == null ? 0 : value;
  }

  public static int toInt(String value) {
    try {
      return isNull(value) ? 0 : Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  public static boolean isInt(String value) {
    if (isNull(value)) {
      return false;
    }
    try {
      Integer.parseInt(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static int toInteger(String value) {
    return toInt(value);
  }

  public static int toInteger(Object value) {
    return toInt(value);
  }

  public static int toInt(Object value) {
    try {
      return value == null ? 0 : Integer.parseInt(toString(value));
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  // >>>----------------------- int -----------------------

  // <<----------------------- long -----------------------

  public static boolean isNull(long value) {
    return value <= 0;
  }

  public static boolean isNull(Long value) {
    return value == null || value <= 0;
  }

  public static boolean equal(Long value1, Long value2) {
    if (value1 == null && value2 == null) {
      return true;
    }
    if (value1 == null || value2 == null) {
      return false;
    }
    return value1.longValue() == value2.longValue();
  }

  public static boolean equal(long value1, long value2) {
    return value1 == value2;
  }

  public static boolean isLong(String value) {
    if (isNull(value)) {
      return false;
    }
    try {
      Long.parseLong(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static long toLong(Long value) {
    return value == null ? 0 : value;
  }

  public static long toLong(String value) {
    try {
      return isNull(value) ? 0 : Long.parseLong(value);
    } catch (NumberFormatException e) {
      return 0L;
    }
  }

  public static long toLong(Object value) {
    try {
      return value == null ? 0 : Long.parseLong(toString(value));
    } catch (NumberFormatException e) {
      return 0L;
    }
  }

  // >>----------------------- long -----------------------

  // <<----------------------- float -----------------------

  public static float toFloat(String value) {
    try {
      return isNull(value) ? 0 : Float.parseFloat(value);
    } catch (NumberFormatException e) {
      return 0F;
    }
  }

  // >>----------------------- float -----------------------

  // <<<----------------------- double -----------------------

  public static boolean isNull(double value) {
    return value <= 0;
  }

  public static boolean isNull(Double value) {
    return value == null || value <= 0;
  }

  public static boolean isDouble(String value) {
    if (isNull(value)) {
      return false;
    }
    try {
      Double.parseDouble(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static double toDouble(Double value) {
    return value == null ? 0 : value;
  }

  public static double toDouble(String value) {
    try {
      return isNull(value) ? 0 : Double.parseDouble(value);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  public static double toDouble(Object value) {
    try {
      return value == null ? 0 : Double.parseDouble(toString(value));
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  public static double toScale(double value, int scale) {
    BigDecimal bigDecimal = new BigDecimal(value);
    double result = bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    return result;
  }

  // >>>----------------------- double -----------------------

  // <<<----------------------- datetime -----------------------

  public static boolean isDateFormat(String value) {
    String format = "yyyy-MM-dd";
    Date date = toDatetime(value, format);
    return date != null;
  }

  public static boolean isDatetimeFormat(String value) {
    String format = "yyyy-MM-dd HH:mm:ss";
    Date date = toDatetime(value, format);
    return date != null;
  }

  public static boolean isDate(String value) {
    if (isDatetimeFormat(value)) {
      return true;
    }
    if (isDateFormat(value)) {
      return true;
    }
    return false;
  }

  public static String getDatePart(String value) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    int index = StringUtils.indexOf(value, " ");
    if (index <= 0) {
      return value;
    }
    String result = StringUtils.substring(value, 0, index);
    return result;
  }

  public static Date toDatetime(String value, String format) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    Date date = null;
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
      date = simpleDateFormat.parse(value);
    } catch (ParseException e) {
    }
    return date;
  }

  public static Date toDatetime(String value) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    String format = datetime_format;
    if (value.length() <= 10) {
      format = date_format;
    }
    Date date = null;
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
      date = simpleDateFormat.parse(value);
    } catch (ParseException e) {
    }
    return date;
  }

  public static Date toDatetime(Object value) {
    return toDatetime(toString(value));
  }

  public static String formatDateTime(Date date) {
    return toString(date, datetime_format);
  }

  public static String formatTimeMillis(long value) {
    Date date = new Date();
    date.setTime(value);
    return toString(date, datetime_sss_format);
  }

  public static Date toDate(String value) {
    return toDatetime(value);
  }

  public static String toString(Date date, String pattern) {
    if (date == null) {
      return null;
    }
    if (StringUtils.isEmpty(pattern)) {
      pattern = datetime_format;
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE);
    String s = null_datetime;
    try {
      s = dateFormat.format(date);
    } catch (Exception e) {
    }
    return s;
  }

  public static String toString(Date date) {
    return toString(date, null);
  }

  public static String getDigitDay() {
    return toString(new Date(), "yyyyMMdd");
  }

  public static String getDigitMillis() {
    return toString(new Date(), "yyyyMMddHHmmssSSS");
  }

  public static long diffSecond(long lastTime) {
    long now = System.currentTimeMillis();
    long ms = now - lastTime;
    int per = 1000;
    return ms / per;
  }

  public static long getSecond(String datetimeStr) {
    Date date = null;
    if (isNull(datetimeStr)) {
      date = new Date();
    } else {
      date = toDatetime(datetimeStr);
    }
    return getSecond(date);
  }

  public static long getSecond(Date date) {
    if (date == null) {
      return 0;
    }
    return date.getTime() / 1000;
  }

  public static long getSecond() {
    return System.currentTimeMillis() / 1000;
  }

  public static Long getConfigTime(String time) {
    if (Tool.isNull(time)) {
      return null;
    }
    if (Tool.isLong(time)) {
      return Tool.toLong(time);
    }
    int len = time.length();
    String val = time.substring(0, len - 1);
    String unit = time.substring(len - 1, len);
    if (!Tool.isLong(val)) {
      throw new CommonException(Code.SETTING_ERROR, "时间配置无效! time: " + time);
    }
    long value = Tool.toLong(val);
    switch (unit) {
      case unit_second:
        break;
      case unit_minute:
        value = value * 60;
        break;
      case unit_hour:
        value = value * 60 * 60;
        break;
      case unit_day:
        value = value * 60 * 60 * 24;
        break;
      default:
        throw new CommonException(Code.SETTING_ERROR, "时间配置单位无效! 单位: " + unit);
    }
    return value;
  }

  public static String getCostTime(long start) {
    long end = System.currentTimeMillis();
    long cost = end - start;
    return toCostTime(cost);
  }

  public static String toCostTime(long cost) {
    String costTime;
    if (cost < 1000) {
      costTime = cost + " ms";
    } else {
      costTime = (cost / 1000) + " s " + (cost % 1000) + " ms";
    }
    return costTime;
  }

  // >>>----------------------- datetime -----------------------

  //<<<----------------------- random -----------------------

  /**
   * 取两值之间的随机整数Value, 在 minValue(包含) 至 maxValue(不包含)之间。
   * @param minValue 最小值，包含这个值。
   * @param maxValue 最大值，不包含这个值。
   * @return 返回随机整数。
   */
  @SneakyThrows
  public static int getRandom(int minValue, int maxValue) {
    Random random = getRandomInstance();
    int value = random.nextInt();
    value = Math.abs(value % (maxValue - minValue)) + minValue;
    return value;
  }

  /**
   * 取两值之间的随机整数Value, 在 minValue(包含) 至 maxValue(不包含)之间。
   * @param minValue 最小值，包含这个值。
   * @param maxValue 最大值，不包含这个值。
   * @return 返回随机长整数。
   */
  public static long getRandom(long minValue, long maxValue) {
    Random random = getRandomInstance();
    long value = random.nextLong();
    value = Math.abs(value % (maxValue - minValue)) + minValue;
    return value;
  }

  /**
   * 取随机整数 Value, 在 0(包含) 至 maxValue(不包含)之间。
   * @param maxValue 最大值，不包含这个值。
   * @return 返回随机整数。
   */
  public static int getRandom(int maxValue) {
    int minValue = 0;
    return getRandom(minValue, maxValue);
  }

  /**
   * 取随机整数 Value, 在 0(包含) 至 1000000000(不包含)之间。
   * @return 返回随机整数。
   */
  public static int getRandom() {
    int minValue = 0;
    int maxValue = 1000000000;
    return getRandom(minValue, maxValue);
  }

  /**
   * 随机生成字符串，包含大小写，数字。
   * @param length 长度。
   * @return 返回随机字符串。
   */
  public static String getRandomString(int length) {
    String source = upperStr + lowerStr + numStr;
    if (length < 1) {
      length = 1;
    }
    final StringBuilder sb = new StringBuilder(length);
    int size = source.length();
    for (int i = 0; i < length; i++) {
      int number = getRandom(size);
      sb.append(source.charAt(number));
    }
    return sb.toString();
  }

  /**
   * 取得不重复随机数组，从 0 - (maxValue - 1) 中随机取得 count 个不重复随机数
   * @param count    随机数个数，必须大于零，且小于 maxValue
   * @param maxValue 最大数，必须大于零
   * @return 不重复随机数组
   */
  public static int[] getRandoms(int count, int maxValue) {
    if (count <= 0 || maxValue <= 0) {
      return null;
    }
    if (count > maxValue) {
      count = maxValue;
    }
    int[] seed = new int[maxValue];
    for (int i = 0; i < maxValue; i++) {
      seed[i] = i;
    }
    int[] result = new int[count];
    Random random = getRandomInstance();
    for (int i = 0; i < count; i++) {
      int j = random.nextInt(maxValue - i);
      result[i] = seed[j];
      seed[j] = seed[maxValue - 1 - i]; // 使用最后一个填充
    }
    return result;
  }

  @SneakyThrows
  private static Random getRandomInstance() {
    //    return ThreadLocalRandom.current();
    return SecureRandom.getInstance("SHA1PRNG");
  }

  //  private static long getRandomSeed() {
  //    long currMillis = System.currentTimeMillis();
  //    long result = currMillis + randomSeedOffset;
  //    randomSeedOffset++;
  //    return result;
  //  }

  //>>>----------------------- random -----------------------

  // <<<----------------------- list -----------------------

  public static boolean isNull(String[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isNull(byte[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isNull(List list) {
    return list == null || list.size() == 0;
  }

  public static boolean isNull(Map map) {
    return map == null || map.size() == 0;
  }

  public static <T> void putMap(Map<String, T> map, String key, T t) {
    if (map == null) {
      log.warn("putMap map is null!");
      return;
    }
    if (isNull(key)) {
      log.warn("putMap key is null!");
      return;
    }
    //    key = toLower(key);
    map.put(key, t);
  }

  public static <T> T getValue(Map<String, T> map, String key) {
    if (map == null) {
      log.warn("getValue map is null!");
      return null;
    }
    if (isNull(key)) {
      log.warn("getValue key is null! key: " + key);
      return null;
    }
    //    key = toLower(key);
    return map.get(key);
  }

  // >>>----------------------- list -----------------------

  // <<----------------------- path -----------------------

  public static String toLocalPath(String filePath) {
    if (filePath == null) {
      return null;
    }
    filePath = filePath.replace('/', File.separatorChar);
    filePath = filePath.replace('\\', File.separatorChar);
    return filePath;
  }

  public static String toUrlPath(String filePath) {
    if (filePath == null) {
      return null;
    }
    filePath = filePath.replace(File.separatorChar, '/');
    return filePath;
  }

  // >>----------------------- path -----------------------

  // <<----------------------- stream -----------------------

  public static String readStream(InputStream inputStream) {
    if (inputStream == null) {
      log.warn("inputStream is null!");
      return null;
    }
    //字节数组
    byte[] bcache = new byte[2048];
    int readSize = 0;//每次读取的字节长度
    int totalSize = 0;//总字节长度
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      //一次性读取2048字节
      while ((readSize = inputStream.read(bcache)) > 0) {
        totalSize += readSize;
        if (totalSize > protected_length) {
          log.warn("inputStream is over size! max: " + protected_length);
          return null;
        }
        byteArrayOutputStream.write(bcache, 0, readSize);
      }
    } catch (IOException e) {
      log.error("write inputStream error!", e);
      return null;
    }

    try {
      return byteArrayOutputStream.toString(charset_default);
    } catch (UnsupportedEncodingException e) {
      log.error("toString error!", e);
      return null;
    }
  }

  // >>----------------------- stream -----------------------

  // <<<----------------------- tool -----------------------

  //  public static boolean isSuccess(BResponse response) {
  //    if (response == null || !response.getSuccess()) {
  //      return false;
  //    }
  //    int code = response.getCode();
  //    return code == Code.SUCCESS.getCode() || code == Code.OK.getCode();
  //  }

  public static void throwNull(Object object, String name) {
    if (object == null) {
      throw new CommonException(Code.OBJECT_NULL, name + " is null!");
    }
  }

  public static void throwNull(Object object) {
    if (object == null) {
      throw new CommonException(Code.OBJECT_NULL);
    }
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- public -----------------------

}