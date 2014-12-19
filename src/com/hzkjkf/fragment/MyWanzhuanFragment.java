package com.hzkjkf.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.activity.AdListActivity;
import com.hzkjkf.activity.CollectActivity;
import com.hzkjkf.activity.DrawRecordActivity2;
import com.hzkjkf.activity.EarnRecordActivity2;
import com.hzkjkf.activity.MainActivity;
import com.hzkjkf.activity.ReferalRecordActivity;
import com.hzkjkf.activity.UserInfoAcitivity;
import com.hzkjkf.activity.YouHuiRecordActivity;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.util.MyApp.SurplusChengedListener;
import com.hzkjkf.util.MySound;
import com.hzkjkf.view.GameView;
import com.hzkjkf.view.GameView.Data;
import com.hzkjkf.view.Head;
import com.hzkjkf.view.HeadListoner;
import com.hzkjkf.view.MyFreshfHead;
import com.hzkjkf.view.MyRefreshLayout;
import com.hzkjkf.wanzhuan.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyWanzhuanFragment extends BaseFragment implements
		OnItemClickListener, SurplusChengedListener {
	/**** 是否有加载过 *****/
	private boolean isLoading = false;
	private GridView gridview;
	private String str[] = { "个人资料", "赚钱记录", "消费记录", "邀请记录", "优惠券", "广告收藏" };
	private int id[] = { R.drawable.grzl, R.drawable.zqjl, R.drawable.dhjl,
			R.drawable.yqjl, R.drawable.yhq, R.drawable.ggsc };
	private TextView balance_tv, phone_tv, name_tv, lv_tv, jingyanzhi_tv;
	private MyRefreshLayout fresh_layout;
	private Handler hd;
	private ProgressBar pb;
	String progress = "";
	String max = "";
	String lv = "";
	HeadListoner listoner = new HeadListoner() {
		@Override
		public void headRefresh() {
			// TODO Auto-generated method stub
			initData();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_my, container, false);
		MyApp.getInstence().setOnSurplusCheanged(this);
		fresh_layout = (MyRefreshLayout) view.findViewById(R.id.fresh_layout);
		fresh_layout.SetOnHeadRefreshListener(listoner);
		hd = ((MainActivity) this.getActivity()).hd;
		pb = (ProgressBar) view.findViewById(R.id.progressBar_lv);
		balance_tv = (TextView) view.findViewById(R.id.textView_balance);
		phone_tv = (TextView) view.findViewById(R.id.textView_phone);
		name_tv = (TextView) view.findViewById(R.id.textView_name);
		lv_tv = (TextView) view.findViewById(R.id.textView_lv);
		jingyanzhi_tv = (TextView) view.findViewById(R.id.textView_jingyanzhi);
		phone_tv.setText("ID : " + MyApp.getInstence().getPhone());
		name_tv.setText("用户名  : " + MyApp.getInstence().name);
		lv_tv.setText("等级  : Lv" + MyApp.getInstence().lv);
		balance_tv.setText("玩币余额 : " + MyApp.getInstence().getSurplus());
		gridview = (GridView) view.findViewById(R.id.gridView_my);
		gridview.setOnItemClickListener(this);
		gridview.setAdapter(new MyAdapter());
		return view;
	}

	@Override
	public boolean isIsloading() {
		return isLoading;
	}

	@Override
	public void setIsloading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		if (!isLoading) {
			initData();
		}
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

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return str.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return str[position];
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
				convertView = View.inflate(
						MyWanzhuanFragment.this.getActivity(),
						R.layout.item_my_gridview, null);
				((TextView) convertView.findViewById(R.id.textView1))
						.setText(str[position]);
				((ImageView) convertView.findViewById(R.id.imageView1))
						.setImageResource(id[position]);
			}
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (position) {
		case 0:
			intent.setClass(this.getActivity(), UserInfoAcitivity.class);
			this.getActivity().startActivity(intent);
			break;
		case 1:
			intent.setClass(this.getActivity(), EarnRecordActivity2.class);
			this.getActivity().startActivity(intent);
			break;
		case 2:
			intent.setClass(this.getActivity(), DrawRecordActivity2.class);
			this.getActivity().startActivity(intent);
			break;
		case 3:
			intent.setClass(this.getActivity(), ReferalRecordActivity.class);
			this.getActivity().startActivity(intent);
			break;
		case 4:
			intent.setClass(this.getActivity(), YouHuiRecordActivity.class);
			this.getActivity().startActivity(intent);
			break;
		case 5:
			intent.setClass(this.getActivity(), CollectActivity.class);
			this.getActivity().startActivity(intent);
			break;
		}
	}

	@Override
	public void SurplusChenged() {
		// TODO Auto-generated method stub
		balance_tv.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				balance_tv
						.setText("玩币余额 : " + MyApp.getInstence().getSurplus());
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "typeUids" },
						new String[] { "10006", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(
						MyWanzhuanFragment.this.getActivity(), url, hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					final JSONObject object = HttpTool.getResultJson(json);
					try {
						progress = object.getString("experienceCount");
						max = object.getString("needMoney");
						lv = object.getString("curLvl");
						MyApp.getInstence().setSurplus(
								Float.parseFloat(object
										.getString("accountBalance")));
						isLoading = true;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						HandleUtil.sendInt(hd, 0);
						HandleUtil.post(hd, new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (fresh_layout.head != null) {
									fresh_layout.head.logic(MyFreshfHead.NO);
								}
							}
						});
					}
					HandleUtil.post(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							jingyanzhi_tv.setText((int) (Float
									.parseFloat(progress) * 100)
									+ "/"
									+ (int) (Float.parseFloat(max) * 100));
							pb.setMax((int) (Float.parseFloat(max) * 100));
							pb.setProgress((int) (Float.parseFloat(progress) * 100));
							lv_tv.setText("等级  : Lv"
									+ FormatStringUtil.getLvInt(lv));
							name_tv.setText("用户名  : "
									+ MyApp.getInstence().name);
							if (fresh_layout.head != null) {
								fresh_layout.head.logic(MyFreshfHead.OK);
							}
						}
					});
				} else {
					HandleUtil.sendInt(hd, 0);
					HandleUtil.post(hd, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (fresh_layout.head != null) {
								fresh_layout.head.logic(MyFreshfHead.NO);
							}
						}
					});
				}
			}
		}).start();
	}
}
