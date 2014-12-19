package com.hzkjkf.javabean;

public class QuestionBean_GMO {
	// 1 ans_stat_cd 回答状态 01：未回答 02：已回答
	// 2 arrivalDay 调查到达日期 ○
	// 3 custom_nm 自定义调查名称
	// 4 encryptId 加密字符串 用于问卷的重定向 ○
	// 5 enqPerPanelStatus 各个Panel的配额组状态 05：实施中 07：已关闭
	// 6 enq_id 配额组ID
	// 7 id Panel代码
	// GMOR用于识别贵公司Panel的代码
	// (用于生成问卷链接)
	// ○
	// 8 lg_img 分类图标
	// 9 lg_nm 分类名称
	// 10 main_enq_id Main-Survey的配额组ID
	// 11 matter_type 工程类别
	// 12 point 积分( 数字 )
	// 13 Point_min 最少积分( 数字 )
	// 14 point_string 积分( 日文 )
	// 15 point_type 积分类别 0：即时 1：事后赋予 2：抽奖
	// 16 redirectSt 问卷链接的 URL ○
	// 17 research_id 调查ID ○
	// 18 research_type 调查类别
	// 19 si_img 状态图标
	// 20 situation 回答状态 未回答 , 已回答, 已结束 ○
	// 21 start_dt 调查开始日期
	// 22 status 配额组状态
	// 00：未开始 05：实施中
	// 07：已关闭 99：删除
	// 23 title 调查名称 ○
	/*** 状态，已回答和未回答 ***/
	private String state = "";
	private String url = "";
	/*** 结束时间 **/
	private String finishTime = "";
	private String title = "";
	/*** 加密字符串 用于问卷的重定向 **/
	private String encryptId = "";
	/**** 各个Panel的配额组状态 05：实施中 07：已关闭 ***/
	private String enqPerPanelStatus = "";
	/***** 积分( 数字 ) ****/
	private String point = "";
	/*** 最少积分( 数字 ) ***/
	private String point_min="";
	/*** panelID,用于生成链接 ***/
	private String id = "";
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEncryptId() {
		return encryptId;
	}

	public void setEncryptId(String encryptId) {
		this.encryptId = encryptId;
	}

	public String getEnqPerPanelStatus() {
		return enqPerPanelStatus;
	}

	public void setEnqPerPanelStatus(String enqPerPanelStatus) {
		this.enqPerPanelStatus = enqPerPanelStatus;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getPoint_min() {
		return point_min;
	}

	public void setPoint_min(String point_min) {
		this.point_min = point_min;
	}
}
