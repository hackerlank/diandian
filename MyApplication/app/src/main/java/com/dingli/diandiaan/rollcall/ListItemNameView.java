package com.dingli.diandiaan.rollcall;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.login.CircleImageView;

/**
 * Created by dingliyuangong on 2016/3/8.
 */
public class ListItemNameView extends LinearLayout {
    RelativeLayout rlbgone;
    TextView tvnameone;
    CircleImageView igvone;
    public ListItemNameView(Context context) {
        super(context);
    }
    public ListItemNameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ListItemNameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rlbgone=getView(R.id.rlbgone);
        tvnameone=getView(R.id.tvnameone);
        igvone=getView(R.id.igvone);
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
