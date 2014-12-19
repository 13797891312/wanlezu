package com.hzkjkf.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.javabean.YouHuiData;
import com.hzkjkf.listener.YaoYiYaoListener;
import com.hzkjkf.listener.YaoYiYaoListener.OnShakeListener;
import com.hzkjkf.update.UpDateInfo;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.impl.InitializeController;

import android.app.Activity;
import android.app.ProgressDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class YouHuiActivity extends Activity implements OnClickListener {
	/**** 摇动监听 ****/
	YaoYiYaoListener mShakeListener = null;
	/*** 震动服务 **/
	Vibrator mVibrator;
	private RelativeLayout youHui_rel;
	private TextView code_tv, name_tv, info_tv, count_tv;
	private Button get_btn;
	private ImageView iv1, iv2;
	private Animation ani1, ani2;
	private String advertiseUids;
	private int drawingNumber = 0;
	private YouHuiData youHuiInfo;
	private ProgressDialog dialog;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(YouHuiActivity.this, "当前无网络，请检查网络",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:// 摇到优惠券
				Toast.makeText(YouHuiActivity.this, "摇中一张优惠券，点击领取",
						Toast.LENGTH_LONG).show();
				youHui_rel.setVisibility(View.VISIBLE);
				code_tv.setText("优惠码 ：" + youHuiInfo.getUids());
				name_tv.setText(youHuiInfo.getName());
				info_tv.setText(youHuiInfo.getInfo());
				count_tv.setText("摇一摇，还剩" + drawingNumber + "次机会");
				break;
			case 2:
				Toast.makeText(YouHuiActivity.this, "很遗憾，您没有摇中优惠券，请再接再厉！",
						Toast.LENGTH_LONG).show();
				count_tv.setText("摇一摇，还剩" + drawingNumber + "次机会");
				break;
			case 3:
				youHui_rel.setVisibility(View.GONE);
				Toast.makeText(YouHuiActivity.this, "领取成功", 0).show();
				break;
			case 4:
				count_tv.setText("摇一摇，还剩" + drawingNumber + "次机会");
				break;
			default:
				Toast.makeText(YouHuiActivity.this,
						ErrorCode.getString(msg.what), 3000).show();
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
		setContentView(R.layout.activity_youhui);
		((TextView) findViewById(R.id.title)).setText("优惠券");
		advertiseUids = getIntent().getStringExtra("advertiseUids");
		code_tv = (TextView) findViewById(R.id.textView_code);
		name_tv = (TextView) findViewById(R.id.textView_name);
		info_tv = (TextView) findViewById(R.id.textView_info);
		youHui_rel = (RelativeLayout) findViewById(R.id.rel_youhui);
		count_tv = (TextView) findViewById(R.id.textView1);
		get_btn = (Button) findViewById(R.id.button1);
		get_btn.setOnClickListener(this);
		iv1 = (ImageView) findViewById(R.id.imageview_2);
		iv2 = (ImageView) findViewById(R.id.imageview_3);
		ani1 = AnimationUtils.loadAnimation(this, R.anim.shake_up);
		ani2 = AnimationUtils.loadAnimation(this, R.anim.shake_down);
		mShakeListener = new YaoYiYaoListener(this);
		mVibrator = (Vibrator) getApplication().getSystemService(
				VIBRATOR_SERVICE);
		mShakeListener.start();
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				startAni();
				mShakeListener.stop();
				mVibrator.vibrate(1000);
				hd.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mShakeListener.start();
						if (drawingNumber == 0) {
							Toast.makeText(YouHuiActivity.this, "您的摇奖次数已用完", 0);
						} else {
							initData();
						}
					}
				}, 1000);
			}
		});
		initCountData();
	}

	private void initCountData() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在加载，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "advertiseUids" },
						new String[] { "10051", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), advertiseUids });
				String json = HttpTool.httpGetJson1(YouHuiActivity.this, url,
						hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					JSONArray result = HttpTool.getResult(json);
					try {
						JSONObject object = result.getJSONObject(0);
						drawingNumber = object.getInt("drawingNumber");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					HandleUtil.sendInt(hd, 4);
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
	}

	private void startAni() {
		// TODO Auto-generated method stub
		iv1.startAnimation(ani1);
		iv2.startAnimation(ani2);

	}

	private void initData() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在抽奖，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "advertiseUids" },
						new String[] { "10040", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), advertiseUids });
				String json = HttpTool.httpGetJson1(YouHuiActivity.this, url,
						hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					JSONArray result = HttpTool.getResult(json);
					try {
						JSONObject object = result.getJSONObject(0);
						drawingNumber = object.getInt("drawingNumber");
						if (object.getBoolean("iswinning")) {
							JSONObject data = object.getJSONArray("data")
									.getJSONObject(0);
							youHuiInfo = new YouHuiData();
							youHuiInfo.setEndDate(data.getString("LIMIT_DATE"));
							youHuiInfo.setName(data.getString("COUPON_NAME"));
							youHuiInfo.setInfo(data.getString("COUPON_INFO"));
							youHuiInfo.setId(data.getString("COUPON_UIDS"));
							youHuiInfo.setUids(data.getString("COUPON_CODE"));
						} else {
							HandleUtil.sendInt(hd, 2);// 未中奖
							return;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					HandleUtil.sendInt(hd, 1);
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();

	}

	public void backClick(View v) {
		this.finish();
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mShakeListener.stop();
		mVibrator.cancel();
		hd = null;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.button1) {
			initGetData();
		}

	}

	private void initGetData() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在领取，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool
						.getUrl(new String[] { "classId", "phoneNumber",
								"requestId", "imei", "couponCode" },
								new String[] { "10042",
										MyApp.getInstence().getPhone(),
										MyApp.getInstence().getToken(),
										MyApp.getInstence().getImei(),
										youHuiInfo.getUids() });
				String json = HttpTool.httpGetJson1(YouHuiActivity.this, url,
						hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					JSONArray result = HttpTool.getResult(json);

					HandleUtil.sendInt(hd, 3);
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();

	}
}
