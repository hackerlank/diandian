package com.dingli.diandiaan.rollcall;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.ResultInfoCallOne;

import java.util.ArrayList;

/**
 * Created by dingliyuangong on 2016/12/1.
 */
public class ListViewAdapter extends BaseAdapter{
    Context context;
    ArrayList<ResultInfoCallOne> arrayList;
    ViewHolderOne viewHolderOne;
    PopupWindow popupWindow;
    ViewHolder.onCancelCollectListener listener;
    String quanbu;
    public  ListViewAdapter(Context context,ArrayList<ResultInfoCallOne> arrayLists,PopupWindow popupWindow,ViewHolder.onCancelCollectListener listener,String quanbu){
        this.context=context;
        this.arrayList=arrayLists;
        this.popupWindow=popupWindow;
        this.listener=listener;
        this.quanbu=quanbu;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            viewHolderOne=new ViewHolderOne();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_listname_view,null);
            viewHolderOne.nametv=(TextView)convertView.findViewById(R.id.nametv);
            viewHolderOne.tvtype=(TextView)convertView.findViewById(R.id.tvtype);
            viewHolderOne.tvbeizhu=(TextView)convertView.findViewById(R.id.tvbeizhu);
            viewHolderOne.checkBox=(ImageView)convertView.findViewById(R.id.checkboxs);
            viewHolderOne.llbgs=(RelativeLayout)convertView.findViewById(R.id.llbgs);
            TextPaint tp = viewHolderOne.tvtype.getPaint();
            tp.setFakeBoldText(true);
            convertView.setTag(viewHolderOne);
        }else{
            viewHolderOne=(ViewHolderOne)convertView.getTag();
        }
        if (quanbu.equals("全部")){
                viewHolderOne.checkBox.setBackgroundResource(R.mipmap.icon_weiyuan);
                viewHolderOne.llbgs.setBackgroundResource(R.color.bg_White);
                if (DianApplication.user.listOut.size()!=0){
                    for (int i=0;i<DianApplication.user.listOut.size();i++){
                    if (arrayList.get(position).id==DianApplication.user.listOut.get(i)){
                        viewHolderOne.checkBox.setBackgroundResource(R.mipmap.icon_zongyuan);
                        viewHolderOne.llbgs.setBackgroundResource(R.color.xuanzhong);
                    }
                }
                 if (DianApplication.user.listIn.size()!=0){
                     for (int i=0;i<DianApplication.user.listIn.size();i++){
                    if (arrayList.get(position).id==DianApplication.user.listIn.get(i)){
                        viewHolderOne.checkBox.setBackgroundResource(R.mipmap.icon_weiyuan);
                        viewHolderOne.llbgs.setBackgroundResource(R.color.bg_White);
                    }
                }
                 }
                }
        }else if (quanbu.equals("选中")){
            viewHolderOne.checkBox.setBackgroundResource(R.mipmap.icon_zongyuan);
            viewHolderOne.llbgs.setBackgroundResource(R.color.xuanzhong);
            if (DianApplication.user.listIn.size()!=0){
                for (int i=0;i<DianApplication.user.listIn.size();i++){
                    if (arrayList.get(position).id==DianApplication.user.listIn.get(i)){
                        viewHolderOne.checkBox.setBackgroundResource(R.mipmap.icon_weiyuan);
                        viewHolderOne.llbgs.setBackgroundResource(R.color.bg_White);
                    }
                }
                if (DianApplication.user.listOut.size()!=0){
                    for (int i=0;i<DianApplication.user.listOut.size();i++){
                        if (arrayList.get(position).id==DianApplication.user.listOut.get(i)){
                            viewHolderOne.checkBox.setBackgroundResource(R.mipmap.icon_zongyuan);
                            viewHolderOne.llbgs.setBackgroundResource(R.color.xuanzhong);
                        }
                    }
                }
            }
        }
        viewHolderOne.nametv.setText(arrayList.get(position).userName);
        if (!TextUtils.isEmpty(arrayList.get(position).signTime)) {
            String[] str = arrayList.get(position).signTime.split(" ");
            viewHolderOne.tvbeizhu.setText(str[1].substring(0,5) +" "+ arrayList.get(position).distance);
        }else{
            viewHolderOne.tvbeizhu.setText("");
        }
        switch (arrayList.get(position).type){
            case "1":
                viewHolderOne.tvtype.setText("已到");
                viewHolderOne.tvtype.setTextColor(context.getResources().getColor(R.color.qianblue));
                viewHolderOne.tvtype.setBackgroundResource(R.color.yidaob);
                break;
            case "2":
                viewHolderOne.tvtype.setText("旷课");
                viewHolderOne.tvtype.setTextColor(context.getResources().getColor(R.color.kuketv));
                viewHolderOne.tvtype.setBackgroundResource(R.color.kukeb);
                break;
            case "3":
                viewHolderOne.tvtype.setText("迟到");
                viewHolderOne.tvtype.setTextColor(context.getResources().getColor(R.color.chidaotv));
                viewHolderOne.tvtype.setBackgroundResource(R.color.chidaob);
                break;
            case "4":
                viewHolderOne.tvtype.setText("请假");
                viewHolderOne.tvtype.setTextColor(context.getResources().getColor(R.color.qingjiatv));
                viewHolderOne.tvtype.setBackgroundResource(R.color.qingjiab);
                break;
            case "5":
                viewHolderOne.tvtype.setText("早退");
                viewHolderOne.tvtype.setTextColor(context.getResources().getColor(R.color.zaotuitv));
                viewHolderOne.tvtype.setBackgroundResource(R.color.zaotuib);
                break;
            case "6":
                viewHolderOne.tvtype.setText("已提交");
                viewHolderOne.tvtype.setTextColor(context.getResources().getColor(R.color.yitijiao));
                viewHolderOne.tvtype.setBackgroundResource(R.color.bg_White);
                break;
            case "7":
                viewHolderOne.tvtype.setText("未提交");
                viewHolderOne.tvtype.setTextColor(context.getResources().getColor(R.color.weitijiao));
                viewHolderOne.tvtype.setBackgroundResource(R.color.bg_White);
                break;
            case "8":
                viewHolderOne.tvtype.setText("异常");
                viewHolderOne.tvtype.setTextColor(context.getResources().getColor(R.color.helpone));
                viewHolderOne.tvtype.setBackgroundResource(R.color.bg_White);
                break;
            case "9":
                viewHolderOne.tvtype.setText("已取消考勤");
                viewHolderOne.tvtype.setTextColor(context.getResources().getColor(R.color.weitijiao));
                viewHolderOne.tvtype.setBackgroundResource(R.color.bg_White);
                break;
        }
        final RelativeLayout llb=viewHolderOne.llbgs;
        final ImageView checbox=viewHolderOne.checkBox;
        final int classId=arrayList.get(position).classId;
        final String className=arrayList.get(position).className;
        final int courseId=arrayList.get(position).courseId;
        final long id=arrayList.get(position).id;
        final int scheduleId=arrayList.get(position).scheduleId;
        final  int studentScheduleId=arrayList.get(position).studentScheduleId;
        final int teacherId=arrayList.get(position).teacherId;
        final  String type=arrayList.get(position).type;
        final int userId=arrayList.get(position).userId;
        final String userName=arrayList.get(position).userName;
        viewHolderOne.setmOnCancelInterface(listener);
        viewHolderOne.llbgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancelCollect(checbox,llb,v,classId,className,courseId,id,scheduleId,studentScheduleId,
                            teacherId,type,userId,userName,checbox);
                }
            }
        });
        return convertView;
    }
}
