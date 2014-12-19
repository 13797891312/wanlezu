package com.hzkjkf.adapter;

import java.util.List;
import java.util.Map;

import com.hzkjkf.fragment.MoreFragment;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Trends_listview_adapter extends BaseAdapter {
	private List<Map<String, String>> list;
	private Context context;

	public Trends_listview_adapter(Context context,
			List<Map<String, String>> list) {
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
		convertView = View
				.inflate(context, R.layout.item_trends_listview, null);
		TextView tv1 = (TextView) convertView.findViewById(R.id.textview_title);
		TextView tv2 = (TextView) convertView.findViewById(R.id.textView_time);
		tv1.setText(list.get(position).get("title").toString());
		tv2.setText(list.get(position).get("time").toString());
		return convertView;
	}

}
