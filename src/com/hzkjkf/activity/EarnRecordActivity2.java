package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.adapter.earnRecord_listview_adapter2;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.view.Foot;
import com.hzkjkf.view.FootListoner;
import com.hzkjkf.view.Head;
import com.hzkjkf.view.HeadListoner;
import com.hzkjkf.view.MyListView;
import com.hzkjkf.view.Myview;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/** 赚钱记录 ***/
public class EarnRecordActivity2 extends Activity implements HeadListoner,
		FootListoner {
	private MyListView earnRecord_listView;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private earnRecord_listview_adapter2 ela;
	private int pageNumber = 1;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (earnRecord_listView.foot.current == Foot.UPDATA) {
				earnRecord_listView.foot.logic(Foot.INIT);
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(EarnRecordActivity2.this, "网络不给力，请检查网络连接",
						Toast.LENGTH_SHORT).show();
				earnRecord_listView.head.logic(com.hzkjkf.view.Head.NO);
				break;
			case 1:// 刷新数据完成
				earnRecord_listView.head.logic(com.hzkjkf.view.Head.OK);
				ela.notifyDataSetChanged();
				break;
			case 3:// 刷新数据完成隐藏foot
				Toast.makeText(EarnRecordActivity2.this, "加载完成",
						Toast.LENGTH_SHORT).show();
				ela.notifyDataSetChanged();
				earnRecord_listView.foot.logic(Foot.INIT);
				break;
			default:
				Toast.makeText(EarnRecordActivity2.this,
						ErrorCode.getString(msg.what), Toast.LENGTH_SHORT)
						.show();
				earnRecord_listView.head.logic(com.hzkjkf.view.Head.NO);
				earnRecord_listView.foot.logic(Foot.INIT);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_earnrecord2);
		((TextView) findViewById(R.id.title)).setText("赚钱记录");
		earnRecord_listView = (MyListView) findViewById(R.id.listview);
		earnRecord_listView.SetOnHeadRefreshListener(this);
		earnRecord_listView.SetOnFootRefreshListener(this);
		ela = new earnRecord_listview_adapter2(this, list);
		earnRecord_listView.setAdapter(ela);
		earnRecord_listView.foot.logic(Foot.UPDATA);
		listAddTitle();
		initData(1);
	}

	private void listAddTitle() {
		Map<String, String> map_title = new HashMap<String, String>();
		map_title.put("time", "完成时间");
		map_title.put("name", "任务名称");
		map_title.put("money", "赚得玩币");
		list.add(map_title);
	}

	/** 赚钱记录数据下载 **/
	private void initData(final int type) {
		final String url = HttpTool.getUrl(new String[] { "classId",
				"phoneNumber", "requestId", "imei", "pageNum" }, new String[] {
				"10062", MyApp.getInstence().getPhone(),
				MyApp.getInstence().getToken(), MyApp.getInstence().getImei(),
				String.valueOf(pageNumber) });
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String json = HttpTool.httpGetJson1(EarnRecordActivity2.this,
						url, hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					JSONArray array = HttpTool.getResult(json);
					final ArrayList<Map<String, String>> temp = new ArrayList<Map<String, String>>();
					if (pageNumber == 1) {
						HandleUtil.post(hd, new Runnable() {
							public void run() {
								list.clear();
								listAddTitle();
							}
						});
					}
					if (array.length() == 19) {
						pageNumber += 1;
					} else {
						pageNumber = -1;
					}
					for (int i = 0; i < array.length(); i++) {
						try {
							final Map<String, String> map = new HashMap<String, String>();
							JSONObject data = array.getJSONObject(i);
							map.put("name", data.getString("TASKNAME"));
							map.put("mony", data.getString("MAKECOUNT"));
							map.put("time", data.getString("FINISHTIME"));
							temp.add(map);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					HandleUtil.post(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							list.addAll(temp);
						}
					});
					if (type == 1) {
						HandleUtil.sendInt(hd, 3);
					} else {
						HandleUtil.sendInt(hd, 1);
					}
				} else {
					HandleUtil.sendInt(hd, 0);
				}
				HandleUtil.post(hd, new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						earnRecord_listView.foot.logic(Foot.INIT);
					}
				});
			}
		}).start();
	}

	/** 返回按钮监听 ***/
	public void backClick(View v) {
		this.finish();
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

	@Override
	public void headRefresh() {
		// TODO Auto-generated method stub
		pageNumber = 1;
		initData(2);
	}

	@Override
	public void footRefresh() {
		// TODO Auto-generated method stub
		if (pageNumber == -1) {
			Toast.makeText(this, "没有更多记录", Toast.LENGTH_SHORT).show();
			return;
		}
		earnRecord_listView.foot.logic(Foot.UPDATA);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				initData(1);
			}
		}).start();
	}

}
