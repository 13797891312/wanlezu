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

/** ׬Ǯ��¼ ***/
public class ReferalRecordActivity extends Activity implements HeadListoner,
		FootListoner {
	private MyListView refRecord_listView;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private earnRecord_listview_adapter2 ela;
	private TextView relCount_tv, relMoney;
	private int pageNumber = 1;
	private String count = "0";
	private String money = "0";
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (refRecord_listView.foot.current == Foot.UPDATA) {
				refRecord_listView.foot.logic(Foot.INIT);
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(ReferalRecordActivity.this, "���粻������������������",
						Toast.LENGTH_SHORT).show();
				refRecord_listView.head.logic(com.hzkjkf.view.Head.NO);
				break;
			case 1:// ˢ���������
				refRecord_listView.head.logic(com.hzkjkf.view.Head.OK);
				ela.notifyDataSetChanged();
				break;
			case 3:// ˢ�������������foot
				Toast.makeText(ReferalRecordActivity.this, "�������",
						Toast.LENGTH_SHORT).show();
				ela.notifyDataSetChanged();
				refRecord_listView.foot.logic(Foot.INIT);
				break;
			default:
				Toast.makeText(ReferalRecordActivity.this,
						ErrorCode.getString(msg.what), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refrecord);
		((TextView) findViewById(R.id.title)).setText("�����¼");
		refRecord_listView = (MyListView) findViewById(R.id.listview);
		relCount_tv = (TextView) findViewById(R.id.textView1);
		relMoney = (TextView) findViewById(R.id.textView2);
		refRecord_listView.SetOnHeadRefreshListener(this);
		refRecord_listView.SetOnFootRefreshListener(this);
		ela = new earnRecord_listview_adapter2(this, list);
		refRecord_listView.setAdapter(ela);
		refRecord_listView.foot.logic(Foot.UPDATA);// �������ֱ����ʾ���ڼ���
		initData(1);
	}

	private void listAddTitle() {
		Map<String, String> map_title = new HashMap<String, String>();
		map_title.put("time", "ע��ʱ��");
		map_title.put("name", "������ID");
		map_title.put("money", "���ѽ���");
		list.add(map_title);
	}

	/** �Ƽ��б��������� **/
	private void initData(final int type) {
		final String url = HttpTool.getUrl(new String[] { "classId",
				"phoneNumber", "requestId", "imei", "pageNum" }, new String[] {
				"10064", MyApp.getInstence().getPhone(),
				MyApp.getInstence().getToken(), MyApp.getInstence().getImei(),
				String.valueOf(pageNumber) });
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String json = HttpTool.httpGetJson1(ReferalRecordActivity.this,
						url, hd);
				if (!json.isEmpty()) {
					Map<String, String> map1 = HttpTool.jsonToMap(json);
					if (!Boolean.parseBoolean(map1.get("flag"))) {
						HandleUtil.sendInt(hd,
								Integer.parseInt(map1.get("errorCode")));
						return;
					}
					final ArrayList<Map<String, String>> temp = new ArrayList<Map<String, String>>();
					try {
						JSONObject object1 = new JSONArray(json).getJSONObject(
								0).getJSONObject("totalInfo");
						count = object1.getString("friendCounts");
						money = FormatStringUtil.getDesplay(object1
								.getString("inviteCount"));
						JSONArray array = HttpTool.getResult(json);
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
						if (array.length() == 4) {
							pageNumber += 1;
						} else {
							pageNumber = -1;
						}
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.getJSONObject(i);
							final Map<String, String> map = new HashMap<String, String>();
							map.put("name",
									object.getString("USER_PHONE").substring(0,
											3)
											+ "****"
											+ object.getString("USER_PHONE")
													.substring(7, 11));
							map.put("mony", object.getString("MONEY"));
							map.put("time", object.getString("CREATE_TIME"));
							temp.add(map);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					HandleUtil.post(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							list.addAll(temp);
							relCount_tv.setText("���������" + count + "λ");
							relMoney.setText("��׬ȡ���ѽ���" + money + "���");
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
						refRecord_listView.foot.logic(Foot.INIT);
					}
				});
			}
		}).start();
	}

	// private void initData() {
	// // TODO Auto-generated method stub
	// for (int i = 0; i <10; i++) {
	// Map<String, String> map=new HashMap<String, String>();
	// map.put("time", "09-12 16:55:12");
	// map.put("name", "13797891312");
	// map.put("money", "10���");
	// list.add(map);
	// }
	// ela.notifyDataSetChanged();
	// }

	/** ���ذ�ť���� ***/
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
			Toast.makeText(this, "û�и����¼", Toast.LENGTH_SHORT).show();
			return;
		}
		refRecord_listView.foot.logic(Foot.UPDATA);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				initData(1);
			}
		}).start();
	}

}
