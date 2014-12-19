package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.activity.baidu.BaiduMapActivity;
import com.hzkjkf.adapter.News_listview_Adapter;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.util.MySqliteHelper;
import com.hzkjkf.util.SqlInfo;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class NewsActivity extends Activity implements OnItemLongClickListener,
		OnItemClickListener {
	private ListView news_listview;
	private ViewStub loading_viewstub;
	private List<Map<String, String>> data_list = new ArrayList<Map<String, String>>();
	private News_listview_Adapter adapter;
	private MySqliteHelper sqliteHelper;
	private Button send_btn;
	private EditText msg_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		sqliteHelper = new MySqliteHelper(this);
		((TextView) findViewById(R.id.title)).setText("我的消息");
		news_listview = (ListView) findViewById(R.id.news_listview);
		loading_viewstub = (ViewStub) findViewById(R.id.viewstub_loading);
		loading_viewstub.setVisibility(View.VISIBLE);
		initData();
		adapter = new News_listview_Adapter(this, data_list);

		news_listview.setAdapter(adapter);
		news_listview.setOnItemLongClickListener(this);
		news_listview.setOnItemClickListener(this);
	}

	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(NewsActivity.this, "加载失败，请检查网络连接", 1000).show();
				break;
			case 1:
				if (loading_viewstub != null) {
					loading_viewstub.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
				}
				break;
			case 3:
				Toast.makeText(NewsActivity.this, "发送成功", 1000).show();
				msg_edit.setText("");
				break;

			}
		};
	};

	/*** 请求消息数据 ***/
	private void initData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10005", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(NewsActivity.this, url, hd);
				if (!json.isEmpty()) {
					JSONArray result = HttpTool.getResult(json);
					for (int i = 0; i < result.length(); i++) {
						try {
							JSONObject object = result.getJSONObject(i);
							String type = object.getString("messageType");
							JSONArray ja2 = object.getJSONArray("data");
							for (int j = 0; j < ja2.length(); j++) {
								JSONObject object2 = ja2.getJSONObject(j);
								Map<String, String> map = new HashMap<String, String>();
								map.put("type", type);
								map.put("date", object2.getString("data"));
								map.put("userName",
										FormatStringUtil.isEmpty(object2
												.getString("userName")) ? "未知用户"
												: object2.getString("userName"));
								map.put("from", object2.getString("from"));
								map.put("sellerName",
										object2.getString("sellerName"));
								map.put("message",
										object2.getString("messageContext"));
								ContentValues values = new ContentValues();
								values.put(SqlInfo.TYPE, map.get("type"));
								values.put(SqlInfo.USERNAME,
										map.get("userName"));
								values.put(SqlInfo.TIME, map.get("date"));
								values.put(SqlInfo.SELLERNAME,
										map.get("sellerName"));
								values.put(SqlInfo.TEXT, map.get("message"));
								values.put(SqlInfo.FROM, map.get("from"));
								values.put(SqlInfo.PHONE, MyApp.getInstence()
										.getPhone());
								values.put(SqlInfo.ISREADED, "false");
								sqliteHelper.insertData(values);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					readFromSqlite();
					HandleUtil.sendInt(hd, 1);
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
	}

	/** 读取数据库消息 ***/
	private void readFromSqlite() {
		Cursor cursor = sqliteHelper.getInfoCursor(SqlInfo.PHONE + "=?",
				new String[] { MyApp.getInstence().getPhone() });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("type",
					cursor.getString(cursor.getColumnIndex(SqlInfo.TYPE)));
			map.put("from",
					cursor.getString(cursor.getColumnIndex(SqlInfo.FROM)));
			map.put("userName",
					cursor.getString(cursor.getColumnIndex(SqlInfo.USERNAME)));
			map.put("date",
					cursor.getString(cursor.getColumnIndex(SqlInfo.TIME)));
			map.put("sellerName",
					cursor.getString(cursor.getColumnIndex(SqlInfo.SELLERNAME)));
			map.put("message",
					cursor.getString(cursor.getColumnIndex(SqlInfo.TEXT)));
			map.put("id", String.valueOf(cursor.getInt(cursor
					.getColumnIndex(SqlInfo.ID))));
			map.put("isRead", String.valueOf(cursor.getString(cursor
					.getColumnIndex(SqlInfo.ISREADED))));
			data_list.add(0, map);
		}
		cursor.close();
	}

	public void backClick(View v) {
		hd = null;
		this.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sqliteHelper.close();
		hd = null;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		createDialog(position);// 长按弹出是否删除对话框
		return true;
	}

	/** 删除对话框弹出 ***/
	public void createDialog(final int position) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(R.drawable.error);
		builder.setTitle("");
		builder.setMessage("是否删除此消息？");
		builder.setPositiveButton("取消", null);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				initDelData(position);
				data_list.remove(position);
				adapter.notifyDataSetChanged();
			}
		});
		builder.create().show();
	}

	/** 请求删除数据 ***/
	private void initDelData(int position) {
		sqliteHelper.deleteData(SqlInfo.ID + "=?", new String[] { data_list
				.get(position).get("id") });
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		createPop(position);

		/*** 修改短信数据为已读 ***/
		if (!Boolean.valueOf(data_list.get(position).get("isRead"))) {
			ContentValues values = new ContentValues();
			values.put(SqlInfo.ISREADED, "true");
			sqliteHelper.updateData(values, SqlInfo.ID + "=?",
					new String[] { data_list.get(position).get("id") });
			data_list.get(position).put("isRead", "true");
			adapter.notifyDataSetChanged();

		}
	}

	/** 消息展开 **/
	private void createPop(final int position) {
		// TODO Auto-generated method stub
		View view = View.inflate(this, R.layout.pop_trends, null);
		((TextView) view.findViewById(R.id.textView_title)).setText(data_list
				.get(position).get("sellerName"));
		if (data_list.get(position).get("type").equals("3")) {
			((TextView) view.findViewById(R.id.textView_from)).setText("来自："
					+ data_list.get(position).get("userName"));
		} else {
			((TextView) view.findViewById(R.id.textView_from)).setText("来自："
					+ "玩乐族官方");
		}
		((TextView) view.findViewById(R.id.textView_text)).setText(data_list
				.get(position).get("message"));
		send_btn = (Button) view.findViewById(R.id.button1);
		send_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMsg(data_list.get(position).get("from"));
			}
		});
		msg_edit = (EditText) view.findViewById(R.id.editText1);
		if (data_list.get(position).get("type").equals("3")) {
			send_btn.setVisibility(View.VISIBLE);
			msg_edit.setVisibility(View.VISIBLE);
		}
		// ((TextView)view.findViewById(R.id.textView_from)).setText("来自："+list.get(position).get("from"));
		((TextView) view.findViewById(R.id.textView_time)).setText("时间："
				+ data_list.get(position).get("date"));
		Dialog dialog = new Dialog(this,
				android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.setContentView(view);
		dialog.show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	void sendMsg(final String phone) {
		if (msg_edit.getText().toString().isEmpty()) {
			Toast.makeText(this, "请输入消息类容", Toast.LENGTH_SHORT).show();
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei", "sendUser",
						"userMsg" }, new String[] { "10037",
						MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei(), phone,
						msg_edit.getText().toString() });
				String json = HttpTool.httpGetJson1(NewsActivity.this, url, hd);
				if (!json.isEmpty()) {
					if (HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, 3);
					} else {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
					}
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
	}
}
