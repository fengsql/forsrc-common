package com.forsrc.common.tool;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.xerial.snappy.Snappy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;

@Slf4j
public class ToolZip {

  private static final String charset_utf8 = "UTF8"; //
  private static final int level_deflate = 9; //0-9
  private static final int buffer_size = 8 * 1024;

  // <<----------------------- public -----------------------

  // <<<----------------------- zip snappy -----------------------

//  public static byte[] zip(byte[] data) {
//    return snappy(data);
//  }
//
//  public static byte[] unzip(byte[] data) {
//    return unSnappy(data);
//  }
//
//  public static String zip(String data) {
//    byte[] bytes = Tool.toBytes(data, charset_utf8);
//    byte[] dest = snappy(bytes);
//    return Tool.toString(dest, charset_utf8);
//  }
//
//  public static String unzip(String data) {
//    byte[] bytes = Tool.toBytes(data, charset_utf8);
//    byte[] dest = unSnappy(bytes);
//    return Tool.toString(dest, charset_utf8);
//  }

  // >>>----------------------- zip snappy -----------------------

  // <<<----------------------- zip deflate -----------------------

  public static byte[] zip(byte[] data) {
    return deflate(data);
  }

  public static byte[] unzip(byte[] data) {
    return unDeflate(data);
  }

  public static String zip(String data) {
    byte[] bytes = Tool.toBytes(data, charset_utf8);
    byte[] dest = deflate(bytes);
    return Tool.toString(dest, charset_utf8);
  }

  public static String unzip(String data) {
    byte[] bytes = Tool.toBytes(data, charset_utf8);
    byte[] dest = unDeflate(bytes);
    return Tool.toString(dest, charset_utf8);
  }

  // >>>----------------------- zip deflate -----------------------

  // <<<----------------------- Snappy -----------------------

  @SneakyThrows
  public static byte[] snappy(byte[] data) {
    return Snappy.compress(data);
  }

  @SneakyThrows
  public static byte[] unSnappy(byte[] data) {
    return Snappy.uncompress(data);
  }

  public static String snappy(String data) {
    byte[] bytes = Tool.toBytes(data, charset_utf8);
    byte[] dest = zip(bytes);
    return Tool.toString(dest, charset_utf8);
  }

  public static String unSnappy(String data) {
    byte[] bytes = Tool.toBytes(data, charset_utf8);
    byte[] dest = unzip(bytes);
    return Tool.toString(dest, charset_utf8);
  }

  // >>>----------------------- Snappy -----------------------

  // <<<----------------------- Deflater -----------------------

