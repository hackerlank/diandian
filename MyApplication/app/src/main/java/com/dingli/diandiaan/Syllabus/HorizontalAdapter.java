package com.dingli.diandiaan.Syllabus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/3/10.
 */
public class HorizontalAdapter extends BaseAdapter {
    Context context;
    ArrayList<Course> arrayList;
    private String currentWeek = "";
    private String selectedWeek="";
    public HorizontalAdapter(Context context){
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

    int weizhi;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
          ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_view, null);
            viewHolder.tv=(TextView)convertView.findViewById(R.id.tvhor);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if (currentWeek.equals(arrayList.get(position).name)) {
            viewHolder.tv.setText(arrayList.get(position).name);
            viewHolder.tv.setBackgroundResource(R.drawable.syform_corner);
            viewHolder.tv.setTextColor(context.getResources().getColor(R.color.sytv));
            viewHolder.tv.setTextSize(20);
            viewHolder.tv.setTextColor(context.getResources().getColor(R.color.sy));
        } else {
            viewHolder.tv.setText(arrayList.get(position).name);
            viewHolder.tv.setBackgroundResource(R.color.shoutous);
            viewHolder.tv.setTextColor(context.getResources().getColor(R.color.sy));
            viewHolder.tv.setTextSize(15);
        }
        convertView.setBackgroundResource(R.drawable.week_selector);
        if(selectedWeek.equals(arrayList.get(position).name)){
            convertView.setEnabled(true);
            viewHolder.tv.setTextColor(context.getResources().getColor(R.color.sys));
        }else{
            viewHolder.tv.setTextColor(context.getResources().getColor(R.color.sy));
            convertView.setEnabled(true);
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv;
    }
    public void refreshzhoulist(List<Course> list){
        arrayList.clear();
        arrayList.addAll(list);
    }
    public void setCurrentWeek(String currentWeek) {
        this.currentWeek = currentWeek;
        notifyDataSetChanged();
    }
    public void setSelectedWeek(String name){
        this.selectedWeek=name;
        notifyDataSetChanged();
    }
}
