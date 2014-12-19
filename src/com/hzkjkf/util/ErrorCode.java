package com.hzkjkf.util;

public class ErrorCode {
	public static String getString(int errorCode) {
		String result = null;
		switch (errorCode) {
		case 10001:
			result = "用户名已存在";
			break;
		case 10002:
			result = "缺少参数";// 缺少参数
			break;
		case 10003:
			result = "终端注册已达上限";
			break;
		case 10004:
			result = "用户名或密码错误";
			break;
		case 10005:
			result = "用户名或密码错误";
			break;
		case 10006:
			result = "账户被锁定";
			break;
		case 10007:
			result = "请求数据出错，请稍后再试";
			break;
		case 10008:
			result = "账号连接已失效，请重新登录";
			break;
		case 10009:
			result = "账号连接已失效，请重新登录";
			break;
		case 10010:
			result = "提现金额错误";
			break;
		case 10011:
			result = "读取用户资料失败";
			break;
		case 10012:
			result = "用户余额不足";
			break;
		case 10013:
			result = "可看广告数量已用完";
			break;
		case 10014:
			result = "用户资料读取失败，或账号已被锁定";
			break;
		case 10015:
			result = "广告已失效";
			break;
		case 10016:
			result = "游戏次数不足";
			break;
		case 10017:
			result = "读取游戏错误";
			break;
		case 10018:
			result = "原始密码有误";
			break;
		case 10019:
			result = "读取广告问题失败";
			break;
		case 10020:
			result = "用户余额已达上限";
			break;
		case 10021:
			result = "当前广告已收藏";
			break;
		case 10022:
			result = "非法请求";
			break;
		case 10023:
			result = "获取短信验证码失败";
			break;
		case 10024:
			result = "短信验证失败";
			break;
		case 10026:
			result = "广告不存在或状态异常";
			break;
		case 10027:
			result = "广告可能已下架";
			break;
		case 10029:
			result = "获取短信次数已达上限";
			break;
		case 10032:
			result = "电话号码已被注册，请直接登陆";
			break;
		case 10030:
			result = "同一账号最多只能有3个终端";
			break;
		case 10033:
			result = "每天只能提现一次，请明天再试";
			break;
		case 10066:
			result = "今天已经分享过啦";
			break;
		case 10067:
			result = "下单数量超过库存量";
			break;
		case 10071:
			result = "修改资料失败，距离上次修改QQ号不超过30天，暂不能修改";
			break;
		case 10072:
			result = "修改资料失败，距离上次修改支付宝号不超过30天，暂不能修改";
			break;
		case 10073:
			result = "QQ号已被其他账号绑定，请重新填写";
			break;
		case 10074:
			result = "支付宝已经被其他账号绑定，请重新填写";
			break;
		case 10080:
			result = "手机号和流量包产品不匹配";
			break;
		}
		return FormatStringUtil.isEmpty(result) ? "错误" + errorCode : result;
	}

}
