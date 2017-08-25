package com.dingli.diandiaan.Syllabus;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Coursecenter;

/**
 * Created by dingliyuangong on 2016/3/8.
 */
public class ListItemCourseView extends RelativeLayout {
    TextView classroomtvac,studentnumc;
    public ListItemCourseView(Context context) {
        super(context);
    }
    public ListItemCourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemCourseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        classroomtvac=getView(R.id.classroomtvac);
        studentnumc=getView(R.id.studentnumc);
    }
    public void setListCourse(Coursecenter result){
       classroomtvac.setText(result.className);
        studentnumc.setText(result.studentCount+"");
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
