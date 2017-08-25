package com.dingli.diandiaan.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.login.ListItemOutView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/3/23.
 */
public class OutQingAdapter extends BaseAdapter {
    Context context;
    List<Course> arraylist;
    public OutQingAdapter(Context context){
        this.context=context;
        arraylist=new ArrayList<Course>();
    }
    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Course getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
                  if(convertView==null){
                     convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_out,null);
                  }
        ListItemOutView listItemOutView=(ListItemOutView)convertView;
        listItemOutView.setOutList(getItem(position));
        listItemOutView.setTag(getItem(position));
        return listItemOutView;
    }
    public void addOutlist(List<Course> list){
        arraylist.addAll(list);
    }
    public void refreshOutlist(List<Course> list){
        arraylist.clear();
        arraylist.addAll(list);
    }
}
