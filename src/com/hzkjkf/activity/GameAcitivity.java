package com.hzkjkf.activity;

import com.hzkjkf.util.BitmapUtil;
import com.hzkjkf.wanzhuan.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class GameAcitivity extends Activity implements
		android.view.View.OnClickListener {
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.gemebanner);
		((ImageView) findViewById(R.id.image_baner)).setImageBitmap(bitmap);
		((TextView) findViewById(R.id.title)).setText("玩游戏");
		findViewById(R.id.layout_dzp).setOnClickListener(this);
		findViewById(R.id.layout_csz).setOnClickListener(this);
		findViewById(R.id.layout_xpg).setOnClickListener(this);
	}

	public void backClick(View v) {
		this.finish();
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stubs
		Intent intent = new Intent();
		int id = v.getId();
		if (id == R.id.layout_dzp) {
			intent.setClass(this, Game1Activity.class);
			this.startActivity(intent);
		} else if (id == R.id.layout_csz) {
			intent.setClass(this, Game2Activity.class);
			this.startActivity(intent);
		} else if (id == R.id.layout_xpg) {
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BitmapUtil.recycle(bitmap);
	}
}
