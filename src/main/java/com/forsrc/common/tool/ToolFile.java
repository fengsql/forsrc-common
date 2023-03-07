package com.forsrc.common.tool;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

@Slf4j
public class ToolFile {
  private static final String DEFAULT_CHARSET = "UTF-8";
  private static final String DEFAULT_CHARSET_GB2312 = "GB2312";
  private static final int flag_bom_utf8 = 0xFEFF; // 带有BOM的UTF8的开头字符，无BOM时无此值
  private static final int UTF8_HEAD_CODE = -17; // 当文本中含有中文时，读取后第一个字符的值，会造成第一个字符为乱码，暂时不知道原因，处理方式为去掉这个字符
  private static final int UTF8_HEAD_CODE_ = 63; // 当文本中含有中文时，第一字符为-17，转换后的值
  private static final String fileSeparator_win = "\\";
  private static final String fileSeparator_linux = "/";

  private static final String fileSeparator = File.separator;

  public static String getFileSeparator() {
    return fileSeparator;
  }

  // <<------------------------------------ 读文件操作 ------------------------------------

  /**
   * 读取文件内容。
   * @param fileName 文件名。
   * @param charset  字符集。
   * @return 返回文件内容。
   */
  public static String readFile(String fileName, String charset) {
    return doReadFile(fileName, charset);
  }

  /**
   * 读取文件内容。
   * @param fileName 文件名。
   * @return 返回文件内容。
   */
  public static String readFile(String fileName) {
    return readFile(fileName, null);
  }

  /**
   * 读取文件内容
   * @param fileName 文件名
   * @return 返回文件内容。
   */
  public static byte[] readBytes(String fileName) {
    return readBytes(fileName, 0, -1);
  }

  /**
   * 读取文件内容
   * @param fileName 文件名
   * @param startPos 起始位置
   * @param length   长度
   * @return byte[]
   */
  public static byte[] readBytes(String fileName, long startPos, long length) {
    if (Tool.isNull(fileName)) {
      log.warn("fileName is null!");
      return null;
    }
    File file = new File(fileName); // 建立一个文件对象
    if (!file.exists()) {
      log.warn("file not exist! fileName: " + fileName);
      return null;
    }
    long fileSize = file.length();
    startPos = startPos < 0 ? 0 : startPos;
    length = length > 0 ? length : fileSize;
    if (length > fileSize - startPos) {
      length = fileSize - startPos;
    }
    byte[] fileContent = new byte[(int) length];
    try {
      BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
      if (startPos > 0) {
        bufferedInputStream.skip(startPos);
      }
      bufferedInputStream.read(fileContent);
      bufferedInputStream.close();
    } catch (Exception e) {
      log.error("readBytes error! fileName: " + fileName, e);
      return null;
    }
    return fileContent;
  }

