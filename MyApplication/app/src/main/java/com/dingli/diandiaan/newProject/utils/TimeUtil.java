package com.dingli.diandiaan.newProject.utils;

import android.text.TextUtils;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间转为工具
 * @author lwq
 * */
public class TimeUtil {

	public static int getYear() {
		Calendar d = Calendar.getInstance(Locale.CHINA);
		// 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
		Date myDate = new Date();
		// 创建一个Date实例
		d.setTime(myDate);
		// 设置日历的时间，把一个新建Date实例myDate传入
		return d.get(Calendar.YEAR);
	}

	public static int getMonth() {

		Calendar d = Calendar.getInstance(Locale.CHINA);
		// 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
		Date myDate = new Date();
		// 创建一个Date实例
		d.setTime(myDate);
		// 设置日历的时间，把一个新建Date实例myDate传入
		return d.get(Calendar.MONTH) + 1;

	}

	public static int getDay() {

		Calendar d = Calendar.getInstance(Locale.CHINA);
		// 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
		Date myDate = new Date();
		// 创建一个Date实例
		d.setTime(myDate);
		// 设置日历的时间，把一个新建Date实例myDate传入
		return d.get(Calendar.DAY_OF_MONTH);
	}

	public static StringBuilder getNowData() {

		Calendar d = Calendar.getInstance(Locale.CHINA);
		// 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
		Date myDate = new Date();
		// 创建一个Date实例
		d.setTime(myDate);
		// 设置日历的时间，把一个新建Date实例myDate传入
		int year = d.get(Calendar.YEAR);
		int month = d.get(Calendar.MONTH);
		int day = d.get(Calendar.DAY_OF_MONTH);
		return getStringDate(year, month, day);
	}

	public static StringBuilder getNowTime() {
		Calendar d = Calendar.getInstance(Locale.CHINA);
		Date myDate = new Date();
		d.setTime(myDate);
		int hour = d.get(Calendar.HOUR_OF_DAY);
		int minute = d.get(Calendar.MINUTE);
		return getStringTime(hour, minute);
	}

	public static StringBuilder getbeforTime(int beforhour) {

		Calendar d = Calendar.getInstance(Locale.CHINA);
		Date myDate = new Date();

		myDate.setTime(System.currentTimeMillis() + beforhour * 60 * 60 * 1000);
		d.setTime(myDate);

		int hour = d.get(Calendar.HOUR_OF_DAY);
		// int minute = d.get(Calendar.MINUTE);

		int minute = 30;

		return getStringTime(hour, minute);
	}

	public static StringBuilder getbeforData(int beforhour) {

		Calendar d = Calendar.getInstance(Locale.CHINA);
		// 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
		Date myDate = new Date();
		myDate.setTime(System.currentTimeMillis() + beforhour * 60 * 60 * 1000);
		// 创建一个Date实例
		d.setTime(myDate);
		// 设置日历的时间，把一个新建Date实例myDate传入
		int year = d.get(Calendar.YEAR);
		int month = d.get(Calendar.MONTH);
		int day = d.get(Calendar.DAY_OF_MONTH);
		return getStringDate(year, month, day);
	}

	public static StringBuilder getStringDate(int year, int month, int day) {
		StringBuilder builder = new StringBuilder();
		builder.append(year);
		builder.append("-");
		if (month + 1 < 10)
			builder.append("0");
		builder.append(month + 1);
		builder.append("-");
		if (day < 10)
			builder.append("0");
		builder.append(day);
		return builder;

	}

	public static StringBuilder getStringTime(int hour, int minute) {

		StringBuilder builder = new StringBuilder();
		if (hour < 10)
			builder.append("0");
		builder.append(hour);
		builder.append(":");
		if (minute < 10) {
			builder.append("0");
		}
		builder.append(minute);
		return builder;
	}

	public static StringBuilder getAllxasDate(String date, String time) {
		return new StringBuilder().append(date).append("T").append(time)
				.append(":00");
	}

