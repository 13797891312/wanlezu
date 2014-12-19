package com.hzkjkf.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hzkjkf.imageloader.Imageloader_shop;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Shop_listview extends BaseAdapter {
	private Context con;
	private Handler hd;
	private List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> list3 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> list4 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> list_sum = new ArrayList<Map<String, String>>();

	public Shop_listview(Handler hd, Context con,
			List<Map<String, String>> list1, List<Map<String, String>> list2,
			List<Map<String, String>> list3,List<Map<String, String>> list4, List<Map<String, String>> list_sum) {
		// TODO Auto-generated constructor stub
		this.list1 = list1;
		this.list2 = list2;
		this.list3 = list3;
		this.list4 = list4;
		this.list_sum = list_sum;
		this.con = con;
		this.hd = hd;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_sum.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_sum.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = View.inflate(con, R.layout.item_shop_listview, null);
		}
		ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);
		TextView tv_name = (TextView) convertView.findViewById(R.id.textView1);
		TextView tv_money = (TextView) convertView
				.findViewById(R.id.textView_qq);
		RelativeLayout rel_fenge = (RelativeLayout) convertView
				.findViewById(R.id.layout_fenge);
		View view = convertView.findViewById(R.id.view);
		if (position == list1.size() - 1
				|| position == list1.size() + list4.size() - 1||position == list1.size() + list2.size()+list4.size() - 1) {
			rel_fenge.setVisibility(View.VISIBLE);
			view.setVisibility(View.VISIBLE);
		} else {
			rel_fenge.setVisibility(View.GONE);
			view.setVisibility(View.GONE);
		}
		iv.setTag(list_sum.get(position).get("picUrl"));
		iv.setImageResource(R.drawable.wanzhuan);
		tv_name.setText(list_sum.get(position).get("goodName"));
		tv_money.setText("商城价格 ： "
				+ FormatStringUtil.getDesplay(list_sum.get(position).get(
						"goodPrice")) + "玩币");
		Imageloader_shop.getInstence().displayImage(
				list_sum.get(position).get("picUrl"), iv, hd, null);
		return convertView;
	}
}
