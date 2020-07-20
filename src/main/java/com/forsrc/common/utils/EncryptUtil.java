package com.forsrc.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by wangzaiwei on 2017/5/9.
 */
public class EncryptUtil {

    private static final Random random = new Random();

    public final static String CONCAT_SESSION = "##";

    public final static String CONCAT_VERIFY = "###";

    /**
     * 位置替换序列：质数-100以内
     */
    private static final String PRIME_STRING = "2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97";
    private static List<Integer> PRIME_LIST;

    /**
     * 混淆字符集合（字母+数字）
     * 注意：不能修改！！！否则已有密文无法解密！！！
     */
    private static final String ENCODE_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int ENCODE_STRING_LENGTH = ENCODE_STRING.length();

    /**
     * 混淆序列：质数-18
     * ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz
     * 2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61
     * BCEGKMQSW24aegkqwy
     */
    private static String EXCHANGE_STRING = "BCEGKMQSW24aegkqwy";
    private static int EXCHANGE_LENGTH = EXCHANGE_STRING.length();

    static {
        String[] split = PRIME_STRING.split(",");
        PRIME_LIST = new ArrayList<Integer>(split.length);
        StringBuilder sb = new StringBuilder();
        for (String i : split) {
            Integer prime = Integer.valueOf(i);
            PRIME_LIST.add(prime);
            if (prime < ENCODE_STRING_LENGTH) {
                sb.append(ENCODE_STRING.charAt(prime - 1));
            }
        }
        if (StringUtils.isBlank(sb)) {
            EXCHANGE_STRING = "BCEGKMQSW24aegkqwy";
        } else {
            EXCHANGE_STRING = sb.toString();
        }
        EXCHANGE_LENGTH = EXCHANGE_STRING.length();
    }

    public static String getFixLenthString(int strLength) {

        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

        BigDecimal bigDecimal = new BigDecimal(pross);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = bigDecimal.toString().replace(".", "");

        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    public static String generateToken(String phoneNumber) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(phoneNumber);
        strBuf.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        strBuf.append(EncryptUtil.getFixLenthString(6));

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return Base64.encodeBase64String(md.digest(strBuf.toString().getBytes()));
    }

    /**
     * phoneNumber:userId:validDays:random
     * @param phoneNumber
     * @param digestSalt  密钥盐值
     * @param validDays   生成token的有效时长
     * @return
     */
    public static String generateLoginToken(String phoneNumber, long userId, String digestSalt, int validDays) throws Exception {
        long validTime = LocalDateTime.now().plusDays(validDays).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(phoneNumber);
        strBuf.append(":");
        strBuf.append(userId);
        strBuf.append(":");
        strBuf.append(validTime);
        strBuf.append(":");
        strBuf.append(EncryptUtil.getFixLenthString(6));

        return AESEncryptUtil.encrypt(strBuf.toString(), digestSalt);
    }

    /**
     * 构造sessionId
     *
     * @param id
     * @return
     */
    public static String enSessionId(Integer id) {
        return enNumber(id, CONCAT_SESSION);
    }

    /**
     * 解析sessionId
     *
     * @param sessionId
     * @return
     * @see EncryptUtil#enSessionId(Integer)
     */
    public static Integer deSessionId(String sessionId) {
        return Integer.parseInt(deNumber(sessionId, CONCAT_SESSION));
    }

    /**
     * 构造verifyId
     *
     * @param id
     * @return
     */
    public static String enVerifyId(Long id) {
        return enNumber(id, CONCAT_VERIFY);
    }

    /**
     * 解析verifyId
     *
     * @param verifyId
     * @return
     * @see EncryptUtil#enVerifyId(Long)
     */
    public static Long deVerifyId(String verifyId) {
        return Long.parseLong(deNumber(verifyId, CONCAT_VERIFY));
    }

    /**
     * 构造Integer成可逆密文
     *
     * @param id
     * @param concat
     * @return
     */
    public static String enNumber(Number id, String concat) {
        return enBase64(id + concat + System.currentTimeMillis());
    }

    /**
     * 解析可逆密文回Integer
     *
     * @param enInteger
     * @param concat
     * @return
     * @see EncryptUtil#enNumber(Number, String)
     */
    public static String deNumber(String enInteger, String concat) {
        String back = deBase64(enInteger);
        String[] split = back.split(concat);
        return split[0];
    }

    /**
     * md5(txt)
     *
     * @param txt
     * @return
     */
    public static String md5(String txt) {
        return DigestUtils.md5Hex(txt);
    }

