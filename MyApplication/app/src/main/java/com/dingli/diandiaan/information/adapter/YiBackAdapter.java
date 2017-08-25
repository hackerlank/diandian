package com.dingli.diandiaan.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.QingJiaSty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class YiBackAdapter extends BaseAdapter {
    Context context;
    List<QingJiaSty> arrlist;
    public YiBackAdapter(Context context){
        this.context=context;
        arrlist=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return arrlist.size();
    }

    @Override
    public QingJiaSty getItem(int position) {
        return arrlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_yiback_view,null);
        }
        ListItemYiBackView listItemWeiDoneView=(ListItemYiBackView)convertView;
        listItemWeiDoneView.setYiDone(getItem(position));
        listItemWeiDoneView.setTag(getItem(position));
        return listItemWeiDoneView;
    }
    public void addWeiDone(List<QingJiaSty> list){
        arrlist.addAll(list);
    }
    public void refreshWeiDone(List<QingJiaSty> list){
        arrlist.clear();
        arrlist.addAll(list);
    }
}
