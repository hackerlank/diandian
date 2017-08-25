package com.dingli.diandiaan.login.pingjiao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.StudentClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/8/6.
 */
public class StudentPjAdapter extends BaseAdapter{
    ArrayList<StudentClass.DataEntity> arrayList;
    Context context;
    public StudentPjAdapter(Context context){
        this.arrayList=new ArrayList<StudentClass.DataEntity>();
        this.context=context;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public StudentClass.DataEntity getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pj_view,null);
        }
        ListItemPJView listItemEvaluationView=(ListItemPJView)convertView;
        listItemEvaluationView.setXingXingByClass(getItem(position));
        listItemEvaluationView.setTag(getItem(position));
        return listItemEvaluationView;
    }
    public void addPinglist(List<StudentClass.DataEntity> list){
        arrayList.addAll(list);
    }
    public void refreshPinglist(List<StudentClass.DataEntity> list){
        arrayList.clear();
        arrayList.addAll(list);
    }
}