  public static byte[] deflate(byte input[]) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Deflater compressor = new Deflater(level_deflate);
    try {
      compressor.setInput(input);
      compressor.finish();
      final byte[] buf = new byte[buffer_size];
      while (!compressor.finished()) {
        int count = compressor.deflate(buf);
        outputStream.write(buf, 0, count);
      }
    } finally {
      compressor.end();
    }
    return outputStream.toByteArray();
  }

  @SneakyThrows
  public static byte[] unDeflate(byte[] input) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Inflater decompressor = new Inflater();
    try {
      decompressor.setInput(input);
      final byte[] buf = new byte[buffer_size];
      while (!decompressor.finished()) {
        int count = decompressor.inflate(buf);
        outputStream.write(buf, 0, count);
      }
    } finally {
      decompressor.end();
    }
    return outputStream.toByteArray();
  }

  public static String deflate(String data) {
    byte[] bytes = Tool.toBytes(data, charset_utf8);
    byte[] dest = deflate(bytes);
    return Tool.toString(dest, charset_utf8);
  }

  public static String unDeflate(String data) {
    byte[] bytes = Tool.toBytes(data, charset_utf8);
    byte[] dest = unDeflate(bytes);
    return Tool.toString(dest, charset_utf8);
  }

  // >>>----------------------- Deflater -----------------------

  // <<<----------------------- file -----------------------

  /**
   * @param srcDir           ?????????????????????
   * @param outFile          ?????????????????????
   * @param KeepDirStructure ?????????????????????????????????,
   *                         true:??????????????????;
   *                         false:???????????????????????????????????????(?????????????????????????????????????????????????????????,???????????????)
   * @throws RuntimeException ????????????????????????????????????
   */
  @SneakyThrows
  public static void zip(String[] srcDir, String outFile, boolean KeepDirStructure) {
    File outputFile = new File(outFile);
    if (!outputFile.getParentFile().exists()) {
      outputFile.getParentFile().mkdirs();
    }
    ZipOutputStream zipOutputStream = null;
    try {
      OutputStream outputStream = new FileOutputStream(outputFile);
      long start = System.currentTimeMillis();

      zipOutputStream = new ZipOutputStream(outputStream);
      List<File> sourceFileList = new ArrayList<>();
      for (String dir : srcDir) {
        File sourceFile = new File(dir);
        sourceFileList.add(sourceFile);
      }
      compress(sourceFileList, zipOutputStream, KeepDirStructure);
      long end = System.currentTimeMillis();
      long ms = end - start;
      log.debug("zip ok. file: {}. cost: {} ms", outFile, ms);
    } finally {
      if (zipOutputStream != null) {
        try {
          zipOutputStream.close();
        } catch (IOException e) {
        }
      }
    }
  }

  /**
   * ????????????????????????????????????????????????????????????
   * @param sourceDir  ????????????
   * @param targetFile ?????????????????????
   */
  public static void zip(String sourceDir, String targetFile) {
    File sourceFile = new File(sourceDir);
    if (!sourceFile.exists()) {
      return;
    }
    String[] srcDir = new String[]{sourceDir};
    zip(srcDir, targetFile, true);
  }

  /**
   * ???????????????????????????????????????????????????????????????
   * @param sourceDir  ????????????
   * @param targetFile ?????????????????????
   */
  public static void zipFile(String sourceDir, String targetFile) {
    File sourceFile = new File(sourceDir);
    if (!sourceFile.exists()) {
      return;
    }
    File[] listFiles = sourceFile.listFiles();
    if (listFiles == null || listFiles.length == 0) {
      return;
    }
    String[] srcDir = new String[listFiles.length];
    int count = listFiles.length;
    for (int i = 0; i < count; i++) {
      File file = listFiles[i];
      srcDir[i] = file.getAbsolutePath();
    }
    zip(srcDir, targetFile, true);
  }

  /**
   * ?????????????????????????????????
   * @param sourceZipFile ??????????????????
   * @param outputPath ???????????????
   * @param rewriteOnExist ???????????????????????????
   */
  @SneakyThrows
  public static void unzip(String sourceZipFile, String outputPath, boolean rewriteOnExist) {
    File inputFile = new File(sourceZipFile);
    if (!inputFile.exists()) {
      throw new RuntimeException("zipFile not exists. zipFile: " + sourceZipFile);
    }
    File outputFile = new File(outputPath);
    if (!outputFile.exists()) {
      outputFile.mkdirs();
    }
    ZipFile zipFile;
    ZipInputStream zipInput = null;
    ZipEntry entry;
    OutputStream output = null;
    InputStream input;
    try {
      zipFile = new ZipFile(inputFile);
      zipInput = new ZipInputStream(new FileInputStream(inputFile));
      String path = outputFile.getAbsolutePath() + File.separator;
      while ((entry = zipInput.getNextEntry()) != null) {
        input = zipFile.getInputStream(entry); // ?????????????????????????????????????????????????????????
        String fileName = path + entry.getName(); // ?????????????????????????????????
        File file = new File(fileName);
        if (file.exists() && !rewriteOnExist) {
          continue;
        }
        newFilePath(file);

        output = new FileOutputStream(file); // ?????????????????????
        write(input, output); // ???????????????????????????

        output.flush();
        output.close();
      }
    } finally {
      try {
        if (output != null) {
          output.close();
        }
        if (zipInput != null) {
          zipInput.close();
        }
      } catch (IOException e) {
      }
    }
  }

  public static void unzip(String sourceZipFile, String outputPath) {
    unzip(sourceZipFile, outputPath, true);
  }

  @SneakyThrows
  public static void unzip(byte[] source, File outputFile, boolean rewriteOnExist) {
    if (source == null) {
      return;
    }
    if (!outputFile.exists()) {
      outputFile.mkdirs();
    }
    ZipInputStream zipInput = null;
    ZipEntry entry;
    OutputStream output = null;
    try {
      zipInput = new ZipInputStream(new ByteArrayInputStream(source));
      String mainPath = outputFile.getAbsolutePath() + File.separator;
      while ((entry = zipInput.getNextEntry()) != null) {
        String fileName = mainPath + entry.getName();
        File file = new File(fileName);
        if (file.exists() && !rewriteOnExist) {
          continue;
        }
        newFilePath(file);

        output = new FileOutputStream(file);
        write(zipInput, output);

        output.flush();
        output.close();
      }
    } finally {
      try {
        if (output != null) {
          output.close();
        }
        if (zipInput != null) {
          zipInput.close();
        }
      } catch (IOException e) {
      }
    }
  }

  public static void unzip(byte[] bytes, String outputPath, boolean rewriteOnExist) {
    File outputFile = new File(outputPath);
    unzip(bytes, outputFile, rewriteOnExist);
  }

  public static void unzip(byte[] bytes, String outputPath) {
    unzip(bytes, outputPath, true);
  }

  // >>>----------------------- file -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- compress -----------------------

  private static void compress(List<File> sourceFileList, ZipOutputStream zipOutputStream, boolean KeepDirStructure) {
    for (File sourceFile : sourceFileList) {
      String name = sourceFile.getName();
      compress(sourceFile, zipOutputStream, name, KeepDirStructure);
    }
  }

  /**
   * ??????????????????
   * @param sourceFile       ?????????
   * @param zipOutputStream  zip?????????
   * @param name             ??????????????????
   * @param KeepDirStructure ?????????????????????????????????,
   *                         true:??????????????????;
   *                         false:???????????????????????????????????????(?????????????????????????????????????????????????????????,???????????????)
   * @throws Exception
   */
  @SneakyThrows
  private static void compress(File sourceFile, ZipOutputStream zipOutputStream, String name, boolean KeepDirStructure) {
    byte[] buf = new byte[buffer_size];
    if (sourceFile.isFile()) {
      zipOutputStream.putNextEntry(new ZipEntry(name));
      int len;
      FileInputStream in = new FileInputStream(sourceFile);
      while ((len = in.read(buf)) != -1) {
        zipOutputStream.write(buf, 0, len);
      }
      zipOutputStream.closeEntry();
      in.close();
    } else {
      File[] listFiles = sourceFile.listFiles();
      if (listFiles == null || listFiles.length == 0) {
        if (KeepDirStructure) {
          zipOutputStream.putNextEntry(new ZipEntry(name + "/"));
          zipOutputStream.closeEntry();
        }
      } else {
        for (File file : listFiles) {
          if (KeepDirStructure) {
            compress(file, zipOutputStream, name + "/" + file.getName(), KeepDirStructure);
          } else {
            compress(file, zipOutputStream, file.getName(), KeepDirStructure);
          }

        }
      }
    }
  }

  // >>>----------------------- compress -----------------------

  // <<<----------------------- tool -----------------------

  @SneakyThrows
  private static void write(InputStream input, OutputStream output) {
    int len;
    byte[] buff = new byte[buffer_size];
    while ((len = input.read(buff)) != -1) {
      output.write(buff, 0, len);
    }
  }

  @SneakyThrows
  private static void newFilePath(File file) {
    if (file.exists()) {
      file.delete();
    }

    File filePath = file.getParentFile(); // ???????????????????????????????????????????????????????????????????????????
    if (!filePath.exists()) {
      filePath.mkdirs();
    }
    file.createNewFile();
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}