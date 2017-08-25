package com.dingli.diandiaan.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Coursecenter;
import com.dingli.diandiaan.Syllabus.ListItemCourseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/4/14.
 */
public class CourseAdapter extends BaseAdapter{
    ArrayList<Coursecenter> arrayList;
    Context context;
    public CourseAdapter(Context context){
        this.context=context;
        arrayList=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Coursecenter getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_course_view,null);
        }
        ListItemCourseView listItemCourseView=(ListItemCourseView)convertView;
        listItemCourseView.setListCourse(getItem(position));
        listItemCourseView.setTag(getItem(position));
        return listItemCourseView;
    }
    public void addOutlist(List<Coursecenter> list){
        arrayList.addAll(list);
    }
    public void refreshOutlist(List<Coursecenter> list){
        arrayList.clear();
        arrayList.addAll(list);
    }
}
