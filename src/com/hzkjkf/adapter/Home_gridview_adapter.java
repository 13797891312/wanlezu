package com.hzkjkf.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.view.Myview;
import com.hzkjkf.wanzhuan.R;
import com.hzkjkf.wanzhuan.R.drawable;

public class Home_gridview_adapter extends BaseAdapter {
	private Context context;
	public int[] id = { R.drawable.index_1, R.drawable.index_2,
			R.drawable.index7_,R.drawable.index_8, R.drawable.index_3, R.drawable.index_4,
			R.drawable.index_5, R.drawable.index_6 };
	private String str1[] = { "剩余", "还能赚", "超多","超多", "小手一抖", "推荐立赚", "今日可得", "可领" };
	private String str2[] = { "个广告可看", "玩币", "等你来拿","做不完", "到手", "奖励", "玩币", "个红包" };
	private String str3[] = { "看图片就能赚钱!", "赚钱的应用市场!", "填写问卷获得高额收益！","任务多多奖励多多！",
			"够胆你就来挑战!", "坐等数钱的节奏", "越签到收益越多!", "玩币快装不下了!" };
	private String str4[] = { "看广告", "下应用", "问卷调查","快速赚", "玩游戏", "乐推广", "每日签到", "领红包" };

	public Home_gridview_adapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str1.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return str1[position];
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
			convertView = View.inflate(context, R.layout.item_home_gridview,
					null);
			holder.iv = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.tv1 = (TextView) convertView.findViewById(R.id.textView1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.textView2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.textView3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.textView4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.textView5);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}

		holder.iv.setImageResource(id[position]);
		holder.tv1.setText(str3[position]);
		holder.tv2.setText(str4[position]);
		holder.tv3.setText(str1[position]);
		holder.tv5.setText(str2[position]);
		if (position == 1 || position == 6) {
			if (position == 6
					&& FormatStringUtil.getDesplay(
							MyApp.getInstence().getHomeData()[position])
							.equals("0")) {
				holder.tv4.setVisibility(View.GONE);
				holder.tv3.setText("今日已经签过到！");
				holder.tv5.setVisibility(View.GONE);
			}
			holder.tv4.setText(FormatStringUtil.getDesplay(MyApp.getInstence()
					.getHomeData()[position]));
		} else {
			holder.tv4.setText(MyApp.getInstence().getHomeData()[position]);
		}
		return convertView;
	}

	static class HolderView {
		TextView tv4;
		TextView tv3;
		TextView tv5;
		TextView tv1;
		TextView tv2;
		ImageView iv;
	}
}
