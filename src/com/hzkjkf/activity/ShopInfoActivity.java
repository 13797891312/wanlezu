package com.hzkjkf.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.imageloader.Imageloader;
import com.hzkjkf.imageloader.Imageloader_shop;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopInfoActivity extends Activity implements OnClickListener {
	private String type;
	private ImageView iv;
	private TextView name_tv, money_tv, sxf_tv, count_tv, id_tv, cut_tv,
			add_tv, num_tv, fenshu_tv, kucun_tv, info_tv;
	private TextView userName_tv, phone_tv, address_tv;
	private EditText phone_editText;
	private int count = 0;
	private String name;
	private String id = "", uids;
	private Button submit;
	private ProgressDialog dialog;
	private String goodType = "";
	private String info = "";
	private int qqNum[] = { 5, 10, 20, 30, 50 };
	private int phoneNum[] = { 10, 20, 30, 50, 100 };
	private int index = 0;
	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.cancel();
			}
			switch (msg.what) {
			case 0:
				Toast.makeText(ShopInfoActivity.this, "网络不给力，请检查网络",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(ShopInfoActivity.this, "下单成功", Toast.LENGTH_LONG)
						.show();
				break;
			default:
				Toast.makeText(ShopInfoActivity.this,
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
		setContentView(R.layout.activity_shopinfo);
		type = this.getIntent().getStringExtra("type");
		uids = this.getIntent().getStringExtra("uids");
		goodType = getIntent().getStringExtra("goodType");
		info = getIntent().getStringExtra("memo");
		info_tv = (TextView) findViewById(R.id.textView6);
		phone_editText = (EditText) findViewById(R.id.editText_phone);
		info_tv.setText(info);
		if (goodType.equals("TX")) {
			((TextView) findViewById(R.id.title)).setText("提现商品");
		} else if (goodType.equals("AUCTION")) {
			((TextView) findViewById(R.id.title)).setText("超级拍商品");
		} else if (goodType.equals("ACTIVITY")) {
			((TextView) findViewById(R.id.title)).setText("普通商品");
		}
		name = getIntent().getStringExtra("goodName");
		iv = (ImageView) findViewById(R.id.imageView2);
		name_tv = (TextView) findViewById(R.id.textView_name);
		money_tv = (TextView) findViewById(R.id.textView_howMuch);
		userName_tv = (TextView) findViewById(R.id.textView_userName);
		phone_tv = (TextView) findViewById(R.id.textView_phone);
		address_tv = (TextView) findViewById(R.id.textView_add);
		submit = (Button) findViewById(R.id.button1);
		submit.setOnClickListener(this);
		sxf_tv = (TextView) findViewById(R.id.textView_sxf);
		count_tv = (TextView) findViewById(R.id.textView4);
		num_tv = (TextView) findViewById(R.id.textView1);
		cut_tv = (TextView) findViewById(R.id.textView3);
		add_tv = (TextView) findViewById(R.id.textView5);
		id_tv = (TextView) findViewById(R.id.textView_id);
		fenshu_tv = (TextView) findViewById(R.id.textView_count);
		kucun_tv = (TextView) findViewById(R.id.textView_kucun);
		cut_tv.setOnClickListener(this);
		add_tv.setOnClickListener(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		Imageloader_shop.getInstence().displayImage(
				getIntent().getStringExtra("picUrl"), iv, null, null);
		name_tv.setText(name);
		money_tv.setText("价格："
				+ FormatStringUtil.getDesplay(getIntent().getStringExtra(
						"goodPrice")) + "玩币");
		sxf_tv.setText("兑换手续费："
				+ FormatStringUtil.getDesplay(getIntent().getStringExtra(
						"costs")) + "玩币");
		if ((name.contains("腾讯") || name.contains("财付通"))
				&& goodType.equals("TX")) {
			id = MyApp.getInstence().QQ;
			id_tv.setText("QQ号 ： "
					+ (FormatStringUtil.isEmpty(id) ? "未填写" : id));
			// if (name.contains("腾讯")) {
			// count=qqNum[0];
			// count_tv.setText(String.valueOf(count));
			// num_tv.setText("数量 ： " + count);
			// }

		} else if (name.contains("话费") && goodType.equals("TX")) {
			id = MyApp.getInstence().getPhone();
			id_tv.setText("手机号 ： " + id);
			// count=phoneNum[0];
			// count_tv.setText(String.valueOf(count));
			// num_tv.setText("数量 ： " + count);
		} else if (name.contains("支付宝") && goodType.equals("TX")) {
			id = MyApp.getInstence().aliCount;
			id_tv.setText("支付宝账号 ： "
					+ (FormatStringUtil.isEmpty(id) ? "未填写" : id));
		} else if (goodType.equals("LLBCP")) {
			kucun_tv.setVisibility(View.GONE);
			findViewById(R.id.layout_qq).setVisibility(View.GONE);
			findViewById(R.id.view2).setVisibility(View.GONE);
			findViewById(R.id.view3).setVisibility(View.GONE);
			fenshu_tv.setVisibility(View.GONE);
			id_tv.setVisibility(View.GONE);
			phone_editText.setVisibility(View.VISIBLE);
			phone_editText.setText(MyApp.getInstence().getPhone());
		} else if (!goodType.equals("TX")) {
			id_tv.setVisibility(View.GONE);
			address_tv.setVisibility(View.VISIBLE);
			findViewById(R.id.view3).setVisibility(View.GONE);
			address_tv
					.setText("收货地址 ： "
							+ (FormatStringUtil.isEmpty(MyApp.getInstence().expPosition) ? "未填写"
									: MyApp.getInstence().expPosition));
			userName_tv
					.setText("收货人 ：  "
							+ (FormatStringUtil.isEmpty(MyApp.getInstence().expName) ? "未填写"
									: MyApp.getInstence().expName));
			phone_tv.setText("电话 ：  " + MyApp.getInstence().getPhone());
			userName_tv.setVisibility(View.VISIBLE);
			phone_tv.setVisibility(View.VISIBLE);
			findViewById(R.id.view4).setVisibility(View.VISIBLE);
			findViewById(R.id.view5).setVisibility(View.VISIBLE);
		}
		if (goodType.equals("AUCTION")) {
			fenshu_tv.setText("已兑换："
					+ getIntent().getStringExtra("exchangeState") + "份");
			fenshu_tv.setVisibility(View.VISIBLE);
			sxf_tv.setVisibility(View.GONE);
			kucun_tv.setVisibility(View.GONE);
		} else if (goodType.equals("ACTIVITY")) {
			fenshu_tv.setVisibility(View.GONE);
			sxf_tv.setVisibility(View.VISIBLE);
			kucun_tv.setVisibility(View.VISIBLE);
			kucun_tv.setText("库存：" + getIntent().getStringExtra("kcNum") + "份");
		} else if (!goodType.equals("LLBCP")) {
			fenshu_tv.setVisibility(View.GONE);
			sxf_tv.setVisibility(View.VISIBLE);
			kucun_tv.setVisibility(View.VISIBLE);
			kucun_tv.setText("库存：" + getIntent().getStringExtra("kcNum") + "份");
		}

		if ((getIntent().getStringExtra("goodType").equals("TX") && FormatStringUtil
				.isEmpty(id))
				|| (!getIntent().getStringExtra("goodType").equals("TX") && (FormatStringUtil
						.isEmpty(MyApp.getInstence().expPosition) || FormatStringUtil
						.isEmpty(MyApp.getInstence().expName)))) {
			Toast.makeText(this, "资料填写不完全，无法提交订单,请先完善您的个人资料", Toast.LENGTH_LONG)
					.show();
			return;
		}
	}

	/*** 返回按钮监听 **/
	public void backClick(View v) {
		this.finish();
	}

	@Override
	public void onClick(View v) {
		int id1 = v.getId();
		if (id1 == R.id.textView3) {
			// if (name.contains("腾讯")) {//如果是qq币
			// if (index>0) {
			// index--;
			// }
			// count=qqNum[index];
			// }else if (name.contains("话费")) {//如果是话费充值
			// if (index>0) {
			// index--;
			// }
			// count=phoneNum[index];
			// }else {
			if (count > 0) {
				count--;
				// }
			}
			count_tv.setText(String.valueOf(count));
			num_tv.setText("数量 ： " + count);
		} else if (id1 == R.id.textView5) {
			// if (name.contains("腾讯")) {//如果是qq币
			// if (index==qqNum.length-1) {
			// index=0;
			// }else {
			// index++;
			// }
			// count=qqNum[index];
			// }else if (name.contains("话费")) {//如果是话费充值
			// if (index==phoneNum.length-1) {
			// index=0;
			// }else {
			// index++;
			// }
			// count=phoneNum[index];
			// }else {
			count++;
			// }
			count_tv.setText(String.valueOf(count));
			num_tv.setText("数量 ： " + count);
		} else if (id1 == R.id.button1) {
			if ((getIntent().getStringExtra("goodType").equals("TX") && (id == null || id
					.isEmpty()))
					|| (!getIntent().getStringExtra("goodType").equals("TX") && (MyApp
							.getInstence().expPosition.isEmpty() || MyApp
							.getInstence().expName.isEmpty()))) {
				Toast.makeText(this, "资料填写不完全，无法提交订单", Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent();
				intent.setClass(this, UserInfoAcitivity.class);
				this.startActivity(intent);
				this.finish();
				return;
			}
			if (goodType.equals("LLBCP")) {
				if (!HttpTool.isPhone(phone_editText.getText().toString())) {
					Toast.makeText(this, "请输入正确的手机号！", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (Float.parseFloat(getIntent().getStringExtra("goodPrice"))
						+ Float.parseFloat(getIntent().getStringExtra("costs")) > MyApp
						.getInstence().getSurplusFloat()) {
					Toast.makeText(this, "您的余额不足", Toast.LENGTH_LONG).show();
					return;
				}
			} else {
				if (count == 0) {
					Toast.makeText(this, "请选择商品数量！", Toast.LENGTH_LONG).show();
					return;
				}
				if (count
						* Float.parseFloat(getIntent().getStringExtra(
								"goodPrice"))
						+ Float.parseFloat(getIntent().getStringExtra("costs")) > MyApp
						.getInstence().getSurplusFloat()) {
					Toast.makeText(this, "您的余额不足", Toast.LENGTH_LONG).show();
					return;
				}
			}
			createSubMitPop();
		}

	}

	public void infoClick(View v) {
		LogUtils.e("sdfasdf", id + "+" + getIntent().getStringExtra("goodType"));
		if ((getIntent().getStringExtra("goodType").equals("TX") && FormatStringUtil
				.isEmpty(id))
				|| ((!getIntent().getStringExtra("goodType").equals("TX") && (FormatStringUtil
						.isEmpty(MyApp.getInstence().expPosition) || FormatStringUtil
						.isEmpty(MyApp.getInstence().expName))))) {
			Intent intent = new Intent();
			intent.setClass(this, UserInfoAcitivity.class);
			this.startActivity(intent);
			this.finish();
			return;
		}
	}

	private void initSubmit() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在提交，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "goodUids", "orderQuantity",
								"orderAccount", "contacterName",
								"contacterPhoneNumber", "contacterAddress" },
						new String[] { "10049", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), uids,
								String.valueOf(count), id,
								MyApp.getInstence().expName,
								MyApp.getInstence().getPhone(),
								MyApp.getInstence().expPosition });
				String json = HttpTool.httpGetJson1(ShopInfoActivity.this, url,
						hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					MyApp.getInstence().setSurplus(
							Float.parseFloat(HttpTool.getString(json,
									"remainingMonay")));
					HandleUtil.sendInt(hd, 1);
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();

	}
	private void initSubmit_LLBCP() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "正在提交，请稍后。。。");
		dialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "goodUids", "custom_product_id","flowPhone"},
								new String[] { "10055", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), uids,getIntent().getStringExtra("flowsign"),phone_editText.getText().toString()});
				String json = HttpTool.httpGetJson1(ShopInfoActivity.this, url,
						hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					MyApp.getInstence().setSurplus(
							MyApp.getInstence().getSurplusFloat()-(Float.parseFloat(getIntent().getStringExtra("goodPrice"))+Float.parseFloat(getIntent().getStringExtra("costs"))));
					HandleUtil.sendInt(hd, 1);
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();
		
	}

	/***** 下单确认 ****/
	private void createSubMitPop() {
		// TODO Auto-generated method stub
		final Dialog dialog1 = new Dialog(this, R.style.MyDialogStyle);
		dialog1.setCanceledOnTouchOutside(true);
		View view = View.inflate(this, R.layout.pop_ok, null);
		Button dismiss_btn = (Button) view.findViewById(R.id.button_cancel);
		Button exit_btn = (Button) view.findViewById(R.id.button_exit);
		dismiss_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog1.dismiss();
			}
		});
		exit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (goodType.equals("LLBCP")) {
					initSubmit_LLBCP();
				}else {
					initSubmit();
				}
				dialog1.dismiss();
			}
		});
		dialog1.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog1.setContentView(view);
		dialog1.show();
	}
}
