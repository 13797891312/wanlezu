package com.hzkjkf.myrunnble;

import android.content.Context;
import android.widget.Toast;

public class ShowToast implements Runnable {
	private String text = "";
	private Context context;

	public ShowToast(String text, Context context) {
		// TODO Auto-generated constructor stub
		this.text = text;
		this.context = context;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

}
