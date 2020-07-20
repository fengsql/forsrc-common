package com.forsrc.common.extend.tool;

import com.forsrc.common.constant.Code;
import com.forsrc.common.constant.Enum;
import com.forsrc.common.exception.CommonException;
import com.forsrc.common.extend.bean.Field;
import com.forsrc.common.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class ToolExport {

  private static final int max_row_load = 10000;  //每次缓存最多记录数，防止oom
  private static final int cell_width_per = 256;
  private static final int max_cell_width = 32 * 256;
  private static final int min_cell_width = 8 * 256;
  private static final int cell_width_datetime = 20 * 256;
  private static final int sheet_width_default = 20;

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  public static void export(HttpServletResponse response, String tableName, String title, List<Field> fields, List<Map<String, Object>> data) {
    checkExport(tableName, fields, data);
    doExport(response, tableName, title, fields, data);
  }

  // >>>----------------------- normal -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  private static void doExport(HttpServletResponse response, String tableName, String title, List<Field> fields, List<Map<String, Object>> data) {
    SXSSFWorkbook workbook = null;
    try {
      workbook = new SXSSFWorkbook(max_row_load);
      addData(workbook, tableName, title, fields, data);
      exportExcel(response, workbook, tableName);
    } finally {
      if (workbook != null) {
        workbook.dispose();
      }
    }
  }

  // >>>----------------------- normal -----------------------

  // <<<----------------------- export -----------------------

  private static void exportExcel(HttpServletResponse response, SXSSFWorkbook workbook, String tableName) {
    String sheetName = getSheetName(tableName);
    OutputStream outputStream = null;
    try {
      //禁用缓存
      response.setHeader("Cache-Control", "no-cache");
      response.setHeader("Pragma", "no-cache");
      response.setDateHeader("Expires", -1);

      response.setCharacterEncoding("UTF-8");
      response.setHeader("content-Type", "application/vnd.ms-excel");

      //      response.setContentType("application/octet-streem");
      //      response.setContentType("application/vnd.ms-excel;charset=utf-8");
      response.setHeader("Cache-Control", "private static");
      response.setHeader("Content-disposition", "attachment;filename=" + sheetName);
      outputStream = response.getOutputStream();
      workbook.write(outputStream);
      outputStream.flush();
      log.info("exportExcel ok. fileName: " + sheetName);
    } catch (final IOException e) {
      log.error("exportExcel IOException!", e);
    } finally {
      try {
        if (outputStream != null) {
          outputStream.close();
        }
      } catch (Exception e) {
      }
    }
  }

  private static void addData(SXSSFWorkbook workbook, String tableName, String title, List<Field> fields, List<Map<String, Object>> data) {
    try {
      Sheet sheet = workbook.createSheet(tableName);
      //      autoSizeColumns(sheet, fields.size());
      setSheet(workbook, sheet, fields);
      addTitle(workbook, sheet, title);
      addHeader(workbook, sheet, fields);
      addBody(workbook, sheet, fields, data);
      mergeRegion(workbook, sheet, fields);
    } catch (Exception e) {
      throw new CommonException(Code.FAIL.getCode(), "填充数据错误");
    }
  }

  private static void setSheet(SXSSFWorkbook workbook, Sheet sheet, List<Field> fields) {
    sheet.setDefaultColumnWidth(sheet_width_default);

  }

  private static void addBody(SXSSFWorkbook workbook, Sheet sheet, List<Field> fields, List<Map<String, Object>> data) {
    CellStyle bodyStyle = ToolWriter.getStyleBody(workbook);
    DataFormat dataFormat = ToolWriter.getDataFormat(workbook);
    int index = 3;
    for (Map<String, Object> map : data) {
      Row row = sheet.createRow(index);
      addRow(index, row, fields, map, bodyStyle, dataFormat);
      index++;
    }
  }

  private static void addRow(int index, Row row, List<Field> fields, Map<String, Object> map, CellStyle bodyStyle, DataFormat dataFormat) {
    int count = fields.size();
    for (int colIndex = 0; colIndex < count; colIndex++) {
      Field field = fields.get(colIndex);
      addField(colIndex, row, map, field, bodyStyle, dataFormat);
    }
  }

  private static void addField(int colIndex, Row row, Map<String, Object> map, Field field, CellStyle bodyStyle, DataFormat dataFormat) {
    String name = field.getName();
    Object object = map.get(name);
    Cell cell = row.createCell(colIndex);
    setValue(object, cell, field, bodyStyle, dataFormat);
  }

  private static void setValue(Object object, Cell cell, Field field, CellStyle bodyStyle, DataFormat dataFormat) {
    ToolWriter.setValue(object, cell, field, bodyStyle, dataFormat);
  }

  private static void addHeader(SXSSFWorkbook workbook, Sheet sheet, List<Field> fields) {
    CellStyle headStyle = ToolWriter.getStyleHead(workbook);
    int count = fields.size();
    Row row = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)
    for (int colIndex = 0; colIndex < count; colIndex++) {
      Cell cell = row.createCell(colIndex); // 创建列头对应个数的单元格
      Field field = fields.get(colIndex);
      setHeader(colIndex, sheet, cell, field, headStyle);
    }
  }

  private static void setHeader(int colIndex, Sheet sheet, Cell cell, Field field, CellStyle headStyle) {
    //    int fieldType = field.getFieldType();
    String title = field.getTitle();
    //    int cellType = ToolWriter.getCellType(fieldType);
    int width = getWidth(field);
    //    cell.set(cellType); // 设置列头单元格的数据类型
    //    HSSFRichTextString text = new HSSFRichTextString(fields[n]);
    cell.setCellValue(title); // 设置列头单元格的值
    sheet.setColumnWidth(colIndex, width);
    cell.setCellStyle(headStyle); // 设置列头单元格样式
  }

  //  private static void setCellType(Cell cell, Field field) {
  //    if(cell.getCellType() == CellType.NUMERIC){
  //      if (HSSFDateUtil.isCellDateFormatted(cell)) {
  //        cellValue = DateFormatUtils.format(cell.getDateCellValue(), "yyyy-MM-dd");
  //      } else {
  //        NumberFormat nf = NumberFormat.getInstance();
  //        cellValue = String.valueOf(nf.format(cell.getNumericCellValue())).replace(",", "");
  //      }
  //    }else if(cell.getCellTypeEnum() == CellType.STRING){
  //      cellValue = String.valueOf(cell.getStringCellValue());
  //    }else if(cell.getCellTypeEnum() == CellType.BOOLEAN){
  //      cellValue = String.valueOf(cell.getBooleanCellValue());
  //    }else if(cell.getCellTypeEnum() == CellType.ERROR){
  //      cellValue = “错误类型”;
  //    }else {
  //      cellValue = “”;
  //    }
  //
  //
  //  }
  //  private static String getCellValue(Cell cell) {
  //    //判断是否为null或空串
  //    if (cell == null || cell.toString().trim().equals("")) {
  //      return "";
  //    }
  //    String cellValue = "";
  //
  //    if (cell.getCellType() == CellType.NUMERIC) {
  //      cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
  //    } else if (cell.getCellTypeEnum() == CellType.STRING) {
  //      cellValue = String.valueOf(cell.getStringCellValue());
  //    } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
  //      cellValue = String.valueOf(cell.getBooleanCellValue());
  //    } else if (cell.getCellTypeEnum() == CellType.ERROR) {
  //      cellValue = "错误类型";
  //    } else {
  //      cellValue = "";
  //    }
  //    return cellValue;
  //  }

  private static int getWidth(Field field) {
    if (field.getExportFieldType() == Enum.ExportFieldType.datetime_) {
      return cell_width_datetime;
    }
    int length = field.getLength();
    int result = length * cell_width_per;
    if (result > max_cell_width) {
      result = max_cell_width;
    }
    if (result < min_cell_width) {
      result = min_cell_width;
    }
    return result;
  }

  private static void addTitle(SXSSFWorkbook workbook, Sheet sheet, String title) {
    Row titleRow = sheet.createRow(0);
    Cell cellTiltle = titleRow.createCell(0);
    CellStyle cellStyle = ToolWriter.getStyleTitle(workbook);// 获取列头样式对象
    cellTiltle.setCellStyle(cellStyle);
    cellTiltle.setCellValue(title);
  }

  // >>>----------------------- export ------------------------

  // <<<----------------------- sheet -----------------------

  private static void autoSizeColumns(Sheet sheet, int columnNumber) {
    for (int i = 0; i < columnNumber; i++) {
      int orgWidth = sheet.getColumnWidth(i);
      sheet.autoSizeColumn(i, true);
      int newWidth = (int) (sheet.getColumnWidth(i) + 100);
      if (newWidth > orgWidth) {
        sheet.setColumnWidth(i, newWidth);
      } else {
        sheet.setColumnWidth(i, orgWidth);
      }
    }
  }

  private static void mergeRegion(SXSSFWorkbook workbook, Sheet sheet, List<Field> fields) {
    sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (fields.size() - 1)));
  }

  private static String getSheetName(String tableName) {
    String date = Tool.toString(new Date(), "yyyyMMdd");
    return Tool.toString(tableName) + "_" + date + ".xlsx";
  }

  // >>>----------------------- sheet -----------------------

  // <<<----------------------- tool -----------------------

  private static void checkExport(String tableName, List<Field> fields, List<Map<String, Object>> data) {
    if (Tool.isNull(tableName)) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "未指定表名");
    }
    if (fields == null || fields.size() == 0) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "未指定查询字段");
    }
    if (data == null || data.size() == 0) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "数据为空");
    }
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------
}