package com.forsrc.common.cipher.algorithm;

import com.forsrc.common.tool.Tool;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * AES Coder<br/>
 * secret key length: 128bit, default: 128 bit<br/>
 * mode: ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/
 * @author fengsql
 */

@SuppressWarnings("unused")
@Slf4j
public class Aes {
  private static final int keySize = 128; // = AES/ECB/PKCS5Padding
  // 加密模式
  private static final String mode_ECB = "ECB"; // 基础的加密方式
  private static final String mode_CFB = "CFB"; // 实际上是一种反馈模式，目的也是增强破解的难度
  private static final String mode_OFB = "OFB"; // 实际上是一种反馈模式，目的也是增强破解的难度
  private static final String mode_CBC = "CBC"; // 是一种循环模式，前一个分组的密文和当前分组的明文异或操作后再加密，这样做的目的是增强破解难度
  private static final String mode_CTR = "CTR"; // 无填充，同明文不同密文，每个块单独运算，适合并行运算。

  private static final String mode_PCBC = "PCBC"; //

  private static final String padding_NoPadding = "NoPadding"; //
  private static final String padding_PKCS5Padding = "PKCS5Padding"; //
  private static final String padding_ISO10126Padding = "ISO10126Padding"; //

  private static final String cipherType_default = "AES"; // = AES/ECB/PKCS5Padding

  private static final String cipherType_ecb = "AES/ECB/PKCS5Padding"; //
  private static final String cipherType_ecb_no = "AES/ECB/NoPadding"; //不加盐，明文与密文长度一致，但明文长度必须16倍数
  private static final String cipherType_cbc = "AES/CBC/PKCS5Padding";
  private static final String cipherType_ctr = "AES/CTR/NoPadding";

  private static String ivParameter = "jM9xD&5a%$d2Z0`@";

  public Aes() {

  }

  // <<------------------------- default -----------------------------
  public static String encrypt(String plainText, String password) {
    if (Tool.isNull(plainText)) {
      return plainText;
    }
    if (Tool.isNull(password)) {
      return plainText;
    }
    return encrypt_ctr(plainText, password);
  }

  public static String decrypt(String cripherText, String password) {
    if (Tool.isNull(cripherText)) {
      return cripherText;
    }
    if (Tool.isNull(password)) {
      return cripherText;
    }
    return decrypt_ctr(cripherText, password);
  }

  // >>------------------------- default -----------------------------

  // <<------------------------- ecb -----------------------------
  public static String encrypt_ecb(String plainText, String password) {
    if (Tool.isNull(plainText)) {
      return plainText;
    }
    if (Tool.isNull(password)) {
      return plainText;
    }
    byte[] encrypt = encryptAes_ecb(plainText, password);
    return Tool.bytesToHex(encrypt);
  }

  public static String decrypt_ecb(String cripherText, String password) {
    if (Tool.isNull(cripherText)) {
      return cripherText;
    }
    if (Tool.isNull(password)) {
      return cripherText;
    }
    byte[] input = Tool.hexToBytes(cripherText);
    if (input == null) {
      return null;
    }
    byte[] decrypt = decryptAes_ecb(input, password);
    return Tool.toString(decrypt);
  }

  private static byte[] encryptAes_ecb(String plainText, String password) {
    String cipherType = cipherType_ecb;
    return encryptAes_ecb(cipherType, plainText, password);
  }

  private static byte[] decryptAes_ecb(byte[] input, String password) {
    String cipherType = cipherType_ecb;
    return decryptAes_ecb(cipherType, input, password);
  }

  private static byte[] encryptAes_ecb(String plainText, byte[] password) {
    String cipherType = cipherType_ecb;
    return encryptAes_ecb(cipherType, plainText, password);
  }

  private static byte[] decryptAes_ecb(byte[] input, byte[] password) {
    String cipherType = cipherType_ecb;
    return decryptAes_ecb(cipherType, input, password);
  }

  // >>------------------------- ecb -----------------------------

  // <<------------------------- ecb_no -----------------------------
//  public static String encrypt_ecb_no(String plainText, byte[] password) {
//    if (Tool.isNull(plainText)) {
//      return plainText;
//    }
//    if (Tool.isNull(password)) {
//      return plainText;
//    }
//    byte[] encrypt = encryptAes_ecb_no(plainText, password);
//    return Tool.bytesToHex(encrypt);
//  }
//
//  public static String decrypt_ecb_no(String cripherText, byte[] password) {
//    if (Tool.isNull(cripherText)) {
//      return cripherText;
//    }
//    if (Tool.isNull(password)) {
//      return cripherText;
//    }
//    byte[] input = Tool.hexToBytes(cripherText);
//    if (input == null) {
//      return null;
//    }
//    byte[] decrypt = decryptAes_ecb_no(input, password);
//    return Tool.toString(decrypt);
//  }

  public static byte[] encryptAes_ecb_no(byte[] plainText, byte[] password) {
    String cipherType = cipherType_ecb_no;
    return encryptAes_ecb(cipherType, plainText, password);
  }

  public static byte[] decryptAes_ecb_no(byte[] input, byte[] password) {
    String cipherType = cipherType_ecb_no;
    return decryptAes_ecb(cipherType, input, password);
  }

  // >>------------------------- ecb_no -----------------------------

  // <<------------------------- cbc -----------------------------
  public static String encrypt_cbc(String plainText, String password) {
    if (Tool.isNull(plainText)) {
      return plainText;
    }
    if (Tool.isNull(password)) {
      return plainText;
    }
    byte[] encrypt = encryptAes_cbc(plainText, password);
    return Tool.bytesToHex(encrypt);
  }

