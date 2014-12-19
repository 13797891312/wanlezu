package com.hzkjkf.adapter;

import java.util.List;
import java.util.Map;

import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class earnRecord_listview_adapter2 extends BaseAdapter {
	Context con;
	List<Map<String, String>> list;

	public earnRecord_listview_adapter2(Context con,
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
			convertView = View.inflate(con, R.layout.item_earnrecord_listview2,
					null);
			holder = new HolderView();
			holder.tv_money = (TextView) convertView
					.findViewById(R.id.textview_menoy);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.textview_name);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.textview_time);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.tv_time.setText(list.get(position).get("time"));
		holder.tv_name.setText(list.get(position).get("name"));
		if (position == 0) {

			holder.tv_money.setText(list.get(position).get("money"));
		} else
			holder.tv_money.setText(FormatStringUtil.getDesplay(list.get(
					position).get("mony"))
					+ "Íæ±Ò");
		return convertView;
	}

	static class HolderView {
		TextView tv_name;
		TextView tv_time;
		TextView tv_money;
	}
}
