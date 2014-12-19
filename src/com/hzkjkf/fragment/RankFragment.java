package com.hzkjkf.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.adapter.Rank_listview_atapter;
import com.hzkjkf.more.Rank;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class RankFragment extends BaseFragment {
	private ArrayList<Map<String, String>> data_list = new ArrayList<Map<String, String>>();
	private boolean isloading;// 是否已经加载
	private boolean isload;// 是否正在加载

	private View view;
	private ListView rank_listview;
	private Rank_listview_atapter rla;
	private String type;
	public Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1:
				Toast.makeText(RankFragment.this.getActivity(), "服务器无响应",
						Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(RankFragment.this.getActivity(), "当前无网络，请检查网络",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public RankFragment(String type) {
		// TODO Auto-generated constructor stub
		this.type = type;
	}

	@Override
	public boolean isIsloading() {
		// TODO Auto-generated method stub
		return isloading;
	}

	@Override
	public void setIsloading(boolean isloading) {
		// TODO Auto-generated method stub
		this.isloading = isloading;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_rank, container, false);
		rank_listview = (ListView) view.findViewById(R.id.listView_rank);
		addFooter();
		rla = new Rank_listview_atapter(this.getActivity(), data_list);
		rank_listview.setAdapter(rla);
		if (!isloading && !isload) {
			initData();
			isload = true;
		}
		return view;
	}

	private void initData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei", "pageNumber",
						"dataType" }, new String[] { "10029",
						MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei(), "1", type });
				String json = HttpTool.httpGetJson1(Rank.rank, url, hd);
				LogUtils.e("sdfasdf", "asdfas");
				if (!json.isEmpty()) {
					JSONArray array = HttpTool.getResult(json);
					for (int i = 0; i < array.length(); i++) {
						try {
							JSONObject object = array.getJSONObject(i);
							JSONArray data = object.getJSONArray("data");
							final ArrayList<Map<String, String>> temp_list = new ArrayList<Map<String, String>>();
							for (int j = 0; j < data.length(); j++) {
								final Map<String, String> map = new HashMap<String, String>();
								JSONObject object1 = data.getJSONObject(j);
								map.put("money",
										FormatStringUtil.getDesplay(object1
												.getString("MONEY")) + "玩币");
								map.put("phone",
										FormatStringUtil.isEmpty(object1
												.getString("USER_NAME")) ? "匿名"
												: object1
														.getString("USER_NAME"));
								map.put("rank", object1.getString("ROWNUM"));
								temp_list.add(map);
							}
							LogUtils.e("sdfasdf", "asdfas" + hd);
							HandleUtil.post(hd, new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									data_list.addAll(temp_list);
									rla.notifyDataSetChanged();
									isloading = true;
								}
							});
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					HandleUtil.sendInt(hd, -1);
				}

				isload = false;
			}
		}).start();

	}

	private void addFooter() {
		// TODO Auto-generated method stub
		View view = View.inflate(this.getActivity(),
				R.layout.item_rank_listview, null);
		((TextView) view.findViewById(R.id.textview_rank))
				.setTextColor(Color.RED);
		((TextView) view.findViewById(R.id.textview_phone))
				.setTextColor(Color.RED);
		((TextView) view.findViewById(R.id.textview_menoy))
				.setTextColor(Color.RED);
		rank_listview.addHeaderView(view);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean isIsload() {
		// TODO Auto-generated method stub
		return isLoad;
	}

	@Override
	public void setIsIsload(boolean isLoad) {
		// TODO Auto-generated method stub
		this.isLoad = isLoad;
	}
}
