package com.hzkjkf.adapter;

import java.util.List;
import java.util.Map;

import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class News_listview_Adapter extends BaseAdapter {
	private List<Map<String, String>> list;
	private Context context;

	public News_listview_Adapter(Context context, List<Map<String, String>> list) {
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
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_news_listview,
					null);
		}
		TextView type_tv = (TextView) convertView
				.findViewById(R.id.textview_type);
		TextView news_tv = (TextView) convertView
				.findViewById(R.id.textview_body);
		TextView date_tv = (TextView) convertView
				.findViewById(R.id.textview_time);
		ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);
		news_tv.setText(list.get(position).get("message").toString());
		date_tv.setText(list.get(position).get("date").toString());
		if (list.get(position).get("type").equals("0")) {
			iv.setBackgroundResource(R.drawable.news_shangjia);
			type_tv.setText("商家回复");
		} else if (list.get(position).get("type").equals("1")) {
			type_tv.setText("商家返礼");
			iv.setBackgroundResource(R.drawable.news_lipin);
		} else if (list.get(position).get("type").equals("2")) {
			type_tv.setText("系统消息");
			iv.setBackgroundResource(R.drawable.news_xitong);
		} else if (list.get(position).get("type").equals("3")) {
			type_tv.setText("用户消息");
			iv.setBackgroundResource(R.drawable.grsz);
		}
		if (!Boolean.parseBoolean(list.get(position).get("isRead"))) {
			type_tv.setTextColor(0xffFF6B09);
		} else
			type_tv.setTextColor(Color.BLACK);
		return convertView;
	}
}
