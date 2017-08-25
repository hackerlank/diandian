package com.dingli.diandiaan.evaluation.choceclassAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Coursecenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/6/13.
 */
public class ChoceClassAdapter extends BaseAdapter{
    List<Coursecenter> arrayList;
    Context context;
    public ChoceClassAdapter(Context context){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_class_view,null);
        }
        ListItemClassView listItemClassView=(ListItemClassView)convertView;
        listItemClassView.setEvaluationList(getItem(position));
        listItemClassView.setTag(getItem(position));
        return listItemClassView;
    }
    public void addChoceClass(List<Coursecenter> list){
        arrayList.addAll(list);
    }
    public void refreshChoceClass(List<Coursecenter> list){
        arrayList.clear();
        arrayList.addAll(list);
    }
}