	/**
	 * 使字符串大写
	 *
	 * @param str
	 * @return
	 */
	public static String bigfont(String str) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<big>");
		stringBuilder.append(str);
		stringBuilder.append("</big>");
		return stringBuilder.toString();

	}

	/**
	 * 比较两个时间谁大 isXianShi("00:00","04:21") 前大反true
	 *
	 * @param qian
	 * @param hou
	 * @return
	 */
	public static boolean isXianShi(String qian, String hou) {
		String[] qianbian = qian.split(":");
		String[] houbian = hou.split(":");
		long qq, hh;
		long q, h;
		if (qianbian[0].split("")[0].equals("0")) {
			q = (Long.parseLong(qianbian[0].split("")[1])) * 60 * 60 * 1000;
			long q1;
			if (qianbian[1].split("")[0].equals("0")) {
				q1 = Long.parseLong(qianbian[1].split("")[1]) * 60 * 1000;
			} else {
				q1 = Long.parseLong(qianbian[1]) * 60 * 1000;
			}
			qq = q + q1;
		} else {
			q = (Long.parseLong(qianbian[0])) * 60 * 60 * 1000;
			long q1;
			if (qianbian[1].split("")[0].equals("0")) {
				q1 = Long.parseLong(qianbian[1].split("")[1]) * 60 * 1000;
			} else {
				q1 = Long.parseLong(qianbian[1]) * 60 * 1000;
			}
			qq = q + q1;
		}

		if (houbian[0].split("")[0].equals("0")) {
			h = (Long.parseLong(houbian[0].split("")[1])) * 60 * 60 * 1000;
			long q1;
			if (houbian[1].split("")[0].equals("0")) {
				q1 = Long.parseLong(houbian[1].split("")[1]) * 60 * 1000;
			} else {
				q1 = Long.parseLong(houbian[1]) * 60 * 1000;
			}
			hh = h + q1;
		} else {
			h = (Long.parseLong(houbian[0])) * 60 * 60 * 1000;
			long q1;
			if (houbian[1].split("")[0].equals("0")) {
				q1 = Long.parseLong(houbian[1].split("")[1]) * 60 * 1000;
			} else {
				q1 = Long.parseLong(houbian[1]) * 60 * 1000;
			}
			hh = h + q1;
		}

		if ((qq - hh) > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 格式 2014-06-03T15:01 将其转化为毫秒
	 *
	 * @param date
	 * @return
	 */
	public static long getMillisecond(String date) {
		try {
			String currentTimezone = Time.getCurrentTimezone();
			Time time = new Time(currentTimezone);
			time.parse3339(date);
			time.switchTimezone(currentTimezone);
			return time.toMillis(true);
		} catch (Exception exception) {
			return -1;
		}
	}

	/**
	 * 得到当前时间毫秒数
	 *
	 * @return
	 */
	public static long getNowMillisecond() {
		//Calendar d1= Calendar.getInstance(Locale.CHINA);
		Date myDate = new Date();

		return myDate.getTime();
	}

	/**
	 * 得到是在多久前发表的 格式 2014-06-03T15:01
	 *
	 * @param date
	 * @return
	 */
	public static String getLongAgo(String date,long millisecond) {
		try {
//			if(TextUtils.isEmpty(date)){
//				return "";
//			}
			final long time = (getNowMillisecond() - millisecond) / 1000;// 秒数
			if (time == -1) {
				return "1分钟前";
			} else if (time <= 60) {
				return "1分钟前";
			} else if (60 < time && time < 60 * 60) {// 一小时
				int a = (int) (time / 60);
				return a + "分钟前";
			} else if (time < 60 * 60 * 24) {// 一天
				int a = (int) (time / 60 / 60);
				if (a == 0) {
					a = 1;
				}
				return a + "小时前";
			} else if (time < 60 * 60 * 24 * 30) {
				int a = (int) (time / 60 / 60 / 24);
				if (a == 0) {
					a = 1;
				}
				return a+"天前" ;//大于1天
			} else {
				return "1个月前";//1个月前
			}

		} catch (Exception e) {
			return "1分钟前";
		}
	}

	/**
	 * 多少 天
	 * @param date
	 * */
	public static int getDay(String date) {
		final long time = (getMillisecond(date)-getNowMillisecond() ) / 1000;// 秒数
		if (time > 60 * 60 * 24) {
			int a = (int) (time / 60 / 60 / 24);
			if (a == 0) {
				a = 1;
			}
			return a ;
		} else {
			return 0;
		}
	}

	/**
	 * 得到是在多久前发表的 格式 2014-06-03T15:01
	 *
	 * @param date
	 * @return
	 */
	public static String getShiFaHouLongAgo(String date,String date1) {
		//	public static String getShiFaHouLongAgo(String date) {	
		//	final long time = (getNowMillisecond() - getMillisecond(date)) / 1000;// 秒数
		final long time1 = (getMillisecond(date)-getMillisecond(date1))/(1000*3600);//小时
		final long time = (getMillisecond(date)-getMillisecond(date1))/(1000*60);// 分钟
		if(time1<1){
			if(time<1){
				return "事发后1分钟";
			}else{
				return "事发后"+time+"分钟";
			}
		}else{
			return "事发后"+time1+"小时";
		}
		//		if (time == -1) {
		//			return date;
		//		} else if (time <= 60) {
		//			return "事发后1分钟";
		//		} else if (time>60 && time < 60 * 60) {// 一小时
		//			int a = (int) (time / 60);
		//			return "事发后" + a + "分钟";
		//		} else if (time < 60 * 60 * 24) {// 一天
		//			int a = (int) (time / 60 / 60);
		//			if (a == 0) {
		//				a = 1;
		//			}
		//			return "事发后"+a + "小时";
		//		} else if (time < 60 * 60 * 24 * 30) {
		//			int a = (int) (time / 60 / 60 / 24);
		//			if (a == 0) {
		//				a = 1;
		//			}
		//			return "事发后"+a*24 + "小时";
		//		} else {
		//			return "事发后"+24*30+"小时";
		//		}

	}


	public static String getLongAgo(long timein) {

		final long time = (getNowMillisecond() - timein) / 1000;
		if (time <= 60) {
			return "1分钟前";
		} else if (60 < time && time < 60 * 60) {// 一小时
			int a = (int) (time / 60);
			return a + "分钟前";
		} else if (time < 60 * 60 * 24) {// 一天
			int a = (int) (time / 60 / 60);
			if (a == 0) {
				a = 1;
			}
			return a + "小时前";
		} else if (time < 60 * 60 * 24 * 30) {
			int a = (int) (time / 60 / 60 / 24);
			if (a == 0) {
				a = 1;
			}
			return a + "天前";
		} else {
			return "1个月前";
		}

	}

	public  static long getTimeMill(String _time){
		final long time = (getNowMillisecond()-getMillisecond(_time));// 毫秒数
		return time;
	}



	/**
	 * 格式 2014060315:01
	 *
	 * @param date
	 * @return
	 */
	@SuppressWarnings("null")
	public static String getMinutesAgo(String date) {
		String[] s = date.split("");
		StringBuffer year = null;
		for (int i = 0; i < 4; i++) {
			year.append(s[i]);
		}
		StringBuffer month = null;
		for (int i = 4; i < 6; i++) {
			month.append(s[i]);
		}
		StringBuffer day = null;
		for (int i = 6; i < 8; i++) {
			day.append(s[i]);
		}
		StringBuffer time = null;
		for (int i = 8; i < s.length; i++) {
			time.append(s[i]);
		}
		return null;
	}

	/**
	 * 将时间转换为秒 格式 00:00
	 *
	 * @param
	 * @return
	 */
	public static long getSecond(String qian) {
		Time time = new Time("GMT+8");
		time.setToNow();

		//	String hou = time.hour + ":" + time.minute;

		String[] qianbian = qian.split(":");
		//String[] houbian = hou.split(":");
		long qq;
		long q;
		if (qianbian[0].split("")[0].equals("0")) {
			q = (Long.parseLong(qianbian[0].split("")[1])) * 60 * 60 * 1000;
			long q1;
			if (qianbian[1].split("")[0].equals("0")) {
				q1 = Long.parseLong(qianbian[1].split("")[1]) * 60 * 1000;
			} else {
				q1 = Long.parseLong(qianbian[1]) * 60 * 1000;
			}
			qq = q + q1;
		} else {
			q = (Long.parseLong(qianbian[0])) * 60 * 60 * 1000;
			long q1;
			if (qianbian[1].split("")[0].equals("0")) {
				q1 = Long.parseLong(qianbian[1].split("")[1]) * 60 * 1000;
			} else {
				q1 = Long.parseLong(qianbian[1]) * 60 * 1000;
			}
			qq = q + q1;
		}

		return qq;
	}
	/* 2016-02-18T17:36:35
        *   2月18日  17:36
        * */
	public static String formatTime(String time) {
		try {
			if(time.isEmpty()){
				return "";
			}
			String[] strings = time.split("T");
			String[] dates = strings[0].split("-");
			String[] times = strings[1].split(":");
			if (strings.length < 2 || dates.length < 3 || times.length < 3) {
				return time;
			}
			if (dates[1].charAt(0) == '0') {
				dates[1] = dates[1].charAt(1) + "";
			}
			if (dates[2].charAt(0) == '0') {
				dates[2] = dates[2].charAt(1) + "";
			}
			if (times[0].charAt(0) == '0') {
				times[0] = times[0].charAt(1) + "";
			}
			if (times[1].charAt(0) == '0') {
				times[1] = times[1].charAt(1) + "";
			}
			return times[0] + ":" + times[1] + "    " + dates[1] + "月" + dates[2] + "日";
		} catch (Exception e) {
			return "";
		}
	}


	public static String getTodayAndTomorday(String date){
		if(TextUtils.isEmpty(date)){
			return  "";
		}
		try {
			final long time = (getMillisecond(date)-getMillisecondCurrentTime() ) / 1000;// 秒数
			if(time ==0){
				return "今天";
			}else if( time<=60 * 60 * 24){
				return "明天";
			}else{
				return date.split("-")[1]+"-"+date.split("-")[2];
			}
		} catch (Exception e) {
			return date;
		}
	}

	public static long getMillisecondCurrentTime(){
		Date date=new Date();//获取当前日期也可以用Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return getMillisecond(sdf.format(date));
	}
	public static String getMillisecondTime(String time){
		String value="";
		try{
			Long a = Long.valueOf(time);
			Date d = new Date();
			d.setTime(a);
			SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm");
			value=sdf.format(d);
		}catch (Exception e){
			e.printStackTrace();
			value="";
		}
		return  value;
	}
	public static long timeToMills(String _time) {//格式：11:03:10
		try{
			String[] my =_time.split(":");
			int hour =Integer.parseInt(my[0]);
			int min =Integer.parseInt(my[1]);
			int sec =Integer.parseInt(my[2]);
			int second =hour*3600+min*60+sec;
			return second*1000;
		}catch (Exception e){
			return  0;
		}
	}
}
