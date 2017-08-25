package com.dingli.diandiaan.rule;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.CommInterface;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.firstpage.callset.CallSetActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/11/29.
 */
public class RuleAdapter extends BaseAdapter{
    ArrayList<Course> arrayList;
    Context context;
    CommInterface commInterface;
    public RuleAdapter(Context context,CommInterface commInterface){
        this.context=context;
        arrayList=new ArrayList<>();
        this.commInterface=commInterface;
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
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.list_item_rule_view,null);
        }
        final ListItemRulView listItemRulView=(ListItemRulView)convertView;
        listItemRulView.setItemRule(getItem(position));
        listItemRulView.setTag(getItem(position));
        listItemRulView.setInterface(commInterface);
        final int id=arrayList.get(position).id;
        final String name=arrayList.get(position).courseName;
        final int lateTime=arrayList.get(position).lateTime;
        final String rollCallTypeOrigin=arrayList.get(position).rollCallTypeOrigin;
        if (listItemRulView.tbrules.getVisibility()==View.VISIBLE){
            listItemRulView.tbrules.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String str="true";
                    if (isChecked){
                        commInterface.KaiOrGuan(listItemRulView,id,str,name,lateTime);
                    }
                }
            });
        }else{
            listItemRulView.tbrule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String str="false";
                    if (!isChecked){
                        commInterface.KaiOrGuan(listItemRulView,id,str,name,lateTime);
                    }
                }
            });
        }
            listItemRulView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,CallSetActivity.class);
                    intent.putExtra(Constant.COURSE_NAME,name);
                    intent.putExtra(Constant.COURSEID,id);
                    intent.putExtra(Constant.RUKOU,"2");
                    intent.putExtra(Constant.CODE,rollCallTypeOrigin);
                    intent.putExtra(Constant.LATE_TIME,lateTime);
                    DianApplication.user.latetime=lateTime;
                    listItemRulView.tbrule.setChecked(true);
                    listItemRulView.tbrules.setChecked(false);
                    context.startActivity(intent);
                }
            });
        return listItemRulView;
    }
    public void refreshRule(List<Course> list){
        arrayList.clear();
        arrayList.addAll(list);
    }
}
