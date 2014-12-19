package com.hzkjkf.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.adapter.DrawRecord_listview_adapter2;
import com.hzkjkf.adapter.SuperRecord_listview_adapter2;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.view.Foot;
import com.hzkjkf.view.FootListoner;
import com.hzkjkf.view.Head;
import com.hzkjkf.view.HeadListoner;
import com.hzkjkf.view.MyListView;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
public class DrawRecordActivity2 extends Activity implements HeadListoner,
		FootListoner, OnClickListener {
	/***** 普通商品 ****/
	private MyListView drawRecord_listView;
	/** 超级拍 **/
	private MyListView drawRecord_listView1;
	/***** 普通商品数据 ****/
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	/***** 超级拍数据 ****/
	private List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
	/**** 普通商品适配器 ***/
	private DrawRecord_listview_adapter2 ela;
	private SuperRecord_listview_adapter2 sla;
	private TextView putong_tv, chaojipai_tv;
	private int pageNumber = 1;
	private int pageNumber1 = 1;
	private String recordType = "ordinary";
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (drawRecord_listView.foot.current == Foot.UPDATA) {
				drawRecord_listView.foot.logic(Foot.INIT);
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(DrawRecordActivity2.this, "网络不给力，请检查网络连接",
						Toast.LENGTH_SHORT).show();
				drawRecord_listView.head.logic(com.hzkjkf.view.Head.NO);
				drawRecord_listView1.head.logic(com.hzkjkf.view.Head.NO);
				break;
			case 1:// 刷新数据完成
				drawRecord_listView.head.logic(com.hzkjkf.view.Head.OK);
				ela.notifyDataSetChanged();
				break;
			case 3:// 刷新数据完成隐藏foot
				Toast.makeText(DrawRecordActivity2.this, "加载完成",
						Toast.LENGTH_SHORT).show();
				ela.notifyDataSetChanged();
				drawRecord_listView.foot.logic(Foot.INIT);
				break;
			}
		};
	};
	Handler hd1 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (drawRecord_listView1.foot.current == Foot.UPDATA) {
				drawRecord_listView1.foot.logic(Foot.INIT);
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(DrawRecordActivity2.this, "网络不给力，请检查网络连接",
						Toast.LENGTH_SHORT).show();
				drawRecord_listView1.head.logic(com.hzkjkf.view.Head.NO);
				drawRecord_listView.head.logic(com.hzkjkf.view.Head.NO);
				break;
			case 1:// 刷新数据完成
				drawRecord_listView1.head.logic(com.hzkjkf.view.Head.OK);
				sla.notifyDataSetChanged();
				break;
			case 3:// 刷新数据完成隐藏foot
				Toast.makeText(DrawRecordActivity2.this, "加载完成",
						Toast.LENGTH_SHORT).show();
				sla.notifyDataSetChanged();
				drawRecord_listView1.foot.logic(Foot.INIT);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawrecord2);
		((TextView) findViewById(R.id.title)).setText("消费记录");
		drawRecord_listView = (MyListView) findViewById(R.id.listview);
		drawRecord_listView1 = (MyListView) findViewById(R.id.listview1);
		putong_tv = (TextView) findViewById(R.id.putong_tv);
		chaojipai_tv = (TextView) findViewById(R.id.textView2);
		putong_tv.setOnClickListener(this);
		chaojipai_tv.setOnClickListener(this);
		drawRecord_listView.SetOnHeadRefreshListener(this);
		drawRecord_listView.SetOnFootRefreshListener(this);
		drawRecord_listView1.SetOnHeadRefreshListener(this);
		drawRecord_listView1.SetOnFootRefreshListener(this);
		ela = new DrawRecord_listview_adapter2(this, list);
		sla = new SuperRecord_listview_adapter2(this, list1);
		drawRecord_listView1.setAdapter(sla);
		drawRecord_listView.setAdapter(ela);
		drawRecord_listView.foot.logic(Foot.UPDATA);// 进入界面直接显示正在加载
		initData(1);
		initData1(1);
	}

	private void listAddTitle() {
		Map<String, String> map_title = new HashMap<String, String>();
		map_title.put("time", "消费时间");
		map_title.put("name", "消费类容");
		map_title.put("money", "消费玩币");
		map_title.put("statas", "消费状态");
		list.add(map_title);
	}

	private void list1AddTitle() {
		Map<String, String> map_title = new HashMap<String, String>();
		map_title.put("name", "商品");
		map_title.put("num", "购买份数");
		map_title.put("myCode", "抽奖号码");
		map_title.put("Code", "幸运号码");
		map_title.put("getName", "幸运得主");
		list1.add(map_title);
	}

	/** 获取下一页兑换的数据 ***/
	private void initData1(final int type) {
		// TODO Auto-generated method stub
		final String url = HttpTool.getUrl(new String[] { "classId",
				"phoneNumber", "requestId", "imei", "pageNum", "costType" },
				new String[] { "10063", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei(), String.valueOf(pageNumber),
						"super" });
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String json = HttpTool.httpGetJson1(DrawRecordActivity2.this,
						url, hd);
				// String json=HttpTool.readJson(DrawRecordActivity2.this,
				// R.raw.superjson);
				if (!json.isEmpty()) {
					Map<String, String> map1 = HttpTool.jsonToMap(json);
					if (!Boolean.parseBoolean(map1.get("flag"))) {
						HandleUtil.sendInt(hd,
								Integer.parseInt(map1.get("errorCode")));
						return;
					}
					JSONArray array = HttpTool.getResult(json);
					final ArrayList<Map<String, String>> temp = new ArrayList<Map<String, String>>();
					if (pageNumber1 == 1) {
						HandleUtil.post(hd, new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								list1.clear();
								list1AddTitle();
							}
						});
					}
					if (array.length() == 19) {
						pageNumber1 += 1;
					} else {
						pageNumber1 = -1;
					}
					for (int i = 0; i < array.length(); i++) {
						try {
							final Map<String, String> map = new HashMap<String, String>();
							JSONObject data = array.getJSONObject(i);
							map.put("num", data.getString("NUM"));
							map.put("name", data.getString("GOOD_NAME"));
							map.put("myCode",
									data.getString("CODE").replace(",", "\n"));
							map.put("Code",
									FormatStringUtil.isEmpty(data
											.getString("LUCK_CODE")) ? "暂无"
											: data.getString("LUCK_CODE"));
							map.put("getName",
									FormatStringUtil.isEmpty(data
											.getString("USER_NAME")) ? "暂无"
											: data.getString("USER_NAME"));
							temp.add(map);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					HandleUtil.post(hd1, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							list1.addAll(temp);
						}
					});
					if (type == 1) {
						HandleUtil.sendInt(hd1, 3);
					} else {
						HandleUtil.sendInt(hd1, 1);
					}
				} else {
					HandleUtil.sendInt(hd1, 0);
				}
				HandleUtil.post(hd1, new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						drawRecord_listView1.foot.logic(Foot.INIT);
					}
				});
			}
		}).start();
	}

	/** 获取下一页兑换的数据 ***/
	private void initData(final int type) {
		// TODO Auto-generated method stub
		final String url = HttpTool.getUrl(new String[] { "classId",
				"phoneNumber", "requestId", "imei", "pageNum", "costType" },
				new String[] { "10063", MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei(), String.valueOf(pageNumber),
						"ordinary" });
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String json = HttpTool.httpGetJson1(DrawRecordActivity2.this,
						url, hd);
				if (!json.isEmpty()) {
					Map<String, String> map1 = HttpTool.jsonToMap(json);
					if (!Boolean.parseBoolean(map1.get("flag"))) {
						HandleUtil.sendInt(hd,
								Integer.parseInt(map1.get("errorCode")));
						return;
					}
					JSONArray array = HttpTool.getResult(json);
					final ArrayList<Map<String, String>> temp = new ArrayList<Map<String, String>>();
					if (pageNumber == 1) {
						HandleUtil.post(hd, new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
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
							map.put("statas", data.getString("ORDER_STATE"));
							map.put("num", data.getString("GOOD_NUM"));
							map.put("time", data.getString("CREATE_TIME"));
							map.put("name", data.getString("GOOD_NAME") + "x"
									+ map.get("num"));
							map.put("money",
									""
											+ String.valueOf(data
													.getDouble("TOTAL_MONEY")
													+ data.getDouble("COSTS")));

							// map.put("time", data.getString("finishTime"));
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
						drawRecord_listView.foot.logic(Foot.INIT);
					}
				});
			}
		}).start();
	}

	// private void initData() {
	// // TODO Auto-generated method stub
	// for (int i = 0; i <20; i++) {
	// Map<String, String> map=new HashMap<String, String>();
	// map.put("time", "09-12 16:55:12");
	// map.put("name", "Q币1个");
	// map.put("money", "10玩币");
	// map.put("statas", "支付完成");
	// list.add(map);
	// }
	//
	// }

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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hd = null;
	}

	@Override
	public void headRefresh() {
		// TODO Auto-generated method stub
		pageNumber = 1;
		pageNumber1 = 1;
		if (recordType.equals("ordinary")) {
			initData(2);
		} else {
			initData1(2);
		}
	}

	@Override
	public void footRefresh() {
		// TODO Auto-generated method stub
		if ((pageNumber == -1 && recordType.equals("ordinary"))
				|| (pageNumber1 == -1 && recordType.equals("super"))) {
			Toast.makeText(this, "没有更多记录", Toast.LENGTH_SHORT).show();
			return;
		}

		if (recordType.equals("ordinary")) {// 如果是普通商品刷新
			drawRecord_listView.foot.logic(Foot.UPDATA);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					initData(1);
				}
			}).start();
		} else {// 如果是超级拍商品刷新
			drawRecord_listView1.foot.logic(Foot.UPDATA);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					initData1(1);
				}
			}).start();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.putong_tv) {
			drawRecord_listView.setVisibility(View.VISIBLE);
			drawRecord_listView1.setVisibility(View.GONE);
			putong_tv.setTextColor(Color.RED);
			putong_tv.setBackgroundColor(Color.WHITE);
			chaojipai_tv.setTextColor(Color.WHITE);
			chaojipai_tv.setBackgroundColor(0x00E22732);
			recordType = "ordinary";
		} else if (id == R.id.textView2) {
			drawRecord_listView1.setVisibility(View.VISIBLE);
			drawRecord_listView.setVisibility(View.GONE);
			putong_tv.setTextColor(Color.WHITE);
			putong_tv.setBackgroundColor(0x00E22732);
			chaojipai_tv.setTextColor(Color.RED);
			chaojipai_tv.setBackgroundColor(Color.WHITE);
			recordType = "super";
		}

	}
}
