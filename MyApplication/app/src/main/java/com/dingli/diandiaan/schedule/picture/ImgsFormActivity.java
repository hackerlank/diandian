package com.dingli.diandiaan.schedule.picture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.lostproperty.FileTraversal;
import com.dingli.diandiaan.lostproperty.ImgCallBack;
import com.dingli.diandiaan.lostproperty.ImgsAdapter;
import com.dingli.diandiaan.lostproperty.Util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImgsFormActivity extends Activity implements OnClickListener {

	Bundle bundle;
	FileTraversal fileTraversal;
	GridView imgGridView;
	ImgsAdapter imgsAdapter;
	LinearLayout select_layout;
	Util util;
	RelativeLayout relativeLayout2;
	HashMap<Integer, ImageView> hashImage;
	LinearLayout btn_ok, btn_back;
	ArrayList<String> filelist;
	RelativeLayout back;
	Button count;
	TextView btn_cancel;
	List<String> data;
	List<CheckBox> listcheck;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogrally);
		imgGridView = (GridView) findViewById(R.id.gridView1);
		bundle = getIntent().getExtras();
		fileTraversal = bundle.getParcelable("data");
		data=new ArrayList<>();
		for (int i=fileTraversal.filecontent.size()-1;i>=0;i--){
			data.add(fileTraversal.filecontent.get(i));
		}
		imgsAdapter = new ImgsAdapter(this, data,
				onItemClickClass);
		imgGridView.setAdapter(imgsAdapter);
		select_layout = (LinearLayout) findViewById(R.id.selected_image_layout);
		relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
		btn_ok = (LinearLayout) findViewById(R.id.btn_ok);
		back = (RelativeLayout) findViewById(R.id.back);
		count = (Button) findViewById(R.id.count);
		btn_back=(LinearLayout) findViewById(R.id.btn_back);
		btn_cancel=(TextView) findViewById(R.id.btn_cancel);
		hashImage = new HashMap<Integer, ImageView>();
		filelist = new ArrayList<String>();
		listcheck=new ArrayList<>();
		util = new Util(this);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendfiles();
			}
		});
		btn_back.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

	}
	@SuppressLint("NewApi")
	public ImageView iconImage(String filepath, int index, CheckBox checkBox)
			throws FileNotFoundException {
		LayoutParams params = new LayoutParams(
				relativeLayout2.getMeasuredHeight() - 10,
				relativeLayout2.getMeasuredHeight() - 10);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(R.mipmap.imgbg);
		float alpha = 1;
		imageView.setAlpha(alpha);
		util.imgExcute(imageView, imgCallBack, filepath);
		imageView.setOnClickListener(new ImgOnclick(filepath, checkBox));
		return imageView;
	}

	ImgCallBack imgCallBack = new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};

	class ImgOnclick implements OnClickListener {
		String filepath;
		CheckBox checkBox;

		public ImgOnclick(String filepath, CheckBox checkBox) {
			this.filepath = filepath;
			this.checkBox = checkBox;
		}

		@Override
		public void onClick(View arg0) {
			checkBox.setChecked(false);
			select_layout.removeView(arg0);
			count.setText(select_layout.getChildCount() + "");
			filelist.remove(filepath);
		}
	}

	ImgsAdapter.OnItemClickClass onItemClickClass = new ImgsAdapter.OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			String filapath = data.get(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				select_layout.removeAllViews();
				filelist.clear();
				count.setText(select_layout.getChildCount() + "");
					listcheck.clear();
			} else {
				checkBox.setChecked(true);
				select_layout.removeAllViews();
				filelist.clear();
				listcheck.add(checkBox);
				for (int i=0;i<listcheck.size();i++){
					if (listcheck.get(i)!=checkBox){
						listcheck.get(i).setChecked(false);
					}else{
						try {
							ImageView imageView = iconImage(filapath, Position,
									checkBox);
							if (imageView != null) {
						if (select_layout.getChildCount() < 1) {
							hashImage.put(Position, imageView);
							filelist.add(filapath);
							select_layout.addView(imageView);
							count.setText(select_layout.getChildCount() + "");
						}
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	};
	public void sendfiles() {
		  if (filelist.size()!=0) {
			  DianApplication.sharedPreferences.saveString("fileone",filelist.get(0));
		  }
		finish();

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			startActivity(new Intent(ImgsFormActivity.this, ImgFileListFormActivity.class));
			finish();
			break;
		case R.id.btn_cancel:
			finish();
			break;
		
		default:
			break;
		}
	}
}
