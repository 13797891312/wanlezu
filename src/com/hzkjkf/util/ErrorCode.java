package com.hzkjkf.util;

public class ErrorCode {
	public static String getString(int errorCode) {
		String result = null;
		switch (errorCode) {
		case 10001:
			result = "�û����Ѵ���";
			break;
		case 10002:
			result = "ȱ�ٲ���";// ȱ�ٲ���
			break;
		case 10003:
			result = "�ն�ע���Ѵ�����";
			break;
		case 10004:
			result = "�û������������";
			break;
		case 10005:
			result = "�û������������";
			break;
		case 10006:
			result = "�˻�������";
			break;
		case 10007:
			result = "�������ݳ������Ժ�����";
			break;
		case 10008:
			result = "�˺�������ʧЧ�������µ�¼";
			break;
		case 10009:
			result = "�˺�������ʧЧ�������µ�¼";
			break;
		case 10010:
			result = "���ֽ�����";
			break;
		case 10011:
			result = "��ȡ�û�����ʧ��";
			break;
		case 10012:
			result = "�û�����";
			break;
		case 10013:
			result = "�ɿ��������������";
			break;
		case 10014:
			result = "�û����϶�ȡʧ�ܣ����˺��ѱ�����";
			break;
		case 10015:
			result = "�����ʧЧ";
			break;
		case 10016:
			result = "��Ϸ��������";
			break;
		case 10017:
			result = "��ȡ��Ϸ����";
			break;
		case 10018:
			result = "ԭʼ��������";
			break;
		case 10019:
			result = "��ȡ�������ʧ��";
			break;
		case 10020:
			result = "�û�����Ѵ�����";
			break;
		case 10021:
			result = "��ǰ������ղ�";
			break;
		case 10022:
			result = "�Ƿ�����";
			break;
		case 10023:
			result = "��ȡ������֤��ʧ��";
			break;
		case 10024:
			result = "������֤ʧ��";
			break;
		case 10026:
			result = "��治���ڻ�״̬�쳣";
			break;
		case 10027:
			result = "���������¼�";
			break;
		case 10029:
			result = "��ȡ���Ŵ����Ѵ�����";
			break;
		case 10032:
			result = "�绰�����ѱ�ע�ᣬ��ֱ�ӵ�½";
			break;
		case 10030:
			result = "ͬһ�˺����ֻ����3���ն�";
			break;
		case 10033:
			result = "ÿ��ֻ������һ�Σ�����������";
			break;
		case 10066:
			result = "�����Ѿ��������";
			break;
		case 10067:
			result = "�µ��������������";
			break;
		case 10071:
			result = "�޸�����ʧ�ܣ������ϴ��޸�QQ�Ų�����30�죬�ݲ����޸�";
			break;
		case 10072:
			result = "�޸�����ʧ�ܣ������ϴ��޸�֧�����Ų�����30�죬�ݲ����޸�";
			break;
		case 10073:
			result = "QQ���ѱ������˺Ű󶨣���������д";
			break;
		case 10074:
			result = "֧�����Ѿ��������˺Ű󶨣���������д";
			break;
		case 10080:
			result = "�ֻ��ź���������Ʒ��ƥ��";
			break;
		}
		return FormatStringUtil.isEmpty(result) ? "����" + errorCode : result;
	}

}
