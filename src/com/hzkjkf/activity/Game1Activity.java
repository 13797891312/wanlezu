package com.hzkjkf.activity;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.activity.MainActivity;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.util.MySound;
import com.hzkjkf.view.GameView;
import com.hzkjkf.view.GameView.Data;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Game1Activity extends Activity implements OnClickListener {
	private TextView count_textview, surplus_textview;
	private Button start_button;
	private int count = 5;
	/**** 是否有加载过 *****/

	/** 中奖结果 **/
	private float bonusMoney = 0.0f;
	/*** 开始抽奖后时候已经返回数据，如果没返回转盘就不能停 ***/
	private boolean isOk;
	private String message;
	private MySound mySound;
	GameView gv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game1);
		((TextView) findViewById(R.id.title)).setText("幸运大转盘");
		gv = (GameView) findViewById(R.id.gemeView);
		surplus_textview = (TextView) findViewById(R.id.textview_surplus_value);
		count_textview = (TextView) findViewById(R.id.textView_count);
		start_button = (Button) findViewById(R.id.button_start);
		start_button.setOnClickListener(this);
		mySound = MySound.getInstence(this.getApplicationContext());
		surplus_textview.setText(MyApp.getInstence().getSurplus() + "玩币");
		initData();
		count_textview.setText("今天还剩" + count + "次抽奖机会");
	}

	private void initData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10013", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool
						.httpGetJson1(Game1Activity.this, url, hd);
				if (!json.isEmpty()) {
					JSONArray result = HttpTool.getResult(json);
					try {
						JSONObject object = result.getJSONObject(0);
						count = object.getInt("remainGameCount");
						HandleUtil.sendInt(hd, 4);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();

	}

	Data lastData;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (count <= 0) {
			Toast.makeText(this, "您今天的抽奖机会已用完，请明天再来", 2000).show();
			return;
		} else if (MyApp.getInstence().getSurplusFloat() < 0.1) {
			Toast.makeText(this, "您的余额不足", 2000).show();
			return;
		}
		// 发送抽奖请求
		httpPost();
		// 开始抽奖
		startBonus();

	}

	/*** 发送抽奖请求 ***/
	private void httpPost() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10012", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool
						.httpGetJson1(Game1Activity.this, url, hd);
				if (!json.isEmpty()) {
					JSONArray result = HttpTool.getResult(json);
					try {
						JSONObject object = result.getJSONObject(0);
						bonusMoney = Float.parseFloat(object
								.getString("picturePosition"));
						message = object.getString("rewardMsg");
						HandleUtil.sendInt(hd, 3);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
	}

	/** 开始转动 ***/
	private void startBonus() {
		start_button.setEnabled(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int count = 0;
				int time = 500;
				int num = 0;
				if (lastData != null) {
					count = lastData.getCount();
				}
				boolean isStop = false;
				boolean isSlow = false;

				int slowNum = 20 + new Random().nextInt(14);// 生成一个随机数用来控制转盘减速
				while (!isStop) {
					if (count >= gv.getList().size()) {
						count = 0;
					}
					gv.getList().get(count).setChecked(true);
					if (lastData != null) {
						lastData.setChecked(false);

					}
					lastData = gv.getList().get(count);
					HandleUtil.sendInt(hd, 1);
					try {
						if (time > 100 && num == 0) {
							time -= 50;
						} else {
							num++;
						}
						if (isOk && num >= slowNum && time <= 500
								&& lastData.getInfo() == bonusMoney) {
							isSlow = true;
						}
						if (isSlow) {
							time += 20;
						}
						if (time >= 400 && lastData.getInfo() == bonusMoney
								&& isSlow) {
							isStop = true;
							isSlow = false;
							HandleUtil.sendInt(hd, 2);
						}
						Thread.sleep(time);
						count++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				Toast.makeText(Game1Activity.this, "网络故障，本次抽奖作废", 3000).show();
				isOk = true;
			} else if (msg.what == 1) {
				mySound.play(mySound.getSound1());
				gv.invalidate();
			} else if (msg.what == 2) {
				MyApp.getInstence().setSurplus(
						MyApp.getInstence().getSurplusFloat() + bonusMoney);
				if (bonusMoney > 0) {
					mySound.play(mySound.getSound2());
					Toast.makeText(
							Game1Activity.this,
							"恭喜您中奖获得"
									+ FormatStringUtil.getDesplay(bonusMoney
											+ "") + "玩币", 3000).show();
				} else {
					mySound.play(mySound.getSound3());
					Toast.makeText(Game1Activity.this, "请再接再厉！", 3000).show();
				}
				bonusMoney = 0;
				surplus_textview.setText(MyApp.getInstence().getSurplus()
						+ "玩币");
				count_textview.setText("今天还剩" + count + "次抽奖机会");
				isOk = false;
				start_button.setEnabled(true);
			} else if (msg.what == 3) {// 得到结果成功，转盘开始转动
				MyApp.getInstence().setSurplus(
						MyApp.getInstence().getSurplusFloat() - 0.1f);
				count--;
				surplus_textview.setText(MyApp.getInstence().getSurplus()
						+ "玩币");
				count_textview.setText("今天还剩" + count + "次抽奖机会");
				isOk = true;

			} else if (msg.what == 4) {// 得到结果成功，转盘开始转动
				count_textview.setText("今天还剩" + count + "次抽奖机会");
			}
		};
	};

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hd = null;
	}

	public void backClick(View v) {
		this.finish();
	}
}
