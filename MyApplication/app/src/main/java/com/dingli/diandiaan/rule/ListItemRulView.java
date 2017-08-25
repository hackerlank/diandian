package com.dingli.diandiaan.rule;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.CommInterface;
import com.dingli.diandiaan.common.Course;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * Created by dingliyuangong on 2016/11/28.
 */
public class ListItemRulView extends RelativeLayout{
    TextView tvrulename;
    SwitchButton tbrule,tbrules;
    CommInterface commInterface;
    public ListItemRulView(Context context) {
        super(context);
    }
    public ListItemRulView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListItemRulView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvrulename=getView(R.id.tvrulename);
        tbrule=getView(R.id.tbrule);
        tbrules=getView(R.id.tbrules);
    }
    public void setItemRule(Course course){
        tvrulename.setText(course.courseName);
        if(course.isOpen.equals("disable")){
            tbrule.setVisibility(GONE);
            tbrules.setVisibility(VISIBLE);
        }else{
            tbrule.setVisibility(VISIBLE);
            tbrules.setVisibility(GONE);
        }
    }
    public void setInterface(CommInterface commInterface){
        this.commInterface=commInterface;
    }
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id){
        try {
            return (E)findViewById(id);
        }catch (Exception e){
            Log.e(e.toString(),"Could not cast View");
            throw e;
        }
    }
}
