package com.hzkjkf.adapter;

import java.util.List;

import com.hzkjkf.activity.DownLoadTaskAcitivity;
import com.hzkjkf.imageloader.Imageloader_myTask;
import com.hzkjkf.javabean.QuestionBean;
import com.hzkjkf.javabean.TaskData;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyTask_listview_adapter extends BaseAdapter {
	private Context con;
	private List<TaskData> list;

	public MyTask_listview_adapter(Context con, List<TaskData> list) {
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
			convertView = View
					.inflate(con, R.layout.item_mytask_listview, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.info_tv = (TextView) convertView
					.findViewById(R.id.textView_info);
			holder.name_tv = (TextView) convertView
					.findViewById(R.id.textView_name);
			holder.money_tv = (TextView) convertView
					.findViewById(R.id.textView_money);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.iv.setTag(list.get(position).getSmallImageUrl());
		holder.iv.setImageResource(R.drawable.wanzhuan);
		Imageloader_myTask.getInstence().displayImage(
				list.get(position).getSmallImageUrl(), holder.iv,
				((DownLoadTaskAcitivity) con).hd, null);
		holder.info_tv.setText(list.get(position).getInfo());
		holder.name_tv.setText(list.get(position).getName());
		holder.money_tv.setText(list.get(position).getMoney() + "Íæ±Ò");
		return convertView;
	}

	class HolderView {
		ImageView iv;
		TextView name_tv, info_tv, money_tv;
	}
}
