package com.hzkjkf.adapter;

import java.util.List;

import com.hzkjkf.javabean.QuestionBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class adapter extends BaseAdapter {
	private Context con;
	private List<QuestionBean> list;

	public adapter(Context con, List<QuestionBean> list) {
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

			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		return convertView;
	}

	class HolderView {

	}
}
