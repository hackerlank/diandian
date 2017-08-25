package com.dingli.diandiaan.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.login.ListItemPinFenView;
import com.dingli.diandiaan.common.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/3/23.
 */
public class PingFenAdapter extends BaseAdapter {
    ArrayList<Course> arrayList;
    Context context;
    public PingFenAdapter(Context context){
        this.context=context;
        arrayList=new ArrayList<Course>();
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
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pingfen,null);
        }
        ListItemPinFenView listItemPinFenView=(ListItemPinFenView)convertView;
        listItemPinFenView.setPinF(getItem(position));
        listItemPinFenView.setTag(getItem(position));
        return listItemPinFenView;
    }
    public void addPlist(List<Course> list){
        arrayList.addAll(list);
    }
    public void refreshPlist(List<Course> list){
        arrayList.clear();
        arrayList.addAll(list);
    }
}
