package com.zanelove.gesturedetectordemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;

public class DateTools {

	/*
	 * 将时间戳转为字符串 ，格式：yyyy-MM-dd HH:mm
	 */
	public static String getStrTime_ymd_hm(String cc_time) {
		String re_StrTime = "";
		if (TextUtils.isEmpty(cc_time) || "null".equals(cc_time)) {
			return re_StrTime;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;

	}

	/*
	 * 将时间戳转为字符串 ，格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getStrTime_ymd_hms(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;

	}

	/*
	 * 将时间戳转为字符串 ，格式：yyyy.MM.dd
	 */
	public static String getStrTime_ymd(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：yyyy
	 */
	public static String getStrTime_y(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：MM-dd
	 */
	public static String getStrTime_md(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：HH:mm
	 */
	public static String getStrTime_hm(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：HH:mm:ss
	 */
	public static String getStrTime_hms(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：MM-dd HH:mm:ss
	 */
	public static String getNewsDetailsDate(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将字符串转为时间戳
	 */
	public static String getTime() {
		String re_time = null;
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date d;
		d = new Date(currentTime);
		long l = d.getTime();
		String str = String.valueOf(l);
		re_time = str.substring(0, 10);
		return re_time;
	}

	/*
	 * 将字符串转为日期
	 */
	public static String getTimeToday(long times) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date d;
		d = new Date(times);
		String str = sdf.format(d);
		re_time = str.substring(5, 10);
		return re_time;
	}

	/*
	 * 将时间戳转为字符串 ，格式：yyyy.MM.dd 星期几
	 */
	public static String getSection(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  EEEE");
		// 对于创建SimpleDateFormat传入的参数：EEEE代表星期，如“星期四”；MMMM代表中文月份，如“十一月”；MM代表月份，如“11”；
		// yyyy代表年份，如“2010”；dd代表天，如“25”
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/** 将时间String转换成long 例如2015-12-12 12:15 */
	public static long getLongTim(String strTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return sdf.parse(strTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 计算两个##:##格式的时间 段(视频总播放时间--格式##:##String)
	 * 
	 * @param start
	 *            开始的时间
	 * @param end
	 *            结束的时间
	 * @return
	 */
	public static String getTimePeriod(String start, String end) {
		String timeStr = null;
		int startInt = Integer.parseInt(start.split(":")[0]) * 60
				+ Integer.parseInt(start.split(":")[1]);
		int endInt = Integer.parseInt(end.split(":")[0]) * 60
				+ Integer.parseInt(end.split(":")[1]);
		int timeInt = endInt - startInt;
		if (timeInt >= 60) {
			if (timeInt % 60 >= 10) {
				timeStr = timeInt / 60 + ":" + timeInt % 60 + ":00";
			} else {
				timeStr = timeInt / 60 + ":0" + timeInt % 60 + ":00";
			}
		} else {
			timeStr = timeInt + ":00";
		}
		return timeStr;
	}

	/**
	 * 计算两个##:##格式的时间 段(总时间int)---视频进度条使用
	 * 
	 * @param start
	 *            开始的时间
	 * @param end
	 *            结束的时间
	 * @return
	 */
	public static int getTimePeriodInt(String start, String end) {
		int timeInt = 0;
		int startInt = Integer.parseInt(start.split(":")[0]) * 60
				+ Integer.parseInt(start.split(":")[1]);
		int endInt = Integer.parseInt(end.split(":")[0]) * 60
				+ Integer.parseInt(end.split(":")[1]);
		timeInt = (endInt - startInt) * 60;

		return timeInt;
	}

	/**
	 * 将int型的时间转成##:##格式的时间
	 * 
	 * @param start
	 *            开始的时间
	 * @param end
	 *            结束的时间
	 * @return
	 */
	public static String getTimeStr(int timeInt) {
		int mi = 1 * 60;
		int hh = mi * 60;

		long hour = (timeInt) / hh;
		long minute = (timeInt - hour * hh) / mi;
		long second = timeInt - hour * hh - minute * mi;

		String strHour = hour < 10 ? "0" + hour : "" + hour;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second;
		if (hour > 0) {
			return strHour + ":" + strMinute + ":" + strSecond;
		} else {
			return strMinute + ":" + strSecond;
		}
	}
}
