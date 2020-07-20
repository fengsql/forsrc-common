package com.forsrc.common.tool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ToolByte {

  private static final int size_dump = 256;

  // <<---------------------------------------- public ----------------------------------------

  //<<<---------------------------------------- read ByteBuf ----------------------------------------

  public static byte readByte(ByteBuf byteBuf, int index) {
    return byteBuf.readByte();
  }

  public static int readUnsignedByte(ByteBuf byteBuf) {
    return byteBuf.readUnsignedByte();
  }

  public static short readShort(ByteBuf byteBuf) {
    return byteBuf.readShort();
  }

  public static int readUnsignedShort(ByteBuf byteBuf) {
    return byteBuf.readUnsignedShort();
  }

  public static int readInt(ByteBuf byteBuf) {
    return byteBuf.readInt();
  }

  public static long readUnsignedInt(ByteBuf byteBuf) {
    return byteBuf.readUnsignedInt();
  }

  public static long readLong(byte[] source, int index) {
    return ToolConvert.toLong(source, index);
  }

  public static double readDouble(byte[] source, int index) {
    return ToolConvert.toDouble(source, index);
  }

  public static byte[] readBytes(ByteBuf byteBuf, int length) {
    if (byteBuf == null || length <= 0) {
      log.warn("byteBuf is null or length is 0!");
      return null;
    }
    if (byteBuf.readableBytes() < length) {
      log.warn("readableBytes < " + length + ";");
      return null;
    }
    byte[] bytes = new byte[length];
    byteBuf.readBytes(bytes, 0, length);
    return bytes;
  }

  public static String readString(ByteBuf byteBuf, int length) {
    byte[] bytes = readBytes(byteBuf, length);
    if (bytes == null) {
      return null;
    }
    return toString(bytes);
  }

  //>>>---------------------------------------- read ByteBuf ----------------------------------------

  //<<<---------------------------------------- read byte[] ----------------------------------------

  public static int readByte(byte[] source, int index) {
    return ToolConvert.getUnsigned(ToolConvert.toByte(source, index));
  }

  public static int readUnsignedByte(byte[] source, int index) {
    return ToolConvert.getUnsigned(ToolConvert.toByte(source, index));
  }

  public static short readShort(byte[] source, int index) {
    return ToolConvert.toShort(source, index);
  }

  public static int readUnsignedShort(byte[] source, int index) {
    return ToolConvert.getUnsigned(ToolConvert.toShort(source, index));
  }

  public static int readInt(byte[] source, int index) {
    return ToolConvert.toInt(source, index);
  }

  public static long readUnsignedInt(byte[] source, int index) {
    return ToolConvert.getUnsigned(ToolConvert.toInt(source, index));
  }

  public static byte[] readBytes(byte[] source, int index, int length) {
    if (source == null || length <= 0) {
      log.warn("source is null or length is 0!");
      return null;
    }
    byte[] bytes = new byte[length];
    copyByte(source, index, bytes, 0, length);
    return bytes;
  }

  public static String readString(byte[] source, int index, int length) {
    byte[] bytes = readBytes(source, index, length);
    if (bytes == null) {
      return null;
    }
    return toString(bytes);
  }

  //>>>---------------------------------------- read byte[] ----------------------------------------

  //<<<---------------------------------------- read byte[] turn ----------------------------------------

  public static short readShort(byte[] source, int index, boolean isTurn) {
    return ToolConvert.toShort(source, index, isTurn);
  }

  public static int readUnsignedShort(byte[] source, int index, boolean isTurn) {
    return ToolConvert.getUnsigned(ToolConvert.toShort(source, index, isTurn));
  }

  public static int readInt(byte[] source, int index, boolean isTurn) {
    return ToolConvert.toInt(source, index, isTurn);
  }

  public static long readUnsignedInt(byte[] source, int index, boolean isTurn) {
    return ToolConvert.getUnsigned(ToolConvert.toInt(source, index, isTurn));
  }

  public static long readLong(byte[] source, int index, boolean isTurn) {
    return ToolConvert.toLong(source, index, isTurn);
  }

  public static double readDouble(byte[] source, int index, boolean isTurn) {
    return ToolConvert.toDouble(source, index, isTurn);
  }

  //>>>---------------------------------------- read byte[] turn ----------------------------------------

  //<<<---------------------------------------- write ----------------------------------------

  public static void writeByte(ByteBuf byteBuf, byte value) {
    byteBuf.writeByte(value);
  }

  public static void writeShort(ByteBuf byteBuf, short value) {
    byteBuf.writeShort(value);
  }

  public static void writeInt(ByteBuf byteBuf, int value) {
    byteBuf.writeInt(value);
  }

  public static void writeLong(ByteBuf byteBuf, long value) {
    byte[] bytes = ToolConvert.toBytes(value);
    byteBuf.writeBytes(bytes);
  }

  public static void writeDouble(ByteBuf byteBuf, double value) {
    byteBuf.writeDouble(value);
  }

  public static void writeBytes(ByteBuf byteBuf, byte[] value) {
    if (value != null) {
      byteBuf.writeBytes(value);
    }
  }

  public static void writeBytes(ByteBuf byteBuf, byte[] value, int length) {
    if (value != null) {
      byteBuf.writeBytes(value, 0, length);
    }
  }

  public static boolean writeString(ByteBuf byteBuf, String value, int length) {
    byte[] bytes = null;
    if (value == null) {
      bytes = new byte[length];
    } else {
      bytes = value.getBytes();
    }
    //		bytes = Base.fillValue(bytes, length, (byte) 0);
    byteBuf.writeBytes(bytes);
    int count = length - bytes.length;
    for (int i = 0; i < count; i++) {
      byteBuf.writeByte(0);
    }
    return true;
  }

  //>>>---------------------------------------- write ----------------------------------------

  //<<<---------------------------------------- write turn ----------------------------------------

  public static void writeShort(ByteBuf byteBuf, short value, boolean isTurn) {
    byte[] bytes = ToolConvert.toBytes(value);
    value = ToolConvert.toShort(bytes, isTurn);
    byteBuf.writeShort(value);
  }

  public static void writeInt(ByteBuf byteBuf, int value, boolean isTurn) {
    byte[] bytes = ToolConvert.toBytes(value);
    value = ToolConvert.toInt(bytes, isTurn);
    byteBuf.writeInt(value);
  }

  public static void writeLong(ByteBuf byteBuf, long value, boolean isTurn) {
    byte[] bytes = ToolConvert.toBytes(value);
    value = ToolConvert.toLong(bytes, isTurn);
    byteBuf.writeLong(value);
  }

  public static void writeDouble(ByteBuf byteBuf, double value, boolean isTurn) {
    byte[] bytes = ToolConvert.toBytes(value);
    value = ToolConvert.toDouble(bytes, isTurn);
    byteBuf.writeDouble(value);
  }

  //>>>---------------------------------------- write turn ----------------------------------------

  // <<<---------------------------------------- bytes ----------------------------------------

  public static int copyByte(byte[] source, int sourceStart, byte[] target, int targetStart, int length) {
    return copyObject(source, sourceStart, target, targetStart, length);
  }

  public static int copyArray(Object src, int srcPos, Object dest, int destPos, int length) {
    return copyObject(src, srcPos, dest, destPos, length);
  }

  // >>>---------------------------------------- bytes ----------------------------------------

  // <<<---------------------------------------- byteBuf ----------------------------------------

  public static String toHex(ByteBuf byteBuf) {
    if (byteBuf == null) {
      return null;
    }
    byte[] bArray = new byte[byteBuf.readableBytes()];
    byteBuf.readBytes(bArray);
    StringBuffer sbuf = new StringBuffer();
    for (int i = 0; i < bArray.length; i++) {
      String hex = Integer.toHexString(bArray[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      sbuf.append(hex);
    }
    return sbuf.toString();
  }

  public static byte[] toBytes(ByteBuf byteBuf) {
    if (byteBuf == null) {
      return null;
    }
    byte[] bytes = new byte[byteBuf.readableBytes()];
    byteBuf.readBytes(bytes);
    return bytes;
  }

  public static String hexDump(ByteBuf byteBuf) {
    int size = byteBuf.readableBytes();
    if (size > size_dump) {
      return ByteBufUtil.prettyHexDump(byteBuf, 0, size_dump);
    } else {
      return ByteBufUtil.prettyHexDump(byteBuf);
    }
  }

  public static String getByteBufInfo(ByteBuf byteBuf) {
    return String.format("byteBuf capacity:%s readerIndex:%s writeIndex:%s readeableBytes:%s writableBytes:%s", byteBuf.capacity(), byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.readableBytes(), byteBuf.writableBytes());
  }

  // >>>---------------------------------------- byteBuf ----------------------------------------

  // <<<---------------------------------------- tool ----------------------------------------

  // >>>---------------------------------------- tool ----------------------------------------

  // >>---------------------------------------- public ----------------------------------------

  // <<---------------------------------------- private ----------------------------------------

  // <<<---------------------------------------- normal ----------------------------------------

  // >>>---------------------------------------- normal ----------------------------------------

  // <<<---------------------------------------- tool ----------------------------------------

  /**
   * Convert char to byte
   * @param c char
   * @return byte
   */
  private static byte charToByte(char c) {
    return (byte) "0123456789ABCDEF".indexOf(c);
  }

  private static int copyObject(Object source, int sourceStart, Object target, int targetStart, int length) {
    if (source == null || target == null) {
      return 0;
    }
    try {
      System.arraycopy(source, sourceStart, target, targetStart, length);
      return length;
    } catch (Exception e) {
      log.error("copyObject Exception", e);
      return 0;
    }
  }

  private static String toString(byte[] bytes) {
    return Tool.toString(Tool.toString(bytes));
  }

  // >>>---------------------------------------- tool ----------------------------------------

  // >>---------------------------------------- private ----------------------------------------

}