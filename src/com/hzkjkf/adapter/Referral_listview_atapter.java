package com.hzkjkf.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.wanzhuan.R;

public class Referral_listview_atapter extends BaseAdapter {
	private Context context;
	private ArrayList<Map<String, String>> list;

	public Referral_listview_atapter(Context context,
			ArrayList<Map<String, String>> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.context = context;
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
		convertView = View.inflate(context, R.layout.item_referral_listview,
				null);
		TextView tv1 = (TextView) convertView.findViewById(R.id.textview_phone);
		TextView tv2 = (TextView) convertView.findViewById(R.id.textview_money);
		tv1.setText(list.get(position).get("number"));
		tv2.setText(FormatStringUtil.getDesplay(list.get(position).get("many"))
				+ "Íæ±Ò");
		return convertView;
	}

}