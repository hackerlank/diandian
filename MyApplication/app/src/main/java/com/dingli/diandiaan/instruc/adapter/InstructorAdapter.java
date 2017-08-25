package com.dingli.diandiaan.instruc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.CourseMing;
import com.dingli.diandiaan.instruc.InstructorDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2017/4/13.
 */

public class InstructorAdapter extends BaseAdapter{
    List<CourseMing> listming;
    Context context;
    public InstructorAdapter(Context context){
        this.context=context;
        listming=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return listming.size();
    }

    @Override
    public CourseMing getItem(int position) {
        return listming.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_instruc_view,null);
        }
        Listinstrucview listinstrucview=(Listinstrucview) convertView;
        listinstrucview.setInstruc(getItem(position));
        listinstrucview.setTag(getItem(position));
        final int rolleverId=listming.get(position).rollCallEverId;
        listinstrucview.btheaderitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, InstructorDetailActivity.class);
                intent.putExtra(Constant.TYPE,rolleverId);
                context.startActivity(intent);
            }
        });
        return listinstrucview;
    }
    public void refreshInstruc(List<CourseMing> listm){
        listming.clear();
        listming.addAll(listm);
    }
}
