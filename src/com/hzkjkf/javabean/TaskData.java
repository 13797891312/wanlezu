package com.hzkjkf.javabean;

import java.io.Serializable;

public class TaskData implements Serializable {
	/** 详细页列表数据 ***/
	private String version;
	private int task1_money, task2_money, task3_money;
	private String task1_info, task2_info, task3_info;
	/*** 0代表可完成，1代表已完成，2代表不可完成 ***/
	private int task1_state, task2_state, task3_state;
	/*** 应用名 ***/
	private String name;
	/*** 包名 **/
	private String APKname;
	private String size;
	private int money;
	private int type = 0;
	private String smallImageUrl;
	/** 简介信息 ***/
	private String info;
	private String uids;
	private String downUrl;
	private String imageUrl;
	/** 详细信息 ***/
	private String appInfo;
	/**** 从表ID ****/
	private String logUids;
	/** 每步骤完成试玩时间 **/
	private int tryTime1, tryTime2, tryTime3;

	public int getTryTime1() {
		return tryTime1;
	}

	public void setTryTime1(int tryTime1) {
		this.tryTime1 = tryTime1;
	}

	public int getTryTime2() {
		return tryTime2;
	}

	public void setTryTime2(int tryTime2) {
		this.tryTime2 = tryTime2;
	}

	public int getTryTime3() {
		return tryTime3;
	}

	public void setTryTime3(int tryTime3) {
		this.tryTime3 = tryTime3;
	}

	public String getLogUids() {
		return logUids;
	}

	public void setLogUids(String logUids) {
		this.logUids = logUids;
	}

	public String getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(String appInfo) {
		this.appInfo = appInfo;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getTask1_money() {
		return task1_money;
	}

	public void setTask1_money(int task1_money) {
		this.task1_money = task1_money;
	}

	public int getTask2_money() {
		return task2_money;
	}

	public void setTask2_money(int task2_money) {
		this.task2_money = task2_money;
	}

	public int getTask3_money() {
		return task3_money;
	}

	public void setTask3_money(int task3_money) {
		this.task3_money = task3_money;
	}

	public String getTask1_info() {
		return task1_info;
	}

	public void setTask1_info(String task1_info) {
		this.task1_info = task1_info;
	}

	public String getTask2_info() {
		return task2_info;
	}

	public void setTask2_info(String task2_info) {
		this.task2_info = task2_info;
	}

	public String getTask3_info() {
		return task3_info;
	}

	public void setTask3_info(String task3_info) {
		this.task3_info = task3_info;
	}

	public int getTask1_state() {
		return task1_state;
	}

	public void setTask1_state(int task1_state) {
		this.task1_state = task1_state;
	}

	public int getTask2_state() {
		return task2_state;
	}

	public void setTask2_state(int task2_state) {
		this.task2_state = task2_state;
	}

	public int getTask3_state() {
		return task3_state;
	}

	public void setTask3_state(int task3_state) {
		this.task3_state = task3_state;
	}

	public String getUids() {
		return uids;
	}

	public void setUids(String uids) {
		this.uids = uids;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAPKname() {
		return APKname;
	}

	public void setAPKname(String aPKname) {
		APKname = aPKname;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSmallImageUrl() {
		return smallImageUrl;
	}

	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getToast() {
		if (task1_state == 0) {
			return task1_info;
		} else if (task2_state == 0) {
			return task2_info;
		} else {
			return task3_info;
		}
	}

	/***
	 * 获取当前任务步骤
	 * ***/
	public int getDesc_Type() {
		if (task1_state == 0) {
			return 0;
		} else if (task2_state == 0) {
			return 1;
		} else if (task3_state == 0) {
			return 2;
		} else {
			return -1;
		}
	}

	/***
	 * 获取当前任务步骤试玩时间
	 * ***/
	public int getDesc_time() {
		if (task1_state == 0) {
			return tryTime1;
		} else if (task2_state == 0) {
			return tryTime2;
		} else if (task3_state == 0) {
			return tryTime3;
		} else {
			return 60 * 60 * 1000;
		}
	}

}
