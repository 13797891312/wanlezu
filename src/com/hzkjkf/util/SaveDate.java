package com.hzkjkf.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveDate {
	private static SaveDate sd;
	SharedPreferences spf;
	private boolean isOnce = true;// 是否第一次进程序
	private Context con;
	private boolean isRecord = true;// 是否记住密码
	private String phone;// 记住号码
	private String password;// 记住密码
	private boolean isMusic;// 是否打开音乐
	private String city = "";
	private String provice = "";
	private String imei = "";
	private String token = "";

	private SaveDate(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}

	/*** con最好需要全局context ***/
	public static SaveDate getInstence(Context con) {
		if (sd == null) {
			sd = new SaveDate(con);
			sd.readDate();
		}
		return sd;
	}

	public void saveDate() {
		spf = con
				.getSharedPreferences("saveDate", Context.MODE_WORLD_WRITEABLE);
		Editor ed = spf.edit();
		ed.putBoolean("isRecord", isRecord);
		ed.putString("phone", phone);
		ed.putString("token", token);
		ed.putString("password", password);
		ed.putBoolean("isMusic", isMusic);
		ed.putString("city", city);
		ed.putString("provice", provice);
		ed.putString("imei", imei);
		ed.commit();
	}

	public void setIsonce() {
		if (spf == null) {
			spf = con.getSharedPreferences("saveDate",
					Context.MODE_WORLD_WRITEABLE);
		}
		Editor ed = spf.edit();
		ed.putBoolean("isOnce", false);
		ed.commit();
	}

	public void readDate() {
		spf = con.getSharedPreferences("saveDate", Context.MODE_WORLD_READABLE);
		isOnce = spf.getBoolean("isOnce", true);
		isRecord = spf.getBoolean("isRecord", true);
		phone = spf.getString("phone", "");
		token = spf.getString("token", "");
		password = spf.getString("password", "");
		isMusic = spf.getBoolean("isMusic", false);
		city = spf.getString("city", "");
		provice = spf.getString("provice", "");
	}

	public boolean isOnce() {
		readDate();
		return isOnce;
	}

	public void setRecord(boolean isChecked) {
		isRecord = isChecked;
		saveDate();
	}

	public boolean isRecord() {
		readDate();
		return isRecord;
	}

	public void setPhone(String phone, String password) {
		this.phone = phone;
		this.password = password;
		saveDate();
	}

	public String getPhone() {
		readDate();
		return phone;
	}

	public void setToken(String token) {
		this.token = token;
		saveDate();
	}

	public String getToken() {
		readDate();
		return token;
	}

	public void setImei(String imei) {
		this.imei = imei;
		saveDate();
	}

	public String getImei() {
		readDate();
		return imei;
	}

	public String getPassword() {
		readDate();
		return password;
	}

	public boolean isMusic() {
		readDate();
		return isMusic;
	}

	public void setMusic(boolean isMusic) {
		this.isMusic = isMusic;
		saveDate();
	}

	public String getCity() {
		readDate();
		return city;
	}

	public void setCity(String city) {
		this.city = city;
		saveDate();
	}

	public String getProvice() {
		readDate();
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
		saveDate();
	}

}
