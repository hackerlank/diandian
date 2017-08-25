package com.dingli.diandiaan.lostproperty;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by dingliyuangong on 2016/10/19.
 */
public class MyGrid extends GridView{

    public MyGrid(Context context) {
        super(context);
    }

    public MyGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
