package com.dingli.diandiaan.information.adapter;

import android.content.Context;
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
public class ListItemYiDoneView extends RelativeLayout{
    TextView tvtime,tvscho,tvnam,tvdate,tvzhouji,tvjijie,qingjiayou,foulixiao,tvgs,tvjujueyuanying,yidonexiangguan;
    LinearLayout lljujue,llyidones;
    RelativeLayout rlziyi;
    ImageView ivstatus;
    ImageView ivleavePic,ivleavePicone,ivleavePictwo;
    View llyidond;
    LinearLayout llod;
    public ListItemYiDoneView(Context context) {
        super(context);
    }
    public ListItemYiDoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListItemYiDoneView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvnam=getView(R.id.tvnamm);
        tvtime=getView(R.id.tvtime);
        tvscho=getView(R.id.tvscho);
        tvdate=getView(R.id.tvdatess);
        tvzhouji=getView(R.id.tvzhoujiss);
        tvjijie=getView(R.id.tvjijiess);
        qingjiayou=getView(R.id.qingjiayouss);
        foulixiao=getView(R.id.foulixiaoss);
        tvgs=getView(R.id.tvgs);
        ivstatus=getView(R.id.ivstatus);
        lljujue=getView(R.id.lljuyi);
        tvjujueyuanying=getView(R.id.tvjujueyuanying);
        rlziyi=getView(R.id.rlziyi);
        ivleavePic=getView(R.id.ivleavePicyid);
        ivleavePicone=getView(R.id.ivleavePiconeyid);
        ivleavePictwo=getView(R.id.ivleavePictwoyid);
        yidonexiangguan=getView(R.id.yidonexiangguan);
        llyidones=getView(R.id.llyidones);
        llyidond=getView(R.id.llyidond);
        llod=getView(R.id.llod);
    }
    public void setYIDone(QingJiaSty resultInfo){
        tvtime.setText(resultInfo.lastModifyDate);
        int dayForWeek = 0;
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        tvnam.setText(resultInfo.studentName);
        tvscho.setText(resultInfo.collegeName+" "+resultInfo.majorName+" "+resultInfo.className);
        if(resultInfo.leaveSchool==true){
            foulixiao.setText("是");
        }else{
            foulixiao.setText("否");
        }
        if(resultInfo.status.equals("pass")){
            ivstatus.setBackgroundResource(R.mipmap.shenpi_tongguo);
            lljujue.setVisibility(GONE);
            rlziyi.setBackgroundColor(getResources().getColor(R.color.lixiaos));
        }else{
            ivstatus.setBackgroundResource(R.mipmap.shenpi_wei);
            lljujue.setVisibility(VISIBLE);
            tvjujueyuanying.setText(resultInfo.rejectContent);
            rlziyi.setBackgroundColor(getResources().getColor(R.color.weid));
        }
        qingjiayou.setText(resultInfo.requestContent);
        if(TextUtils.isEmpty(resultInfo.endDate)){
            tvgs.setVisibility(GONE);
            tvzhouji.setVisibility(VISIBLE);
            tvdate.setText(resultInfo.startDate);
                tvjijie.setVisibility(VISIBLE);
            tvjijie.setText(resultInfo.name);
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
                    tvzhouji.setText("星期一");
                    break;
                case 2:
                    tvzhouji.setText("星期二");
                    break;
                case 3:
                    tvzhouji.setText("星期三");
                    break;
                case 4:
                    tvzhouji.setText("星期四");
                    break;
                case 5:
                    tvzhouji.setText("星期五");
                    break;
                case 6:
                    tvzhouji.setText("星期六");
                    break;
                case 7:
                    tvzhouji.setText("星期日");
                    break;
            }
        }else{
            tvzhouji.setVisibility(GONE);
            tvdate.setText(resultInfo.startDate+"--"+resultInfo.endDate);
            tvjijie.setVisibility(GONE);
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
                tvgs.setVisibility(VISIBLE);
                tvgs.setText("共" + lgo + "天");
            }
        }
        if (TextUtils.isEmpty(resultInfo.leavePictureUrls)){
            llod.setVisibility(GONE);
            llyidond.setVisibility(GONE);
            llyidones.setVisibility(GONE);
        }else if (resultInfo.leavePictureUrls!=null){
            llyidones.setVisibility(VISIBLE);
            if(!resultInfo.leavePictureUrls.contains(";")){
                ivleavePic.setVisibility(VISIBLE);
                ivleavePicone.setVisibility(GONE);
                ivleavePictwo.setVisibility(GONE);
                llod.setVisibility(VISIBLE);
                llyidond.setVisibility(VISIBLE);
                yidonexiangguan.setText("(1):");
                Glide.with(getContext()).load(resultInfo.leavePictureUrls).into(ivleavePic);
            }else {
                String[] http=resultInfo.leavePictureUrls.split(";");
                switch (http.length){
                    case 2:
                        ivleavePic.setVisibility(VISIBLE);
                        ivleavePicone.setVisibility(VISIBLE);
                        ivleavePictwo.setVisibility(GONE);
                        llyidond.setVisibility(VISIBLE);
                        llod.setVisibility(VISIBLE);
                        yidonexiangguan.setText("(2):");
                        Glide.with(getContext()).load(http[0]).into(ivleavePic);
                        Glide.with(getContext()).load(http[1]).into(ivleavePicone);
                        break;
                    case 3:
                        ivleavePic.setVisibility(VISIBLE);
                        ivleavePicone.setVisibility(VISIBLE);
                        ivleavePictwo.setVisibility(VISIBLE);
                        llyidond.setVisibility(VISIBLE);
                        llod.setVisibility(VISIBLE);
                        yidonexiangguan.setText("(3):");
                        Glide.with(getContext()).load(http[0]).into(ivleavePic);
                        Glide.with(getContext()).load(http[1]).into(ivleavePicone);
                        Glide.with(getContext()).load(http[2]).into(ivleavePictwo);
                        break;
                }
            }
        }else{
            ivleavePic.setVisibility(GONE);
            llod.setVisibility(GONE);
            llyidond.setVisibility(GONE);
            llyidones.setVisibility(GONE);
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
