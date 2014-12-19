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
	private String str1[] = { "ʣ��", "����׬", "����","����", "С��һ��", "�Ƽ���׬", "���տɵ�", "����" };
	private String str2[] = { "�����ɿ�", "���", "��������","������", "����", "����", "���", "�����" };
	private String str3[] = { "��ͼƬ����׬Ǯ!", "׬Ǯ��Ӧ���г�!", "��д�ʾ��ø߶����棡","�����ཱ����࣡",
			"�����������ս!", "������Ǯ�Ľ���", "Խǩ������Խ��!", "��ҿ�װ������!" };
	private String str4[] = { "�����", "��Ӧ��", "�ʾ����","����׬", "����Ϸ", "���ƹ�", "ÿ��ǩ��", "����" };

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
				holder.tv3.setText("�����Ѿ�ǩ������");
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
