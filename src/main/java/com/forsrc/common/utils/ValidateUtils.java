package com.forsrc.common.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kevinlei on 2018/8/2.
 */
public class ValidateUtils {

    public static boolean validatePhoneNumber(String phoneNumber){
        if(StringUtils.isBlank(phoneNumber)) {
            return false;
        }
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    public static boolean validateNickName(String nickName) {
        if(StringUtils.isEmpty(nickName)) {
            return false;
        }
        if(EmojiFilter.containsEmoji(nickName)) {
            return false;
        }
        return nickName.length() <= 20;
    }

//    public static void main(String[] args) {
//       boolean vali =  ValidateUtils.validatePhoneNumber("17500000000");
//        System.out.println(vali);
//    }
}
