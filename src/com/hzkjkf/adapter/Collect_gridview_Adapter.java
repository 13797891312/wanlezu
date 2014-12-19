package com.hzkjkf.adapter;

import java.util.List;
import java.util.Map;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.hzkjkf.imageloader.Imageloader;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;

public class Collect_gridview_Adapter extends BaseAdapter {
	private boolean isDelete;
	private Handler hd;
	private Context context;
	private List<Map<String, String>> data_list;

	public Collect_gridview_Adapter(Context context,
			List<Map<String, String>> data_list, Handler hd) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data_list = data_list;
		this.hd = hd;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_collect_gridview,
					null);
			holderView = new HolderView();
			holderView.iv = (ImageView) convertView
					.findViewById(R.id.imageView1);
			holderView.delete_iv = (ImageView) convertView
					.findViewById(R.id.imageView_delete);
			holderView.tv = (TextView) convertView
					.findViewById(R.id.textView_title);
			holderView.tv_quan = (TextView) convertView
					.findViewById(R.id.textView1);
			holderView.tv_xiang = (TextView) convertView
					.findViewById(R.id.textView2);
			holderView.bar = (ProgressBar) convertView
					.findViewById(R.id.progressBar1);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		if (isDelete) {
			holderView.delete_iv.setVisibility(View.VISIBLE);
		} else {
			holderView.delete_iv.setVisibility(View.GONE);
		}
		holderView.tv_quan.setVisibility(data_list.get(position)
				.get("couponAble").equals("0") ? View.GONE : View.VISIBLE);
		holderView.tv_xiang.setVisibility(data_list.get(position)
				.get("shareAble").equals("0") ? View.GONE : View.VISIBLE);

		holderView.delete_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initDelData(data_list.get(position).get("advertisId"));
				data_list.remove(position);
				MyApp.getInstence().collectCount -= 1;
				Collect_gridview_Adapter.this.notifyDataSetChanged();
			}
		});
		holderView.tv.setText(data_list.get(position).get("advertisName"));
		Imageloader.getInstence().displayImage(
				data_list.get(position).get("thumbnailUrl"), holderView.iv,
				new Handler(), holderView.bar);
		return convertView;
	}

	static class HolderView {
		public ImageView iv;
		public TextView tv, tv_quan, tv_xiang;
		public ProgressBar bar;
		public ImageView delete_iv;
	}

	/** «Î«Û…æ≥˝URL **/
	private void initDelData(final String advertiseId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "advertiseId" },
						new String[] { "10028", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), advertiseId });
				HttpTool.httpGetJson1(context, url, hd);
			}
		}).start();
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

}
