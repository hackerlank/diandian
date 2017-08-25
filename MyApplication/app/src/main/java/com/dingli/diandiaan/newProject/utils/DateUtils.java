package com.dingli.diandiaan.newProject.utils;

/**
 * 时间戳转换
 */

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    private static DateUtils util;
    public static Map<Integer, String> weekMap = new HashMap<>();
    public static Map<Integer, String> monthMap = new HashMap<>();

    static {
        weekMap.put(3, "周二");
        weekMap.put(4, "周三");
        weekMap.put(5, "周四");
        weekMap.put(6, "周五");
        weekMap.put(7, "周六");
        weekMap.put(1, "周日");
        weekMap.put(2, "周一");
        monthMap.put(1, "一月");
        monthMap.put(2, "二月");
        monthMap.put(3, "三月");
        monthMap.put(4, "四月");
        monthMap.put(5, "五月");
        monthMap.put(6, "六月");
        monthMap.put(7, "七月");
        monthMap.put(8, "八月");
        monthMap.put(9, "九月");
        monthMap.put(10, "十月");
        monthMap.put(11, "十一月");
        monthMap.put(12, "十二月");
    }

    public static DateUtils getInstance() {

        if (util == null) {
            util = new DateUtils();
        }
        return util;

    }

    private DateUtils() {
        super();
    }

    public SimpleDateFormat date_Formater_1 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");


    public SimpleDateFormat date_Formater_2 = new SimpleDateFormat("yyyy-MM-dd");

    public Date getDate(String dateStr) {
        Date date = new Date();
        if (TextUtils.isEmpty(dateStr)) {
            return date;
        }
        try {
            date = date_Formater_1.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return date;

    }

    public String getDataString_1(Date date) {
        if (date == null) {
            date = new Date();
        }
        return date_Formater_1.format(date);

    }

    public String getDataString_2(Date date) {
        if (date == null) {
            date = new Date();
        }
        return date_Formater_2.format(date);

    }

    /**
     * 将日期变成常见中文格式
     *
     * @param date
     * @return
     */
    public String getRencentTime(String date) {
        Date time = getDate(date);
        if (time == null) {
            return "一个月前";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        String curDate = date_Formater_2.format(cal.getTime());
        String paramDate = date_Formater_2.format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = "一个月前";
        } else {
            ftime = date_Formater_2.format(time);
        }
        return ftime;
    }

    /**
     * 日期时间格式转换
     *
     * @param typeFrom 原格式
     * @param typeTo   转为格式
     * @param value    传入的要转换的参数
     * @return
     */
    public String stringDateToStringData(String typeFrom, String typeTo,
                                         String value) {
        String re = value;
        SimpleDateFormat sdfFrom = new SimpleDateFormat(typeFrom);
        SimpleDateFormat sdfTo = new SimpleDateFormat(typeTo);

        try {
            re = sdfTo.format(sdfFrom.parse(re));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re;
    }

    /**
     * 得到这个月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public int getMonthLastDay(int year, int month) {
        if (month == 0) {
            return 0;
        }
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        return a.get(Calendar.DATE);
    }

    /**
     * 得到年份
     *
     * @return
     */
    public String getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "";
    }

    /**
     * 得到年份
     *
     * @return
     */
    public int getCurrentYearInt() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * 得到月份
     *
     * @return
     */
    public String getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return (c.get(Calendar.MONTH) + 1) + "";
    }

    /**
     * 得到月份
     *
     * @return
     */
    public int getCurrentMonthInt() {
        Calendar c = Calendar.getInstance();
        return (c.get(Calendar.MONTH) + 1);
    }

    /**
     * 获得当天的日期
     *
     * @return
     */
    public String getCurrDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH) + "";
    }

    /**
     * 获得当天的日期
     *
     * @return
     */
    public int getCurrDayInt() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到几天/周/月/年后的日期，整数往后推,负数往前移动
     *
     * @param calendar
     * @param calendarType Calendar.DATE,Calendar.WEEK_OF_YEAR,Calendar.MONTH,Calendar.
     *                     YEAR
     * @param next
     * @return
     */
    public String getDayByDate(Calendar calendar, int calendarType, int next) {

        calendar.add(calendarType, next);
        Date date = calendar.getTime();
        return date_Formater_1.format(date);

    }


    public int getDateWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        //获取指定年份月份中指定某天是星期几
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /*返回当前的日期2015-12-25样式*/
    public static String getTime() {
        int currentYear = DateUtils.getInstance().getCurrentYearInt();
        int currentMonth = DateUtils.getInstance().getCurrentMonthInt();
        int currentDay = DateUtils.getInstance().getCurrDayInt();
        return new StringBuffer().append(currentYear).append("-").append(currentMonth).append("-").append(currentDay).toString();
    }
    //  时间戳转换成月日显示

    public static String getDateToString(String time) {
        SimpleDateFormat sf = null;
        Long a = Long.valueOf(time);
        Date d = new Date(a);
        sf = new SimpleDateFormat("MM-dd");
        return sf.format(d);
    }
    //  时间戳转换成月日显示

    public static String getDateToStringTime(String time) {
        SimpleDateFormat sf = null;
        Long a = Long.valueOf(time);
        Date d = new Date(a);
        sf = new SimpleDateFormat("yyyy/MM/dd");
        return sf.format(d);
    }
    public static String getDateToStringTimeYear(String time) {
        SimpleDateFormat sf = null;
        Long a = Long.valueOf(time);
        Date d = new Date(a);
        sf = new SimpleDateFormat("yyyy月MM月dd日 HH:SS");
        return sf.format(d);
    }
    public static String getDateToStringTimeYue(String time) {
        SimpleDateFormat sf = null;
        Long a = Long.valueOf(time);
        Date d = new Date(a);
        sf = new SimpleDateFormat("MM月dd日 HH:SS");
        return sf.format(d);
    }
    public static String getDateToStringTimeYearNew(String time) {
        SimpleDateFormat sf = null;
        Long a = Long.valueOf(time);
        Date d = new Date(a);
        sf = new SimpleDateFormat("yyyy-MM-dd HH:SS");
        return sf.format(d);
    }

    public static void main(String[] args) {
        String date ="Tue, 3 Aug 2010 07:53:24";
        //由于 默认的本地语言是中文，所以这里要改成，Locale.Locale.ENGLISH,不然的话，会出现解析错误
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            System.out.println(sdf1.format(sdf2.parse(date)));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
