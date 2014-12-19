package com.hzkjkf.activity;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hzkjkf.util.BitmapUtil;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.OnSnsPlatformClickListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShareAcitivity extends Activity implements OnItemClickListener,
		OnClickListener {
	private ImageView ewm_iv;
	private ListView share_listview;
	private TextView copyDown_tv, copy_tv, url_tv, downUrl_tv;
	private final int WI = 400, HI = 400;
	String str[] = { "新浪微博", "腾讯微博", "QQ空间", "微信朋友圈", "人人网" };
	private MyAdapter ma;
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private RelativeLayout share_layout;
	private Bitmap Referral_bitmap;
	private String url, url_down;
	private ProgressDialog dialog;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(ShareAcitivity.this, "网络不给力，请检查网络",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				ma = new MyAdapter();
				share_listview.setAdapter(ma);
				break;
			case 2:
				Toast.makeText(ShareAcitivity.this, "分享成功", Toast.LENGTH_LONG)
						.show();
				ma.notifyDataSetChanged();
				break;
			default:

				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_share);
		((TextView) findViewById(R.id.title)).setText("乐推广");
		url = "http://www.wanzhuan6.com/appService/loadAPK/"
				+ MyApp.getInstence().InviteCode;
		url_down = MyApp.downAddress + MyApp.getInstence().InviteCode;
		url_tv = (TextView) findViewById(R.id.textView4);
		downUrl_tv = (TextView) findViewById(R.id.textView_downAdress);
		ewm_iv = (ImageView) findViewById(R.id.imageView_ewm);
		downUrl_tv.setText(url_down);
		url_tv.setText(url);
		copyDown_tv = (TextView) findViewById(R.id.textView6);
		copy_tv = (TextView) findViewById(R.id.textView7);
		copyDown_tv.setOnClickListener(this);
		copy_tv.setOnClickListener(this);
		share_listview = (ListView) findViewById(R.id.listview_share);
		share_listview.setOnItemClickListener(this);
		initData();
		umShare();
		share_layout = (RelativeLayout) findViewById(R.id.layout_share);
		WXShare();
		QQShare();
		Referral_bitmap = createImage();
		ewm_iv.setImageBitmap(Referral_bitmap);
		mController.getConfig().setPlatforms(SHARE_MEDIA.QZONE,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.TENCENT,
				SHARE_MEDIA.SINA, SHARE_MEDIA.RENREN, SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN);
		share_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mController.openShare(ShareAcitivity.this, false);
				mController.registerListener(snsPostListener);
			}
		});

	}

	private void initData() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在加载，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10038", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(ShareAcitivity.this, url,
						hd);
				if (!json.isEmpty()) {
					JSONArray result = HttpTool.getResult(json);
					for (int i = 0; i < result.length(); i++) {
						try {
							JSONObject object = result.getJSONObject(i);
							Map<String, String> map = new HashMap<String, String>();
							map.put("shareType", object.getString("shareType"));
							map.put("wbConunt", object.getString("wbConunt"));
							map.put("state", object.getString("state"));
							map.put("name", str[i]);
							list.add(map);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					HandleUtil.sendInt(hd, 1);
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();

	}

	public void backClick(View v) {
		BitmapUtil.recycle(Referral_bitmap);
		hd = null;
		this.finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		BitmapUtil.recycle(Referral_bitmap);
		super.onBackPressed();
		hd = null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	class MyAdapter extends BaseAdapter {
		int id[] = { R.drawable.xlwb, R.drawable.txwb, R.drawable.qqkj,
				R.drawable.wxpyq, R.drawable.rrw };

		public MyAdapter() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = View.inflate(ShareAcitivity.this,
						R.layout.item_share_listview, null);
			}
			TextView state_tv = (TextView) convertView
					.findViewById(R.id.textView2);
			TextView money_tv = (TextView) convertView
					.findViewById(R.id.textView1);
			money_tv.setText("+"
					+ FormatStringUtil.getDesplay(list.get(position).get(
							"wbConunt")) + "玩币");
			if (list.get(position).get("state").equals("0")) {
				state_tv.setText("未完成");
				state_tv.setTextColor(Color.RED);
			} else {
				state_tv.setText("已完成");
				state_tv.setTextColor(Color.GRAY);
			}
			((ImageView) convertView.findViewById(R.id.imageView2))
					.setImageResource(id[position]);
			((TextView) convertView.findViewById(R.id.textview_title))
					.setText(str[position]);
			return convertView;
		}

	}

	private void umShare() {
		// 设置微信分享内容
		UMImage shareImage = new UMImage(this, R.drawable.u4_normal);
		shareImage.setTargetUrl(url);
		WeiXinShareContent weixinContent = new WeiXinShareContent(shareImage);
		weixinContent
				.setShareContent("玩乐族————变玩边赚钱的APP，更有机会1毛钱买iphone6，点击直接注册");
		weixinContent.setTitle("玩乐族分享");
		mController.setShareMedia(weixinContent);
		// 设置朋友圈分享类容
		CircleShareContent circleShareContent = new CircleShareContent(
				shareImage);
		circleShareContent.setShareContent("玩乐族分享");
		circleShareContent.setTitle("玩乐族————变玩边赚钱的APP，更有机会1毛钱买iphone6，点击直接注册");
		mController.setShareMedia(circleShareContent);
		// 腾讯微博分享内容
		UMImage shareImage1 = new UMImage(this, R.drawable.u4_normal);
		TencentWbShareContent tencentShareContent = new TencentWbShareContent(
				shareImage1);
		tencentShareContent
				.setShareContent("玩乐族，边玩边赚钱，更有机会1毛钱买iphone6，赶紧猛戳注册下载+" + url);
		mController.setShareMedia(tencentShareContent);

		// 其他
		// 设置分享内容
		mController.setShareContent("玩乐族，边玩边赚钱，更有机会1毛钱买iphone6，赶紧猛戳注册下载" + url);
		// 设置分享图片, 参数2为图片的url地址
		UMImage imageShare = new UMImage(this, R.drawable.u4_normal);
		imageShare.setTargetUrl(url);
		mController.setShareMedia(imageShare);
	}

	/*** 添加微信分享 **/
	private void WXShare() {
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wxdda391bef2a7a4fa";
		String appSecret = "0301085b7ce7224f8cd98240dddfbb96";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/*** 添加QQ分享 **/
	private void QQShare() {
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "101104412",
				"e0d6d9517229b1e445db61781bdea50d");
		qqSsoHandler.addToSocialSDK();

		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
				"101104412", "e0d6d9517229b1e445db61781bdea50d");
		qZoneSsoHandler.addToSocialSDK();

		// 设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		// 设置腾讯微博SSO handler
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
	}

	SnsPostListener snsPostListener = new SnsPostListener() {
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			Toast.makeText(ShareAcitivity.this, "分享开始", Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
			// TODO Auto-generated method stub
			if (arg1 == 200) {
				String name = arg0.name();
				Log.e("sadf", arg0 + "=" + arg0.name());
				if (arg0.name().equals("QZONE")) {
					initShareResult(2);
				} else if (arg0.name().equals("SINA")) {
					initShareResult(0);
				} else if (arg0.name().equals("RENREN")) {
					initShareResult(4);
				} else if (arg0.name().equals("WEIXIN_CIRCLE")) {
					initShareResult(3);
				} else if (arg0.name().equals("TENCENT")) {
					initShareResult(1);
				}
			} else
				Toast.makeText(ShareAcitivity.this, "分享失败" + arg1,
						Toast.LENGTH_LONG).show();
		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		mController.openShare(ShareAcitivity.this, false);
		mController.unregisterListener(snsPostListener);
		mController.registerListener(snsPostListener);
	}

	private void initShareResult(final int positon) {
		// TODO Auto-generated method stub
		if (list.get(positon).get("state").equals("1")) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "shareType" },
						new String[] { "10034", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(),
								list.get(positon).get("shareType") });
				String json = HttpTool.httpGetJson1(ShareAcitivity.this, url,
						hd);
				if (!json.isEmpty()) {
					if (HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, 2);
						list.get(positon).put("state", "1");
						MyApp.getInstence().setSurplus(
								MyApp.getInstence().getSurplusFloat()
										+ Float.parseFloat(list.get(positon)
												.get("wbConunt")));
					} else {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
					}
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		int id = v.getId();
		if (id == R.id.textView6) {
			cmb.setText(url_down);
			Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show();
		} else if (id == R.id.textView7) {
			// 得到剪贴板管理器
			cmb.setText(url);
			Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show();
		}
	}

	/** 弹出二维码视图 ***/
	private void creatDialog() {
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(Referral_bitmap);
		Dialog dialog = new Dialog(this,
				android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		dialog.setContentView(iv);
		dialog.show();
	}

	/** 创建二维码图片 **/
	private Bitmap createImage() {
		Bitmap bitmap = null;
		try {
			// 需要引入core包
			QRCodeWriter writer = new QRCodeWriter();
			String text = MyApp.ewmAddress + MyApp.getInstence().InviteCode;
			if (text == null || "".equals(text) || text.length() < 1) {
				return null;
			}

			// 把输入的文本转为二维码
			BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE, WI,
					HI);

			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(text,
					BarcodeFormat.QR_CODE, WI, HI, hints);
			int[] pixels = new int[WI * WI];
			for (int y = 0; y < WI; y++) {
				for (int x = 0; x < HI; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * WI + x] = 0xff000000;
					} else {
						pixels[y * WI + x] = 0xffffffff;
					}
				}
			}

			bitmap = Bitmap.createBitmap(WI, WI, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, WI, 0, 0, WI, WI);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
