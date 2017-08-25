package com.dingli.diandiaan.evaluation.choceclassAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Coursecenter;

/**
 * Created by dingliyuangong on 2016/3/8.
 */
public class ListItemClassView extends RelativeLayout {
    TextView classnamed;
    ImageView selected;
    public ListItemClassView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public ListItemClassView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemClassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        selected=getView(R.id.selected);
        classnamed=getView(R.id.classnamed);
    }
    public void setEvaluationList(Coursecenter result){
        classnamed.setText(result.className);
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
