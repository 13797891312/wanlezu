package com.hzkjkf.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.hardware.Camera.Face;

public class FormatStringUtil {
	public static String formatTime(String date, boolean isChanage) {
		String date1;
		StringBuffer sb = new StringBuffer();
		if (isChanage) {
			date1 = date;
			Calendar ca = Calendar.getInstance();
			int currentYear = ca.get(Calendar.YEAR);
			int currentMonth = ca.get(Calendar.MONTH) + 1;
			int currentDay = ca.get(Calendar.DAY_OF_MONTH);
			sb.append(currentYear + "-");
			sb.append((currentMonth < 10 ? "0" + currentMonth : currentMonth)
					+ "-");
			sb.append((currentDay < 10 ? "0" + currentDay : currentDay));
			if (date1.equals(sb.toString())) {
				return "½ñÌì";
			}
		} else
			date1 = date.substring(0, date.indexOf(" "));
		return date1;
	}

	public static String getDay(String date) {
		String day = null;
		day = date.substring(date.lastIndexOf("-") + 1, date.length());
		return day;
	}

	public static String getMonth(String date) {
		String month = null;
		month = date.substring(0, date.lastIndexOf("-"));

		return month;
	}

	public static float[] getLv(String lv) {
		float result[] = new float[2];
		if (lv.equals("LV1")) {
			result[0] = 1;
			result[1] = 0.05f;
		} else if (lv.equals("LV2")) {
			result[0] = 2;
			result[1] = 0.05f;
		} else if (lv.equals("LV3")) {
			result[0] = 3;
			result[1] = 0.05f;
		} else if (lv.equals("LV4")) {
			result[0] = 4;
			result[1] = 0.06f;
		} else if (lv.equals("LV5")) {
			result[0] = 5;
			result[1] = 0.06f;
		} else if (lv.equals("LV6")) {
			result[0] = 6;
			result[1] = 0.07f;
		}
		return result;
	}

	public static int getLvInt(String lv) {
		int result = 1;
		if (lv.equals("V1")) {
			result = 1;
		} else if (lv.equals("V2")) {
			result = 2;
		} else if (lv.equals("V3")) {
			result = 3;
		} else if (lv.equals("V4")) {
			result = 4;
		} else if (lv.equals("V5")) {
			result = 5;
		} else if (lv.equals("V6")) {
			result = 6;
		}
		return result;
	}

	public static String getMoney(int type) {
		String result = "";
		switch (type) {
		case 1:
			result = "¶Ò»»Ö§¸¶±¦";
			break;
		case 2:
			result = "¶Ò»»²Æ¸¶Í¨";

			break;
		case 3:
			result = "¶Ò»»QQ±Ò";

			break;

		}
		return result;
	}

	public static String getDesplay(String money) {
		if (money == null) {
			return "0";
		}
		float f = Float.parseFloat(money);
		DecimalFormat decimalFormat = new DecimalFormat("0");// È¥µÍ°«Ð¡Êýµã.
		String p = decimalFormat.format(f * 100);// format ï¿½ï¿½ï¿½Øµï¿½ï¿½ï¿½ï¿½Ö·ï¿½
		return p;
	}

	/*** ºÁÃë×ª»»ÎªÊ±·ÖÃë ***/
	public static String formatLongToTimeStr(Long l) {
		int hour = 0;
		int minute = 0;
		int second = 0;

		second = l.intValue() / 1000;

		if (second > 60) {
			minute = second / 60;
			second = second % 60;
		}
		if (minute > 60) {
			hour = minute / 60;
			minute = minute % 60;
		}
		return (getTwoLength(hour) + ":" + getTwoLength(minute) + ":" + getTwoLength(second));
	}

	private static String getTwoLength(final int data) {
		if (data < 10) {
			return "0" + data;
		} else {
			return "" + data;
		}
	}

	public static long getMilTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = sdf.parse(time);
			System.out.println(d.getTime());
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.isEmpty() || str.trim().equals("null")) {
			return true;
		}
		return false;
	}
	public static boolean isEmptyOr0(String str) {
		if (str == null || str.isEmpty() || str.trim().equals("null")||str.equals("0")) {
			return true;
		}
		return false;
	}

	public static boolean isImei(String str) {
		if (str == null || str.isEmpty() || str.trim().equals("null")
				|| str.trim().equals("000000000000000")) {
			return false;
		}
		return true;

	}

}
