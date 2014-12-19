package com.hzkjkf.adapter;

import java.util.List;
import java.util.Map;

import com.google.zxing.common.StringUtils;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.view.Head;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SuperRecord_listview_adapter2 extends BaseAdapter {
	Context con;
	List<Map<String, String>> list;

	public SuperRecord_listview_adapter2(Context con,
			List<Map<String, String>> list) {
		// TODO Auto-generated constructor stub
		this.con = con;
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
			convertView = View.inflate(con, R.layout.item_superrecord_listview,
					null);
			holder = new HolderView();
			holder.tv_num = (TextView) convertView.findViewById(R.id.textView2);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.tv_MyCode = (TextView) convertView
					.findViewById(R.id.textView3);
			holder.tv_code = (TextView) convertView
					.findViewById(R.id.textView4);
			holder.tv_luckName = (TextView) convertView
					.findViewById(R.id.textView5);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.tv_name.setText(list.get(position).get("name"));
		holder.tv_num.setText(list.get(position).get("num"));
		holder.tv_MyCode.setText(list.get(position).get("myCode"));
		holder.tv_code.setText(list.get(position).get("Code"));
		holder.tv_luckName.setText(list.get(position).get("getName"));
		return convertView;
	}

	static class HolderView {
		TextView tv_name;
		TextView tv_num;
		TextView tv_MyCode;
		TextView tv_code;
		TextView tv_luckName;
	}
}
