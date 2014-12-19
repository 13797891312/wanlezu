package com.hzkjkf.adapter;

import java.util.List;
import java.util.Map;

import com.hzkjkf.javabean.QuestionBean;
import com.hzkjkf.javabean.TaskInfoData;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Taskinfo_listview_adapter extends BaseAdapter {
	private Context con;
	private List<TaskInfoData> list;

	public Taskinfo_listview_adapter(Context con, List<TaskInfoData> list) {
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
			holder = new HolderView();
			convertView = View.inflate(con, R.layout.item_taskinfo_listview,
					null);
			holder.money_tv = (TextView) convertView
					.findViewById(R.id.textView_money);
			holder.state_tv = (TextView) convertView
					.findViewById(R.id.textView_state);
			holder.title_tv = (TextView) convertView
					.findViewById(R.id.textView_title);
			holder.position_tv = (TextView) convertView
					.findViewById(R.id.textView_order);
			holder.view1 = convertView.findViewById(R.id.view1);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.position_tv.setText(String.valueOf(position + 1));
		switch (list.get(position).getState()) {
		case 0:
			holder.state_tv.setText("任务可完成");
			holder.money_tv
					.setBackgroundResource(R.drawable.shape_taskinfo_money);
			holder.state_tv.setTextColor(Color.RED);
			holder.view1.setVisibility(View.GONE);
			break;
		case 1:
			holder.state_tv.setText("任务已完成");
			holder.money_tv
					.setBackgroundResource(R.drawable.shape_taskinfo_money1);
			holder.state_tv.setTextColor(Color.GRAY);
			holder.view1.setVisibility(View.VISIBLE);
			break;
		case 2:
			holder.state_tv.setText("任务未开始");
			holder.money_tv
					.setBackgroundResource(R.drawable.shape_taskinfo_money1);
			holder.state_tv.setTextColor(Color.GRAY);
			holder.view1.setVisibility(View.GONE);
			break;
		}
		holder.title_tv.setText(list.get(position).getTitle());
		holder.money_tv.setText(String.valueOf(list.get(position).getMoney()));
		return convertView;
	}

	static class HolderView {
		TextView title_tv, state_tv, money_tv, position_tv;
		View view1;
	}
}
