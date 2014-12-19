package com.hzkjkf.adapter;

import java.util.List;
import java.util.Map;

import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Game2Record_listview_adapter extends BaseAdapter {
	Context con;
	List<Map<String, String>> list;

	public Game2Record_listview_adapter(Context con,
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
			convertView = View.inflate(con, R.layout.item_game2record_listview,
					null);
			holder = new HolderView();
			holder.tv_rank = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.tv_luckUser = (TextView) convertView
					.findViewById(R.id.textView2);
			holder.tv_code = (TextView) convertView
					.findViewById(R.id.textView3);
			holder.tv_money = (TextView) convertView
					.findViewById(R.id.textView4);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.tv_rank.setText(list.get(position).get("rank"));
		holder.tv_code.setText(list.get(position).get("code"));
		holder.tv_money.setText(list.get(position).get("money") + "Íæ±Ò");
		holder.tv_luckUser.setText(list.get(position).get("name"));
		if (list.get(position).get("phone")
				.equals(MyApp.getInstence().getPhone())) {
			holder.tv_rank.setTextColor(Color.RED);
			holder.tv_code.setTextColor(Color.RED);
			holder.tv_money.setTextColor(Color.RED);
			holder.tv_luckUser.setTextColor(Color.RED);
		} else {
			holder.tv_rank.setTextColor(Color.BLACK);
			holder.tv_code.setTextColor(Color.BLACK);
			holder.tv_money.setTextColor(Color.BLACK);
			holder.tv_luckUser.setTextColor(Color.BLACK);
		}
		return convertView;
	}

	static class HolderView {
		TextView tv_code;
		TextView tv_luckUser;
		TextView tv_rank;
		TextView tv_money;
	}
}
