package com.dingli.diandiaan.instruc.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.CourseMing;

/**
 * Created by dingliyuangong on 2017/4/13.
 */

public class Listinstrucview extends LinearLayout{
    TextView btheaderitem,tvheadzongitem,tvliushuitem,tvheadtimeitem;

    public Listinstrucview(Context context) {
        super(context);
    }

    public Listinstrucview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Listinstrucview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        btheaderitem=getView(R.id.btheaderitem);
        tvheadzongitem=getView(R.id.tvheadzongitem);
        tvliushuitem=getView(R.id.tvliushuitem);
        tvheadtimeitem=getView(R.id.tvheadtimeitem);
    }
    public void setInstruc(CourseMing ming){
        tvheadtimeitem.setText(ming.openTime);
        tvliushuitem.setText(ming.commitCount+"/");
        tvheadzongitem.setText(String.valueOf(ming.totalCount));
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
