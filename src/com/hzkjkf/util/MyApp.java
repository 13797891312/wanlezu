package com.hzkjkf.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.telephony.TelephonyManager;
import android.widget.BaseAdapter;

public class MyApp extends Application {
	/** 余额变动刷新视图 **/
	public interface SurplusChengedListener {
		public void SurplusChenged();
	}

	/** 首页数据刷新视图 **/
	public interface HomeChengedListener {
		public void homeChenged();
	}

	private HomeChengedListener homeListener;
	private SurplusChengedListener listener;

	public static MyApp mainApp;
	public static Map<String, Activity> map = new HashMap<String, Activity>();
	public int[] color = { 0xffE90397, 0xff13AF0D, 0xffff0000, 0xffEBD203,
			0xff04A7EA, 0xffEB9703 };
	private float surplus = 0.0f;
	public float maxMoney = 0.0f;
	public String InviteCode;
	public String name;
	public String edu;
	public String aliCount;
	public String QQ;
	public String birthday;
	public String expName;
	public String expPosition;
	public String sex;
	public String profession;
	public String email;
	public int remainAd;
	public int remainAdcurrent;
	public String city = "";
	private String token;
	private String phone;
	public String version = "";
	public int collectCount = 0;
	public int lv = 1;
	private String imei;
	public double latitude;
	public String addrStr = "";
	public double longitude;
	private String homeData[] = { "0", "10", "奖励","任务", "玩币", "0", "10", "10" };// 下表千万不要随便修改下标
	
	
	public String getImei() {
		if (FormatStringUtil.isEmpty(imei)) {
			imei = ((TelephonyManager) this
					.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		}
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getToken() {
		return token;
	}
	public String getToken_downTask() {
		if (FormatStringUtil.isEmpty(token)) {
			token = SaveDate.getInstence(this).getToken();
		}
		return token;
	}

	public void setToken(String token) {
		SaveDate.getInstence(this).setToken(token);
		this.token = token;
	}

	public String getPhone() {
		if (FormatStringUtil.isEmpty(phone)) {
			phone = SaveDate.getInstence(this).getPhone();
		}
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public MyApp() {
		// TODO Auto-generated constructor stub
		mainApp = this;
	}

	public static MyApp getInstence() {
		return mainApp;
	}

	public static String address = "http://www.wanzhuan6.com/appService/exChange?a=";
	// public static String address
	// ="http://192.168·.99.176:8055/frame/appService/exChange?a=";
	// public static String address
	// ="http://114.215.210.99:80/appService/exChange?a=";
//	 public static String address =
//	 "http://192.168.99.44:8080/frame/appService/exChange?a=";
	// public static String
	// questionList="http://192.168.99.44:8080/frame/questionnaireSurvey/questionList?";
	// public static String
	// questionList="http://114.215.210.99:80/frame/questionnaireSurvey/questionList?";
	public static String questionList = "http://www.wanzhuan6.com/questionnaireSurvey/questionList?";
	public static String bauduAddress = "http://api.map.baidu.com/geodata/v3/poi/create";
	public static String baiduUpdate = "http://api.map.baidu.com/geodata/v3/poi/update";

	/** 专属包地址 **/
	public static String downAddress = "http://www.wanzhuan6.com/appService/downloadAPK/";
	// public static String
	// downAddress="http://www.wanzhuan6.com/appService/downloadAPK/PZ9RUE";
	/**** 二维码地址 ***/
	// public static String ewmAddress =
	// "http://192.168.99.44:8080/frame/appService/requestDecide                      /";
	public static String ewmAddress = "http://www.wanzhuan6.com/appService/requestDecide/";

	public String getSurplus() {
		DecimalFormat decimalFormat = new DecimalFormat("0");// 只留2为小数点.
		String p = decimalFormat.format(surplus * 100);// format
		return p;
	}

	public float getSurplusFloat() {
		return surplus;
	}

	/*** 设置首页数据变动监听 ****/
	public void setOnHomeCheanged(HomeChengedListener listener) {
		this.homeListener = listener;
	}

	/** 设置余额变动监听 ***/
	public void setOnSurplusCheanged(SurplusChengedListener listener) {
		this.listener = listener;
	}

	public void setSurplus(float surplus) {
		this.surplus = surplus;
		if (listener != null) {
			listener.SurplusChenged();
		}
	}

	public String[] getHomeData() {
		return homeData;
	}

	public void setHomeData(int index, String data) {
		homeData[index] = data;
		if (homeListener != null) {
			homeListener.homeChenged();
		}
	}
}
