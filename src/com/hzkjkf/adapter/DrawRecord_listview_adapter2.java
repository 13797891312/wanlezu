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

public class DrawRecord_listview_adapter2 extends BaseAdapter {
	Context con;
	List<Map<String, String>> list;

	public DrawRecord_listview_adapter2(Context con,
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
			convertView = View.inflate(con, R.layout.item_drawrecord_listview2,
					null);
			holder = new HolderView();
			holder.tv_money = (TextView) convertView
					.findViewById(R.id.textView2);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.textView3);
			holder.tv_statas = (TextView) convertView
					.findViewById(R.id.textView4);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.tv_time.setText(list.get(position).get("time"));
		if (position == 0) {
			holder.tv_money.setText(list.get(position).get("money"));
			holder.tv_statas.setText("消费状态");
			holder.tv_money.setText("消费玩币");
			holder.tv_name.setText("消费类容");
			return convertView;
		}
		holder.tv_money.setText(FormatStringUtil.getDesplay(list.get(position)
				.get("money")) + "玩币");
		holder.tv_name.setText(list.get(position).get("name"));

		switch (Integer.parseInt(list.get(position).get("statas"))) {
		case 0:
			holder.tv_statas.setText("待审核");
			break;
		case 1:
			holder.tv_statas.setText("审核中");
			break;
		case 2:
			holder.tv_statas.setText("消费成功");
			break;
		case -1:
			holder.tv_statas.setText("已拒绝");
			break;
		case -2:
			holder.tv_statas.setText("已拒绝");
			break;
		}
		return convertView;
	}

	static class HolderView {
		TextView tv_name;
		TextView tv_time;
		TextView tv_money;
		TextView tv_statas;
	}
}
