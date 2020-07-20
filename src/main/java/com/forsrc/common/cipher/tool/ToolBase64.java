package com.forsrc.common.cipher.tool;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Slf4j
public class ToolBase64 {
  public static final String UTF_8 = "UTF-8";
  public static Base64.Encoder encoder;
  public static Base64.Decoder decoder;
  //即为安全的编码方式，替换“+” “/” “-”为“_”
  public static Base64.Encoder urlEncoder;
  public static Base64.Decoder urlDecoder;
  //
  public static Base64.Encoder mimeEncoder;
  public static Base64.Decoder mimeDecoder;

  static {
    encoder = Base64.getEncoder();
    decoder = Base64.getDecoder();
    urlEncoder = Base64.getUrlEncoder();
    urlDecoder = Base64.getUrlDecoder();

    mimeEncoder = Base64.getMimeEncoder();
    mimeDecoder = Base64.getMimeDecoder();
  }

  //encode
  public static String encode(String string) {
    byte[] encode = encodeToBytes(string.getBytes());
    try {
      return new String(encode, UTF_8);
    } catch (UnsupportedEncodingException e) {
      log.error("encode fail!", e);
    }
    return null;
  }

  public static String encode(byte[] bytes) {
    return encoder.encodeToString(bytes);
  }

  public static byte[] encodeToBytes(byte[] bytes) {
    return encoder.encode(bytes);
  }

  public static byte[] encodeToBytes(String string) {
    return encodeToBytes(string.getBytes());
  }

  //decode
  public static String decode(String string) {
    byte[] decode = decodeToBytes(string.getBytes());
    try {
      return new String(decode, UTF_8);
    } catch (UnsupportedEncodingException e) {
      log.error("decode fail!", e);
    }
    return null;
  }

  public static String decode(byte[] bytes) {
    try {
      return new String(decodeToBytes(bytes), UTF_8);
    } catch (UnsupportedEncodingException e) {
      log.error("decode fail!", e);
    }
    return null;
  }

  public static byte[] decodeToBytes(byte[] bytes) {
    return decoder.decode(bytes);
  }

  public static byte[] decodeToBytes(String string) {
    return decodeToBytes(string.getBytes());
  }

  //urlEncoder
  public static String urlEncode(String string) {
    byte[] encode = urlEncodeToBytes(string.getBytes());
    try {
      return new String(encode, UTF_8);
    } catch (UnsupportedEncodingException e) {
      log.error("urlEncode fail!", e);
    }
    return null;
  }

  public static String urlEncode(byte[] bytes) {
    return urlEncoder.encodeToString(bytes);
  }

  public static byte[] urlEncodeToBytes(byte[] bytes) {
    return urlEncoder.encode(bytes);
  }

  public static byte[] urlEncodeToBytes(String string) {
    return urlEncodeToBytes(string.getBytes());
  }

  //urlDecode
  public static String urlDecode(String string) {
    byte[] decode = urlDecodeToBytes(string.getBytes());
    try {
      return new String(decode, UTF_8);
    } catch (UnsupportedEncodingException e) {
      log.error("urlDecode fail!", e);
    }
    return null;
  }

  public static String urlDecode(byte[] bytes) {
    try {
      return new String(urlDecodeToBytes(bytes), UTF_8);
    } catch (UnsupportedEncodingException e) {
      log.error("urlDecode fail!", e);
    }
    return null;
  }

  public static byte[] urlDecodeToBytes(byte[] bytes) {
    return urlDecoder.decode(bytes);
  }

  public static byte[] urlDecodeToBytes(String string) {
    return urlDecodeToBytes(string.getBytes());
  }

  //mimeEncoder
  public static String mimeEncode(String string) {
    byte[] encode = mimeEncodeToBytes(string.getBytes());
    try {
      return new String(encode, UTF_8);
    } catch (UnsupportedEncodingException e) {
      log.error("mimeEncode fail!", e);
    }
    return null;
  }

  public static String mimeEncode(byte[] bytes) {
    return mimeEncoder.encodeToString(bytes);
  }

  public static byte[] mimeEncodeToBytes(byte[] bytes) {
    return mimeEncoder.encode(bytes);
  }

  public static byte[] mimeEncodeToBytes(String string) {
    return mimeEncodeToBytes(string.getBytes());
  }

  //mimeDecode
  public static String mimeDecode(String string) {
    byte[] decode = mimeDecodeToBytes(string.getBytes());
    try {
      return new String(decode, UTF_8);
    } catch (UnsupportedEncodingException e) {
      log.error("mimeDecode fail!", e);
    }
    return null;
  }

  public static String mimeDecode(byte[] bytes) {
    try {
      return new String(mimeDecodeToBytes(bytes), UTF_8);
    } catch (UnsupportedEncodingException e) {
      log.error("mimeDecode fail!", e);
    }
    return null;
  }

  public static byte[] mimeDecodeToBytes(byte[] bytes) {
    return mimeDecoder.decode(bytes);
  }

  public static byte[] mimeDecodeToBytes(String string) {
    return mimeDecodeToBytes(string.getBytes());
  }

}