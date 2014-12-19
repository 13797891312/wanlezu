package com.hzkjkf.activity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class UserInfoAcitivity extends Activity implements OnItemClickListener,
		OnCheckedChangeListener {
	private ListView info_listview;
	String str[] = { "用户名", "性别", "出生日期", "职业", "学 历", "QQ号", "支付宝账号", "收货人",
			"收货地址", "联系电话", "Email" };
	private String str_info[] = { MyApp.getInstence().name,
			MyApp.getInstence().sex, MyApp.getInstence().birthday,
			MyApp.getInstence().profession, MyApp.getInstence().edu,
			MyApp.getInstence().QQ, MyApp.getInstence().aliCount,
			MyApp.getInstence().expName, MyApp.getInstence().expPosition,
			MyApp.getInstence().getPhone(), MyApp.getInstence().email };
	private String str_info1[] = { MyApp.getInstence().name,
			MyApp.getInstence().sex, MyApp.getInstence().birthday,
			MyApp.getInstence().profession, MyApp.getInstence().edu,
			MyApp.getInstence().QQ, MyApp.getInstence().aliCount,
			MyApp.getInstence().expName, MyApp.getInstence().expPosition,
			MyApp.getInstence().getPhone(), MyApp.getInstence().email };
	private String edus[] = new String[] { "小学", "初中", "高中", "大专", "本科及以上" };
	private String jobs[] = new String[] { "中学生", "大学生", "餐饮业", "娱乐休闲", "酒店旅游",
			"互联网及软件", "金融财会", "广告营销", "汽车房产", "建筑装饰", "法律教育", "设备及制造", "医疗美容",
			"物流及交通", "零售及网购", "服务行业", "电气能源", "其他" };
	private MyAdapter ma;
	private ToggleButton edit_btn;
	private RelativeLayout editLayout;
	private boolean isEdit;
	private ProgressDialog dialog;
	private Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(UserInfoAcitivity.this, "网络不给力，请检查网络",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				if (dialog != null) {
					dialog.cancel();
				}
				Toast.makeText(UserInfoAcitivity.this, "修改成功",
						Toast.LENGTH_LONG).show();
				break;
			default:
				Toast.makeText(UserInfoAcitivity.this,
						ErrorCode.getString(msg.what), Toast.LENGTH_LONG)
						.show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_userinfo);
		((TextView) findViewById(R.id.title)).setText("个人资料");
		info_listview = (ListView) findViewById(R.id.listview_info);
		edit_btn = (ToggleButton) findViewById(R.id.edit_btn);
		editLayout = (RelativeLayout) findViewById(R.id.edit_btn_layout);
		editLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edit_btn.isChecked()) {
					edit_btn.setChecked(false);
				} else {
					edit_btn.setChecked(true);
				}
			}
		});
		editLayout.setVisibility(View.VISIBLE);
		edit_btn.setOnCheckedChangeListener(this);
		edit_btn.setClickable(false);
		ma = new MyAdapter();
		info_listview.setAdapter(ma);
		info_listview.setOnItemClickListener(this);
	}

	public void backClick(View v) {
		if (isEdit) {
			createExitPop();
		} else {
			this.finish();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	class MyAdapter extends BaseAdapter {

		int id[] = { R.drawable.information, R.drawable.information1,
				R.drawable.information2, R.drawable.information3,
				R.drawable.information4, R.drawable.information5,
				R.drawable.information6, R.drawable.information1,
				R.drawable.address1, R.drawable.phone, R.drawable.mail };

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return str.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return str[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = View.inflate(UserInfoAcitivity.this,
					R.layout.item_userinfo_listview, null);
			// ((TextView)convertView.findViewById(R.id.textView2)).setText(count[position]);
			((TextView) convertView.findViewById(R.id.textview_title))
					.setText(str[position]);
			TextView info_tv = ((TextView) convertView
					.findViewById(R.id.textView1));
			if (str_info[position] == null || str_info[position].isEmpty()
					|| str_info[position].equals("null")) {
				info_tv.setText("未填写");
			} else {
				info_tv.setText(str_info[position]);
			}
			((ImageView) convertView.findViewById(R.id.imageView2))
					.setImageResource(id[position]);
			ImageView edit = (ImageView) convertView
					.findViewById(R.id.imageView1);
			if (isEdit) {
				if (position == 9) {
					edit.setVisibility(View.GONE);
				} else {
					edit.setVisibility(View.VISIBLE);
				}
			} else {
				edit.setVisibility(View.GONE);
			}
			// if (position == str.length - 1 || position == str.length - 2||
			// position == str.length - 3) {
			// edit.setVisibility(View.GONE);
			// }
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			int position, long id) {
		// TODO Auto-generated method stub
		if (!isEdit) {
			return;
		}
		switch (position) {
		case 0:
			createNamePop(0);
			break;
		case 1:
			createSexPop();
			break;
		case 2:
			creatBothPop();
			break;
		case 3:
			creatJobDialog();
			break;
		case 4:
			creatEduDialog();
			break;
		case 5:
			createNamePop(5);
			break;
		case 6:
			createNamePop(6);
			break;
		case 7:
			createNamePop(7);
			break;
		case 8:
			createNamePop(8);
			break;
		case 10:
			createNamePop(10);
			break;
		}
	}

	private void createSexPop() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this, R.style.MyDialogStyle);
		dialog.setCanceledOnTouchOutside(true);
		View view = View.inflate(this, R.layout.pop_userinfo_sex, null);
		final RadioGroup group = (RadioGroup) view.findViewById(R.id.group);
		Button dismiss_btn = (Button) view.findViewById(R.id.button_cancel);
		Button ok_btn = (Button) view.findViewById(R.id.button_ok);
		dismiss_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		ok_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str_info[1] = group.getCheckedRadioButtonId() == R.id.radioButton1 ? "男"
						: "女";
				ma.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.show();
	}

	private void createNamePop(final int position) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this, R.style.MyDialogStyle);
		dialog.setCanceledOnTouchOutside(true);
		View view = View.inflate(this, R.layout.pop_userinfo_name, null);
		TextView name_tv = (TextView) view.findViewById(R.id.textView_name);
		name_tv.setText(str[position]);
		final EditText et = (EditText) view.findViewById(R.id.editText1);
		switch (position) {
		case 5:
			name_tv.setText("QQ号每月只能修改一次");
			name_tv.setTextSize(12);
			break;
		case 6:
			name_tv.setText("支付宝每月只能修改一次");
			name_tv.setTextSize(12);
			break;
		case 10:
			name_tv.setText("Email为参加调查问卷条件");
			name_tv.setTextSize(12);
			break;
		}
		Button dismiss_btn = (Button) view.findViewById(R.id.button_cancel);
		Button ok_btn = (Button) view.findViewById(R.id.button_ok);
		dismiss_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		ok_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et.getText().toString().isEmpty()) {
					if (position == 10
							&& !HttpTool.isEmail(et.getText().toString())) {
						Toast.makeText(UserInfoAcitivity.this, "请填写正确的Email",
								Toast.LENGTH_SHORT).show();
						return;
					}
					str_info[position] = et.getText().toString();
					ma.notifyDataSetChanged();
				}
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.show();
	}

	private void creatBothPop() {
		Dialog dataDialog = new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				str_info[2] = (year
						+ "-"
						+ ((monthOfYear + 1) >= 10 ? String
								.valueOf((monthOfYear + 1))
								: ("0" + (monthOfYear + 1))) + "-" + ((dayOfMonth) >= 10 ? String
						.valueOf((dayOfMonth)) : ("0" + (dayOfMonth))));
				ma.notifyDataSetChanged();
			}
		}, 1990, 01, 01);
		dataDialog.show();
	}

	private void creatEduDialog() {
		new AlertDialog.Builder(this)
				.setTitle("选择学历")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(edus, -1,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								str_info[4] = (edus[which]);
								ma.notifyDataSetChanged();
							}
						}).setNegativeButton("确定", null).show();
	}

	private void creatJobDialog() {
		new AlertDialog.Builder(this).setTitle("选择职业")
				.setItems(jobs, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						str_info[3] = (jobs[which]);
						ma.notifyDataSetChanged();
					}
				}).show();

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		isEdit = isChecked;
		ma.notifyDataSetChanged();
		if (!isChecked) {
			initSubmit();
		}
	}

	/****** 提交修改结果 *****/
	private void initSubmit() {
		// TODO Auto-generated method stub
		if (Arrays.equals(str_info, str_info1)) {
			Toast.makeText(this, "无任何修改", Toast.LENGTH_LONG).show();
			return;
		}
		dialog = ProgressDialog.show(this, "", "正在提交，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei", "userName", "sex",
						"birthdate", "profession", "education", "qqNum",
						"aliCount", "expName", "expPosition", "email" },
						new String[] { "10021", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), str_info[0],
								str_info[1].equals("男") ? "1" : "0",
								str_info[2], str_info[3], str_info[4],
								str_info[5], str_info[6], str_info[7],
								str_info[8], str_info[10] });
				String json = HttpTool.httpGetJson1(UserInfoAcitivity.this,
						url, hd);
				if (!json.isEmpty()) {
					if (HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, 1);
						upDataInfo();
					} else {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
					}
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}

		}).start();
	}

	private void upDataInfo() {
		// TODO Auto-generated method stub
		for (int i = 0; i < str_info.length; i++) {
			str_info1[i] = str_info[i];
		}
		MyApp.getInstence().name = str_info[0];
		MyApp.getInstence().sex = str_info[1];
		MyApp.getInstence().birthday = str_info[2];
		MyApp.getInstence().profession = str_info[3];
		MyApp.getInstence().edu = str_info[4];
		MyApp.getInstence().QQ = str_info[5];
		MyApp.getInstence().aliCount = str_info[6];
		MyApp.getInstence().expName = str_info[7];
		MyApp.getInstence().expPosition = str_info[8];
		MyApp.getInstence().email = str_info[10];
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (isEdit) {
			createExitPop();
		} else {
			super.onBackPressed();
		}
	}

	private void createExitPop() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this, R.style.MyDialogStyle);
		dialog.setCanceledOnTouchOutside(true);
		View view = View.inflate(this, R.layout.pop_exit, null);
		Button dismiss_btn = (Button) view.findViewById(R.id.button_cancel);
		TextView tv = (TextView) view.findViewById(R.id.textView_info);
		tv.setText("正在编辑中，是否保存？");
		Button exit_btn = (Button) view.findViewById(R.id.button_exit);
		exit_btn.setText("保存");
		dismiss_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (UserInfoAcitivity.this != null) {
					UserInfoAcitivity.this.finish();
				}
			}
		});
		exit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				edit_btn.setChecked(false);
			}
		});
		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.setContentView(view);
		dialog.show();
	}
}
