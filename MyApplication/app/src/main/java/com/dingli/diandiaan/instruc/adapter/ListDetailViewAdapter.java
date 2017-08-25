package com.dingli.diandiaan.instruc.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.ResultInfoCallOne;
import com.dingli.diandiaan.rollcall.ViewHolder;
import com.dingli.diandiaan.rollcall.ViewHolderOne;

import java.util.ArrayList;

/**
 * Created by dingliyuangong on 2016/12/1.
 */
public class ListDetailViewAdapter extends BaseAdapter{
    Context context;
    ArrayList<ResultInfoCallOne> arrayList;
    ViewHolderOne viewHolderOne;
    ViewHolder.onCancelCollectListener listener;
    public ListDetailViewAdapter(Context context, ArrayList<ResultInfoCallOne> arrayList ,ViewHolder.onCancelCollectListener listener){
        this.context=context;
        this.arrayList=arrayList;
        this.listener=listener;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public ResultInfoCallOne getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            viewHolderOne=new ViewHolderOne();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_instrdetail_view,null);
            viewHolderOne.nametv=(TextView)convertView.findViewById(R.id.nametv);
            viewHolderOne.tvtype=(TextView)convertView.findViewById(R.id.tvtype);
            viewHolderOne.tvbeizhu=(TextView)convertView.findViewById(R.id.tvbeizhu);
            viewHolderOne.llbgs=(RelativeLayout)convertView.findViewById(R.id.llbgs);
            viewHolderOne.ivphone=(ImageView) convertView.findViewById(R.id.ivphone);
            TextPaint tp = viewHolderOne.tvtype.getPaint();
            tp.setFakeBoldText(true);
            convertView.setTag(viewHolderOne);
        }else{
            viewHolderOne=(ViewHolderOne)convertView.getTag();
        }
        viewHolderOne.nametv.setText(arrayList.get(position).studentName);
        if(arrayList.get(position).haveReport){
            viewHolderOne.tvtype.setText(arrayList.get(position).signTime);
            viewHolderOne.tvtype.setTextColor(parent.getContext().getResources().getColor(R.color.julis));
            viewHolderOne.tvbeizhu.setText(arrayList.get(position).gpsDetail);
            viewHolderOne.tvbeizhu.setVisibility(View.VISIBLE);
            viewHolderOne.ivphone.setVisibility(View.GONE);
        }else{
            viewHolderOne.tvtype.setText("未提交");
            viewHolderOne.tvtype.setTextColor(parent.getContext().getResources().getColor(R.color.wenzico));
            viewHolderOne.tvbeizhu.setVisibility(View.GONE);
            viewHolderOne.ivphone.setVisibility(View.VISIBLE);
        }
        if (arrayList.get(position).leaveStatus){
            viewHolderOne.tvtype.setText("请假");
            viewHolderOne.tvtype.setTextColor(parent.getContext().getResources().getColor(R.color.fucolor));
        }
        final RelativeLayout llb=viewHolderOne.llbgs;
        final ImageView imageView=viewHolderOne.ivphone;
        final int classId=arrayList.get(position).classId;
        final String studentName=arrayList.get(position).studentName;
        final int courseId=arrayList.get(position).courseId;
        final long id=arrayList.get(position).id;
        final int scheduleId=arrayList.get(position).scheduleId;
        final  int studentScheduleId=arrayList.get(position).studentScheduleId;
        final int studentId=arrayList.get(position).studentId;
        final  String gpsLocation=arrayList.get(position).gpsLocation+"/"+arrayList.get(position).gpsDetail;
        final String signtime=arrayList.get(position).signTime;
        final int userId=arrayList.get(position).userId;
        viewHolderOne.setmOnCancelInterface(listener);
        viewHolderOne.llbgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (listener != null) {
                        if (!TextUtils.isEmpty(arrayList.get(position).gpsDetail)) {
                            if (!arrayList.get(position).gpsDetail.equals("null")) {
                                listener.onCancelCollect(imageView, llb, v, classId, studentName, courseId, id, scheduleId, studentScheduleId,
                                        studentId, gpsLocation, userId, signtime, imageView);
                            }else{
                                listener.refresh(studentId,id);
                            }
                        }else{
                                listener.refresh(studentId,id);
                        }
                }
            }
        });
        viewHolderOne.ivphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.refresh(studentId,id);
            }
        });
        return convertView;
    }
}
