package com.forsrc.common.cipher.algorithm;

import com.forsrc.common.tool.Tool;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

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
  private static final Charset charset_utf8 = StandardCharsets.UTF_8;
  // 加密模式，对于顺序流使用CBC（和其他顺序模式），对随机访问使用ECB，并行使用CTR
  // 如果数据通过非对称签名进行身份验证,则使用CBC,否则使用GCM
  // GCM是一种非常快速但可以说是复杂的CTR模式和GHASH的组合，是目前使用最广泛的模式
  // 密码 password 长度只能是 16、25 或 32 字节
  private static final String mode_ECB = "ECB"; // 基础的加密方式，同明文同密文，其余模式都是同明文不同密文，有利于并行计算，适于加密小消息，不能隐藏明文的模式
  private static final String mode_CFB = "CFB"; // 实际上是一种反馈模式，唯一的IV，不适合并行
  private static final String mode_OFB = "OFB"; // 实际上是一种反馈模式，需要引入IV参数，不适合并行
  private static final String mode_CBC = "CBC"; // 一种循环模式，需要引入IV参数，不适合并行
  private static final String mode_CTR = "CTR"; // 无填充，需要引入IV参数，同明文不同密文，每个块单独运算，适合并行运算
  private static final String mode_GCM = "GCM"; // GCM是一种非常快速但可以说是复杂的CTR模式和GHASH的组合，是目前使用最广泛的模式，适合并行运算

  private static final String mode_PCBC = "PCBC"; //

  private static final int gcm_taglength = 128; //authentication tag 比特位数，必须是 128、120、112、104、96 之一

  private static final String padding_NoPadding = "NoPadding"; //
  private static final String padding_PKCS5Padding = "PKCS5Padding"; //
  private static final String padding_ISO10126Padding = "ISO10126Padding"; //

  private static final String cipherType_default = "AES"; // = AES/ECB/PKCS5Padding
  // 需要Padding的有：CBC（，PCBC也需要，本文未涉及该加密模式）、ECB。不需要Padding的有：CFB、OFB、CTR。
  private static final String cipherType_ecb = "AES/ECB/PKCS5Padding"; //
  private static final String cipherType_ecb_no = "AES/ECB/NoPadding"; //不加盐，明文与密文长度一致，但明文长度必须16倍数
  private static final String cipherType_cbc = "AES/CBC/PKCS5Padding";
  private static final String cipherType_ctr = "AES/CTR/NoPadding";
  private static final String cipherType_gcm = "AES/GCM/NoPadding";

  private static final String ivParameter = "jM9xD&5a%$d2Z0`@"; //字节数必须为 16 字节、24 字节或 32 字节
  private static final String aadParameter = "?@M-#&*S^s@!6F7j9^U4z4#R$]Wc3_+"; //长度无限制，可为空

  public Aes() {

  }

  // <<------------------------- default -----------------------------

  /**
   * 无填充，需要引入IV参数，同明文不同密文，每个块单独运算，适合并行运算
   */
  public static byte[] encrypt(byte[] input, byte[] password) {
    return encrypt_ctr(input, password);
  }

  public static byte[] decrypt(byte[] input, byte[] password) {
    return decrypt_ctr(input, password);
  }

  public static String encrypt(String plainText, String password) {
    return encrypt_ctr(plainText, password);
  }

  public static String decrypt(String cripherText, String password) {
    return decrypt_ctr(cripherText, password);
  }

  // >>------------------------- default -----------------------------

  // <<------------------------- ecb -----------------------------

  /**
   * 基础的加密方式，同明文同密文，其余模式都是同明文不同密文，有利于并行计算，适于加密小消息，不适合随机加密，不能隐藏明文的模式
   */
  public static byte[] encrypt_ecb(byte[] input, byte[] password) {
    return encryptAes_noiv(cipherType_ecb, input, password);
  }

  public static byte[] decrypt_ecb(byte[] input, byte[] password) {
    return decryptAes_noiv(cipherType_ecb, input, password);
  }

  public static String encrypt_ecb(String input, String password) {
    return encryptAes_noiv(cipherType_ecb, input, password);
  }

  public static String decrypt_ecb(String input, String password) {
    return decryptAes_noiv(cipherType_ecb, input, password);
  }

  // >>------------------------- ecb -----------------------------

  // <<------------------------- ecb_no -----------------------------

  /**
   * 不加盐，明文与密文长度一致，但明文长度必须16倍数，有利于并行计算，适于加密小消息，不适合随机加密，不能隐藏明文的模式
   */
  public static byte[] encrypt_ecb_no(byte[] input, byte[] password) {
    return encryptAes_noiv(cipherType_ecb_no, input, password);
  }

  public static byte[] decrypt_ecb_no(byte[] input, byte[] password) {
    return decryptAes_noiv(cipherType_ecb_no, input, password);
  }

  public static String encrypt_ecb_no(String input, String password) {
    return encryptAes_noiv(cipherType_ecb_no, input, password);
  }

  public static String decrypt_ecb_no(String input, String password) {
    return decryptAes_noiv(cipherType_ecb_no, input, password);
  }

  // >>------------------------- ecb_no -----------------------------

  // <<------------------------- cbc -----------------------------

  /**
   * 安全性好于ECB，适合传输长度长的报文，需要引入IV参数，不适合并行
   */
  public static byte[] encrypt_cbc(byte[] input, byte[] password) {
    return encryptAes_ivParameter(cipherType_cbc, input, password);
  }

  public static byte[] decrypt_cbc(byte[] input, byte[] password) {
    return decryptAes_ivParameter(cipherType_cbc, input, password);
  }

  public static String encrypt_cbc(String input, String password) {
    return encryptAes_ivParameter(cipherType_cbc, input, password);
  }

  public static String decrypt_cbc(String input, String password) {
    return decryptAes_ivParameter(cipherType_cbc, input, password);
  }

  // >>------------------------- cbc -----------------------------

  // <<------------------------- ctr -----------------------------

  public static byte[] encrypt_ctr(byte[] input, byte[] password) {
    return encryptAes_ivParameter(cipherType_ctr, input, password);
  }

  public static byte[] decrypt_ctr(byte[] input, byte[] password) {
    return decryptAes_ivParameter(cipherType_ctr, input, password);
  }

  public static String encrypt_ctr(String input, String password) {
    return encryptAes_ivParameter(cipherType_ctr, input, password);
  }

  public static String decrypt_ctr(String input, String password) {
    return decryptAes_ivParameter(cipherType_ctr, input, password);
  }

  // >>------------------------- ctr -----------------------------

  // <<------------------------- gcm -----------------------------

  public static byte[] encrypt_gcm(byte[] input, byte[] password) {
    return encryptAes_aad(cipherType_gcm, input, password, toBytes(aadParameter));
  }

  public static byte[] decrypt_gcm(byte[] input, byte[] password) {
    return decryptAes_aad(cipherType_gcm, input, password, toBytes(aadParameter));
  }

  public static String encrypt_gcm(String input, String password) {
    return encryptAes_aad(cipherType_gcm, input, password, aadParameter);
  }

  public static String decrypt_gcm(String input, String password) {
    return decryptAes_aad(cipherType_gcm, input, password, aadParameter);
  }

  // >>------------------------- gcm -----------------------------

  // <<------------------------- cipher -----------------------------
  // bytes
  private static byte[] encryptAes_noiv(String cipherType, byte[] input, byte[] password) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("encrypt error!", e);
    }
    return result;
  }

  private static byte[] decryptAes_noiv(String cipherType, byte[] input, byte[] password) {
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

  // Hex
  public static String encryptAes_noiv(String cipherType, String inputText, String password) {
    if (Tool.isNull(inputText)) {
      return inputText;
    }
    if (Tool.isNull(password)) {
      return inputText;
    }
    byte[] input = strToBytes(inputText);
    if (input == null) {
      return inputText;
    }
    byte[] pwd = toBytes(password);
    byte[] encrypt = encryptAes_noiv(cipherType, input, pwd);
    return bytesToStr(encrypt);
  }

  public static String decryptAes_noiv(String cipherType, String inputText, String password) {
    if (Tool.isNull(inputText)) {
      return inputText;
    }
    if (Tool.isNull(password)) {
      return inputText;
    }
    byte[] input = strToBytes(inputText);
    if (input == null) {
      return null;
    }
    byte[] pwd = toBytes(password);
    byte[] decrypt = decryptAes_noiv(cipherType, input, pwd);
    return bytesToStr(decrypt);
  }

  // bytes
  private static byte[] encryptAes_ivParameter(String cipherType, byte[] input, byte[] password) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      IvParameterSpec ivParameterSpec = createIvParameter(); // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("encrypt error!", e);
    }
    return result;
  }

  private static byte[] decryptAes_ivParameter(String cipherType, byte[] input, byte[] password) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      IvParameterSpec ivParameterSpec = createIvParameter(); // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("decrypt error!", e);
    }
    return result;
  }

  private static String encryptAes_ivParameter(String cipherType, String inputText, String password) {
    if (Tool.isNull(inputText)) {
      return inputText;
    }
    if (Tool.isNull(password)) {
      return inputText;
    }
    byte[] input = strToBytes(inputText);
    if (input == null) {
      return inputText;
    }
    byte[] pwd = toBytes(password);
    byte[] encrypt = encryptAes_ivParameter(cipherType, input, pwd);
    return bytesToStr(encrypt);
  }

  private static String decryptAes_ivParameter(String cipherType, String inputText, String password) {
    if (Tool.isNull(inputText)) {
      return inputText;
    }
    if (Tool.isNull(password)) {
      return inputText;
    }
    byte[] input = strToBytes(inputText);
    if (input == null) {
      return null;
    }
    byte[] pwd = toBytes(password);
    byte[] decrypt = decryptAes_ivParameter(cipherType, input, pwd);
    return bytesToStr(decrypt);
  }

  /**
   * @param aad AAD 长度无限制，可为空
   */
  private static byte[] encryptAes_aad(String cipherType, byte[] input, byte[] password, byte[] aad) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      IvParameterSpec ivParameterSpec = createIvParameter();
      AlgorithmParameterSpec parameter = getGCMParameterSpec(gcm_taglength);
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.ENCRYPT_MODE, key, parameter);
      if (aad != null) {
        cipher.updateAAD(aad);
      }
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("encrypt error!", e);
    }
    return result;
  }

  private static byte[] decryptAes_aad(String cipherType, byte[] input, byte[] password, byte[] aad) {
    byte[] result = null;
    try {
      Key key = generateKey(password);
      IvParameterSpec ivParameterSpec = createIvParameter();
      AlgorithmParameterSpec parameter = getGCMParameterSpec(gcm_taglength);
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.DECRYPT_MODE, key, parameter);
      if (aad != null) {
        cipher.updateAAD(aad);
      }
      result = cipher.doFinal(input);
    } catch (Exception e) {
      log.error("decrypt error!", e);
    }
    return result;
  }

  private static String encryptAes_aad(String cipherType, String inputText, String password, String aadText) {
    if (Tool.isNull(inputText)) {
      return inputText;
    }
    if (Tool.isNull(password)) {
      return inputText;
    }
    byte[] input = strToBytes(inputText);
    if (input == null) {
      return inputText;
    }
    byte[] pwd = toBytes(password);
    byte[] aad = toBytes(aadText);
    byte[] encrypt = encryptAes_aad(cipherType, input, pwd, aad);
    return bytesToStr(encrypt);
  }

  private static String decryptAes_aad(String cipherType, String inputText, String password, String aadText) {
    if (Tool.isNull(inputText)) {
      return inputText;
    }
    if (Tool.isNull(password)) {
      return inputText;
    }
    byte[] input = strToBytes(inputText);
    if (input == null) {
      return null;
    }
    byte[] pwd = toBytes(password);
    byte[] aad = toBytes(aadText);
    byte[] decrypt = decryptAes_aad(cipherType, input, pwd, aad);
    return bytesToStr(decrypt);
  }

  // >>------------------------- cipher -----------------------------

  private static IvParameterSpec createIvParameter() {
    return new IvParameterSpec(ivParameter.getBytes());
  }

  private static Key generateKey(byte[] key) throws Exception {
    //    if (Tool.isWindows()) { // win
    return new SecretKeySpec(key, "AES");
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

  /**
   * @param tagLength authentication tag 比特位数，必须是 128、120、112、104、96 之一
   */
  private static GCMParameterSpec getGCMParameterSpec(int tagLength) {
    return new GCMParameterSpec(tagLength, ivParameter.getBytes());
  }

  private static byte[] toBytes(String source) {
    return source.getBytes(charset_utf8);
  }

  private static byte[] strToBytes(String source) {
    return Tool.hexToBytes(source);
  }

  private static String bytesToStr(byte[] source) {
    return Tool.bytesToHex(source);
  }

}
