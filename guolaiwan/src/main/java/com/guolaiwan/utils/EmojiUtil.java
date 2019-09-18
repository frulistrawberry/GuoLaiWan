package com.guolaiwan.utils;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/3/13.
 * 说明:
 */
public class EmojiUtil {


    /*将用户输入的表情转换为字符串*/
    public static String emojiToString(String userInput){
        int length = userInput.length();
        String resultString = "";
        //循环遍历字符串，将字符串拆分为一个一个字符
        for (int i = 0; i < length; i++) {
            char codePoint = userInput.charAt(i);
            //判断字符是否是emoji表情的字符
            if (isEmojiCharacter(codePoint)) {
                //如果是将以大括号括起来
                String emoji = "{" + Integer.toHexString(codePoint) + "}";
                resultString = resultString + emoji;
                continue;
            }
            resultString = resultString + codePoint;
        }
        return resultString;
    }

    /*判断用户输入是否包含Emoji*/
    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    /*将Emoji字符串转作为表情进行回显*/
    public static String stringToEmoji(String strInDataBase) {
        String resultString = strInDataBase;
        String rep = "\\{(.*?)\\}";
        Pattern p = Pattern.compile(rep);
        Matcher m = p.matcher(resultString);
        while (m.find()) {
            String s1 = m.group().toString();
            String s2 = s1.substring(1, s1.length() - 1);
            String s3;
            try {
                s3 = String.valueOf((char) Integer.parseInt(s2, 16));
                resultString = resultString.replace(s1, s3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

}
