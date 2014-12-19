package com.hzkjkf.util;

import android.os.Handler;

public class HandleUtil {
	public static void sendInt(Handler hd, int arg) {
		if (hd != null) {
			hd.sendEmptyMessage(arg);
		}
	}

	public static void post(Handler hd, Runnable runnable) {
		if (hd != null) {
			hd.post(runnable);
		}
	}

	public static void postDelayed(Handler hd, Runnable runnable, long time) {
		if (hd != null) {
			hd.postDelayed(runnable, time);
		}

	}

	public static void removeCallbacks(Handler hd, Runnable runnable) {
		if (hd != null) {
			hd.removeCallbacks(runnable);
		}
	}
}
