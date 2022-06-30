package com.forsrc.common.tool;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ToolConvert {
  private static final Charset charset_utf8 = StandardCharsets.UTF_8;

  private static final int length_byte = 1;
  private static final int length_char = 1;
  private static final int length_short = 2;
  private static final int length_int = 4;
  private static final int length_long = 8;
  private static final int length_double = 8;

  //<<---------------------------------------- initialize ----------------------------------------

  //>>---------------------------------------- initialize ----------------------------------------

  //<<---------------------------------------- public ----------------------------------------

  //<<<---------------------------------------- toShort ----------------------------------------

  //<<<<---------------------------------------- toShort turn ----------------------------------------

  public static byte toByte(byte[] buffer, int index) {
    return bytesToByte(buffer, index);
  }

  public static char toChar(byte[] buffer, int index) {
    return bytesToChar(buffer, index);
  }

  public static short toShort(byte[] buffer, int index, boolean isTurn) {
    if (isTurn) {
      buffer = getAryShort(buffer, index);
      buffer = toTurn(buffer);
    }
    return bytesToShort(buffer, 0);
  }

  public static int toInt(byte[] buffer, int index, boolean isTurn) {
    if (isTurn) {
      buffer = getAryInt(buffer, index);
      buffer = toTurn(buffer);
    }
    return bytesToInt(buffer, 0);
  }

  public static long toLong(byte[] buffer, int index, boolean isTurn) {
    if (isTurn) {
      buffer = getAryLong(buffer, index);
      buffer = toTurn(buffer);
    }
    return bytesToLong(buffer, 0);
  }

  public static double toDouble(byte[] buffer, int index, boolean isTurn) {
    if (isTurn) {
      buffer = getAryDouble(buffer, index);
      buffer = toTurn(buffer);
    }
    return bytesToDouble(buffer, 0);
  }

  //
  public static short toShort(byte[] buffer, boolean isTurn) {
    return toShort(buffer, 0, isTurn);
  }

  public static int toInt(byte[] buffer, boolean isTurn) {
    return toInt(buffer, 0, isTurn);
  }

  public static long toLong(byte[] buffer, boolean isTurn) {
    return toLong(buffer, 0, isTurn);
  }

  public static double toDouble(byte[] buffer, boolean isTurn) {
    return toDouble(buffer, 0, isTurn);
  }

  //>>>>---------------------------------------- toShort turn ----------------------------------------

  //<<<<---------------------------------------- toShort noTurn ----------------------------------------

  public static short toShort(byte[] buffer, int index) {
    return bytesToShort(buffer, index);
  }

  public static int toInt(byte[] buffer, int index) {
    return bytesToInt(buffer, index);
  }

  public static long toLong(byte[] buffer, int index) {
    return bytesToLong(buffer, index);
  }

  public static double toDouble(byte[] buffer, int index) {
    return bytesToDouble(buffer, index);
  }

  //
  public static byte toByte(byte[] buffer) {
    return bytesToByte(buffer, 0);
  }

  public static char toChar(byte[] buffer) {
    return bytesToChar(buffer, 0);
  }

  public static short toShort(byte[] buffer) {
    return bytesToShort(buffer, 0);
  }

  public static int toInt(byte[] buffer) {
    return bytesToInt(buffer, 0);
  }

  public static long toLong(byte[] buffer) {
    return bytesToLong(buffer, 0);
  }

  public static double toDouble(byte[] buffer) {
    return bytesToDouble(buffer, 0);
  }

  public static int toIntege(byte[] buffer) {
    if (buffer == null) {
      return 0;
    }
    int size = buffer.length;
    if (size == 1) {
      return toByte(buffer);
    }
    if (size == 2) {
      return toShort(buffer);
    }
    if (size == 4) {
      return toInt(buffer);
    }
    log.warn("invalid int length! size: " + size);
    return 0;
  }

  //>>>>---------------------------------------- toShort noTurn ----------------------------------------

  //>>>---------------------------------------- toShort ----------------------------------------

  //<<<---------------------------------------- toBytes ----------------------------------------

  public static byte[] toBytes(byte value, boolean isTurn) {
    byte[] buffer = byteToBytes(value);
    if (isTurn) {
      buffer = toTurn(buffer);
    }
    return buffer;
  }

  public static byte[] toBytes(char value, boolean isTurn) {
    byte[] buffer = charToBytes(value);
    if (isTurn) {
      buffer = toTurn(buffer);
    }
    return buffer;
  }

  public static byte[] toBytes(short value, boolean isTurn) {
    byte[] buffer = shortToBytes(value);
    if (isTurn) {
      buffer = toTurn(buffer);
    }
    return buffer;
  }

  public static byte[] toBytes(int value, boolean isTurn) {
    byte[] buffer = intToBytes(value);
    if (isTurn) {
      buffer = toTurn(buffer);
    }
    return buffer;
  }

  public static byte[] toBytes(long value, boolean isTurn) {
    byte[] buffer = longToBytes(value);
    if (isTurn) {
      buffer = toTurn(buffer);
    }
    return buffer;
  }

  public static byte[] toBytes(double value, boolean isTurn) {
    byte[] buffer = doubleToBytes(value);
    if (isTurn) {
      buffer = toTurn(buffer);
    }
    return buffer;
  }

  public static byte[] toBytes(byte value) {
    return byteToBytes(value);
  }

  public static byte[] toBytes(char value) {
    return charToBytes(value);
  }

  public static byte[] toBytes(short value) {
    return shortToBytes(value);
  }

  public static byte[] toBytes(int value) {
    return intToBytes(value);
  }

  public static byte[] toBytes(long value) {
    return longToBytes(value);
  }

  public static byte[] toBytes(double value) {
    return doubleToBytes(value);
  }

  public static byte[] toBytes(String value) {
    return Tool.toBytes(value);
  }

  /**
   * 字节转char数组，此方法转换后无法通过 toBytes 恢复。
   * @param bytes 字节数组
   * @return chars
   */
  public static char[] toChars(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    Charset cs = charset_utf8;
    ByteBuffer bb = ByteBuffer.allocate(bytes.length);
    bb.put(bytes);
    bb.flip();
    CharBuffer cb = cs.decode(bb);
    return cb.array();
  }

  /**
   * 字符数组转成字节数组，此方法转换后无法通过 toChars 恢复。
   * @param chars 字符数组
   * @return 字节数组
   */
  public static byte[] toBytes(char[] chars) {
    if (chars == null) {
      return null;
    }
    Charset cs = charset_utf8;
    CharBuffer cb = CharBuffer.allocate(chars.length);
    cb.put(chars);
    cb.flip();
    ByteBuffer bb = cs.encode(cb);
    return bb.array();
    
//    char[] chars0 = new char[chars.length];
//    System.arraycopy(chars, 0, chars0, 0, chars.length);
//    CharBuffer charBuffer = CharBuffer.wrap(chars0);
//    ByteBuffer byteBuffer = charset_utf8.encode(charBuffer);
//    byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
//    Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
//    Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
//    return bytes;
  }

  //>>>---------------------------------------- toBytes ----------------------------------------

  //<<<---------------------------------------- toHex ----------------------------------------

  public static String toHex(byte value) {
    byte[] bytes = toBytes(value);
    return Tool.bytesToHex(bytes);
  }

  public static String toHex(short value) {
    byte[] bytes = toBytes(value);
    return Tool.bytesToHex(bytes);
  }

  public static String toHex(int value) {
    byte[] bytes = toBytes(value);
    return Tool.bytesToHex(bytes);
  }

  public static String toHex(int value, int size) {
    byte[] bytes = toBytes(value);
    byte[] target = new byte[size];
    int index = bytes.length - size;
    ToolByte.copyArray(bytes, index, target, 0, size);
    return Tool.bytesToHex(target);
  }

  public static String toHex(long value, int size) {
    byte[] bytes = toBytes(value);
    byte[] target = new byte[size];
    int index = bytes.length - size;
    ToolByte.copyArray(bytes, index, target, 0, size);
    return Tool.bytesToHex(target);
  }

  public static String bytesToHex(byte[] src) {
    StringBuilder stringBuilder = new StringBuilder("");
    if (src == null || src.length <= 0) {
      return null;
    }
    for (int i = 0; i < src.length; i++) {
      int val = src[i] & 0xFF;
      String hex = Integer.toHexString(val);
      if (hex.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(hex);
    }
    return stringBuilder.toString();
  }

  /**
   * Convert hex string to byte[]
   * @param hexString the hex string
   * @return byte[]
   */
  public static byte[] hexToBytes(String hexString) {
    if (Tool.isNull(hexString)) {
      return null;
    }
    hexString = hexString.toUpperCase();
    int length = hexString.length() / 2;
    char[] hexChars = hexString.toCharArray();
    byte[] d = new byte[length];
    for (int i = 0; i < length; i++) {
      int pos = i * 2;
      d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
    }
    return d;
  }

  //>>>---------------------------------------- toHex ----------------------------------------

  //<<<---------------------------------------- tool ----------------------------------------

  /**
   * 将data字节型数据转换为0~255 (0xFF 即BYTE)。
   */
  public static int getUnsigned(byte data) { //
    return data & 0xFF;
  }

  /**
   * 将data字节型数据转换为0~65535 (0xFFFF 即 WORD)。
   */
  public static int getUnsigned(short data) { //
    return data & 0xFFFF;
  }

  /**
   * 将int数据转换为0~4294967295 (0xFFFFFFFF即DWORD)。
   */
  public static long getUnsigned(int data) { //
    return data & 0xFFFFFFFFl;
  }

  /**
   * 判断二进制第几位为1。
   * @param source 源。
   * @param pos    源二进制表示的第几位，第一位为0。
   * @return true: 为1；false: 为0.
   */
  public static boolean isOne(int source, int pos) {
    return (byte) (source >> pos & 0x01) == 1;
  }

  public static int getBit(int source, int pos) {
    return (byte) (source >> pos & 0x01);
  }

  public static int getByteHigh(int source) {
    return ((source & 0xf0) >> 4);
  }

  public static int getByteLow(int source) {
    return (source & 0x0f);
  }

  //>>>---------------------------------------- tool ----------------------------------------

  //>>---------------------------------------- public ----------------------------------------

  //<<---------------------------------------- private ----------------------------------------

  //<<<---------------------------------------- getAry ----------------------------------------

  private static byte[] getAryByte(byte[] buffer, int index) {
    byte[] result = new byte[length_byte];
    ToolByte.copyByte(buffer, index, result, 0, length_byte);
    return buffer;
  }

  private static byte[] getAryChar(byte[] buffer, int index) {
    byte[] result = new byte[length_char];
    ToolByte.copyByte(buffer, index, result, 0, length_char);
    return result;
  }

  private static byte[] getAryShort(byte[] buffer, int index) {
    byte[] result = new byte[length_short];
    ToolByte.copyByte(buffer, index, result, 0, length_short);
    return result;
  }

  private static byte[] getAryInt(byte[] buffer, int index) {
    byte[] result = new byte[length_int];
    ToolByte.copyByte(buffer, index, result, 0, length_int);
    return result;
  }

  private static byte[] getAryLong(byte[] buffer, int index) {
    byte[] result = new byte[length_long];
    ToolByte.copyByte(buffer, index, result, 0, length_long);
    return result;
  }

  private static byte[] getAryDouble(byte[] buffer, int index) {
    byte[] result = new byte[length_double];
    ToolByte.copyByte(buffer, index, result, 0, length_double);
    return result;
  }

  //>>>---------------------------------------- getAry ----------------------------------------

  //<<<---------------------------------------- shortToBytes ----------------------------------------

  private static byte[] byteToBytes(byte number) {
    byte[] aryByte = new byte[1];
    aryByte[0] = number;
    return aryByte;
  }

  private static byte[] charToBytes(char ch) {
    int number = (int) ch;
    byte[] b = new byte[2];
    b[0] = (byte) (number >> 8);
    b[1] = (byte) (number);
    return b;
  }

  private static byte[] shortToBytes(short number) {
    byte[] b = new byte[2];
    b[0] = (byte) (number >> 8);
    b[1] = (byte) (number);
    return b;
  }

  private static byte[] intToBytes(int number) {
    byte b[] = new byte[4];
    b[0] = (byte) (number >> 24);
    b[1] = (byte) (number >> 16);
    b[2] = (byte) (number >> 8);
    b[3] = (byte) number;
    return b;
  }

  private static byte[] longToBytes(long number) {
    byte[] b = new byte[8];
    for (int i = 0; i < 8; i++) {
      b[i] = (byte) (number >>> (56 - i * 8));
    }
    return b;
  }

  private static byte[] doubleToBytes(double d) {
    byte[] b = new byte[8];
    long temp = Double.doubleToLongBits(d);
    for (int i = 0; i < b.length; i++) {
      b[i] = new Long(temp).byteValue();
      temp = temp >> 8;
    }
    return b;
  }

  //>>>---------------------------------------- shortToBytes ----------------------------------------

  //<<<---------------------------------------- bytesToShort ----------------------------------------

  private static byte bytesToByte(byte[] b, int index) {
    if (Tool.isNull(b)) {
      return 0;
    }
    return b[index];
  }

  private static char bytesToChar(byte[] b, int index) {
    if (Tool.isNull(b)) {
      return (char) 0;
    }
    try {
      short s = (short) (((b[index] << 8) | b[index + 1] & 0xff));
      return (char) s;
    } catch (Exception e) {
      log.error("bytesToChar Exception", e);
      return (char) 0;
    }
  }

  private static short bytesToShort(byte[] b, int index) {
    if (Tool.isNull(b)) {
      return 0;
    }
    try {
      return (short) (((b[index] << 8) | b[index + 1] & 0xff));
    } catch (Exception e) {
      log.error("bytesToShort Exception", e);
      return 0;
    }
  }

  private static int bytesToInt(byte[] byteValue, int index) {
    if (Tool.isNull(byteValue)) {
      return 0;
    }
    try {
      return byteValue[index + 3] & 0xff | (byteValue[index + 2] & 0xff) << 8 | (byteValue[index + 1] & 0xff) << 16 | (byteValue[index] & 0xff) << 24;
    } catch (Exception e) {
      log.error("bytesToInt Exception", e);
      return 0;
    }
  }

  private static long bytesToLong(byte[] byteValue, int index) {
    if (Tool.isNull(byteValue)) {
      return 0;
    }
    ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.put(byteValue, index, 8);
    buffer.flip();//need flip
    return buffer.getLong();
  }

  private static double bytesToDouble(byte[] b, int index) {
    if (Tool.isNull(b)) {
      return 0;
    }
    try {
      long l;

      l = b[index];
      l &= 0xff;
      l |= ((long) b[index + 1] << 8);
      l &= 0xffff;
      l |= ((long) b[index + 2] << 16);
      l &= 0xffffff;
      l |= ((long) b[index + 3] << 24);
      l &= 0xffffffffl;
      l |= ((long) b[index + 4] << 32);
      l &= 0xffffffffffl;

      l |= ((long) b[index + 5] << 40);
      l &= 0xffffffffffffl;
      l |= ((long) b[index + 6] << 48);
      l &= 0xffffffffffffffl;
      l |= ((long) b[index + 7] << 56);
      return Double.longBitsToDouble(l);
    } catch (Exception e) {
      log.error("bytesToDouble Exception", e);
      return 0;
    }
  }

  //>>>---------------------------------------- bytesToShort ----------------------------------------

  //<<<---------------------------------------- tool ----------------------------------------

  /**
   * 将整型转化为n位的字节类型
   * @param source 要转化的数字
   */
  private static byte[] toTurn(byte[] source) {
    if (source == null) {
      return null;
    }
    byte[] target = new byte[source.length];
    for (int i = 0; i < source.length; i++) {
      target[i] = source[source.length - i - 1];
    }
    return target;
  }

  /**
   * Convert char to byte
   * @param c char
   * @return byte
   */
  private static byte charToByte(char c) {
    return (byte) "0123456789ABCDEF".indexOf(c);
  }

  //>>>---------------------------------------- tool ----------------------------------------

  //>>---------------------------------------- private ----------------------------------------

}
