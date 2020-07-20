package com.forsrc.common.cipher.tool;

import com.forsrc.common.cipher.algorithm.Aes;
import com.forsrc.common.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.security.MessageDigest;

@Slf4j
public class ToolCipher {

  private static final String algorithm_md5 = "MD5";
  private static final String algorithm_sha1 = "SHA-1";
  private static final String charset_default = "UTF-8";

  // md5加密
  public static String md5(String inputText, String charset) {
    return encrypt(inputText, algorithm_md5, charset);
  }

  // md5加密
  public static String md5(String inputText) {
    return encrypt(inputText, algorithm_md5);
  }

  // md5加密
  public static String md5(byte[] source) {
    return encrypt(source, algorithm_md5);
  }

  // 16位md5加密
  public static String md5_16(String inputText) {
    String source = md5(inputText);
    String result = Tool.subString(source, 0, 16);
    return result;
  }

  // 16位md5加密
  public static String md5_16(byte[] source) {
    String target = md5(source);
    String result = Tool.subString(target, 0, 16);
    return result;
  }

  // 16位md5加密
  public static String md5_15(String inputText) {
    String source = md5(inputText);
    String result = Tool.subString(source, 0, 15);
    return result;
  }

  // sha加密
  public static String sha1(String inputText, String charset) {
    return encrypt(inputText, algorithm_sha1, charset);
  }

  // sha加密
  public static String sha1(String inputText) {
    return encrypt(inputText, algorithm_sha1);
  }

  // <<------------------ AES 加密 -------------------------

  public static String encryptAes(String plainText, String password) {
    return Aes.encrypt(plainText, password);
  }

  public static String decryptAes(String cripherText, String password) {
    return Aes.decrypt(cripherText, password);
  }

  public static String encryptAes_cbc(String plainText, String password) {
    return Aes.encrypt_cbc(plainText, password);
  }

  public static String decryptAes_cbc(String cripherText, String password) {
    return Aes.decrypt_cbc(cripherText, password);
  }

  public static String encryptAes_ctr(String plainText, String password) {
    return Aes.encrypt_ctr(plainText, password);
  }

  public static String decryptAes_ctr(String cripherText, String password) {
    return Aes.decrypt_ctr(cripherText, password);
  }

  // >>------------------ AES 加密 -------------------------

  /**
   * md5或者sha-1加密
   * @param encryptName 加密算法名称：md5或者sha-1，不区分大小写
   * @return
   */
  private static String encrypt(byte[] source, String encryptName) {
    if (source == null) {
      return null;
    }
    if (Tool.isNull(encryptName)) {
      encryptName = algorithm_md5;
    }
    try {
      MessageDigest messageDigest = MessageDigest.getInstance(encryptName);
      messageDigest.update(source);
      byte[] bytes = messageDigest.digest();
      return toHex(bytes);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  /**
   * md5或者sha-1加密
   * @param inputText 要加密的内容
   * @param encryptName 加密算法名称：md5或者sha-1，不区分大小写
   * @param charset 字符集：默认为 utf-8
   * @return
   */
  private static String encrypt(String inputText, String encryptName, String charset) {
    if (Tool.isNull(inputText)) {
      return inputText;
    }
    byte[] source = Tool.toBytes(inputText, charset);
    return encrypt(source, encryptName);
  }

  /**
   * md5或者sha-1加密, 字符集默认为 utf-8
   * @param inputText 要加密的内容
   * @param encryptName 加密算法名称：md5或者sha-1，不区分大小写
   * @return
   */
  private static String encrypt(String inputText, String encryptName) {
    String charset = charset_default;
    return encrypt(inputText, encryptName, charset);
  }

  /**
   * 返回十六进制字符串
   * @param bytes
   * @return
   */
  private static String toHex(byte[] bytes) {
    if (Tool.isNull(bytes)) {
      return null;
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < bytes.length; ++i) {
      stringBuilder.append(Integer.toHexString((bytes[i] & 0xFF) | 0x100).substring(1, 3));
    }
    return stringBuilder.toString().toUpperCase();
  }

}