  public static char[] getChars(String sourceFile) {
    char[] result = null;
    FileInputStream is = null;
    try {
      is = new FileInputStream(sourceFile);
      Reader reader = new BufferedReader(new InputStreamReader(is, DEFAULT_CHARSET));
      if (reader != null) {
        char[] chars = new char[8192];
        StringBuffer buf = new StringBuffer();
        int count;
        while ((count = reader.read(chars, 0, chars.length)) > 0) {
          buf.append(chars, 0, count);
        }
        result = new char[buf.length()];
        buf.getChars(0, result.length, result, 0);
      }
    } catch (IOException e) {
      log.error("Compilation error", e);
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException exc) {
          // Ignore
        }
      }
    }
    return result;
  }

  /**
   * 按行读取文件。
   * @param fileName 文件名，空或文件不存在时，返回 false；
   * @param lines    存放读取的内容，null 将返回 false。
   * @return true: 成功；false: 失败。
   */
  @SneakyThrows
  public static boolean readFileByLine(String fileName, List<String> lines) {
    if (lines == null) {
      return false;
    }
    lines.clear();
    if (!existFile(fileName)) {
      return false;
    }
    InputStream inputStream = new FileInputStream(fileName);
    return readFileByLine(inputStream, lines);
  }

  public static boolean readFileByLine(InputStream inputStream, List<String> lines) {
    if (lines == null) {
      return false;
    }
    lines.clear();
    BufferedReader reader = null;
    try {
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, DEFAULT_CHARSET);
      reader = new BufferedReader(inputStreamReader);
      int index = 0;
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (index <= 0) {
          line = ridBomUtf8(line); // 去掉带有BOM的UTF8的开头字符，无BOM时无此值
        }
        lines.add(line);
        index++;
      }
      reader.close();
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
        }
      }
    }
    return false;
  }

  private static String doReadFile(String fileName, String charset) {
    StringWriter writer = new StringWriter();
    try {
      Reader reader = new InputStreamReader(new FileInputStream(fileName), DEFAULT_CHARSET);
      try {
        char[] chars = new char[8 * 1024];
        int len;
        while ((len = reader.read(chars)) > 0) {
          writer.write(chars, 0, len);
        }
      } finally {
        reader.close();
      }
    } catch (IOException e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    return writer.toString();
  }

  // >>------------------------------------ 读文件操作 ------------------------------------

  // <<------------------------------------ 写文件操作 ------------------------------------

  /**
   * 将数据写入到文本文件，如果文件已经存在，覆盖原有文件。
   * @param fileName 绝对路径。
   * @param content  文件内容。
   * @return true: 成功；false: 失败。
   */
  public static boolean writeFile(String fileName, String content) {
    return saveToFile(fileName, content, true);
  }

  /**
   * 将数据写入到文本文件，如果文件已经存在，在原有文件后面追加。
   * @param fileName 绝对路径。
   * @param content  文件内容。
   * @return true: 成功；false: 失败。
   */
  public static boolean appendFile(String fileName, String content) {
    return saveToFile(fileName, content, false);
  }

  /**
   * 写入文件内容。
   * @param fileName   文件名。
   * @param startIndex content 的起始位置。
   * @param content    写入字节内容。
   * @return -1 错误；大于等于 0 为写入的字节数。
   */
  public static int writeFile(String fileName, long startIndex, byte[] content) {
    if (Tool.isNull(fileName)) {
      log.warn("fileName is null!");
      return -1;
    }
    if (content == null) {
      log.warn("content is null!");
      return -1;
    }
    int length = content.length;
    if (startIndex >= length) {
      log.warn("startIndex out of content.length!");
      return -1;
    }
    File file = new File(fileName);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        log.error("create file error!", e);
        return -1;
      }
    }
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
      bufferedOutputStream.write(content, (int) startIndex, length);
      bufferedOutputStream.close();
    } catch (Exception e) {
      log.error("writeFile error! fileName: " + fileName, e);
      return -1;
    }
    return length;
  }

  /**
   * 写入文件内容。
   * @param fileName 文件名。
   * @param content  写入字节内容。
   * @return -1 错误；大于等于 0 为写入的字节数。
   */
  public static int writeFile(String fileName, byte[] content) {
    return writeFile(fileName, 0, content);
  }

  /**
   * 按行保存文件，如果文件已存在将覆盖。
   * @param fileName 文件名，为空或文件不存在将返回 false；
   * @param lines    保存的内容，null 将返回 false。
   * @return true: 成功；false: 失败。
   */
  public static boolean writeFileByLine(String fileName, List<String> lines) {
    if (lines == null) {
      return false;
    }
    BufferedWriter writer = null;
    try {
      // writer = new BufferedWriter(new FileWriter(fileName));
      FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
      writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, DEFAULT_CHARSET));
      int count = lines.size();
      for (int i = 0; i < count; i++) {
        String line = lines.get(i);
        writer.write(line);
        writer.newLine();
      }
      writer.close();
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (Exception e) {
        }
      }
    }
    return false;
  }

  private static boolean saveToFile(String fileName, String content, boolean isOverWrite) {
    if (Tool.isNull(fileName)) {
      return false;
    }
    if (!forceFilePath(fileName)) {
      log.warn("saveToFile error! forceFilePath fail. fileName = " + fileName);
      return false;
    }
    content = Tool.toString(content);
    try {
      File file = new File(fileName);
      if (!(file.exists())) {
        boolean success = file.createNewFile();
        if (!success) {
          log.warn("saveToFile error! createNewFile fail. fileName = " + fileName);
          return false;
        }
      }
      RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
      if (isOverWrite) {
        randomAccessFile.setLength(0);
        randomAccessFile.seek(0);
      } else {
        randomAccessFile.seek(randomAccessFile.length());
      }
      byte[] bytes = Tool.toBytes(content);
      randomAccessFile.write(bytes);
      randomAccessFile.close();
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  // >>------------------------------------ 写文件操作 ------------------------------------

  // <<------------------------------------ 文件操作 ------------------------------------

  /**
   * 判断文件是否存在
   * @param fileName 文件名。
   * @return true 存在，false 不存在。
   */
  public static boolean existFile(String fileName) {
    if (Tool.isNull(fileName)) {
      return false;
    }
    try {
      File file = new File(fileName);
      return file.exists();
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 判断文件夹是否存在
   * @param filePath 文件夹
   * @return true 存在，false 不存在。
   */
  public static boolean existPath(String filePath) {
    if (Tool.isNull(filePath)) {
      return false;
    }
    try {
      File file = new File(filePath);
      if (!file.exists()) {
        return false;
      }
      if (!file.isDirectory()) {
        return false;
      }
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean isExist(String fileName) {
    if (fileName == null) {
      return false;
    }
    File file = new File(fileName);
    return file.exists();
  }

  public static boolean hasFiles(String filePath) {
    File file = new File(filePath);
    File[] files = file.listFiles();
    if (files != null && files.length > 0) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean createPath(String filePath) {
    try {
      File f = new File(filePath);
      if (!f.exists()) {
        return f.mkdirs();
      }
      return true;
    } catch (Exception ex) {
      log.error(ExceptionUtils.getStackTrace(ex));
      return false;
    }
  }

  /**
   * 强制创建路径，如果文件路径不存在时，创建文件路径，路径可以是多级
   * @param filePath 文件路径，直接创建。
   * @return true 成功，false 失败。
   */
  public static boolean forcePath(String filePath) {
    if (existPath(filePath)) {
      return true;
    } else {
      return createPath(filePath);
    }
  }

  /**
   * 强制创建文件所在的路径，如果文件路径不存在时，创建文件路径，路径可以是多级。
   * @param fileName 文件名称，创建的路径为文件的上一级路径。
   * @return true 成功，false 失败。
   */
  public static boolean forceFilePath(String fileName) {
    String filePath = getFilePath(fileName);
    return forcePath(filePath);
  }

  public static long getFileSize(String fileName) {
    try {
      File f = new File(fileName);
      if (f.exists()) {
        return f.length();
      } else {
        return 0;
      }
    } catch (Exception ex) {
      log.error(ExceptionUtils.getStackTrace(ex));
      return 0;
    }
  }

  public static String getFileSizeText(long size) {
    if (size < 1024) {
      return size + " B";
    } else {
      size = size / 1024;
    }
    if (size < 1024) {
      return size + " KB";
    } else {
      size = size * 100 / 1024;
    }
    if (size < 1024 * 100) {  //因为如果以MB为单位的话，要保留最后1位小数，把此数乘以100之后再取余
      return (size / 100) + "." + (size % 100) + " MB";
    } else {  //否则如果要以GB为单位的，先除于1024再作同样的处理
      size = size / 1024;
      return (size / 100) + "." + (size % 100) + " GB";
    }
  }

  public static String getFileName(String fileName) {
    try {
      File f = new File(fileName);
      return f.getName();
    } catch (Exception ex) {
      log.error(ExceptionUtils.getStackTrace(ex));
      return null;
    }
  }

  public static String getFilePath(String fileName) {
    try {
      File f = new File(fileName);
      return f.getParent();
    } catch (Exception ex) {
      log.error(ExceptionUtils.getStackTrace(ex));
      return null;
    }
  }

  public static String getFilePath(File file) {
    if (file == null) {
      return null;
    }
    try {
      return file.getParent();
    } catch (Exception ex) {
      log.error(ExceptionUtils.getStackTrace(ex));
      return null;
    }
  }

  /**
   * 获取父级的文件夹。
   * @param filePath   当前文件夹；
   * @param parentRank 父级级别。
   * @return 返回文件夹。
   */
  public static String getParentPath(String filePath, int parentRank) {
    String curPath = filePath;
    if ((parentRank <= 0) || (Tool.isNull(filePath))) {
      return curPath;
    }
    //    String fileSeparator = fileSeparator;
    if (curPath.endsWith(fileSeparator)) {
      curPath = curPath.substring(0, curPath.length() - 1 - fileSeparator.length());
    }
    int index = 0;
    while (parentRank > index) {
      int pos1 = curPath.lastIndexOf(fileSeparator);
      if (pos1 < 0) {
        break;
      } else {
        curPath = curPath.substring(0, pos1);
      }
      index++;
    }
    curPath += fileSeparator;
    return curPath;
  }

  public static String getParentPath(String filePath) {
    return getParentPath(filePath, 1);
  }

  public void clearFileContent(File file) {
    try {
      RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
      randomFile.setLength(0);//
      randomFile.close();
    } catch (FileNotFoundException e) {
      log.error(ExceptionUtils.getStackTrace(e));
    } catch (IOException e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
  }

  // /**
  // * 读取 sd 卡上的文件。
  // * @bean fileName
  // * @return
  // */
  // @SuppressWarnings("unused")
  // private static String readFile(String fileName) {
  // if (Tool.isNull(fileName)) {
  // return null;
  // }
  // String result = null;
  // byte[] data = new byte[1024];
  // int len = 0;
  // FileInputStream fileInputStream = null;
  // ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  // try {
  // fileInputStream = new FileInputStream(fileName);
  // while ((len = fileInputStream.read(data)) != -1) {
  // outputStream.write(data, 0, len);
  // }
  // result = bytesToString(outputStream.toByteArray());
  // } catch (Exception e) {
  // e.printStackTrace();
  // } finally {
  // if (fileInputStream != null) {
  // try {
  // fileInputStream.close();
  // } catch (Exception e) {
  // e.printStackTrace();
  // }
  // }
  // }
  // return result;
  // }

  // /**
  // * @bean fileName
  // * @bean content
  // * @bean append
  // * @return
  // */
  // @SuppressWarnings("unused")
  // private static boolean writeFile(String fileName, String content, boolean append) {
  // if (Tool.isNull(fileName)) {
  // return false;
  // }
  // if (content == null) {
  // content = "";
  // }
  // boolean result = false;
  // FileOutputStream fileOutputStream = null;
  // try {
  // if (forceFilePath(fileName)) {
  // return false;
  // }
  // fileOutputStream = new FileOutputStream(fileName, append);
  // fileOutputStream.write(content.getBytes());
  // result = true;
  // } catch (Exception e) {
  // e.printStackTrace();
  // } finally {
  // if (fileOutputStream != null) {
  // try {
  // fileOutputStream.close();
  // } catch (Exception e) {
  // e.printStackTrace();
  // }
  // }
  // }
  // return result;
  // }

  // 当文本中含有中文时，读取后第一个字符的值，会造成第一个字符为乱码，暂时不知道原因，处理方式为去掉这个字符
  private static String adjustFileContent(String source) {
    String firstString = Tool.subString(source, 0, 1);
    if (Tool.isNull(source)) {
      return source;
    }
    String result = source;
    int code = (int) firstString.charAt(0);
    if (code == UTF8_HEAD_CODE_) {
      result = Tool.subString(source, 1);
    }
    return result;
  }

  // /**
  // * 创建文本文件
  // * @bean fileName 绝对路径
  // * @bean sText
  // * @return
  // */
  // public static boolean createTextFile(String fileName, String sText) {
  // try {
  // if (sText == null) {
  // sText = "";
  // }
  // // sText = toIso(sText);
  // OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileName)); // ,
  // // "GB2312"
  // try {
  // osw.write(sText, 0, sText.length());
  // osw.flush();
  // } catch (IOException e) {
  // log.error(ExceptionUtils.getStackTrace(e));
  // }
  // try {
  // osw.close();
  // return true;
  // } catch (Exception ex) {
  // log.error(ExceptionUtils.getStackTrace(ex));
  // return false;
  // }
  // } catch (Exception e) {
  // log.error(ExceptionUtils.getStackTrace(e));
  // return false;
  // }
  // }

  public static String getFileExt(String fileName) {
    if (fileName == null) {
      return null;
    }
    int index = Tool.lastIndexOf(".", fileName);
    return Tool.subString(fileName, index + 1);
  }

  public static String ridFileExt(String fileName) {
    if (fileName == null) {
      return null;
    }
    int index = Tool.lastIndexOf(".", fileName);
    return Tool.subString(fileName, 0, index);
  }

  /**
   * 创建文件，如果文件已经存在，不创建；如果不存在，创建一个新的文件。
   * @param fileName 文件名。
   * @return true 成功，false 失败。
   */
  public static boolean createFile(String fileName) {
    if (Tool.isNull(fileName)) {
      return false;
    }
    try {
      File f = new File(fileName);
      if (!f.exists()) { // 如果文件不存在
        boolean success = forceFilePath(fileName);
        if (!success) {
          return false;
        }
        f.createNewFile(); // 生成这个文件
      }
      return true;
    } catch (Exception e) {
      log.error("createFile error! fileName = " + fileName, e);
      return false;
    }
  }

  public static boolean deleteFile(String fileName) {
    boolean result = false;
    try {
      File f = new File(fileName);
      if (f.exists()) {
        result = f.delete();
      } else {
        return true;
      }
    } catch (Exception ex) {
      log.error(ExceptionUtils.getStackTrace(ex));
    }
    return result;
  }

  public static boolean deleteFile(File file) {
    if (file == null) {
      return false;
    }
    boolean result = false;

    try {
      if (file.exists()) {
        result = file.delete();
      } else {
        return true;
      }
    } catch (Exception ex) {
      log.error(ExceptionUtils.getStackTrace(ex));
    }
    return result;
  }

  /**
   * 删除文件夹下的所有文件, 删除文件夹自身。
   * @param path 文件夹；
   * @return true 删除成功；false 删除失败。
   */
  public static boolean deletePath(String path) {
    return deleteFiles(path);
  }

  /**
   * 删除文件夹下的所有文件, 删除文件夹自身。
   * @param path 文件夹
   * @return true 删除成功；false 删除失败。
   */
  private static boolean deleteFiles(String path) { //
    if (Tool.isNull(path)) {
      return false;
    }
    File file = new File(path);
    if (!file.exists()) {
      return true;
    } else {
      boolean result = true;
      File[] files = file.listFiles();
      for (File one : files) {
        if (one.isDirectory()) {
          result = deleteFiles(one.getAbsolutePath());
          if (!result) {
            break;
          }
        } else {
          result = deleteFile(one.getAbsolutePath());
          if (!result) {
            break;
          }
        }
      }
      if (!result) {
        return result;
      }
      result = file.delete();
      return result;
    }
  }

  // >>------------------------------------ 文件操作 ------------------------------------

  // <<------------------------------------ 文件复制 ------------------------------------
  // 复制文件
  public static boolean copyFile(File sourceFile, File targetFile) {
    boolean ok = deleteFile(targetFile);
    if (!ok) {
      log.warn("deleteFile error! targetFile: " + targetFile.getName());
      return ok;
    }
    try {
      // 新建文件输入流并对它进行缓冲
      FileInputStream input = new FileInputStream(sourceFile);
      BufferedInputStream inBuff = new BufferedInputStream(input);

      // 新建文件输出流并对它进行缓冲
      FileOutputStream output = new FileOutputStream(targetFile);
      BufferedOutputStream outBuff = new BufferedOutputStream(output);

      // 缓冲数组
      byte[] b = new byte[1024 * 5];
      int len;
      while ((len = inBuff.read(b)) != -1) {
        outBuff.write(b, 0, len);
      }
      // 刷新此缓冲的输出流
      outBuff.flush();

      // 关闭流
      inBuff.close();
      outBuff.close();
      output.close();
      input.close();
      return true;
    } catch (IOException e) {
      log.error("copyFile error! sourceFile = " + sourceFile.getName() + "; targetFile = " + targetFile.getName(), e);
      return false;
    }
  }

  public static boolean copyFile(String sourceFile, String targetFile) {
    if (Tool.isNull(sourceFile)) {
      log.warn("copyFile fail! sourceFile is null!");
      return false;
    }
    if (Tool.isNull(targetFile)) {
      log.warn("copyFile fail! targetFile is null!");
      return false;
    }
    if (!existFile(sourceFile)) {
      log.warn("copyFile fail! sourceFile not exist!");
      return false;
    }
    if (!forceFilePath(targetFile)) {
      log.warn("forceFilePath fail! targetFile: " + targetFile);
      return false;
    }

    File source = new File(sourceFile);
    File target = new File(targetFile);
    return copyFile(source, target);
  }

  // 复制文件夹
  private static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
    // 新建目标目录
    (new File(targetDir)).mkdirs();
    // 获取源文件夹当前下的文件或目录
    File[] file = (new File(sourceDir)).listFiles();
    for (int i = 0; i < file.length; i++) {
      if (file[i].isFile()) {
        // 源文件
        File sourceFile = file[i];
        // 目标文件
        File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
        copyFile(sourceFile, targetFile);
      }
      if (file[i].isDirectory()) {
        // 准备复制的源文件夹
        String dir1 = sourceDir + fileSeparator + file[i].getName();
        // 准备复制的目标文件夹
        String dir2 = targetDir + fileSeparator + file[i].getName();
        copyDirectiory(dir1, dir2);
      }
    }
  }

  /**
   * 将 sourceDir 内的文件或文件夹复制到 targetDir 内，不复制 sourceDir 本身。
   * @param sourceDir 源目录。
   * @param targetDir 目标目录。
   * @return true 成功，false 失败。
   */
  public static boolean copyPath(String sourceDir, String targetDir) {
    if (!existPath(sourceDir)) {
      log.warn("sourceDir not exist! sourceDir = " + sourceDir);
      return false;
    }
    if (Tool.isNull(targetDir)) {
      log.warn("targetDir is null!");
      return false;
    }
    try {
      copyDirectiory(sourceDir, targetDir);
      return true;
    } catch (IOException e) {
      log.error("copyPath error!", e);
      return false;
    }
  }

  // >>------------------------------------ 文件复制 ------------------------------------

  //  /**
  //   * 读取文件并得到文件内容
  //   * @param fileName 文件名
  //   * @param startPos 起始位置
  //   * @param length   长度
  //   * @return
  //   */
  //  public static byte[] readBytes(String fileName, int startPos, int length) {
  //    if (Tool.isNull(fileName)) {
  //      return null;
  //    }
  //    File file = new File(fileName); // 建立一个文件对象
  //    byte[] fileContent = new byte[(length)];
  //    try {
  //      RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
  //      randomAccessFile.seek(startPos);
  //      randomAccessFile.read(fileContent);
  //      randomAccessFile.close();
  //    } catch (Exception e) {
  //      log.error("readBytes error! fileName: " + fileName + "; startPos: " + startPos, e);
  //      return null;
  //    }
  //    return fileContent;
  //  }

  public static void closeFile(BufferedInputStream bufferedInputStream) {
    if (bufferedInputStream == null) {
      log.warn("bufferedInputStream is null!");
      return;
    }
    try {
      bufferedInputStream.close();
    } catch (Exception e) {
      log.error("closeFile error!", e);
      return;
    }
  }

  /**
   * 得到文件块数
   * @param fileName  文件名
   * @param blockSize 块的大小
   * @return int 块的个数
   */
  public static long getFileBlockNumber(String fileName, int blockSize) {
    if (blockSize == 0) {
      return 0;
    }
    long fileSize = getFileSize(fileName);
    return (fileSize + blockSize - 1) / blockSize;
  }

  /**
   * 得到文个的某一块
   * @param fileName   文件名
   * @param indexBlock 第几块从1开如
   * @param blockSize  块大小
   * @return byte[]
   */
  public static byte[] getFileBlock(String fileName, int indexBlock, int blockSize) {
    if (Tool.isNull(fileName)) {
      return null;
    }
    if (blockSize == 0) {
      return null;
    }
    File file = new File(fileName); // 建立一个文件对象
    byte[] block;
    int fileSize = (int) file.length();
    int maxBlock = indexBlock * blockSize;
    if (fileSize < maxBlock) {
      block = new byte[blockSize - (maxBlock - fileSize)];
    } else {
      block = new byte[blockSize];
    }
    try {
      RandomAccessFile rf = new RandomAccessFile(file, "r");
      rf.seek((indexBlock - 1) * blockSize);
      rf.read(block);
      rf.close();
    } catch (FileNotFoundException e) {
      log.error("getFileBlock error! fileName = " + fileName, e);
    } catch (IOException e1) {
      log.error("getFileBlock error! fileName = " + fileName, e1);
    }
    return block;
  }

  /**
   * 获取文件夹下的所有文件。
   * @param path      路径。
   * @param fileNames 返回文件列表。
   */
  public static void getFiles(String path, List<String> fileNames) { //
    if (Tool.isNull(path) || fileNames == null) {
      return;
    }
    File file = new File(path);
    if (!file.exists()) {
      log.warn("Not find path! path = " + path);
    } else {
      File[] files = file.listFiles();
      if (files.length == 0) {
        log.info("Path is empty. path = " + path);
        return;
      } else {
        for (File one : files) {
          if (one.isDirectory()) {
            getFiles(one.getAbsolutePath(), fileNames);
          } else {
            fileNames.add(one.getAbsolutePath());
          }
        }
      }
    }
  }

  /**
   * 获取文件夹下的所有文件。
   * @param path      路径。
   * @param fileNames 返回文件列表。
   * @param ext       扩展名。
   */
  public static void getFiles(String path, List<String> fileNames, String ext) { //
    if (Tool.isNull(path) || fileNames == null) {
      return;
    }
    File file = new File(path);
    if (!file.exists()) {
      log.warn("Not find path! path = " + path);
    } else {
      File[] files = file.listFiles();
      if (files.length == 0) {
        log.info("Path is empty. path = " + path);
        return;
      } else {
        for (File one : files) {
          if (one.isDirectory()) {
            getFiles(one.getAbsolutePath(), fileNames, ext);
          } else {
            String name = one.getAbsolutePath();
            if (ext == null) {
              fileNames.add(name);
            } else {
              if (name.endsWith(ext)) {
                fileNames.add(name);
              }
            }
          }
        }
      }
    }
  }

  /**
   * 获取文件夹下的所有文件。
   * @param path      路径。
   * @param fileNames 返回文件列表。
   * @param ext       扩展名。
   */
  public static void getFiles(File path, List<File> fileNames, String ext) { //
    if (path == null || fileNames == null) {
      return;
    }
    if (!path.exists()) {
      log.warn("Not find path! path = " + path);
    } else {
      File[] files = path.listFiles();
      if (files.length == 0) {
        log.info("Path is empty. path = " + path);
        return;
      } else {
        for (File one : files) {
          if (one.isDirectory()) {
            getFiles(one, fileNames, ext);
          } else {
            String name = one.getAbsolutePath();
            if (ext == null) {
              fileNames.add(one);
            } else {
              if (name.endsWith(ext)) {
                fileNames.add(one);
              }
            }
          }
        }
      }
    }
  }

  /**
   * 获取文件夹下的所有子文件夹。
   * @param path        路径。
   * @param folderNames 只有文件夹名称，不包含路径。
   */
  public static void getFolders(String path, List<String> folderNames) { //
    if (Tool.isNull(path) || folderNames == null) {
      return;
    }
    File file = new File(path);
    if (!file.exists()) {
      log.warn("Not find path! path = " + path);
    } else {
      File[] files = file.listFiles();
      if (files.length == 0) {
        log.info("Path is empty. path = " + path);
        return;
      } else {
        for (File one : files) {
          if (one.isDirectory()) {
            String name = one.getName();
            folderNames.add(name);
          }
        }
      }
    }
  }

  /**
   * 以追加方式写文件
   * @param fileName 文件名
   * @param file     文件内容
   */
  public static void appendTextFile(String fileName, byte[] file) {
    if (Tool.isNull(fileName)) {
      return;
    }
    try {
      File f = new File(fileName);
      if (!(f.exists())) { // 如果文件不存在
        f.createNewFile(); // 生成这个文件
      }
      RandomAccessFile rf = new RandomAccessFile(f, "rw");
      rf.seek(rf.length());
      rf.write(file);
      rf.close();
    } catch (Exception e) {
      log.error("appendTextFile error! fileName = " + fileName, e);
    }
  }

  // 去掉带有BOM的UTF8的开头字符，无BOM时无此值
  private static String ridBomUtf8(String line) {
    if (Tool.isNull(line)) {
      return line;
    }
    if (line.charAt(0) == flag_bom_utf8) {
      line = Tool.subString(line, 1);
    }
    return line;
  }

  /**
   * 连接路径和文件。
   * @param filePath 路径，可以分隔符结尾，也可以没有；
   * @param fileName 文件名称，可以分隔符结尾，也可以没有。
   * @return 返回拼接后的路径及文件名。
   */
  public static String joinFileName(String filePath, String fileName) {
    if (Tool.isNull(filePath)) {
      return fileName;
    }
    if (Tool.isNull(fileName)) {
      return filePath;
    }
    filePath = toLocalPath(filePath);
    if (!Tool.isSuffix(filePath, fileSeparator)) {
      filePath = filePath + fileSeparator;
    }
    if (Tool.isHeader(fileName, fileSeparator)) {
      fileName = Tool.subString(fileName, 1);
    }
    String result = filePath + fileName;

    return result;
  }

  public static String toLocalPath(String filePath) {
    if (filePath == null) {
      return null;
    }
    filePath = filePath.replace('/', File.separatorChar);
    filePath = filePath.replace('\\', File.separatorChar);
    return filePath;
  }

  public static String getRandomFile(String uploadPath, String fileExt) {
    if (!Tool.isHeader(fileExt, ".")) {
      fileExt = "." + fileExt;
    }
    String digit = Tool.getDigitMillis();
    String fileName = digit;
    int index = 0;
    while (true) {
      if (index > 0) {
        if (index <= 9) {
          fileName = digit + Tool.toString(index);
        } else {
          int random = Tool.getRandom(0, 100000);
          fileName = digit + Tool.toString(index) + Tool.toString(random);
        }
      }
      fileName = fileName + fileExt;
      fileName = ToolFile.joinFileName(uploadPath, fileName);
      File file = new File(fileName);
      if (!file.exists()) {
        break;
      }
      index++;
    }
    return fileName;
  }

  // <<----------------------------- crc32 -----------------------------

  public static String getCrc32(File file) {
    CRC32 crc32 = new CRC32();
    FileInputStream fileinputstream = null;
    CheckedInputStream checkedinputstream = null;
    String crc = null;
    if (!file.exists()) {
      return null;
    }
    try {
      fileinputstream = new FileInputStream(file);
      checkedinputstream = new CheckedInputStream(fileinputstream, crc32);
      while (checkedinputstream.read() != -1) {
      }
      crc = Long.toHexString(crc32.getValue()).toUpperCase();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fileinputstream != null) {
        try {
          fileinputstream.close();
        } catch (IOException e2) {
          e2.printStackTrace();
        }
      }
      if (checkedinputstream != null) {
        try {
          checkedinputstream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return crc;
  }

  public static String getCrc32(String fileName) {
    File file = new File(fileName);
    return getCrc32(file);
  }

  // >>----------------------------- crc32 -----------------------------

  // <<----------------------------- private -----------------------------

  // >>----------------------------- private -----------------------------

}
