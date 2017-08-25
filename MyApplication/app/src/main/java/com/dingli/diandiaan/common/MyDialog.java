package com.dingli.diandiaan.common;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;

/**
 * Created by Administrator on 2015/5/27.
 */
public class MyDialog {
	AlertDialog alertDialog = null;
	Context context = null;
	public MyDialog(Context context) {
		this.context = context;
	}

	public void dismiss(){
		alertDialog.dismiss();
	}

	// waiting
	public void waiting(String text, Boolean cancelAble) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View dialog_view = inflater.inflate(R.layout.layout_dialog_loading, null);
			((TextView) dialog_view.findViewById(R.id.text)).setText(text);
			alertDialog = new AlertDialog.Builder(context).create();
			alertDialog.setCancelable(false);
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(dialog_view);
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
}
