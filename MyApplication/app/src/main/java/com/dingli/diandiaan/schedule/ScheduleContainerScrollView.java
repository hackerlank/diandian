package com.dingli.diandiaan.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

/**
 * Created by dong on 2016/3/30..
 */
public class ScheduleContainerScrollView extends HorizontalScrollView {

//    private OnHorizontalScrollListener listener;

    public ScheduleContainerScrollView(Context context) {
        this(context, null);
    }

    public ScheduleContainerScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleContainerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        if (listener != null)
//            listener.onScroll(l, t);
        super.onScrollChanged(l, t, oldl, oldt);
    }
//    public interface OnHorizontalScrollListener {
//        void onScroll(int x, int y);
//    }
//
//    public void setOnHorizontalScrollListener(OnHorizontalScrollListener listener) {
//        this.listener = listener;
//    }

}
