package com.hzkjkf.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.activity.StartEarnActivity;
import com.hzkjkf.fragment.MoreFragment;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/** 常见问题 **/
public class Help extends Activity implements OnItemClickListener {
	private ListView help_listview;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private int type;
	private ProgressDialog dialog;
	Myadapter ma;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(Help.this, "加载失败，请检查网络连接", 1000).show();
				dialog.cancel();
				break;
			case 1:
				if (dialog != null) {
					dialog.cancel();
				}
				ma.notifyDataSetChanged();
				break;
			default:
				Toast.makeText(Help.this, ErrorCode.getString(msg.what), 1000)
						.show();
				dialog.cancel();
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
		setContentView(R.layout.activity_help);
		type = this.getIntent().getIntExtra("type", 0);
		((TextView) findViewById(R.id.title)).setText("新手帮助");
		help_listview = (ListView) findViewById(R.id.listview_help);
		help_listview.setOnItemClickListener(this);
		initData();
		ma = new Myadapter();
		help_listview.setAdapter(ma);
	}

	/*** 初始化数据 ***/
	private void initData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei" }, new String[] {
						"10067", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(Help.this, url, hd);
				if (!json.isEmpty()) {
					LogUtils.e("flag", String.valueOf(HttpTool.getFlag(json)));
					if (!HttpTool.getFlag(json)) {
						hd.sendEmptyMessage(HttpTool.getErrorCode(json));
						return;
					}
					JSONArray result = HttpTool.getResult(json);
					for (int i = 0; i < result.length(); i++) {
						try {
							JSONObject object = result.getJSONObject(i);
							Map<String, String> map = new HashMap<String, String>();
							map.put("title", object.getString("newTitle"));
							map.put("url", object.getString("newContextUrl"));
							list.add(map);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					hd.sendEmptyMessage(1);
				} else {
					hd.sendEmptyMessage(0);
				}
			}
		}).start();

	}

	public void backClick(View v) {
		this.finish();
	}

	class Myadapter extends BaseAdapter {
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
				convertView = View.inflate(Help.this,
						R.layout.item_more_listview, null);
			}
			TextView tv1 = (TextView) convertView
					.findViewById(R.id.textview_title);
			ImageView iv = (ImageView) convertView
					.findViewById(R.id.imageView2);
			iv.setImageResource(R.drawable.help);
			tv1.setText(list.get(position).get("title"));
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, TrendsInfo.class);
		intent.putExtra("url", list.get(position).get("url"));
		intent.putExtra("title", list.get(position).get("title"));
		this.startActivity(intent);
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
}
