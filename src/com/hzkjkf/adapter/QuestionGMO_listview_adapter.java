package com.hzkjkf.adapter;

import java.util.List;

import com.hzkjkf.javabean.QuestionBean;
import com.hzkjkf.javabean.QuestionBean_GMO;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuestionGMO_listview_adapter extends BaseAdapter {
	private Context con;
	private List<QuestionBean_GMO> list;

	public QuestionGMO_listview_adapter(Context con, List<QuestionBean_GMO> list) {
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
			convertView = View.inflate(con,
					R.layout.item_questiongmo_listview, null);
			holder = new HolderView();
			holder.name_tv = (TextView) convertView
					.findViewById(R.id.textview_title);
			holder.money_tv = (TextView) convertView
					.findViewById(R.id.textView3);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.name_tv.setText(list.get(position).getTitle());
		holder.money_tv.setText(list.get(position).getPoint());

		return convertView;
	}

	class HolderView {
		TextView name_tv;
		TextView money_tv;
	}
}
