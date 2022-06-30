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
  public static byte[] md5(byte[] source) {
    return encrypt(source, algorithm_md5);
  }

  // md5加密
  public static String md5Str(byte[] source) {
    byte[] bytes = encrypt(source, algorithm_md5);
    return toHex(bytes);
  }

  // 16位md5加密
  public static String md5_16(String inputText) {
    String source = md5(inputText);
    return Tool.subString(source, 0, 16);
  }

  // 16位md5加密
  public static String md5_16(byte[] source) {
    String target = md5Str(source);
    return Tool.subString(target, 0, 16);
  }

  // 16位md5加密
  public static String md5_15(String inputText) {
    String source = md5(inputText);
    return Tool.subString(source, 0, 15);
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

  public static byte[] encryptAes(byte[] plainText, byte[] password) {
    return Aes.encrypt(plainText, password);
  }

  public static byte[] decryptAes(byte[] cripherText, byte[] password) {
    return Aes.decrypt(cripherText, password);
  }

  public static byte[] encryptAes_ecb(byte[] plainText, byte[] password) {
    return Aes.encrypt_ecb(plainText, password);
  }

  public static byte[] decryptAes_ecb(byte[] cripherText, byte[] password) {
    return Aes.decrypt_ecb(cripherText, password);
  }

  public static byte[] encryptAes_ecb_no(byte[] plainText, byte[] password) {
    return Aes.encrypt_ecb_no(plainText, password);
  }

  public static byte[] decryptAes_ecb_no(byte[] cripherText, byte[] password) {
    return Aes.decrypt_ecb_no(cripherText, password);
  }

  public static byte[] encryptAes_cbc(byte[] plainText, byte[] password) {
    return Aes.encrypt_cbc(plainText, password);
  }

  public static byte[] decryptAes_cbc(byte[] cripherText, byte[] password) {
    return Aes.decrypt_cbc(cripherText, password);
  }

  public static byte[] encryptAes_ctr(byte[] plainText, byte[] password) {
    return Aes.encrypt_ctr(plainText, password);
  }

  public static byte[] decryptAes_ctr(byte[] cripherText, byte[] password) {
    return Aes.decrypt_ctr(cripherText, password);
  }

  public static byte[] encryptAes_gcm(byte[] plainText, byte[] password) {
    return Aes.encrypt_gcm(plainText, password);
  }

  public static byte[] decryptAes_gcm(byte[] cripherText, byte[] password) {
    return Aes.decrypt_gcm(cripherText, password);
  }

  //
  public static String encryptAes(String plainText, String password) {
    return Aes.encrypt(plainText, password);
  }

  public static String decryptAes(String cripherText, String password) {
    return Aes.decrypt(cripherText, password);
  }

  public static String encryptAes_ecb(String plainText, String password) {
    return Aes.encrypt_ecb(plainText, password);
  }

  public static String decryptAes_ecb(String cripherText, String password) {
    return Aes.decrypt_ecb(cripherText, password);
  }

  public static String encryptAes_ecb_no(String plainText, String password) {
    return Aes.encrypt_ecb_no(plainText, password);
  }

  public static String decryptAes_ecb_no(String cripherText, String password) {
    return Aes.decrypt_ecb_no(cripherText, password);
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

  public static String encryptAes_gcm(String plainText, String password) {
    return Aes.encrypt_gcm(plainText, password);
  }

  public static String decryptAes_gcm(String cripherText, String password) {
    return Aes.decrypt_gcm(cripherText, password);
  }

  // >>------------------ AES 加密 -------------------------

  /**
   * md5或者sha-1加密
   * @param encryptName 加密算法名称：md5或者sha-1，不区分大小写
   */
  private static byte[] encrypt(byte[] source, String encryptName) {
    if (source == null) {
      return null;
    }
    if (Tool.isNull(encryptName)) {
      encryptName = algorithm_md5;
    }
    try {
      MessageDigest messageDigest = MessageDigest.getInstance(encryptName);
      messageDigest.update(source);
      return messageDigest.digest();
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  /**
   * md5或者sha-1加密
   * @param inputText   要加密的内容
   * @param encryptName 加密算法名称：md5或者sha-1，不区分大小写
   * @param charset     字符集：默认为 utf-8
   * @return
   */
  private static String encrypt(String inputText, String encryptName, String charset) {
    if (Tool.isNull(inputText)) {
      return inputText;
    }
    byte[] source = Tool.toBytes(inputText, charset);
    byte[] bytes = encrypt(source, encryptName);
    return toHex(bytes);
  }

  /**
   * md5或者sha-1加密, 字符集默认为 utf-8
   * @param inputText   要加密的内容
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
