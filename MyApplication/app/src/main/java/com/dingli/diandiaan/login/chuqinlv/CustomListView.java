package com.dingli.diandiaan.login.chuqinlv;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by admin on 2016/7/21.
 */
public class CustomListView extends ListView {
    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int length=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, length);
    }
}
