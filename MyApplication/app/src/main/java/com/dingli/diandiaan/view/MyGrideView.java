package com.dingli.diandiaan.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

public class MyGrideView extends GridView {

	public MyGrideView(Context context) {
		super(context);
	}

	public MyGrideView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGrideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	

}
