package com.dingli.diandiaan.login;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Course;

/**
 * Created by dingliyuangong on 2016/3/8.
 */
public class ListItemOutView extends RelativeLayout {
    TextView tvoutlu,tvoutmin;
    public ListItemOutView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public ListItemOutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemOutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvoutlu=getView(R.id.tvoutlu);
        tvoutmin=getView(R.id.tvoutmin);
    }
    public void setOutList(Course course){
        tvoutmin.setText(course.courseName);
        long   l1   =   Math.round(course.rollCallPercent * 100);   //四舍五入
//     String dou=df.format(studentChuQin.percentage);
        double   dou   =   l1/100.0;
//        DecimalFormat    df   = new DecimalFormat("######0.000");

//        String dou=df.format(course.rollCallPercent);
        tvoutlu.setText(l1+"%");
    }
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e(ex.toString(), "Could not cast View to concrete class");
            throw ex;
        }
    }
}
