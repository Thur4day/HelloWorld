package com.dxz.helloworld.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duanjunlei on 2015/12/21.
 */
public class HiUtils {

    public static String convertLongToString(long timeSTamp) {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        return convertLongToString(timeSTamp, formatter);
    }

    public static String convertLongToString(long timeSTamp, DateFormat dateFormat) {
        Date date = new Date(timeSTamp);
        String dateFormatted = dateFormat.format(date);

        return dateFormatted;
    }

    public static <T> List<T> arrayFromData(String str) {
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        return new Gson().fromJson(str, listType);
    }

    public static boolean emailCheck(String str) {
        String check = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*"
                + "@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*"
                + "((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(str);
        return matcher.matches();
    }

    public static boolean nickNameCheck(String str) {
        String strPattern = "^[0-9a-zA-Z\u4e00-\u9fa5-_]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean phoneCheck(String str) {
        String check = "^1[3|4|5|7|8][0-9]" + "\\" + "d{4,8}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(str);
        return matcher.matches();
    }

    public static boolean checkFull(String str) {
        String checkFull = "[\uFF00-\uFFFF]";
        Pattern regexFull = Pattern.compile(checkFull);
        Matcher matcherFull = regexFull.matcher(str);
        return matcherFull.matches();
    }

    public static boolean checkChinese(String str) {
        String checkChinese = "[\u4e00-\u9fa5]";
        Pattern regexChinese = Pattern.compile(checkChinese);
        Matcher matcherChinese = regexChinese.matcher(str);
        return matcherChinese.matches();
    }

    public static String splitStr(String str, String subStr) {
        if (str.startsWith(subStr) && str.length() > subStr.length()) {
            str = str.substring(subStr.length() + 1);
        }
        return str;
    }

    public static String buildTransaction(final String type, final String id, final int scene) {
        return (TextUtils.isEmpty(type) || TextUtils.isEmpty(id)) ? String.valueOf(System.currentTimeMillis()) : type
                + "_" + id + "_" + scene + "_" + System.currentTimeMillis();
    }
}
