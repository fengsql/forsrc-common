package com.forsrc.common.extend.tool;

import com.forsrc.common.constant.Enum;
import com.forsrc.common.extend.bean.Field;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

@Slf4j
public class ToolWriter {

  private static final short font_size_title = 18;
  private static final String font_name_title = "宋体";  //Courier New
  private static final short font_size_head = 10;
  private static final String font_name_head = "宋体";
  private static final short font_size_body = 10;
  private static final String font_name_body = "宋体";

  // <<----------------------- public -----------------------

  // <<<----------------------- notmal -----------------------

  public static DataFormat getDataFormat(SXSSFWorkbook workbook) {
    return workbook.createDataFormat();
  }

  // >>>----------------------- notmal -----------------------

  // <<<----------------------- getStyle -----------------------

  public static CellStyle getStyleTitle(SXSSFWorkbook workbook) {
    CellStyle cellStyle = createStyleTitle(workbook);
    Font font = createFontTitle(workbook);
    cellStyle.setFont(font);
    return cellStyle;
  }

  public static CellStyle getStyleHead(SXSSFWorkbook workbook) {
    CellStyle cellStyle = createStyleHead(workbook);
    Font font = createFontHead(workbook);
    cellStyle.setFont(font);
    return cellStyle;
  }

  public static CellStyle getStyleBody(SXSSFWorkbook workbook) {
    CellStyle cellStyle = createStyleBody(workbook);
    Font font = createFontBody(workbook);
    cellStyle.setFont(font);
    return cellStyle;
  }

  // >>>----------------------- getStyle -----------------------

  // <<<----------------------- getFieldValue -----------------------

  public static void setValue(Object object, Cell cell, Field field, CellStyle cellStyle, DataFormat dataFormat) {
    Enum.ExportFieldType exportFieldType = field.getExportFieldType();
    switch (exportFieldType) {
      case constant_:
        setValue_constant(object, cell, field, cellStyle, dataFormat);
        break;
      case integer_:
      case long_:
        setValue_integer(object, cell, field, cellStyle, dataFormat);
        break;
      case decimal_:
        setValue_decimal(object, cell, field, cellStyle, dataFormat);
        break;
      case datetime_:
        setValue_datetime(object, cell, field, cellStyle, dataFormat);
        break;
      case string_:
        setValue_string(object, cell, field, cellStyle, dataFormat);
        break;
      default:
        setValue_def(object, cell, field, cellStyle, dataFormat);
        break;
    }
  }

  // >>>----------------------- getFieldValue -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- createStyle -----------------------

  private static CellStyle createStyleTitle(SXSSFWorkbook workbook) {
    CellStyle cellStyle = createStyleDefault(workbook);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyle.setWrapText(true);
    return cellStyle;
  }

  private static CellStyle createStyleHead(SXSSFWorkbook workbook) {
    CellStyle cellStyle = createStyleDefault(workbook);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyle.setWrapText(true);
    return cellStyle;
  }

  private static CellStyle createStyleBody(SXSSFWorkbook workbook) {
    CellStyle cellStyle = createStyleDefault(workbook);
    cellStyle.setAlignment(HorizontalAlignment.LEFT);
    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyle.setWrapText(false);
    return cellStyle;
  }

  // >>>----------------------- createStyle -----------------------

  // <<<----------------------- createFont -----------------------

  private static Font createFontTitle(SXSSFWorkbook workbook) {
    Font font = createFontDefault(workbook);
    font.setBold(true);
    font.setFontHeightInPoints(font_size_title);
    font.setFontName(font_name_title);
    return font;
  }

  private static Font createFontHead(SXSSFWorkbook workbook) {
    Font font = createFontDefault(workbook);
    font.setBold(true);
    font.setFontHeightInPoints(font_size_head);
    font.setFontName(font_name_head);
    return font;
  }

  private static Font createFontBody(SXSSFWorkbook workbook) {
    Font font = createFontDefault(workbook);
    font.setBold(false);
    font.setFontHeightInPoints(font_size_body);
    font.setFontName(font_name_body);
    return font;
  }

  // >>>----------------------- createFont -----------------------

  // <<<----------------------- default -----------------------

  private static CellStyle createStyleDefault(SXSSFWorkbook workbook) {
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyle.setWrapText(true); //是否自动换行

    // 设置单元格边框为细线条（上下左右）
    cellStyle.setBorderLeft(BorderStyle.THIN);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);

    DataFormat format = workbook.createDataFormat();
    cellStyle.setDataFormat(format.getFormat("@"));
    return cellStyle;
  }

  private static Font createFontDefault(SXSSFWorkbook workbook) {
    Font font = workbook.createFont();
    font.setBold(false);
    //    font.setFontHeight((short) font_size_title);
    //    font.setFontName(font_name_title);
    return font;
  }

  // >>>----------------------- default -----------------------

  // <<<----------------------- setValue -----------------------

  private static void setValue_def(Object object, Cell cell, Field field, CellStyle cellStyle, DataFormat dataFormat) {
    String value = Tool.toString(object);
    setString(cellStyle, dataFormat);
    cell.setCellValue(value);
    cell.setCellStyle(cellStyle);
  }

  private static void setValue_constant(Object object, Cell cell, Field field, CellStyle cellStyle, DataFormat dataFormat) {
    int val = Tool.toInt(object);
    String value = ToolEnum.getTitle(field.getName(), val);
    if (value == null) {
      value = val + "";
    }
    setString(cellStyle, dataFormat);
    cell.setCellValue(value);
    cell.setCellStyle(cellStyle);
  }

  private static void setValue_integer(Object object, Cell cell, Field field, CellStyle cellStyle, DataFormat dataFormat) {
    String value = Tool.toString(object);
    setInt(cellStyle, dataFormat);
    cell.setCellValue(value);
    cell.setCellStyle(cellStyle);
  }

  private static void setValue_decimal(Object object, Cell cell, Field field, CellStyle cellStyle, DataFormat dataFormat) {
    String value = Tool.toString(object);
    setDouble(cellStyle, dataFormat);
    cell.setCellValue(value);
    cell.setCellStyle(cellStyle);
  }

  private static void setValue_string(Object object, Cell cell, Field field, CellStyle cellStyle, DataFormat dataFormat) {
    String value = Tool.toString(object);
    setString(cellStyle, dataFormat);
    cell.setCellValue(value);
    cell.setCellStyle(cellStyle);
  }

  private static void setValue_datetime(Object object, Cell cell, Field field, CellStyle cellStyle, DataFormat dataFormat) {
    String value = Tool.toString(Tool.toDatetime(object));
    setString(cellStyle, dataFormat);
    cell.setCellValue(value);
    cell.setCellStyle(cellStyle);
  }

  // >>>----------------------- setValue -----------------------

  // <<<----------------------- dataFormat -----------------------

  private static void setString(CellStyle cellStyle, DataFormat dataFormat) {
    cellStyle.setDataFormat(dataFormat.getFormat("@"));
  }

  private static void setInt(CellStyle cellStyle, DataFormat dataFormat) {
    cellStyle.setDataFormat(dataFormat.getFormat("#,##0"));
  }

  private static void setDouble(CellStyle cellStyle, DataFormat dataFormat) {
    cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
  }

  // >>>----------------------- dataFormat -----------------------

  // <<<----------------------- tool -----------------------

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}