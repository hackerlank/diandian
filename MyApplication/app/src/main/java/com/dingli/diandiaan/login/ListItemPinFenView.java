package com.dingli.diandiaan.login;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Course;

import java.text.DecimalFormat;

/**
 * Created by dingliyuangong on 2016/3/8.
 */
public class ListItemPinFenView extends RelativeLayout {
    TextView tvfenp,tvfenm;
    public ListItemPinFenView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public ListItemPinFenView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemPinFenView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvfenp=getView(R.id.tvfenp);
        tvfenm=getView(R.id.tvfenmb);
    }
    public void setPinF(Course course){
        tvfenp.setText(course.courseName);
        DecimalFormat df   = new DecimalFormat("######0.00");
//        long   l1   =   Math.round(course.average);   //四舍五入
     String dou=df.format(course.average);
//        double   dou   =   l1/100.0;
//        String dou=df.format(course.average);
        tvfenm.setText(dou+getResources().getString(R.string.fen));
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
