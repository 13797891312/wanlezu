package com.hzkjkf.adtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.activity.AdListActivity;
import com.hzkjkf.activity.StartEarnActivity;
import com.hzkjkf.adapter.QuestionGMO_listview_adapter;
import com.hzkjkf.adapter.Question_listview_adapter;
import com.hzkjkf.javabean.QuestionBean;
import com.hzkjkf.javabean.QuestionBean_GMO;
import com.hzkjkf.util.Blowfish;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class QuestionGMOAcitvity extends Activity {
	/**获取列表的URL**/
	private String listUrl = "https://st-cn.infopanel.asia/pollon/jp/gmor/research/pollon/enqueteList/facade/EnqueteList.json?";
	public static final String PANEL = "99";
	private ListView task_listView;
	private ProgressDialog dialog;
	private String type = "2";
	private QuestionGMO_listview_adapter qla;
	private List<QuestionBean_GMO> list = new ArrayList<QuestionBean_GMO>();
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(QuestionGMOAcitvity.this, "网络不给力，请检查网络",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				qla.notifyDataSetChanged();
				break;
			default:
				Toast.makeText(QuestionGMOAcitvity.this,
						ErrorCode.getString(msg.what), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questiontask);
		((TextView) findViewById(R.id.title)).setText("问卷调查");
		type = getIntent().getStringExtra("type");
		initData();
		findViewById(R.id.textView_info).setVisibility(
				type.equals("0") ? View.VISIBLE : View.GONE);
		task_listView = (ListView) findViewById(R.id.listView);
		task_listView.setOnItemClickListener(itemClick);
		qla = new QuestionGMO_listview_adapter(this, list);
		task_listView.setAdapter(qla);
	}

	private void initData() {
		// TODO Auto-generated method stub
		/** 初始化数据 **/
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在加载，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url="";
				try {
					url = listUrl+"panelType="+PANEL+"&crypt="+Blowfish.getcrytValue(MyApp.getInstence().getPhone().substring(3));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				LogUtils.e("phone", url);
				String json = HttpTool.httpClientGetJson(url);
				if (!json.isEmpty()) {
					try {
						JSONArray result =new JSONArray(json);
						for (int i = 0; i < result.length(); i++) {
							JSONObject object;
							object = result.getJSONObject(i);
							QuestionBean_GMO data=new QuestionBean_GMO();
							data.setEncryptId(object.getString("encryptId"));
							data.setState(object.getString("ans_stat_cd"));
							data.setFinishTime(object.getString("arrivalDay"));
							data.setPoint(object.getString("point"));
							data.setPoint_min(object.getString("point_min"));
							data.setTitle(object.getString("title"));
							data.setUrl(object.getString("redirectSt"));
							data.setId(object.getString("id"));
							list.add(data);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						HandleUtil.sendInt(hd, 0);
						return;
					}
					HandleUtil.sendInt(hd, 1);
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
	}

	/** 返回按钮监听 ***/
	public void backClick(View v) {
		this.finish();
		hd = null;
	}

	OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(QuestionGMOAcitvity.this,
					QuestionInfoActivity.class);
			String url=list.get(position).getUrl()+list.get(position).getId()+"="+list.get(position).getEncryptId();
			intent.putExtra("url", url);
			QuestionGMOAcitvity.this.startActivity(intent);
		}
	};
}
