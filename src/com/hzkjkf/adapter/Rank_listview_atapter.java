package com.hzkjkf.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.hzkjkf.util.LogUtils;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Rank_listview_atapter extends BaseAdapter {
	private Context context;
	private ArrayList<Map<String, String>> list;

	public Rank_listview_atapter(Context context,
			ArrayList<Map<String, String>> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}

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
		HolderView holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_rank_listview,
					null);
			holder = new HolderView();
			holder.rank_tv = (TextView) convertView
					.findViewById(R.id.textview_rank);
			holder.phone_tv = (TextView) convertView
					.findViewById(R.id.textview_phone);
			holder.money_tv = (TextView) convertView
					.findViewById(R.id.textview_menoy);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.rank_tv.setText(list.get(position).get("rank"));
		holder.phone_tv.setText(list.get(position).get("phone"));
		holder.money_tv.setText(list.get(position).get("money"));
		return convertView;
	}

	static class HolderView {
		TextView rank_tv;
		TextView phone_tv;
		TextView money_tv;
	}

}
