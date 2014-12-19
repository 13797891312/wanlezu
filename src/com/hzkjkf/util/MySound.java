package com.hzkjkf.util;

import java.io.IOException;

import com.hzkjkf.wanzhuan.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class MySound {
	private static MySound mySound;
	SoundPool sp;
	private int sound1;
	private int sound2;
	private int sound3;
	private Context context;
	private SaveDate sd;

	public static MySound getInstence(Context context) {
		if (mySound == null) {
			mySound = new MySound(context);
		}
		return mySound;
	}

	private MySound(Context context) {
		sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 1);
		// sound1=sp.load(context, R.raw.gb_roulette_click, 1);
		// sound2=sp.load(context, R.raw.gb_roulette_popup_win, 1);
		// sound3=sp.load(context, R.raw.gb_roulette_popup_lose, 1);
		this.context = context;
		sd = SaveDate.getInstence(context);
	}

	public void play(int id) {
		if (sd.isMusic()) {
			sp.play(id, 1.0f, 1.0f, 0, 0, 1.0f);
		}
	}

	public void clear() {
		sp.release();
	}

	public int getSound1() {
		return sound1;
	}

	public int getSound2() {
		return sound2;
	}

	public int getSound3() {
		return sound3;
	}

}
