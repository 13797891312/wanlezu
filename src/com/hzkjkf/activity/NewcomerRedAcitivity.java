package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.javabean.RedData;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewcomerRedAcitivity extends Activity implements
		OnItemClickListener {
	private ListView red_listview;
	private ArrayList<RedData> redList = new ArrayList<RedData>();
	String type = "";
	private String title;
	private MyAdapter ma;
	private ProgressDialog dialog;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(NewcomerRedAcitivity.this, "网络不给力，请检查网络",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				ma.notifyDataSetChanged();
				break;
			default:
				Toast.makeText(NewcomerRedAcitivity.this,
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
		setContentView(R.layout.activity_red);
		((ImageView) findViewById(R.id.image_baner))
				.setImageBitmap(RedAcitivity.backBitmap);
		title = getIntent().getStringExtra("title");
		type = getIntent().getStringExtra("type");
		((TextView) findViewById(R.id.title)).setText(title);
		if (type.equals("SHARE")) {
			((TextView) findViewById(R.id.textView_info))
					.setVisibility(View.VISIBLE);
		}
		red_listview = (ListView) findViewById(R.id.listview_red);
		ma = new MyAdapter();
		red_listview.setAdapter(ma);
		red_listview.setOnItemClickListener(this);
		initDate();
	}

	private void initDate() {
		dialog = ProgressDialog.show(this, "", "正在加载，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "packetType" },
						new String[] { "10035", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), type });
				String json = HttpTool.httpGetJson1(NewcomerRedAcitivity.this,
						url, hd);
				if (!json.isEmpty()) {
					JSONArray result = HttpTool.getResult(json);
					for (int i = 0; i < result.length(); i++) {
						try {
							JSONObject object = result.getJSONObject(i);
							RedData data = new RedData();
							data.setName(object.getString("redPacketName"));
							data.setMoney(object.getString("wbCount"));
							data.setState(object.getInt("state"));
							data.setId(object.getString("uids"));

							redList.add(data);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					LogUtils.e("sdaf", redList.size() + "");
					HandleUtil.sendInt(hd, 1);
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
		// TODO Auto-generated method stub
	}

	public void backClick(View v) {
		hd = null;
		this.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return redList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return redList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = View.inflate(NewcomerRedAcitivity.this,
					R.layout.item_reditem_listview, null);
			((TextView) convertView.findViewById(R.id.textView1))
					.setText((int) Float.parseFloat(FormatStringUtil
							.getDesplay(redList.get(position).getMoney()))
							+ "玩币");
			((TextView) convertView.findViewById(R.id.textview_title))
					.setText(redList.get(position).getName());
			if (type.equals("NEWER")) {
				if (redList.get(position).getState() == 0) {
					((TextView) convertView.findViewById(R.id.textView3))
							.setText("可领取");
					((TextView) convertView.findViewById(R.id.textView3))
							.setTextColor(Color.RED);

				} else if (redList.get(position).getState() == 1) {
					((TextView) convertView.findViewById(R.id.textView3))
							.setText("已领取");
				} else if (redList.get(position).getState() == 2) {
					((TextView) convertView.findViewById(R.id.textView3))
							.setText("不可领取");
				}
			} else {
				if (redList.get(position).getState() == 0) {
					((TextView) convertView.findViewById(R.id.textView3))
							.setText("不可领取");
				} else if (redList.get(position).getState() == 1) {
					((TextView) convertView.findViewById(R.id.textView3))
							.setText("已领取");
				} else if (redList.get(position).getState() == 2) {
					((TextView) convertView.findViewById(R.id.textView3))
							.setText("可领取");
					((TextView) convertView.findViewById(R.id.textView3))
							.setTextColor(Color.RED);
				}
			}
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (type.equals("NEWER")) {
			if (redList.get(position).getState() == 0) {
				getRed(position);
			} else if (redList.get(position).getState() == 1) {
				Toast.makeText(this, "红包已经被领取", Toast.LENGTH_SHORT).show();
			} else if (redList.get(position).getState() == 2) {
				Toast.makeText(this, "红包不可领取", Toast.LENGTH_SHORT).show();
			}
		} else {
			if (redList.get(position).getState() == 0) {
				Toast.makeText(this, "红包不可领取", Toast.LENGTH_SHORT).show();
			} else if (redList.get(position).getState() == 1) {
				Toast.makeText(this, "红包已经被领取", Toast.LENGTH_SHORT).show();
			} else if (redList.get(position).getState() == 2) {
				getRed(position);
			}
		}
	}

	private void getRed(final int position) {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在领取，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool
						.getUrl(new String[] { "classId", "phoneNumber",
								"requestId", "imei", "redPackUid" },
								new String[] {
										"10036",
										MyApp.getInstence().getPhone(),
										MyApp.getInstence().getToken(),
										MyApp.getInstence().getImei(),
										String.valueOf(redList.get(position)
												.getId()) });
				String json = HttpTool.httpGetJson1(NewcomerRedAcitivity.this,
						url, hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					JSONArray result = HttpTool.getResult(json);
					redList.get(position).setState(1);
					MyApp.getInstence().setSurplus(
							MyApp.getInstence().getSurplusFloat()
									+ Float.parseFloat(redList.get(position)
											.getMoney()));
					int count = Integer.parseInt(MyApp.getInstence()
							.getHomeData()[7]);
					count = count - 1;
					MyApp.getInstence().setHomeData(7, String.valueOf(count));
					HandleUtil.post(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog.cancel();
							Toast.makeText(
									NewcomerRedAcitivity.this,
									"成功领取玩币"
											+ FormatStringUtil
													.getDesplay(redList.get(
															position)
															.getMoney()),
									Toast.LENGTH_SHORT).show();
							ma.notifyDataSetChanged();
						}
					});
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
	}
}
