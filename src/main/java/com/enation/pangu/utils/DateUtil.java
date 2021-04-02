package com.enation.pangu.utils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期相关的操作
 *
 * @author Dawei
 */

@SuppressWarnings({"AlibabaCollectionInitShouldAssignCapacity", "AlibabaUndefineMagicConstant"})
public class DateUtil {





    /**
     * 将一个字符串转换成日期格式
     *
     * @param date    字符串日期
     * @param pattern 日期格式
     * @return
     */
    public static Date toDate(String date, String pattern) {
        if ("".equals("" + date)) {
            return null;
        }
        if (pattern == null) {
            pattern = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date newDate = new Date();
        try {
            newDate = sdf.parse(date);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return newDate;
    }


    /**
     * 把日期转换成字符串型
     *
     * @param date    日期
     * @param pattern 类型
     * @return
     */
    public static String toString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        if (pattern == null) {
            pattern = "yyyy-MM-dd";
        }
        String dateString = "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            dateString = sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateString;
    }

    /**
     * 时间戳转换成时间类型
     *
     * @param time    时间戳
     * @param pattern 格式
     * @return
     */
    public static String toString(Long time, String pattern) {
        if (time > 0) {
            if (time.toString().length() == 10) {
                time = time * 1000;
            }
            Date date = new Date(time);
            String str = DateUtil.toString(date, pattern);
            return str;
        }
        return "";
    }

    /**
     * 获取指定日期的时间戳
     *
     * @param date 指定日期
     * @return 时间戳
     */
    public static long getDateline(String date) {
        return toDate(date, "yyyy-MM-dd").getTime() / 1000;
    }

    /**
     * 为了方便mock 设置此属性
     * 如果设置了此属性，则回直接返回设置的值
     */
    public static Long mockDate;

    /**
     * 获取当前时间的时间戳
     *
     * @return 时间戳
     */
    public static long getDateline() {
        if (mockDate != null) {
            return mockDate;
        }
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 根据日期格式及日期获取时间戳
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return 时间戳
     */
    public static long getDateline(String date, String pattern) {
        return toDate(date, pattern).getTime() / 1000;
    }



    public static long getDateHaveHour(String date) {
        return toDate(date, "yyyy-MM-dd HH").getTime() / 1000;
    }

    /**
     * 获取当前时间的年
     *
     * @return
     */
    public static Integer getYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前时间的月
     *
     * @return
     */
    public static Integer getMonth() {
        Calendar calendar = Calendar.getInstance();
        Integer i = calendar.get(Calendar.MONTH);
        return (i + 1);
    }
}