    /**
     * base64En(txt)
     *
     * @param txt
     * @returnb
     */
    public static String base64En(String txt) {
        try {
            byte[] bytes = txt.getBytes("utf-8");
            return Base64.encodeBase64String(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * base64De(base64)
     *
     * @param base64
     * @return
     */
    public static String base64De(String base64) {
        byte[] bytes = Base64.decodeBase64(base64);
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hunxiaoEn(String txt) {
        int length = txt.length() > EXCHANGE_LENGTH ? EXCHANGE_LENGTH : txt.length();
        int nextInt = random.nextInt(length);
        StringBuilder result = new StringBuilder()
                .append(EXCHANGE_STRING.charAt(nextInt))
                .append(txt.subSequence(0, nextInt))
                .append(EXCHANGE_STRING.charAt(nextInt))
                .append(txt.substring(nextInt));
        return result.toString();
    }

    public static String hunxiaoDe(String txt) {
        char ch = txt.charAt(0);
        int nextInt = EXCHANGE_STRING.indexOf(ch);
        StringBuilder sb = new StringBuilder()
                .append(txt.substring(1, nextInt + 1))
                .append(txt.substring(nextInt + 2));
        return sb.toString();
    }

    /**
     * 质数位置替换
     *
     * @param txt 长度限制：100
     * @return
     */
    public static String primeExchEn(String txt) {
        //96e79218965eb72c92a549dd5a330112
        //9672981e9657b22c95ad49d15a33021e
        //OTZlNzkyMTg5NjVlYjcyYzkyYTU0OWRkNWEzMzAxMTI
        //OTlzNyk5MTgjNjVlYycyYzkWYTU0OkRzNWEzMTAxMZI
        StringBuilder sb = new StringBuilder();
        int length = txt.length();
        int i, j = 0;
        char charFirst = 0, charAt = 0;
        int size = PRIME_LIST.size();
        for (int k = 0; k < size; k++) {
            i = j;
            j = PRIME_LIST.get(k);
            if (j < length) {
                if (0 == k) {
                    // 开始
                    charFirst = txt.charAt(j);
                } else {
                    // 中间
                    i++;
                    charAt = txt.charAt(j);
                }
            } else {
                // 结尾
                i++;
                j = length;
                charAt = charFirst;
                k = size;// break;
            }
            //
            if (0 == k) {
                sb.append(txt.substring(i, j));
            } else {
                sb.append(charAt).append(txt.substring(i, j));
            }
        }
        return sb.toString();
    }

    /**
     * 质数位置替换(special:'&')
     *
     * @param txt
     * @return
     */
    public static String primeExchDe(String txt) {
        return primeExchDe(txt, '&');
    }

    /**
     * 质数位置替换
     *
     * @param txt
     * @param special 不能是txt包含的字符
     * @return
     */
    public static String primeExchDe(String txt, char special) {
        if (txt.contains(String.valueOf(special))) {
            throw new RuntimeException("unexpeted special error:txt contains '" + String.valueOf(special) + "'");
        }
        StringBuilder sb = new StringBuilder();
        int length = txt.length();
        int i = 0, j;
        char charFirst = 0, charAt = special;
        int size = PRIME_LIST.size();
        for (int k = 0; k < size; k++) {
            j = PRIME_LIST.get(k);
            if (j >= length) {
                // 结尾
                j = length;
                sb.append(txt.substring(i, j));
                charFirst = charAt;
                k = size;// break;
            } else {
                sb.append(txt.substring(i, j)).append(charAt);
                charAt = txt.charAt(j);
            }
            i = j + 1;
        }
        return sb.toString().replace(special, charFirst);
    }

    /**
     * hunxiaoEn(primeExchEn(base64En(txt)));
     *
     * @param txt
     * @return
     */
    public static String enBase64(String txt) {
        return hunxiaoEn(primeExchEn(base64En(txt)));
    }

    /**
     * base64De(primeExchDe(hunxiaoDe(txt)));
     *
     * @param txt
     * @return
     */
    public static String deBase64(String txt) {
        return base64De(primeExchDe(hunxiaoDe(txt)));
    }


    /**
     * base64De(primeExchDe(hunxiaoDe(txt)));
     *
     * @param txt
     * @return
     */
    public static String deHmac(String txt) {
        return base64De(primeExchDe(hunxiaoDe(txt)));
    }


    public static void main(String[] args) {
        String primeExchEn = EncryptUtil.enVerifyId(123l);
        System.out.println(primeExchEn);
        System.out.println(EncryptUtil.deVerifyId(primeExchEn));
        System.out.println(EncryptUtil.deNumber(primeExchEn, "###"));
//        test();
//        testHunxiao();
    }

}
