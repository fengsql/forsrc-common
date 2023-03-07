package com.forsrc.common.extend.tool;

import com.forsrc.common.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class ToolReader {

  private static final String EMPTY = "";
  private static final String POINT = ".";
  private static final String postfix_xls = "xls";
  private static final String postfix_xlsx = "xlsx";
  private static final int max_row_load = 10000;
  private static final int rownum_start = 0;  //起始行，0第一行

  //<<---------------------------------------- initialize ----------------------------------------

  //>>---------------------------------------- initialize ----------------------------------------

  //<<---------------------------------------- public ----------------------------------------

  /**
   * read the Excel .xlsx,.xls
   * @param file 文件
   * @return 返回内容。
   */
  public static List<List<List<String>>> readExcel(MultipartFile file) {
    if (file == null) {
      log.warn("readExcel file is null!");
      return null;
    }
    String fileName = file.getOriginalFilename();
    if (Tool.isNull(fileName)) {
      log.warn("readExcel filename is null!");
      return null;
    }
    log.info("readExcel: " + fileName);
    String postfix = getPostfix(fileName);
    if (Tool.isNull(postfix)) {
      log.warn("readExcel postfix is null!");
      return null;
    }
    if (Tool.equal(postfix, postfix_xls)) {
      return readXls(file);
    } else if (Tool.equal(postfix, postfix_xlsx)) {
      return readXlsx(file);
    } else {
      log.warn("unknow file! postfix: " + postfix);
      return null;
    }
  }

  //>>---------------------------------------- public ----------------------------------------

  //<<---------------------------------------- protected ----------------------------------------

  //>>---------------------------------------- protected ----------------------------------------

  //<<---------------------------------------- private ----------------------------------------

  //<<<---------------------------------------- readXls ----------------------------------------

  /**
   * read the Excel 2003-2007 .xls
   */
  private static List<List<List<String>>> readXls(MultipartFile file) {
    List<List<List<String>>> result = new ArrayList<>();
    InputStream input = null;
    HSSFWorkbook workbook = null;
    try {
      input = file.getInputStream();
      workbook = new HSSFWorkbook(input);
      int count = workbook.getNumberOfSheets();
      for (int numSheet = 0; numSheet < count; numSheet++) {
        HSSFSheet sheet = workbook.getSheetAt(numSheet);
        if (sheet == null) {
          continue;
        }
        List<List<String>> sheetList = getSheet(sheet);
        result.add(sheetList);
      }
    } catch (Exception e) {
      log.error("readXls error!", e);
    } finally {
      try {
        input.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  private static List<List<String>> getSheet(HSSFSheet sheet) {
    List<List<String>> sheetList = new ArrayList<>();
    int totalRows = sheet.getLastRowNum();
    for (int rowNum = rownum_start; rowNum <= totalRows; rowNum++) {
      HSSFRow row = sheet.getRow(rowNum);
      if (row != null) {
        List<String> rowList = getRow(row);
        sheetList.add(rowList);
      }
    }
    return sheetList;
  }

  private static List<String> getRow(HSSFRow row) {
    List<String> rowList = new ArrayList<>();
    int totalCells = row.getLastCellNum();
    //读取列，从第一列开始
    for (short c = 0; c <= totalCells + 1; c++) {
      HSSFCell cell = row.getCell(c);
      if (cell == null) {
        continue;
      }
      String value = getValue(cell);
      rowList.add(value);
    }
    return rowList;
  }

  //>>>---------------------------------------- readXls ----------------------------------------

  //<<<---------------------------------------- readXlsx ----------------------------------------

  /**
   * read the Excel 2010 .xlsx
   */
  private static List<List<List<String>>> readXlsx(MultipartFile file) {
    List<List<List<String>>> result = new ArrayList<>();
    InputStream input = null;
    XSSFWorkbook workbook = null;
    try {
      input = file.getInputStream();
      workbook = new XSSFWorkbook(input);
      int count = workbook.getNumberOfSheets();
      for (int numSheet = 0; numSheet < count; numSheet++) {
        XSSFSheet sheet = workbook.getSheetAt(numSheet);
        if (sheet == null) {
          continue;
        }
        List<List<String>> sheetList = getSheet(sheet);
        result.add(sheetList);
      }
    } catch (IOException e) {
      log.error("readXlsx error!", e);
    } finally {
      try {
        input.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  private static List<List<String>> getSheet(XSSFSheet sheet) {
    List<List<String>> sheetList = new ArrayList<>();
    int totalRows = sheet.getLastRowNum();
    for (int rowNum = rownum_start; rowNum <= totalRows; rowNum++) {
      XSSFRow row = sheet.getRow(rowNum);
      if (row != null) {
        List<String> rowList = getRow(row);
        sheetList.add(rowList);
      }
    }
    return sheetList;
  }

  private static List<String> getRow(XSSFRow row) {
    List<String> rowList = new ArrayList<>();
    if (row != null) {
      int totalCells = row.getLastCellNum();
      for (int c = 0; c <= totalCells + 1; c++) {
        XSSFCell cell = row.getCell(c);
        if (cell == null) {
          continue;
        }
        String value = getValue(cell);
        rowList.add(value);
      }
    }
    return rowList;
  }

  //>>>---------------------------------------- readXlsx ----------------------------------------

  //<<<---------------------------------------- readXlsxX ----------------------------------------

  /**
   * read the Excel 2010 .xlsx
   */
  private static List<List<List<String>>> readXlsxX(MultipartFile file) {
    List<List<List<String>>> result = new ArrayList<>();
    InputStream input = null;
    XSSFWorkbook workbook1 = null;
    try {
      input = file.getInputStream();
      workbook1 = new XSSFWorkbook(input);
      SXSSFWorkbook workbook = new SXSSFWorkbook(workbook1, max_row_load);
      int count = workbook.getNumberOfSheets();
      for (int numSheet = 0; numSheet < count; numSheet++) {
        SXSSFSheet sheet = workbook.getSheetAt(numSheet);
        if (sheet == null) {
          continue;
        }
        List<List<String>> sheetList = getSheetX(sheet);
        result.add(sheetList);
      }
    } catch (IOException e) {
      log.error("readXlsx error!", e);
    } finally {
      try {
        input.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  private static List<List<String>> getSheetX(SXSSFSheet sheet) {
    List<List<String>> sheetList = new ArrayList<>();
    int totalRows = sheet.getLastRowNum();
    for (int rowNum = rownum_start; rowNum <= totalRows; rowNum++) {
      SXSSFRow row = sheet.getRow(rowNum);
      if (row != null) {
        List<String> rowList = getRowX(row);
        sheetList.add(rowList);
      }
    }
    return sheetList;
  }

  private static List<String> getRowX(SXSSFRow row) {
    List<String> rowList = new ArrayList<>();
    if (row != null) {
      int totalCells = row.getLastCellNum();
      for (int c = 0; c <= totalCells + 1; c++) {
        SXSSFCell cell = row.getCell(c);
        if (cell == null) {
          continue;
        }
        String value = getValue(cell);
        rowList.add(value);
      }
    }
    return rowList;
  }

  //>>>---------------------------------------- readXlsxX ----------------------------------------

  //<<<---------------------------------------- inner ----------------------------------------

  //>>>---------------------------------------- inner ----------------------------------------

  //<<<---------------------------------------- tool ----------------------------------------

  private static String getPostfix(String path) {
    if (Tool.isNull(path)) {
      return EMPTY;
    }
    if (path.contains(POINT)) {
      return path.substring(path.lastIndexOf(POINT) + 1, path.length());
    }
    return EMPTY;
  }

  /**
   * 获取单元格值
   */
  private static String getValue(Cell cell) {
    if (cell == null) {
      return null;
    }
    String result = null;
    CellType cellType = cell.getCellType();
    switch (cellType) {
      case BOOLEAN:
        result = getValueBoolean(cell);
        break;
      case NUMERIC:
        result = getValueNumeric(cell);
        break;
      default:
        result = getValueDefault(cell);
        break;
    }
    return result;
  }

  private static String getValueBoolean(Cell cell) {
    boolean value = cell.getBooleanCellValue();
    return Tool.toString(value);
  }

  private static String getValueNumeric(Cell cell) {
    String result = "";
    if (DateUtil.isCellDateFormatted(cell)) {
      Date value = DateUtil.getJavaDate(cell.getNumericCellValue());
      result = Tool.toString(value);
    } else {
      double value = cell.getNumericCellValue();
      String source = formatNumeric(value);
      result = toValue(source);
    }
    return result;
  }

  private static String getValueDefault(Cell cell) {
    String value = cell.getStringCellValue();
    return Tool.toString(value);
  }

  private static String formatNumeric(double value) {
    DecimalFormat df = new DecimalFormat("0");
    return df.format(value);
  }

  private static String toValue(String value) {
    String result = Tool.toString(value);
    int index = result.indexOf(POINT);
    if (index >= 0) {
      String strArr = Tool.subString(result, index + 1, result.length() - index - 1);
      if (Tool.toInt(strArr) <= 0) {
        result = result.substring(0, index);
      }
    }
    return result;
  }

  //  /**
  //   * 获取单元格值
  //   */
  //  private static String getValue(HSSFCell cell) {
  //    if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
  //      return String.valueOf(cell.getBooleanCellValue());
  //    } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
  //      String cellValue = "";
  //      if (HSSFDateUtil.isCellDateFormatted(cell)) {
  //        Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
  //        cellValue = sdf.format(date);
  //      } else {
  //        DecimalFormat df = new DecimalFormat("#.##");
  //        cellValue = df.format(cell.getNumericCellValue());
  //        String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
  //        if (strArr.equals("00")) {
  //          cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
  //        }
  //      }
  //      return cellValue;
  //    } else {
  //      return String.valueOf(cell.getStringCellValue());
  //    }
  //  }
  //
  //  /**
  //   * 获取单元格值
  //   */
  //  private static String getValue(XSSFCell cell) {
  //    if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
  //      return String.valueOf(cell.getBooleanCellValue());
  //    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
  //      String cellValue = "";
  //      if (DateUtil.isCellDateFormatted(cell)) {
  //        Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
  //        cellValue = sdf.format(date);
  //      } else {
  //        DecimalFormat df = new DecimalFormat("#.##");
  //        cellValue = df.format(cell.getNumericCellValue());
  //        String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
  //        if (strArr.equals("00")) {
  //          cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
  //        }
  //      }
  //      return cellValue;
  //    } else {
  //      return String.valueOf(cell.getStringCellValue());
  //    }
  //  }
  //
  //  /**
  //   * 获取单元格值
  //   */
  //  private static String getValue(SXSSFCell cell) {
  //    if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
  //      return String.valueOf(cell.getBooleanCellValue());
  //    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
  //      String cellValue = "";
  //      if (DateUtil.isCellDateFormatted(cell)) {
  //        Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
  //        cellValue = sdf.format(date);
  //      } else {
  //        DecimalFormat df = new DecimalFormat("#.##");
  //        cellValue = df.format(cell.getNumericCellValue());
  //        String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
  //        if (strArr.equals("00")) {
  //          cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
  //        }
  //      }
  //      return cellValue;
  //    } else {
  //      return String.valueOf(cell.getStringCellValue());
  //    }
  //  }

  //>>>---------------------------------------- tool ----------------------------------------

  //>>---------------------------------------- private ----------------------------------------

  //<<---------------------------------------- get ----------------------------------------

  //>>---------------------------------------- get ----------------------------------------

  //<<---------------------------------------- set ----------------------------------------

  //>>---------------------------------------- set ----------------------------------------

  //<<---------------------------------------- get set ----------------------------------------

  //>>---------------------------------------- get set ----------------------------------------

}  