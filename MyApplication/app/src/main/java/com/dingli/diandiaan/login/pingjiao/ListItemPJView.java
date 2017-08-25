package com.dingli.diandiaan.login.pingjiao;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.StudentClass;

/**
 * Created by dingliyuangong on 2016/3/8.
 */
public class ListItemPJView extends RelativeLayout {
    TextView tvjiapin,tvjianqi,tvshiri;
    ImageView liangone,liangtwo,liangthree,liangfour,liangfive,anone,antwo,anthree,anfour,anfive;
    public ListItemPJView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public ListItemPJView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemPJView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvjiapin=getView(R.id.tvjiapin);
        tvshiri=getView(R.id.tvshiri);
        liangone=getView(R.id.liangone);
        liangtwo=getView(R.id.liangtwo);
        liangthree=getView(R.id.liangthree);
        liangfour=getView(R.id.liangfour);
        liangfive=getView(R.id.liangfive);
        anone=getView(R.id.anone);
        antwo=getView(R.id.antwo);
        anthree=getView(R.id.anthree);
        anfour=getView(R.id.anfour);
        anfive=getView(R.id.anfive);
    }
    public void setEvaluationList(StudentClass studentClass,Course result){
        if(studentClass==null&&result!=null){

        }

    }
    public void setEvaluationList(Course result){
        if(TextUtils.isEmpty(result.assessTime)) {
            tvshiri.setText(result.createDate);
        }else{
            tvshiri.setText(result.assessTime);
        }
//        if(TextUtils.isEmpty(result.content)){
//            tvjiapin.setBackgroundColor(getResources().getColor(R.color.bg_Gray_light));
//        }else {
        tvjiapin.setText(result.content);
//            tvjiapin.setBackgroundColor(getResources().getColor(R.color.bg_White));
//        }

        if(result.score.equals("1")){
            liangone.setVisibility(VISIBLE);
            anone.setVisibility(GONE);
        }else if(result.score.equals("2")){
            liangone.setVisibility(VISIBLE);
            liangtwo.setVisibility(VISIBLE);
            anone.setVisibility(GONE);
            antwo.setVisibility(GONE);
        }else if(result.score.equals("3")){
            liangone.setVisibility(VISIBLE);
            liangtwo.setVisibility(VISIBLE);
            liangthree.setVisibility(VISIBLE);
            anone.setVisibility(GONE);
            antwo.setVisibility(GONE);
            anthree.setVisibility(GONE);
        }else if(result.score.equals("4")){
            liangone.setVisibility(VISIBLE);
            liangtwo.setVisibility(VISIBLE);
            liangthree.setVisibility(VISIBLE);
            liangfour.setVisibility(VISIBLE);
            anthree.setVisibility(GONE);
            anone.setVisibility(GONE);
            antwo.setVisibility(GONE);
            anfour.setVisibility(GONE);
        }else if(result.score.equals("5")){
            liangone.setVisibility(VISIBLE);
            liangtwo.setVisibility(VISIBLE);
            liangthree.setVisibility(VISIBLE);
            liangfour.setVisibility(VISIBLE);
            liangfive.setVisibility(VISIBLE);
            anone.setVisibility(GONE);
            antwo.setVisibility(GONE);
            anthree.setVisibility(GONE);
            anfour.setVisibility(GONE);
            anfive.setVisibility(GONE);
        }
    }
    public void setXingXingByClass(StudentClass.DataEntity result){
        if(TextUtils.isEmpty(result.getAssessTime())) {
            tvshiri.setText("默认时间为空");
        }else{
            tvshiri.setText(result.getAssessTime());
        }
//        if(TextUtils.isEmpty(result.content)){
//            tvjiapin.setBackgroundColor(getResources().getColor(R.color.bg_Gray_light));
//        }else {
        tvjiapin.setText(result.getContent());
//            tvjiapin.setBackgroundColor(getResources().getColor(R.color.bg_White));
//        }

        if(result.getScore()==1){
            liangone.setVisibility(VISIBLE);
            anone.setVisibility(GONE);
        }else if(result.getScore()==2){
            liangone.setVisibility(VISIBLE);
            liangtwo.setVisibility(VISIBLE);
            anone.setVisibility(GONE);
            antwo.setVisibility(GONE);
        }else if(result.getScore()==3){
            liangone.setVisibility(VISIBLE);
            liangtwo.setVisibility(VISIBLE);
            liangthree.setVisibility(VISIBLE);
            anone.setVisibility(GONE);
            antwo.setVisibility(GONE);
            anthree.setVisibility(GONE);
        }else if(result.getScore()==4){
            liangone.setVisibility(VISIBLE);
            liangtwo.setVisibility(VISIBLE);
            liangthree.setVisibility(VISIBLE);
            liangfour.setVisibility(VISIBLE);
            anthree.setVisibility(GONE);
            anone.setVisibility(GONE);
            antwo.setVisibility(GONE);
            anfour.setVisibility(GONE);
        }else if(result.getScore()==5){
            liangone.setVisibility(VISIBLE);
            liangtwo.setVisibility(VISIBLE);
            liangthree.setVisibility(VISIBLE);
            liangfour.setVisibility(VISIBLE);
            liangfive.setVisibility(VISIBLE);
            anone.setVisibility(GONE);
            antwo.setVisibility(GONE);
            anthree.setVisibility(GONE);
            anfour.setVisibility(GONE);
            anfive.setVisibility(GONE);
        }
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
