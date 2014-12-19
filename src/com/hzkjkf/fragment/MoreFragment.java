package com.hzkjkf.fragment;

import java.util.HashMap;
import java.util.Map;

import com.hzkjkf.activity.LoginActivity;
import com.hzkjkf.more.About;
import com.hzkjkf.more.Help;
import com.hzkjkf.more.PassWord;
import com.hzkjkf.more.Rank;
import com.hzkjkf.more.Trends;
import com.hzkjkf.more.Version;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import com.umeng.fb.ContactActivity;
import com.umeng.fb.ConversationActivity;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Constants;
import com.umeng.fb.model.UserInfo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MoreFragment extends BaseFragment implements OnItemClickListener {
	private ListView listview_more;
	private String str[] = { "�޸�����", "�������", "���ְ���", "����������", "�����嶯̬", "�汾����",
			"�û�����" };
	private int id[] = { R.drawable.grsz, R.drawable.wzbd, R.drawable.xsbz,
			R.drawable.gywz, R.drawable.wzdt, R.drawable.bbgx, R.drawable.shdz };
	private Class<?> more_class[] = { PassWord.class, Rank.class, Help.class,
			About.class, Trends.class, Version.class };
	private boolean isLoading = true;
	private Handler hd = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.fragment_more, container,
				false);
		listview_more = (ListView) view.findViewById(R.id.listview_more);
		Button foot = new Button(this.getActivity());
		foot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MoreFragment.this.getActivity(),
						LoginActivity.class);//
				MoreFragment.this.startActivity(intent);
				MoreFragment.this.getActivity().finish();
			}
		});
		foot.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
		foot.setText("�˳���¼");
		foot.setBackgroundColor(Color.rgb(200, 69, 44));
		foot.setTextColor(Color.WHITE);
		listview_more.addFooterView(foot);

		listview_more.setOnItemClickListener(this);
		MyAdapter ma = new MyAdapter();
		listview_more.setAdapter(ma);
		// hd.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// if (view != null && view.getWidth() > 0 && view.getHeight() > 0) {
		// // �˴������activity�Ѿ���ʼ�����
		// // ֹͣ���
		// getTotalHeightofListView(listview_more);
		// hd.removeCallbacks(this);
		// } else {
		// hd.postDelayed(this, 20);
		// }
		// }
		// }, 10);
		return view;
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
				convertView = View.inflate(MoreFragment.this.getActivity(),
						R.layout.item_more_listview, null);
				TextView tv1 = (TextView) convertView
						.findViewById(R.id.textview_title);
				TextView tv2 = (TextView) convertView
						.findViewById(R.id.textView2);
				ImageView iv = (ImageView) convertView
						.findViewById(R.id.imageView2);
				iv.setImageResource(id[position]);
				tv1.setText(str[position]);
				if (position == str.length - 2) {
					tv2.setText("��ǰ�汾 : v" + MyApp.getInstence().version);
					tv2.setVisibility(View.VISIBLE);
				}
			}
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (position == str.length - 1) {
			userFB();// �û�����
		} else {
			Intent intent = new Intent(this.getActivity(), more_class[position]);//
			this.startActivity(intent);
		}
	}

	private void userFB() {
		FeedbackAgent agent = new FeedbackAgent(this.getActivity());
		UserInfo info = agent.getUserInfo();
		if (info == null)
			info = new UserInfo();
		Map<String, String> contact = info.getContact();
		if (contact == null)
			contact = new HashMap<String, String>();
		String contact_info = MyApp.getInstence().getPhone();
		contact.put("plain", contact_info);
		info.setContact(contact);
		agent.setUserInfo(info);
		agent.startFeedbackActivity();
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

	}

	// public static void getTotalHeightofListView(ListView listView) {
	// ListAdapter mAdapter = listView.getAdapter();
	// if (mAdapter == null) {
	// return;
	// }
	// int totalHeight = 0;
	// View view=listView.getChildAt(0);
	// totalHeight=view.getMeasuredHeight()*mAdapter.getCount();
	// Log.w("HEIGHT", String.valueOf(totalHeight));
	// ViewGroup.LayoutParams params = listView.getLayoutParams();
	// params.height = totalHeight + (listView.getDividerHeight() *
	// (mAdapter.getCount() - 1));
	// listView.setLayoutParams(params);
	// listView.requestLayout();
	// }

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
