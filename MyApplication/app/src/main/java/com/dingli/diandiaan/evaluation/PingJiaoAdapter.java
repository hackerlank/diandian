package com.dingli.diandiaan.evaluation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/3/9.
 */
public class PingJiaoAdapter extends BaseAdapter {
    ArrayList<Course> arrayList;
    Context context;
    public PingJiaoAdapter(Context context){
        this.arrayList=new ArrayList<Course>();
        this.context=context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_evaluation_view,null);
        }
        ListItemEvaluationView listItemEvaluationView=(ListItemEvaluationView)convertView;
        listItemEvaluationView.setEvaluationList(getItem(position));
        listItemEvaluationView.setTag(getItem(position));
        return listItemEvaluationView;
    }
    public void addPinglist(List<Course> list){
        arrayList.addAll(list);
    }
    public void refreshPinglist(List<Course> list){
        arrayList.clear();
        arrayList.addAll(list);
    }
}
