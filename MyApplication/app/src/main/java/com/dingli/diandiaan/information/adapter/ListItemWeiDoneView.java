package com.dingli.diandiaan.information.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.QingJiaSty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class ListItemWeiDoneView extends RelativeLayout{
    TextView tvti,tvxuexiao,tvnm,tvrq,tvzj,vtlix,vtshijia,vtjieshou,vtjujue,tvjshu,vtzgongs,xiangguantu;
    onTongyiListener listener;
    ImageView ivleavePic,ivleavePicone,ivleavePictwo;
    LinearLayout fulian;
    View llvieres;
    LinearLayout llw;
    public ListItemWeiDoneView(Context context) {
        super(context);
    }
    public ListItemWeiDoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListItemWeiDoneView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvti=getView(R.id.tvti);
        tvxuexiao=getView(R.id.tvxuexiao);
        tvnm=getView(R.id.tvnm);
        tvrq=getView(R.id.tvrq);
        tvzj=getView(R.id.tvzj);
        vtlix=getView(R.id.vtlix);
        vtshijia=getView(R.id.vtshijia);
        vtjieshou=getView(R.id.vtjieshou);
        vtjujue=getView(R.id.vtjujue);
        tvjshu=getView(R.id.tvjshu);
        vtzgongs=getView(R.id.vtzgongs);
        ivleavePic=getView(R.id.ivleavePic);
        ivleavePicone=getView(R.id.ivleavePicone);
        ivleavePictwo=getView(R.id.ivleavePictwo);
        xiangguantu=getView(R.id.xiangguantu);
        fulian=getView(R.id.fulian);
        llvieres=getView(R.id.llvieres);
        llw=getView(R.id.llw);
    }
    public void setWeiDone(QingJiaSty resultInfo){
        int dayForWeek = 0;
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
       tvti.setText(resultInfo.createdDate);
        tvxuexiao.setText(resultInfo.collegeName+" "+resultInfo.majorName+" "+resultInfo.className);
        tvnm.setText(resultInfo.studentName);
        if(TextUtils.isEmpty(resultInfo.endDate)){
            vtzgongs.setVisibility(GONE);
            tvzj.setVisibility(VISIBLE);
            tvrq.setText(resultInfo.startDate);
                tvjshu.setVisibility(VISIBLE);
                tvjshu.setText(resultInfo.name);
            Date datesd = null;
            try {
                datesd = matter1.parse(resultInfo.startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar.setTime(datesd);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                dayForWeek = 7;
            }else{
                dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            }
            switch (dayForWeek){
                case 1:
                    tvzj.setText("星期一");
                    break;
                case 2:
                    tvzj.setText("星期二");
                    break;
                case 3:
                    tvzj.setText("星期三");
                    break;
                case 4:
                    tvzj.setText("星期四");
                    break;
                case 5:
                    tvzj.setText("星期五");
                    break;
                case 6:
                    tvzj.setText("星期六");
                    break;
                case 7:
                    tvzj.setText("星期日");
                    break;
            }
        }else{
            tvzj.setVisibility(GONE);
            tvrq.setText(resultInfo.startDate+"--"+resultInfo.endDate);
            tvjshu.setVisibility(GONE);
            Date dates = null;
            Date datess=null;
            try {
                dates = matter1.parse(resultInfo.startDate);
                datess=matter1.parse(resultInfo.endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long lo=datess.getTime()-dates.getTime();
            if(lo>0) {
                long log = lo / 3600000 / 24;
                  long lgo=log+1;
                vtzgongs.setVisibility(VISIBLE);
                vtzgongs.setText("共" + lgo + "天");
            }
        }
        if(resultInfo.leaveSchool==true){
            vtlix.setText("是");
        }else{
            vtlix.setText("否");
        }
        vtshijia.setText(resultInfo.requestContent);
        if(TextUtils.isEmpty(resultInfo.leavePictureUrls)){
            llw.setVisibility(GONE);
            llvieres.setVisibility(GONE);
            ivleavePic.setVisibility(GONE);
            fulian.setVisibility(GONE);
        }else if (resultInfo.leavePictureUrls!=null){
            fulian.setVisibility(VISIBLE);
            if(!resultInfo.leavePictureUrls.contains(";")){
                ivleavePic.setVisibility(VISIBLE);
                ivleavePicone.setVisibility(GONE);
                ivleavePictwo.setVisibility(GONE);
                llvieres.setVisibility(VISIBLE);
                xiangguantu.setText("(1):");
                llw.setVisibility(VISIBLE);
                Glide.with(getContext()).load(resultInfo.leavePictureUrls).into(ivleavePic);
            }else {
                String[] http=resultInfo.leavePictureUrls.split(";");
                switch (http.length){
                    case 2:
                        ivleavePic.setVisibility(VISIBLE);
                        ivleavePicone.setVisibility(VISIBLE);
                        ivleavePictwo.setVisibility(GONE);
                        Glide.with(getContext()).load(http[0]).into(ivleavePic);
                        Glide.with(getContext()).load(http[1]).into(ivleavePicone);
                        xiangguantu.setText("(2):");
                        llw.setVisibility(VISIBLE);
                        llvieres.setVisibility(VISIBLE);
                        break;
                    case 3:
                        xiangguantu.setText("(3):");
                        llw.setVisibility(VISIBLE);
                        llvieres.setVisibility(VISIBLE);
                        ivleavePic.setVisibility(VISIBLE);
                        ivleavePicone.setVisibility(VISIBLE);
                        ivleavePictwo.setVisibility(VISIBLE);
                        Glide.with(getContext()).load(http[0]).into(ivleavePic);
                        Glide.with(getContext()).load(http[1]).into(ivleavePicone);
                        Glide.with(getContext()).load(http[2]).into(ivleavePictwo);
                        break;
                }
            }
        }else{
            llw.setVisibility(GONE);
            llvieres.setVisibility(GONE);
            ivleavePic.setVisibility(GONE);
            fulian.setVisibility(GONE);
        }
    }
    public void setonTongyiListener(onTongyiListener listener){
        this.listener=listener;
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
    public interface  onTongyiListener{
        void OnTongYiListener(String str,int id,String url);
    }
}
