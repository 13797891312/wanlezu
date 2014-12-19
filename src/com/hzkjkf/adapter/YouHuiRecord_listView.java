package com.hzkjkf.adapter;

import java.util.List;

import com.hzkjkf.adapter.Home_gridview_adapter.HolderView;
import com.hzkjkf.javabean.YouHuiData;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class YouHuiRecord_listView extends BaseAdapter {
	private List<YouHuiData> list;
	private Context con;
	private ViewHolder holder;

	public YouHuiRecord_listView(List<YouHuiData> list, Context con) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.con = con;
	}

	/** 切换显示未使用，已使用和已作废 ***/
	public void changeData(List<YouHuiData> list) {
		this.list = list;
		this.notifyDataSetChanged();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View
					.inflate(con, R.layout.item_youhui_listview, null);
			holder.name_tv = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.time_tv = (TextView) convertView
					.findViewById(R.id.textView3);
			holder.code_tv = (TextView) convertView
					.findViewById(R.id.textView4);
			holder.info_tv = (TextView) convertView
					.findViewById(R.id.info_value);
			holder.rel = (RelativeLayout) convertView
					.findViewById(R.id.layOut_info);
			holder.relativeLayout1 = (RelativeLayout) convertView
					.findViewById(R.id.relativeLayout1);
			holder.rel.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.relativeLayout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (list.get(position).isShow()) {
					list.get(position).setShow(false);
				} else {
					list.get(position).setShow(true);
				}
				YouHuiRecord_listView.this.notifyDataSetInvalidated();
			}
		});
		if (list.get(position).isShow()) {
			holder.rel.setVisibility(View.VISIBLE);
		} else {
			holder.rel.setVisibility(View.GONE);
		}
		holder.name_tv.setText(list.get(position).getName());
		holder.time_tv.setText("有效期 ： " + list.get(position).getEndDate());
		holder.code_tv.setText("优惠码 ： " + list.get(position).getCode());
		holder.info_tv.setText(list.get(position).getInfo());
		return convertView;
	}

	static class ViewHolder {
		TextView name_tv;
		TextView time_tv;
		TextView code_tv;
		TextView info_tv;
		RelativeLayout rel;
		RelativeLayout relativeLayout1;
	}

}