  public static String decrypt_cbc(String cripherText, String password) {
    if (Tool.isNull(cripherText)) {
      return cripherText;
    }
    if (Tool.isNull(password)) {
      return cripherText;
    }
    byte[] input = Tool.hexToBytes(cripherText);
    if (input == null) {
      return null;
    }
    byte[] decrypt = decryptAes_cbc(input, password);
    return Tool.toString(decrypt);
  }

  private static byte[] encryptAes_cbc(String plainText, String password) {
    String cipherType = cipherType_cbc;
    return encryptAes_ivParameter(cipherType, plainText, password);
  }

  private static byte[] decryptAes_cbc(byte[] input, String password) {
    String cipherType = cipherType_cbc;
    return decryptAes_ivParameter(cipherType, input, password);
  }

  // >>------------------------- cbc -----------------------------

  // <<------------------------- ctr -----------------------------
  public static String encrypt_ctr(String plainText, String password) {
    if (Tool.isNull(plainText)) {
      return plainText;
    }
    if (Tool.isNull(password)) {
      return plainText;
    }
    byte[] encrypt = encryptAes_ctr(plainText, password);
    return Tool.bytesToHex(encrypt);
  }

  public static String decrypt_ctr(String cripherText, String password) {
    if (Tool.isNull(cripherText)) {
      return cripherText;
    }
    if (Tool.isNull(password)) {
      return cripherText;
    }
    byte[] input = Tool.hexToBytes(cripherText);
    if (input == null) {
      return null;
    }
    byte[] decrypt = decryptAes_ctr(input, password);
    return Tool.toString(decrypt);
  }

  private static byte[] encryptAes_ctr(String plainText, String password) {
    String cipherType = cipherType_ctr;
    return encryptAes_ivParameter(cipherType, plainText, password);
  }

  private static byte[] decryptAes_ctr(byte[] input, String password) {
    String cipherType = cipherType_ctr;
    return decryptAes_ivParameter(cipherType, input, password);
  }

  // >>------------------------- ctr -----------------------------

  // <<------------------------- cipher -----------------------------

  private static byte[] encryptAes_ecb(String cipherType, byte[] plainText, String password) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      result = cipher.doFinal(plainText);
    } catch (Exception e) {
      log.error("encrypt error!", e);
    }
    return result;
  }

  private static byte[] encryptAes_ecb(String cipherType, String plainText, String password) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] input = plainText.getBytes();
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("encrypt error!", e);
    }
    return result;
  }

  private static byte[] encryptAes_ecb(String cipherType, String plainText, byte[] password) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] input = plainText.getBytes();
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("encrypt error!", e);
    }
    return result;
  }

  private static byte[] encryptAes_ecb(String cipherType, byte[] plainText, byte[] password) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      SecretKeySpec keySpec = new SecretKeySpec(password, "AES");
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] input = plainText;
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("encrypt error!", e);
    }
    return result;
  }

  private static byte[] decryptAes_ecb(String cipherType, byte[] input, String password) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.DECRYPT_MODE, key);
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("decrypt error!", e);
    }
    return result;
  }

  private static byte[] decryptAes_ecb(String cipherType, byte[] input, byte[] password) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.DECRYPT_MODE, key);
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("decrypt error!", e);
    }
    return result;
  }

  private static byte[] encryptAes_ivParameter(String cipherType, String plainText, String password) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      Cipher cipher = Cipher.getInstance(cipherType);
      IvParameterSpec ivParameterSpec = createIvParameter(); // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
      cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
      byte[] input = plainText.getBytes();
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("encrypt error!", e);
    }
    return result;
  }

  private static byte[] decryptAes_ivParameter(String cipherType, byte[] input, String password) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      Cipher cipher = Cipher.getInstance(cipherType);
      IvParameterSpec ivParameterSpec = createIvParameter(); // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
      cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("decrypt error!", e);
    }
    return result;
  }

  // >>------------------------- cipher -----------------------------

  private static IvParameterSpec createIvParameter() {
    return new IvParameterSpec(ivParameter.getBytes());
  }

  private static Key generateKey(String key) throws Exception {
//    if (Tool.isWindows()) { // win
      try {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        return keySpec;
      } catch (Exception e) {
        throw e;
      }
//    } else { // linux
//      try {
//        KeyGenerator generator = KeyGenerator.getInstance("AES");
//        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//        secureRandom.setSeed(key.getBytes());
//        generator.init(keySize, secureRandom);
//        SecretKey secretKey = generator.generateKey();
//
//        byte[] enCodeFormat = secretKey.getEncoded();
//        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
//        return secretKeySpec;
//      } catch (Exception e) {
//        throw new RuntimeException(" 初始化密钥出现异常 ");
//      }
//    }
  }

  private static Key generateKey(byte[] key) throws Exception {
//    if (Tool.isWindows()) { // win
      try {
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        return keySpec;
      } catch (Exception e) {
        throw e;
      }
//    } else { // linux
//      try {
//        KeyGenerator generator = KeyGenerator.getInstance("AES");
//        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//        secureRandom.setSeed(key);
//        generator.init(keySize, secureRandom);
//        SecretKey secretKey = generator.generateKey();
//
//        byte[] enCodeFormat = secretKey.getEncoded();
//        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
//        return secretKeySpec;
//      } catch (Exception e) {
//        throw new RuntimeException(" 初始化密钥出现异常 ");
//      }
//    }
  }
}
