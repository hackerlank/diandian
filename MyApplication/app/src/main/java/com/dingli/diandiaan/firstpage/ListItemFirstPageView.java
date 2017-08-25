package com.dingli.diandiaan.firstpage;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Course;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dingliyuangong on 2016/3/8.
 */
public class ListItemFirstPageView extends RelativeLayout {
    TextView dididian,dishijia,dijie,kechengming,latertime,tvleaved;
    private onCancelCollectListener mOnCancelInterface;
    SimpleDateFormat formatter;
    int id;
    TextView btxiaos,btxiangqis;
    long secod;
    View viw;
    LinearLayout llchu,lilater;
    public ListItemFirstPageView(Context context) {
        super(context);
    }
    public ListItemFirstPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemFirstPageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dididian=getView(R.id.dididian);
        dishijia=getView(R.id.dishijia);
        dijie=getView(R.id.dijie);
        kechengming = getView(R.id.kechengming);
        btxiaos = getView(R.id.btxiaos);
        viw=findViewById(R.id.viwe);
        tvleaved=getView(R.id.tvleaved);
        llchu=getView(R.id.llchu);
        btxiangqis=getView(R.id.btxiangqis);
        lilater=getView(R.id.lilater);
        latertime=getView(R.id.latertv);
    }
    public void setListFirst(Course result){
        formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm");
        Date curDate    =   new    Date();
        secod=curDate.getTime();
        dididian.setText(result.classRoom);
        dishijia.setText(result.classBeginTime+"-"+result.classEndTime);
        dijie.setText(result.whichLesson);
        kechengming.setText(result.courseName);
        btxiaos.setVisibility(VISIBLE);
        btxiaos.setBackgroundResource(R.drawable.circlefirst_corner);
        btxiangqis.setVisibility(GONE);
        btxiaos.setText("查看考勤");
        btxiangqis.setBackgroundResource(R.drawable.ke_click);
        btxiangqis.setText("查看评教");
        viw.setVisibility(VISIBLE);
        if (TextUtils.isEmpty(result.attendance)){
            llchu.setVisibility(GONE);
        }else if (result.attendance.equals("0")){
            llchu.setVisibility(GONE);
        }else if(result.attendance.equals("0.0")){
            llchu.setVisibility(GONE);
        }else{
            llchu.setVisibility(VISIBLE);
            tvleaved.setText(result.attendance);
        }
        if (result.classrommRollCall!=true) {
            if (result.lateTime!=0) {
                lilater.setVisibility(VISIBLE);
                latertime.setText("上课" + String.valueOf(result.lateTime) + "分钟之后");
            }else{
                lilater.setVisibility(GONE);
            }
        }else{
            if (result.rollCall==true) {
                if (result.lateTime != 0) {
                    lilater.setVisibility(VISIBLE);
                    latertime.setText("上课" + String.valueOf(result.lateTime) + "分钟之后");
                } else {
                    lilater.setVisibility(GONE);
                }
            }else{
                lilater.setVisibility(GONE);
            }
        }

        dateCompares(result.teach_time + " " + result.classBeginTime, result.teach_time + " " + result.classEndTime,result.isOpen,result.rollCall);

    }
    void dateCompares(String one,String two,String isOpen,boolean rollcall){
        try {
            Date d1 = formatter.parse(one);
            Date d2=formatter.parse(two);
            if(d1.getTime() - secod-4*60*1000>0) {
                viw.setVisibility(GONE);
                btxiaos.setVisibility(GONE);
            }else if (d2.getTime()-secod<0){
                if (rollcall==true){
                    btxiangqis.setVisibility(VISIBLE);
                    viw.setVisibility(VISIBLE);
                    btxiaos.setVisibility(VISIBLE);
                }else{
                    btxiangqis.setVisibility(GONE);
                    btxiaos.setVisibility(VISIBLE);
                    btxiaos.setBackgroundResource(R.drawable.ke_click);
                    btxiaos.setText("查看评教");
                    viw.setVisibility(VISIBLE);
                }
            }else{
//                if (rollcall==true){
//                    btxiangqis.setVisibility(VISIBLE);
//                }else{
                    btxiangqis.setVisibility(GONE);
//                }
                viw.setVisibility(VISIBLE);
                btxiaos.setVisibility(VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setId(int id) {
        this.id = id;
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
    public void setmOnCancelInterface(onCancelCollectListener mInter) {
        mOnCancelInterface = mInter;
    }
    public interface onCancelCollectListener {
                void onCancelCollect(View v,int coureid,int id, String s, String o, String e, boolean blean,int lateTime,int reUser);
//             void  InCourse(int id);
    }
}
