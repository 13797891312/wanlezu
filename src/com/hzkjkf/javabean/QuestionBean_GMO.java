package com.hzkjkf.javabean;

public class QuestionBean_GMO {
	// 1 ans_stat_cd �ش�״̬ 01��δ�ش� 02���ѻش�
	// 2 arrivalDay ���鵽������ ��
	// 3 custom_nm �Զ����������
	// 4 encryptId �����ַ��� �����ʾ���ض��� ��
	// 5 enqPerPanelStatus ����Panel�������״̬ 05��ʵʩ�� 07���ѹر�
	// 6 enq_id �����ID
	// 7 id Panel����
	// GMOR����ʶ���˾Panel�Ĵ���
	// (���������ʾ�����)
	// ��
	// 8 lg_img ����ͼ��
	// 9 lg_nm ��������
	// 10 main_enq_id Main-Survey�������ID
	// 11 matter_type �������
	// 12 point ����( ���� )
	// 13 Point_min ���ٻ���( ���� )
	// 14 point_string ����( ���� )
	// 15 point_type ������� 0����ʱ 1���º��� 2���齱
	// 16 redirectSt �ʾ����ӵ� URL ��
	// 17 research_id ����ID ��
	// 18 research_type �������
	// 19 si_img ״̬ͼ��
	// 20 situation �ش�״̬ δ�ش� , �ѻش�, �ѽ��� ��
	// 21 start_dt ���鿪ʼ����
	// 22 status �����״̬
	// 00��δ��ʼ 05��ʵʩ��
	// 07���ѹر� 99��ɾ��
	// 23 title �������� ��
	/*** ״̬���ѻش��δ�ش� ***/
	private String state = "";
	private String url = "";
	/*** ����ʱ�� **/
	private String finishTime = "";
	private String title = "";
	/*** �����ַ��� �����ʾ���ض��� **/
	private String encryptId = "";
	/**** ����Panel�������״̬ 05��ʵʩ�� 07���ѹر� ***/
	private String enqPerPanelStatus = "";
	/***** ����( ���� ) ****/
	private String point = "";
	/*** ���ٻ���( ���� ) ***/
	private String point_min="";
	/*** panelID,������������ ***/
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
