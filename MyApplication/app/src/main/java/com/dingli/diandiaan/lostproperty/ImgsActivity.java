package com.dingli.diandiaan.lostproperty;

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
import com.dingli.diandiaan.common.DianTool;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImgsActivity extends Activity implements OnClickListener {

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
		// imgGridView.setOnItemClickListener(this);
		util = new Util(this);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(null==filelist||filelist.size()==0){
					return;
				}
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
				select_layout.removeView(hashImage.get(Position));
				filelist.remove(filapath);
				count.setText(select_layout.getChildCount() + "");
			} else {
				try {
					checkBox.setChecked(true);
					ImageView imageView = iconImage(filapath, Position,
							checkBox);
					if (imageView != null) {
						if (select_layout.getChildCount() < 3) {
							hashImage.put(Position, imageView);
							filelist.add(filapath);
							select_layout.addView(imageView);
							count.setText(select_layout.getChildCount() + "");
						} else {
							checkBox.setChecked(false);
							DianTool.showTextToast(ImgsActivity.this, "最多只能选3张图片");
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	};
	public void sendfiles() {
			DianApplication.user.alist=filelist;
		finish();

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			startActivity(new Intent(ImgsActivity.this, ImgFileListActivity.class));
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
