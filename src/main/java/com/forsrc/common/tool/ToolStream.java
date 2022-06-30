package com.forsrc.common.tool;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

@Data
@Slf4j
public class ToolStream {

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  // >>>----------------------- normal -----------------------

  // <<<----------------------- toBytes -----------------------

  /**
   * stream2byte[]
   * @param input 输入流
   * @return 字节
   * @throws IOException IOException
   */
  public static byte[] toBytes(InputStream input) throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    try {
      byte[] buffer = new byte[4096];
      int n = 0;
      while (-1 != (n = input.read(buffer))) {
        output.write(buffer, 0, n);
      }
      return output.toByteArray();
    } finally {
      close(output, input);
    }
  }

  // >>>----------------------- toBytes -----------------------

  // <<<----------------------- LocalDate -----------------------

  // >>>----------------------- LocalDate -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  // >>>----------------------- normal -----------------------

  // <<<----------------------- tool -----------------------

  /**
   * 关闭流
   * @param outs Closeable
   */
  private static void close(Closeable... outs) {
    if (outs != null) {
      for (Closeable out : outs) {
        if (out != null) {
          try {
            out.close();
          } catch (Exception e) {
            log.error("close", e);
          }
        }
      }
    }
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}