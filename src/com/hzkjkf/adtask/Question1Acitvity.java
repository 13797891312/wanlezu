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
import com.hzkjkf.adapter.Question_listview_adapter;
import com.hzkjkf.javabean.QuestionBean;
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

public class Question1Acitvity extends Activity {
	private ListView task_listView;
	private ProgressDialog dialog;
	private String type = "0";
	private Question_listview_adapter qla;
	private List<QuestionBean> list = new ArrayList<QuestionBean>();
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(Question1Acitvity.this, "网络不给力，请检查网络",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				qla.notifyDataSetChanged();
				break;
			default:
				Toast.makeText(Question1Acitvity.this,
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
		qla = new Question_listview_adapter(this, list);
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
				String url = MyApp.getInstence().questionList + "questionType="
						+ type + "&phone=" + MyApp.getInstence().getPhone();
				String json = HttpTool.httpGetJson1(Question1Acitvity.this,
						url, hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					JSONArray result = HttpTool.getResult(json);
					// "createTime": "2014-11-06 00:00:00",
					// "auditStatus": "0",
					// "uids": "f25752g3481c2f9301481cdf9bc8109d",
					// "answerNumber": 100,
					// "questionName": "“预约旅游”相关调查 测试",
					// "state": "0",
					// "answerUrl":
					// "http://wenjuanba.com/s/5438c1ebeb0e5bd0fa000009?ati=5438ca16eb0e5baef3000013"
					try {
						for (int i = 0; i < result.length(); i++) {
							JSONObject object;
							object = result.getJSONObject(i);
							QuestionBean data = new QuestionBean();
							data.setId(object.getString("uids"));
							data.setName(object.getString("questionName"));
							data.setUrl(object.getString("answerUrl"));
							data.setMoney(FormatStringUtil.getDesplay(object
									.getString("money")));
							data.setInfo(object.getString("memo"));
							list.add(data);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						HandleUtil.sendInt(hd, 0);
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
			createInfoPop(position);
		}
	};

	/*** 创建提示对话框 ****/
	private void createInfoPop(final int position) {
		// TODO Auto-generated method stub
		View view = View.inflate(this, R.layout.pop_question_info, null);
		final Dialog dialog = new Dialog(this, R.style.MyDialogStyle);
		dialog.setCanceledOnTouchOutside(true);
		TextView info_tv = (TextView) view.findViewById(R.id.textView_info);
		info_tv.setText(FormatStringUtil.isEmpty(list.get(position).getInfo()) ? "暂无信息"
				: list.get(position).getInfo());
		Button dismiss_btn = (Button) view.findViewById(R.id.button_cancel);
		dismiss_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Question1Acitvity.this,
						QuestionInfoActivity.class);
				if (type.equals("0")) {
					intent.putExtra("url", list.get(position).getUrl()
							+ "&agent_user_id="
							+ MyApp.getInstence().getPhone() + "&task_id="
							+ list.get(position).getId());
				} else if (type.equals("1")) {
					intent.putExtra(
							"url",
							list.get(position)
									.getUrl()
									.replace("#userID#",
											MyApp.getInstence().getPhone()));
				}
				Question1Acitvity.this.startActivity(intent);
				dialog.dismiss();
			}
		});
		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.setContentView(view);
		dialog.show();
	}
}
