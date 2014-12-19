package com.hzkjkf.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.activity.MainActivity;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.util.MySound;
import com.hzkjkf.view.GameView;
import com.hzkjkf.view.GameView.Data;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Game2Activity extends Activity implements OnClickListener {
	private Map<String, String> map = new HashMap<String, String>();
	private ProgressDialog dialog;
	private TextView code_tv, time_tv, money_tv, next_tv, yue_tv;
	private boolean isOver;
	private EditText[] edits = new EditText[4];
	private TextView images[] = new TextView[4];
	private RelativeLayout history, ok_rel;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(Game2Activity.this, "网络不给力，请检查网络",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				initView();
				break;

			case 2:
				Toast.makeText(Game2Activity.this, "投注成功，请耐心等待开奖结果",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(Game2Activity.this,
						ErrorCode.getString(msg.what), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	private void initView() {
		// TODO Auto-generated method stub
		code_tv.setText("第" + map.get("code") + "期");
		money_tv.setText("本期奖池"
				+ FormatStringUtil.getDesplay(map.get("poolMoney")) + "玩币");
		next_tv.setText("上一期竞猜号码：" + map.get("beforeWinNumber"));
		edits[Integer.parseInt(map.get("resultIndex"))].setText(map
				.get("resultNumber"));
		edits[Integer.parseInt(map.get("resultIndex"))].setEnabled(false);
		images[Integer.parseInt(map.get("resultIndex"))].setText(map
				.get("resultNumber"));
		// time_tv.setText(FormatStringUtil.formatLongToTimeStr(FormatStringUtil.getMilTime(map.get("endTime"))-System.currentTimeMillis()));
		CountDownTimer timer = new CountDownTimer(
				FormatStringUtil.getMilTime(map.get("endTime"))
						- System.currentTimeMillis(), 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				time_tv.setText(FormatStringUtil
						.formatLongToTimeStr(millisUntilFinished));
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				time_tv.setText("开奖处理中");
				isOver = true;
			}
		}.start();
		if (isOver) {
			time_tv.setText("正在开奖中");
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game2);
		((TextView) findViewById(R.id.title)).setText("猜数字");
		code_tv = (TextView) findViewById(R.id.textView1);
		time_tv = (TextView) findViewById(R.id.textView2);
		money_tv = (TextView) findViewById(R.id.textView4);
		next_tv = (TextView) findViewById(R.id.textView12);
		yue_tv = (TextView) findViewById(R.id.textView_yue);
		yue_tv.setText("余额 : " + MyApp.getInstence().getSurplus() + "玩币");
		images[0] = (TextView) findViewById(R.id.imageView2);
		images[1] = (TextView) findViewById(R.id.imageView3);
		images[2] = (TextView) findViewById(R.id.imageView4);
		images[3] = (TextView) findViewById(R.id.imageView5);
		edits[0] = (EditText) findViewById(R.id.editText1);
		edits[1] = (EditText) findViewById(R.id.editText2);
		edits[2] = (EditText) findViewById(R.id.editText3);
		edits[3] = (EditText) findViewById(R.id.editText4);
		history = (RelativeLayout) findViewById(R.id.layout_submit);
		ok_rel = (RelativeLayout) findViewById(R.id.layout_ok);
		ok_rel.setOnClickListener(this);
		history.setOnClickListener(this);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在加载，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10045", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool
						.httpGetJson1(Game2Activity.this, url, hd);
				if (json != null && !json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					JSONArray result = HttpTool.getResult(json);
					try {
						/** 本期投注是否结束 **/
						isOver = !new JSONArray(json).getJSONObject(0)
								.getString("msg").isEmpty();
						for (int i = 0; i < result.length(); i++) {
							JSONObject object = result.getJSONObject(0);
							map.put("uids", object.getString("uids"));
							map.put("code", object.getString("code"));
							map.put("createTime",
									object.getString("createTime"));
							map.put("endTime", object.getString("endTime"));
							map.put("resultIndex",
									object.getString("resultIndex"));
							map.put("resultNumber",
									object.getString("resultNumber"));
							map.put("poolMoney", object.getString("poolMoney"));
							map.put("beforeWinNumber",
									object.getString("beforeWinNumber"));
							map.put("beforeIssue",
									object.getString("beforeIssue"));
						}
						HandleUtil.sendInt(hd, 1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						HandleUtil.sendInt(hd, 0);
					}

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
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.layout_submit) {
			Intent intent = new Intent(this, Game2RecordActivity.class);
			intent.putExtra("number", map.get("beforeIssue"));
			intent.putExtra("luck", map.get("beforeWinNumber"));
			this.startActivity(intent);
		} else if (id == R.id.layout_ok) {
			initSubmit();
		}

	}

	private void initSubmit() {
		// TODO Auto-generated method stub
		if (isOver) {
			Toast.makeText(this, "投注已结束，请等待下期", Toast.LENGTH_SHORT).show();
			return;
		}
		final String number = edits[0].getText().toString()
				+ edits[1].getText().toString() + edits[2].getText().toString()
				+ edits[3].getText().toString();
		if (number.length() != 4) {
			Toast.makeText(this, "请填写正确的投注号码", Toast.LENGTH_SHORT).show();
			return;
		}
		dialog = ProgressDialog.show(this, "", "正在提交，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei", "periodUids",
						"quizNumber" }, new String[] { "10046",
						MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei(), map.get("uids"), number });
				final String json = HttpTool.httpGetJson1(Game2Activity.this,
						url, hd);
				if (json != null && !json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}

					HandleUtil.post(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (dialog != null) {
								dialog.cancel();
							}
							Toast.makeText(Game2Activity.this,
									HttpTool.getMsg(json), Toast.LENGTH_SHORT)
									.show();
						}
					});
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
	}
}
