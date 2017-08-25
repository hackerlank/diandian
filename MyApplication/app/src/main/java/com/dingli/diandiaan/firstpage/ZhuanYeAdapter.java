package com.dingli.diandiaan.firstpage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.Syllabus.CourseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.DianApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/3/9.
 */
public class ZhuanYeAdapter extends BaseAdapter {
    ArrayList<Course> arrayList;
    Context context;
    private ListItemFirstPageView.onCancelCollectListener mOnCancelInterface;
    public ZhuanYeAdapter(Context context,ListItemFirstPageView.onCancelCollectListener listener){
        this.arrayList=new ArrayList<Course>();
        this.context=context;
        mOnCancelInterface=listener;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Course getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_firstpage_view,null);
        }
       final ListItemFirstPageView listItemFirstPageView=(ListItemFirstPageView)convertView;
        listItemFirstPageView.setListFirst(getItem(position));
        listItemFirstPageView.setTag(getItem(position));
       final  int  id =arrayList.get(position).id;
        final int courseId=arrayList.get(position).coureseId;
        final boolean blean=arrayList.get(position).rollCall;
        final String courseName=arrayList.get(position).courseName;
        final String teach_time=arrayList.get(position).teach_time;
        final String classEndTime=arrayList.get(position).classEndTime;
        final int lateTime=arrayList.get(position).lateTime;
        final int reUser=arrayList.get(position).reUser;
        listItemFirstPageView.setmOnCancelInterface(mOnCancelInterface);
       listItemFirstPageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(parent.getContext(), CourseActivity.class);
               intent.putExtra(Constant.KE_ID, id);
               DianApplication.user.KE_ID = String.valueOf(id);
               parent.getContext().startActivity(intent);
           }
       });
        listItemFirstPageView.btxiaos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItemFirstPageView.btxiaos.getText().toString().equals("查看评教")){
                    Intent intent = new Intent(parent.getContext(), CourseActivity.class);
                    intent.putExtra(Constant.KE_ID, id);
                    DianApplication.user.KE_ID = String.valueOf(id);
                    parent.getContext().startActivity(intent);
                }else {
                    mOnCancelInterface.onCancelCollect(v, courseId, id, courseName, teach_time, classEndTime, blean, lateTime, reUser);
                }
                }
        });
        listItemFirstPageView.btxiangqis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), CourseActivity.class);
                intent.putExtra(Constant.KE_ID, id);
                DianApplication.user.KE_ID = String.valueOf(id);
                parent.getContext().startActivity(intent);
            }
        });
        return listItemFirstPageView;
    }
    public void addFirstlist(List<Course> list){
        arrayList.addAll(list);
    }
    public void refreshFirstlist(List<Course> list){
        arrayList.clear();
        arrayList.addAll(list);
    }

}
