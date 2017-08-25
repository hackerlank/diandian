package com.dingli.diandiaan.common;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dingli.diandiaan.R;


public class DuiHuaDialogs extends Dialog{


	TextView tvwenzd,titletv;
	SelectDialogButtonListener listener;
	Context context;
	Button btncal,btnok;
	String content;
	String title;
	String queren;
	String zuoqu;
	public DuiHuaDialogs(Context context) {
		super(context);
		this.context=context;
	}

    public DuiHuaDialogs(Context context, String title, String content, String zuo, String quren,
						 SelectDialogButtonListener Listener) {
		super(context, R.style.HintDialogStyle);
		this.context=context;
		this.content=content;
		this.listener=Listener;
		this.title=title;
		this.queren=quren;
		this.zuoqu=zuo;
	}
	public DuiHuaDialogs(Context context, int theme) {
		super(context, R.style.HintDialogStyle);
		this.context=context;
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		View huihuadialog=getLayoutInflater().inflate(R.layout.dialog_dianmings, null);
    	setContentView(huihuadialog);
		tvwenzd=(TextView)huihuadialog.findViewById(R.id.tvwenzd);
		btncal=(Button)huihuadialog.findViewById(R.id.btncal);
		btnok=(Button)huihuadialog.findViewById(R.id.btnok);
		titletv=(TextView)huihuadialog.findViewById(R.id.titletv);
		tvwenzd.setText(content);
		titletv.setText(title);
		btnok.setText(queren);
		if (zuoqu.equals("new")){
			btncal.setVisibility(View.GONE);
		}else{
			btncal.setVisibility(View.VISIBLE);
		}
		initlistener();
	}
	
	private void initlistener() {
		ButtonListener listener=new ButtonListener();
		btnok.setOnClickListener(listener);
		btncal.setOnClickListener(listener);
	}
	private class ButtonListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btncal:
				listener.checkButton(R.id.btncal);
				dismiss();
				break;
			case R.id.btnok:
				listener.checkButton(R.id.btnok);
				dismiss();
				break;
			}
		}
	}
	public interface SelectDialogButtonListener{
		
		public void checkButton(int id);
	}
}
