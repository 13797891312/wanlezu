package com.hzkjkf.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzkjkf.imageloader.Imageloader;
import com.hzkjkf.util.ErrorCode;
import com.hzkjkf.util.FormatStringUtil;
import com.hzkjkf.util.HandleUtil;
import com.hzkjkf.util.HttpTool;
import com.hzkjkf.util.LogUtils;
import com.hzkjkf.util.MyApp;
import com.hzkjkf.util.MySound;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.test.suitebuilder.annotation.Smoke;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartEarnActivity extends Activity implements OnClickListener,
		OnDismissListener {
	private int type;
	private ImageView iv_bg;
	private RelativeLayout rel_start;
	private PopupWindow pop, result_pop;
	private ImageView iv_alpha;
	private ViewStub loading_viewsStub;
	private ViewStub loading_pop_viewStub, loading_result;
	private Map<String, String> map = new HashMap<String, String>();
	private TextView question_textview, a_textview, b_textview, c_textview,
			d_textview;
	private AnswerLisoner lisoner = new AnswerLisoner();
	private RelativeLayout pop_bg;
	private Button next_button, collect_button, info_button, youhui_button,
			share_button;
	private Map<String, String> shareMap = new HashMap<String, String>();
	private ImageView result_iamgeview;
	private TextView result_textview, time_tv;
	private ProgressDialog dialog;
	private String advertiseId, imageUrl, smallImage;
	private EditText subMit_title, subMit_contexts;
	private WebView web;
	private ProgressBar progress;
	/*** �Ƿ��з����н�0����û�У�1������ ***/
	private int shareAble = 0;
	/*** �Ƿ����Ż�ȯ��0��û�У�1������ ***/
	private int couponAble = 0;
	/*** ������ ***/
	private int count = 10;
	private int startCount = 10;
	private final int ALPHA = 200;
	/*** ��������Ϣ ***/
	private String intro = "";
	/** �Ƿ������ȷ�� ***/
	boolean isAnser = false;
	// ����������Activity��������³�Ա����
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	final UMShakeService mShakeController = UMShakeServiceFactory
			.getShakeService("com.umeng.share");
	private String shareUrl="http://www.wanzhuan6.com/appService/advertiseInfo/";

	/**** ����������� ***/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startearn);
		advertiseId = this.getIntent().getStringExtra("adUids");// ��ȡ���ID����Ϊ�����Ǵ��ղؽ���ֱ����ת�����ģ�����Ҫ����ָ�����
		imageUrl = this.getIntent().getStringExtra("bigImage");// ��ȡ���ͼƬURL;
		smallImage = this.getIntent().getStringExtra("smallImage");
		time_tv = (TextView) findViewById(R.id.textView_time);
		shareAble = getIntent().getIntExtra("shareAble", 0);
		couponAble = getIntent().getIntExtra("couponAble", 0);
		intro = getIntent().getStringExtra("intro");
		type = this.getIntent().getIntExtra("type", 0);
		iv_alpha = (ImageView) findViewById(R.id.imageview_alpha);
		loading_viewsStub = (ViewStub) findViewById(R.id.viewstub_loading);
		collect_button = ((Button) findViewById(R.id.button_collect));
		youhui_button = (Button) findViewById(R.id.button_youhui);
		share_button = (Button) findViewById(R.id.button_share);
		info_button = ((Button) findViewById(R.id.button_info));
		collect_button.getBackground().setAlpha(ALPHA);
		share_button.getBackground().setAlpha(ALPHA);
		info_button.getBackground().setAlpha(ALPHA);
		youhui_button.getBackground().setAlpha(ALPHA);
		youhui_button.setOnClickListener(this);
		collect_button.setOnClickListener(this);
		share_button.setOnClickListener(this);
		info_button.setOnClickListener(this);
		iv_bg = (ImageView) findViewById(R.id.iamgeview_bg);
		rel_start = (RelativeLayout) findViewById(R.id.rel_start);
		rel_start.setOnClickListener(this);
		initData();
	}

	// button����Ч��
	private void initButton() {
		Animation start = AnimationUtils.loadAnimation(this,
				R.anim.button_animation);
		Animation info = AnimationUtils.loadAnimation(this,
				R.anim.button_animation1);
		if (collect_button.getVisibility() == View.VISIBLE) {
			collect_button.startAnimation(info);
			share_button.startAnimation(info);
			youhui_button.startAnimation(info);
		}
		info_button.startAnimation(info);
		rel_start.startAnimation(start);
	}

	Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// ���Ӵ���
				Toast.makeText(StartEarnActivity.this, "������ϣ�������������",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:// ��ȡ���
					// Imageloader.getInstence().urlToBitmap1(map.get("pictureURL"),
					// iv_bg, hd, null);
				break;
			case 2:// ���ͼƬ��ʾ���
				loading_viewsStub.setVisibility(View.GONE);
				CountDownTimer timer = new CountDownTimer(10000, 1000) {
					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub
						time_tv.setText(millisUntilFinished / 1000 + "���ɴ���");
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						time_tv.setVisibility(View.GONE);
						info_button.setVisibility(View.VISIBLE);
						if (type == 0) {
							collect_button.setVisibility(View.VISIBLE);
							share_button.setVisibility(View.VISIBLE);
							youhui_button.setVisibility(View.VISIBLE);
							rel_start.setVisibility(View.VISIBLE);
						}
						initButton();
					}
				}.start();
				break;
			case 3:// ��ȡ����Ŀ
				question_textview.setText(map.get("question"));
				a_textview.setText(map.get("a"));
				b_textview.setText(map.get("b"));
				c_textview.setText(map.get("c"));
				d_textview.setText(map.get("d"));
				loading_pop_viewStub.setVisibility(View.GONE);
				break;
			case 4:// ������ȷ
				isAnser = true;
				next_button.setEnabled(true);
				result_textview
						.setText("����ˣ��ɹ�׬ȡ "
								+ FormatStringUtil.getDesplay(map.get("money"))
								+ "��ң�");
				MyApp.getInstence().setSurplus(
						MyApp.getInstence().getSurplusFloat()
								+ Float.parseFloat(map.get("money")));
				int count = Integer
						.parseInt(MyApp.getInstence().getHomeData()[0]);
				if (count > 0) {
					MyApp.getInstence().setHomeData(0,
							String.valueOf(count - 1));
				}
				result_pop.setBackgroundDrawable(new ColorDrawable());
				loading_result.setVisibility(View.GONE);

				break;
			case 5:// �������
				next_button.setEnabled(true);
				result_iamgeview.setImageResource(R.drawable.glicon_03);
				next_button.setVisibility(View.GONE);
				result_textview.setText("����ˣ���ȴ�10���Զ�����");
				result_textview.setTextSize(20);
				loading_result.setVisibility(View.GONE);
				hd.postDelayed(runnable, 1000);
				break;
			case 6:// ��ȡ���̼���Ϣ
				if (dialog != null) {
					dialog.cancel();
				}
				;
				View view = View.inflate(StartEarnActivity.this,
						R.layout.pop_startearn_shangjia, null);

				((TextView) view.findViewById(R.id.title)).setText("�̼���Ϣ");
				((Button) view.findViewById(R.id.button_ok))
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								subMitData();
							}
						});
				web = ((WebView) view.findViewById(R.id.webview));
				progress = ((ProgressBar) view.findViewById(R.id.progress));
				((TextView) view.findViewById(R.id.textview_name)).setText(map
						.get("advertisName"));
				TextView web_tv = ((TextView) view
						.findViewById(R.id.textview_web));
				TextView phone_tv = ((TextView) view
						.findViewById(R.id.textview_tel));
				web_tv.setText(map.get("sellerWebsite"));
				web_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// �»���
				web_tv.setOnClickListener(StartEarnActivity.this);
				phone_tv.setOnClickListener(StartEarnActivity.this);
				phone_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// �»���
				((TextView) view.findViewById(R.id.textview_tel)).setText(map
						.get("sellerPhone"));
				((TextView) view.findViewById(R.id.textview_adrass))
						.setText("��ϵ��ַ:" + map.get("sellerAdd"));
				subMit_title = (EditText) view
						.findViewById(R.id.edittext_title);
				subMit_contexts = (EditText) view
						.findViewById(R.id.edittext_idea);
				createPop(view);
				break;
			case 7:// �ղسɹ�
				if (dialog != null) {
					dialog.cancel();
				}
				;
				Toast toast = Toast.makeText(StartEarnActivity.this, "�ղسɹ�",
						2000);

				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				MyApp.getInstence().collectCount += 1;
				break;
			case 8:
				Toast.makeText(StartEarnActivity.this, "��ǰ���޹��ɿ�",
						Toast.LENGTH_SHORT).show();
				StartEarnActivity.this.finish();
				break;
			case 9:// ���ϲ���������
				Toast.makeText(StartEarnActivity.this, "����������������",
						Toast.LENGTH_SHORT).show();
				break;
			case 10:// �ύ����ɹ�
				if (dialog != null) {
					dialog.cancel();
				}
				;
				Toast.makeText(StartEarnActivity.this, "�ύ�ɹ�����л���ı������", 2000)
						.show();
				subMit_title.setText("");
				subMit_contexts.setText("");
				break;
			default:// ��������
				if (dialog != null) {
					dialog.cancel();
				}
				;
				Toast toast1 = Toast.makeText(StartEarnActivity.this,
						ErrorCode.getString(msg.what), 2000);
				toast1.setGravity(Gravity.CENTER, 0, 0);
				toast1.show();
				break;
			}
		};
	};

	/**** ��ȡͼƬ���� ****/
	private void initData() {
		// TODO Auto-generated method stub
		loading_viewsStub.setVisibility(View.VISIBLE);
		info_button.setVisibility(View.GONE);
		collect_button.setVisibility(View.GONE);
		share_button.setVisibility(View.GONE);
		youhui_button.setVisibility(View.GONE);
		rel_start.setVisibility(View.GONE);
		iv_bg.setImageBitmap(null);
		Imageloader.getInstence().recBitmap();
		map.clear();
		Imageloader.getInstence().urlToBitmap1(imageUrl, iv_bg, hd, null);
	}

	/*** ��������Ի�����̼���Ϣ�Ի��� ****/
	private void createPop(View v) {
		// TODO Auto-generated method stub
		pop = new PopupWindow(v, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new ColorDrawable());
		pop.setOnDismissListener(this);
		Button btn = (Button) v.findViewById(R.id.button_close);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (web != null) {
					web.clearCache(true);
					web.clearDisappearingChildren();
					web.clearFormData();
					web.clearHistory();
					web.clearMatches();
					web.clearSslPreferences();
					web.clearView();
				}
				pop.dismiss();
			}
		});
		pop.setAnimationStyle(android.R.style.Animation_InputMethod);
		pop.setFocusable(true);
		iv_alpha.setVisibility(View.VISIBLE);
		pop.showAtLocation(findViewById(R.id.rel), Gravity.BOTTOM, 0, 0);
	}

	/*** ���������н���ʾ�Ի��� ****/
	private void createSharePop() {
		// TODO Auto-generated method stub
		View view = View.inflate(this, R.layout.pop_share, null);
		final Dialog dialog = new Dialog(this, R.style.MyDialogStyle);
		dialog.setCanceledOnTouchOutside(true);
		LinearLayout qqkj_layout = (LinearLayout) view.findViewById(R.id.qqkj);
		LinearLayout txwb_layout = (LinearLayout) view.findViewById(R.id.txwb);
		LinearLayout wxpyq_layout = (LinearLayout) view
				.findViewById(R.id.wxpyq);
		LinearLayout xlwb_layout = (LinearLayout) view.findViewById(R.id.xlwb);
		TextView qqkj_tv = (TextView) view.findViewById(R.id.textView_qqkj);
		TextView txwb_tv = (TextView) view.findViewById(R.id.textView_txwb);
		TextView wxpyq_tv = (TextView) view.findViewById(R.id.textView_wxpyq);
		TextView xlwb_tv = (TextView) view.findViewById(R.id.textView_xlwb);
		qqkj_tv.setText(Integer.parseInt(shareMap.get("SHARE_PAY_QQ"))/2+"");
		txwb_tv.setText(Integer.parseInt(shareMap.get("SHARE_PAY_WB"))/2+"");
		xlwb_tv.setText(Integer.parseInt(shareMap.get("SHARE_PAY_XL"))/2+"");
		wxpyq_tv.setText(Integer.parseInt(shareMap.get("SHARE_PAY_WX"))/2+"");
		LogUtils.e("sdfsad", shareMap.toString());
		qqkj_layout
				.setVisibility(shareMap.get("SHARE_PAY_QQ").equals("0") ? View.GONE
						: View.VISIBLE);
		txwb_layout
				.setVisibility(shareMap.get("SHARE_PAY_WB").equals("0") ? View.GONE
						: View.VISIBLE);
		wxpyq_layout
				.setVisibility(shareMap.get("SHARE_PAY_WX").equals("0") ? View.GONE
						: View.VISIBLE);
		xlwb_layout
				.setVisibility(shareMap.get("SHARE_PAY_XL").equals("0") ? View.GONE
						: View.VISIBLE);
		Button dismiss_btn = (Button) view.findViewById(R.id.button_cancel);
		dismiss_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mController.openShare(StartEarnActivity.this, false);
				mController.unregisterListener(snsPostListener);
				mController.registerListener(snsPostListener);
				dialog.dismiss();
			}
		});
		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.setContentView(view);
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		View view;
		int id = v.getId();
		if (id == R.id.rel_start) {
			view = View.inflate(this, R.layout.pop_startearn_start, null);
			pop_bg = (RelativeLayout) view.findViewById(R.id.pop_bg);
			// back_bg = (RelativeLayout) view.findViewById(R.id.back_bg);
			question_textview = (TextView) view
					.findViewById(R.id.textview_question);
			b_textview = (TextView) view.findViewById(R.id.textView2);
			a_textview = (TextView) view.findViewById(R.id.textView1);
			c_textview = (TextView) view.findViewById(R.id.textView3);
			d_textview = (TextView) view.findViewById(R.id.textView4);
			a_textview.setTag("1");
			b_textview.setTag("2");
			c_textview.setTag("3");
			d_textview.setTag("4");
			b_textview.setOnClickListener(lisoner);
			a_textview.setOnClickListener(lisoner);
			c_textview.setOnClickListener(lisoner);
			d_textview.setOnClickListener(lisoner);
			loading_pop_viewStub = (ViewStub) view
					.findViewById(R.id.viewstub_loading);
			initPopData();
			createPop(view);
		} else if (id == R.id.button_collect) {
			initCollectData();
		} else if (id == R.id.button_info) {
			initInfoData();
		} else if (id == R.id.button_share) {
			umShare();
			WXShare();
			QQShare();
			initShareData();
		} else if (id == R.id.button_youhui) {
			if (couponAble == 0) {
				Toast.makeText(this, "�˹��û���Ż�ȯ", 0).show();
				return;
			}
			Intent intent = new Intent(this, YouHuiActivity.class);
			intent.putExtra("advertiseUids", advertiseId);
			this.startActivity(intent);
		} else if (id == R.id.textview_web) {
			if (map.get("sellerWebsite").contains("http")) {
				web.setVisibility(View.VISIBLE);
				loadWeb(map.get("sellerWebsite"));
			}
		} else if (id == R.id.textview_tel) {
			callPhone(map.get("sellerPhone"));
		}

	}

	private void initShareData() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "���ڼ��أ����Ժ�...");
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "advertiseId", "imei" },
						new String[] { "10065", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(), advertiseId,
								MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(StartEarnActivity.this,
						url, hd);
				if (!json.isEmpty()) {
					if (!HttpTool.getFlag(json)) {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
						return;
					}
					try {
						JSONObject object = new JSONArray(json)
								.getJSONObject(0).getJSONObject("result");
						shareMap.put("SHARE_PAY_QQ", FormatStringUtil.isEmptyOr0(object.getString("SHARE_PAY_QQ"))?"0": FormatStringUtil
								.getDesplay(object.getString("SHARE_PAY_QQ")));
						shareMap.put("SHARE_PAY_WX", FormatStringUtil.isEmptyOr0(object.getString("SHARE_PAY_WX"))?"0": FormatStringUtil
								.getDesplay(object.getString("SHARE_PAY_WX")));
						shareMap.put("SHARE_PAY_XL",FormatStringUtil.isEmptyOr0(object.getString("SHARE_PAY_XL"))?"0": FormatStringUtil
								.getDesplay(object.getString("SHARE_PAY_XL")));
						shareMap.put("SHARE_PAY_WB", FormatStringUtil.isEmptyOr0(object.getString("SHARE_PAY_WB"))?"0": FormatStringUtil
								.getDesplay(object.getString("SHARE_PAY_WB")));
						HandleUtil.post(hd, new Runnable() {
							@Override
							public void run() {                 
								// TODO Auto-generated method stub
								if (dialog != null) {
									dialog.cancel();
								}
								if (shareAble == 1) {
									createSharePop();
								} else {
									mController.openShare(
											StartEarnActivity.this, false);
									mController
											.unregisterListener(snsPostListener);
									mController
											.registerListener(snsPostListener);
								}
							}
						});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					hd.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	/** ��ʼ���Ѳ����� **/
	private void initCollectData() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "�����ύ�����Ժ�");
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "advertiseId", "imei" },
						new String[] { "10016", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(), advertiseId,
								MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(StartEarnActivity.this,
						url, hd);
				if (!json.isEmpty()) {
					Map<String, String> map = HttpTool.jsonToMap(json);
					if (Boolean.parseBoolean(map.get("flag").toString())) {
						hd.sendEmptyMessage(7);// ���ؽ�����
					} else {
						hd.sendEmptyMessage(Integer.parseInt(map
								.get("errorCode")));
					}
				} else {
					hd.sendEmptyMessage(0);
				}
			}
		}).start();

	}

	/*** ��ʼ���̼���Ϣ ***/
	private void initInfoData() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", "���ڼ��أ����Ժ�");
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "advertiseId", "imei" },
						new String[] { "10011", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(), advertiseId,
								MyApp.getInstence().getImei() });
				String json = HttpTool.httpGetJson1(StartEarnActivity.this,
						url, hd);
				if (!json.isEmpty()) {
					JSONArray array = HttpTool.getResult(json);
					for (int i = 0; i < array.length(); i++) {
						try {
							JSONObject object = array.getJSONObject(i);
							map.put("sellerAdd",
									object.getString("sellerAdd")
											.equals("null") ? "����" : object
											.getString("sellerAdd"));
							map.put("sellerPhone",
									object.getString("sellerPhone").equals(
											"null") ? "����" : object
											.getString("sellerPhone"));
							map.put("sellerWebsite",
									object.getString("sellerWebsite").equals(
											"null") ? "����" : object
											.getString("sellerWebsite"));
							map.put("advertisName",
									object.getString("advertiseName"));
							hd.sendEmptyMessage(6);// ���ؽ�����
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					hd.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	/*** ��ȡ�������� ***/
	private void initPopData() {
		// TODO Auto-generated method stub
		loading_pop_viewStub.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(
						new String[] { "classId", "phoneNumber", "requestId",
								"imei", "advertiseId" },
						new String[] { "10017", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(),
								MyApp.getInstence().getImei(), advertiseId });
				String json = HttpTool.httpGetJson1(StartEarnActivity.this,
						url, hd);
				if (!json.isEmpty()) {
					JSONArray array = HttpTool.getResult(json);
					for (int i = 0; i < array.length(); i++) {
						try {
							JSONObject object = array.getJSONObject(i);
							map.put("question", object.getString("question"));
							JSONObject object_answer = object
									.getJSONObject("questionSort");
							map.put("a", object_answer.getString("a"));
							map.put("b", object_answer.getString("b"));
							map.put("c", object_answer.getString("c"));
							map.put("d", object_answer.getString("d"));
							hd.sendEmptyMessage(3);// ���ؽ�����
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					hd.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	/** �ύ�������� **/
	private void subMitData() {
		// TODO Auto-generated method stub
		if (subMit_title.getText().toString().isEmpty()) {
			Toast.makeText(this, "���������", Toast.LENGTH_SHORT).show();
			return;
		}
		if (subMit_contexts.getText().toString().isEmpty()) {
			Toast.makeText(this, "����������ı�", Toast.LENGTH_SHORT).show();
			return;
		}
		dialog = ProgressDialog.show(this, "", "�����ύ�����Ժ�");
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "advertiseId", "imei",
						"suggestTitle", "suggestContent" },
						new String[] { "10025", MyApp.getInstence().getPhone(),
								MyApp.getInstence().getToken(), advertiseId,
								MyApp.getInstence().getImei(),
								subMit_title.getText().toString(),
								subMit_contexts.getText().toString() });
				String json = HttpTool.httpGetJson1(StartEarnActivity.this,
						url, hd);
				if (!json.isEmpty()) {
					if (HttpTool.getFlag(json)) {
						hd.sendEmptyMessage(10);// ���ؽ�����
					} else {
						hd.sendEmptyMessage(HttpTool.getErrorCode(json));
					}
				} else {
					hd.sendEmptyMessage(0);
				}
			}
		}).start();

	}

	/** ����ļ��� ***/
	class AnswerLisoner implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pop_bg.setVisibility(View.GONE);
			View view = View.inflate(StartEarnActivity.this,
					R.layout.pop_startearn_answer, null);
			loading_result = (ViewStub) view
					.findViewById(R.id.viewstub_loading);
			loading_result.setVisibility(View.VISIBLE);
			createResultPop(view);
			initResultData((String) ((TextView) v).getTag());
		}

		/*** ��ʼ�������� **/
		private void initResultData(final String answer) {
			// TODO Auto-generated method stub
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String url = HttpTool.getUrl(new String[] { "classId",
							"phoneNumber", "requestId", "advertiseId",
							"questionAnswer", "imei" }, new String[] { "10018",
							MyApp.getInstence().getPhone(),
							MyApp.getInstence().getToken(), advertiseId,
							answer, MyApp.getInstence().getImei() });
					String json = HttpTool.httpGetJson1(StartEarnActivity.this,
							url, hd);
					if (!json.isEmpty()) {
						if (!HttpTool.getFlag(json)) {
							hd.sendEmptyMessage(HttpTool.getErrorCode(json));
							return;
						}
						JSONArray array = HttpTool.getResult(json);
						for (int i = 0; i < array.length(); i++) {
							try {
								JSONObject object = array.getJSONObject(i);
								map.put("money", object.getString("earnMoney"));
								if (Boolean.parseBoolean(object
										.getString("rightAnswer"))) {
									MyApp.getInstence().remainAdcurrent = Integer
											.parseInt(object
													.getString("remainAd"));
									hd.sendEmptyMessage(4);// ������ȷ
								} else {
									hd.sendEmptyMessage(5);// �������
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						hd.sendEmptyMessage(0);
						hd.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								result_pop.dismiss();
							}
						});
					}
				}
			}).start();

		}
	}

	/** ������ **/
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			count--;
			result_textview.setText("����ˣ���ȴ�" + count + "���Զ�����");
			if (count > 0) {
				hd.postDelayed(this, 1000);
			} else {
				result_pop.dismiss();
				count = 10;
				pop_bg.setVisibility(View.VISIBLE);
			}
		}
	};

	/** ��������Ի��� ****/
	private void createResultPop(View v) {
		// TODO Auto-generated method stub
		result_pop = new PopupWindow(v, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		result_pop.setOnDismissListener(this);
		next_button = (Button) v.findViewById(R.id.button_next);
		next_button.setEnabled(false);
		result_iamgeview = (ImageView) v.findViewById(R.id.imageView1);
		result_textview = (TextView) v.findViewById(R.id.textView1);
		next_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				result_pop.dismiss();
				pop.dismiss();
				StartEarnActivity.this.setResult(1,
						new Intent().putExtra("isAnser", isAnser));
				StartEarnActivity.this.finish();
			}
		});
		result_pop.setFocusable(true);
		iv_alpha.setVisibility(View.VISIBLE);
		result_pop.showAtLocation(findViewById(R.id.rel), Gravity.CENTER, 0, 0);
	}

	/*** pop��ʧ��ʱ��ص� ****/
	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		iv_alpha.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Imageloader.getInstence().recBitmap();
		hd = new Handler();
	}

	private void loadWeb(String url) {
		// TODO Auto-generated method stub
		web.getSettings().setJavaScriptEnabled(true);
		//
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(web, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				progress.setVisibility(View.VISIBLE);
			}
		});
		// web.setWebChromeClient(new WebChromeClient(){
		// @Override
		// public void onProgressChanged(WebView view, int newProgress) {
		// // TODO Auto-generated method stub
		// super.onProgressChanged(view, newProgress);
		// progress.setProgress(newProgress);
		// }
		//
		// });
		web.loadUrl(url);
	}

	private void callPhone(String phone) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();// ����һ����ͼ���������������ŵ�Activity
		intent.setAction("android.intent.action.CALL");
		intent.setData(Uri.parse("tel:" + phone));
		startActivity(intent);// �����ڲ����Զ�������,android.intent.category.DEFAULT
	}

	private void umShare() {
		String text = "�����壬�����׬Ǯ�����л���1ëǮ��iphone6���Ͻ��ʹ�ע������http://www.wanzhuan6.com";
		String title="�����������";
		// ����΢�ŷ�������
		UMImage shareImage = new UMImage(this, smallImage);
		shareImage.setTargetUrl(shareUrl+advertiseId);
		WeiXinShareContent weixinContent = new WeiXinShareContent(shareImage);
		weixinContent.setShareContent(title);
		weixinContent.setTitle("����������ķ���");
		mController.setShareMedia(weixinContent);
		// ��������Ȧ��������
		CircleShareContent circleShareContent = new CircleShareContent(title);
		circleShareContent.setShareMedia(shareImage);
		circleShareContent.setTitle(title);
		mController.setShareMedia(circleShareContent);
		// ��Ѷ΢����������

		UMImage shareImage1 = new UMImage(this, smallImage);
		TencentWbShareContent tencentShareContent = new TencentWbShareContent(
				shareImage1);
		tencentShareContent
				.setShareContent(title+shareUrl+advertiseId);
		mController.setShareMedia(tencentShareContent);

		// ����
		// ���÷�������
		mController.setShareContent(title);
		// ���÷���ͼƬ, ����2ΪͼƬ��url��ַ
		UMImage imageShare = new UMImage(this, smallImage);
		imageShare.setTargetUrl(shareUrl+advertiseId);
		mController.setShareMedia(imageShare);
	}

	/*** ���΢�ŷ��� **/
	private void WXShare() {
		// wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
		String appId = "wxdda391bef2a7a4fa";
		String appSecret = "0301085b7ce7224f8cd98240dddfbb96";
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
		wxHandler.addToSocialSDK();
		// ֧��΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/*** ���QQ���� **/
	private void QQShare() {
		// ����1Ϊ��ǰActivity������2Ϊ��������QQ���������APP ID������3Ϊ��������QQ���������APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "101104412",
				"e0d6d9517229b1e445db61781bdea50d");
		qqSsoHandler.addToSocialSDK();

		// ����1Ϊ��ǰActivity������2Ϊ��������QQ���������APP ID������3Ϊ��������QQ���������APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
				"101104412", "e0d6d9517229b1e445db61781bdea50d");
		qZoneSsoHandler.addToSocialSDK();
		// ��������SSO handler
		SinaSsoHandler sina = new SinaSsoHandler();
		sina.setShareAfterAuthorize(true);
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// ������Ѷ΢��SSO handler
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
	}

	SnsPostListener snsPostListener = new SnsPostListener() {
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			Toast.makeText(StartEarnActivity.this, "����ʼ", Toast.LENGTH_LONG)
					.show();

		}

		@Override
		public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
			// TODO Auto-generated method stub
			if (arg1 == 200) {
				String name = arg0.name();
				String shareType = "";
				if (name.equals("QZONE")) {
					shareType = "SHARE_PAY_QQ";
				} else if (name.equals("SINA")) {
					shareType = "SHARE_PAY_XL";
				} else if (name.equals("WEIXIN_CIRCLE")) {
					shareType = "SHARE_PAY_WX";
				} else if (name.equals("TENCENT")) {
					shareType = "SHARE_PAY_WB";
				} else {
					shareType = name;
				}
				LogUtils.e("asdfsadf", shareType + "  " + name);
				if (shareMap.get(shareType) != null
						&& !shareMap.get(shareType).equals("0")) {
					initShareResult(shareType);
				}
				Toast.makeText(StartEarnActivity.this, "����ɹ�" + arg1,
						Toast.LENGTH_LONG).show();
			} else
				Toast.makeText(StartEarnActivity.this, "����ʧ��" + arg1,
						Toast.LENGTH_LONG).show();
		}

	};

	private void initShareResult(final String name) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = HttpTool.getUrl(new String[] { "classId",
						"phoneNumber", "requestId", "imei", "shareType",
						"advertiseId" }, new String[] { "10066",
						MyApp.getInstence().getPhone(),
						MyApp.getInstence().getToken(),
						MyApp.getInstence().getImei(), name, advertiseId });
				String json = HttpTool.httpGetJson1(StartEarnActivity.this,
						url, hd);
				if (!json.isEmpty()) {
					if (HttpTool.getFlag(json)) {
						MyApp.getInstence().setSurplus(
								MyApp.getInstence().getSurplusFloat()
										+ Float.parseFloat(shareMap.get(name))
										/ 100);
					} else {
						HandleUtil.sendInt(hd, HttpTool.getErrorCode(json));
					}
				} else {
					HandleUtil.sendInt(hd, 0);
				}
			}
		}).start();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		mController.dismissShareBoard();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** ʹ��SSO��Ȩ����������´��� */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}