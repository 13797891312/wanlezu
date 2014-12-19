package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.util.BitmapUtil;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import com.tencent.mm.sdk.modelbase.BaseResp.ErrCode;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignAcitivity extends Activity {
	private Bitmap bitmap;
	private GridView sign_gridView;
	private ArrayList<MyDate> list = new ArrayList<SignAcitivity.MyDate>();
	private com.hzkjkf.activity.SignAcitivity.myAdapter myAdapter;
	private ViewStub loading;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(SignAcitivity.this, "网络不给力，请检查网络",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(SignAcitivity.this,
						ErrorCode.getString(msg.what), Toast.LENGTH_SHORT)
						.show();
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
		setContentView(R.layout.activity_sign);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mrqd);
		((ImageView) findViewById(R.id.rel)).setImageBitmap(bitmap);
		loading = (ViewStub) findViewById(R.id.loading);
		loading.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.title)).setText("每日签到");
		((TextView) findViewById(R.id.textView_time)).setText(getDay());
		((TextView) findViewById(R.id.textView11)).setText((aCalendar
				.get(Calendar.MONTH) + 1) + "月签到日历");
		sign_gridView = (GridView) findViewById(R.id.gridview_sign);
		myAdapter = new myAdapter();
		sign_gridView.setAdapter(myAdapter);
		initList();
	}

	private void initList() {
		// TODO Auto-generated method stub
		int endday = getDayOfMonth();
		for (int i = 1; i <= endday; i++) {
			MyDate date = new MyDate(i);
			list.add(date);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10032", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool
						.httpGetJson1(SignAcitivity.this, url, hd);
				if (json != null && !json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}

					JSONArray result = HttpTool.getResult(json);
					try {
						JSONObject jo = result.getJSONObject(0);
						JSONArray data = jo.getJSONArray("data");
						MyApp.getInstence().setSurplus(
								MyApp.getInstence().getSurplusFloat()
										+ Float.parseFloat(jo
												.getString("money")));
						MyApp.getInstence().setHomeData(6, "0");
						final String msg = jo.getString("dMsg");
						for (int i = 0; i < data.length(); i++) {
							JSONObject object = data.getJSONObject(i);
							String time = object.getString("signTime");
							int position = Integer.parseInt(time.substring(time
									.lastIndexOf("-") + 1));
							list.get(position - 1).setSign(true);
						}
						HandleUtil.post(hd, new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(SignAcitivity.this, msg,
										Toast.LENGTH_SHORT).show();
								myAdapter.notifyDataSetChanged();
								loading.setVisibility(View.GONE);

							}
						});

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

	class myAdapter extends BaseAdapter {

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
				convertView = View.inflate(SignAcitivity.this,
						R.layout.item_sign_gridview, null);
			}
			RelativeLayout rel = (RelativeLayout) convertView
					.findViewById(R.id.rel);
			TextView tv = (TextView) convertView.findViewById(R.id.textView1);
			tv.setText(String.valueOf(list.get(position).getDate()));
			if (list.get(position).isSign()) {
				rel.setBackgroundColor(Color.RED);
				tv.setTextColor(Color.WHITE);
			}
			return convertView;
		}

	}

	/** 日历元素 ***/
	class MyDate {
		private boolean isSign = false;
		private int date;

		public MyDate(int date) {
			// TODO Auto-generated constructor stub
			this.date = date;
		}

		public boolean isSign() {
			return isSign;
		}

		public void setSign(boolean isSign) {
			this.isSign = isSign;
		}

		public int getDate() {
			return date;
		}

		public void setDate(int date) {
			this.date = date;
		}
	}

	Calendar aCalendar = Calendar.getInstance(Locale.CHINA);

	/** 获取当月最大天数 ***/
	public int getDayOfMonth() {
		int day = aCalendar.getActualMaximum(Calendar.DATE);
		return day;
	}

	/*** 获取当前日期 **/
	public String getDay() {
		int mon = aCalendar.get(Calendar.MONTH) + 1;
		int day = aCalendar.get(Calendar.DAY_OF_MONTH);
		return aCalendar.get(Calendar.YEAR) + "-"
				+ (mon >= 10 ? mon : "0" + mon) + "-"
				+ (day >= 10 ? day : "0" + day);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BitmapUtil.recycle(bitmap);
	}
}
