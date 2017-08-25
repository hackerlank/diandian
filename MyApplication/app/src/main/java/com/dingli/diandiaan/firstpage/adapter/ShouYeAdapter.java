package com.dingli.diandiaan.firstpage.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.ResultOne;
import com.dingli.diandiaan.firstpage.ShouyeFragment;
import com.dingli.diandiaan.firstpage.WebViewActivity;
import com.dingli.diandiaan.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2017/4/27.
 */

public class ShouYeAdapter extends BaseAdapter{
    List<ResultOne> listshouye;
    public ShouYeAdapter(){
        listshouye=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return listshouye.size();
    }
    @Override
    public ResultOne getItem(int position) {
        return listshouye.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ShouYeHolder shouYeHolder=null;
        if (convertView==null){
            shouYeHolder=new ShouYeHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_shouye_view,null);
            shouYeHolder.iv=(ImageView) convertView.findViewById(R.id.ivshouye);
            shouYeHolder.tvtitle=(TextView) convertView.findViewById(R.id.tvtitld);
            shouYeHolder.tvnumber=(TextView) convertView.findViewById(R.id.tvreadnum);
            shouYeHolder.tvup=(TextView) convertView.findViewById(R.id.upnum);
            shouYeHolder.llitem=(LinearLayout) convertView.findViewById(R.id.llitem);
            convertView.setTag(shouYeHolder);
        }else{
            shouYeHolder=(ShouYeHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(listshouye.get(position).picUrl).into(shouYeHolder.iv);
        shouYeHolder.tvtitle.setText(listshouye.get(position).title);
        shouYeHolder.tvnumber.setText("浏览量"+String.valueOf(listshouye.get(position).hitCount));
        shouYeHolder.tvup.setText(String.valueOf(listshouye.get(position).praiseCount));
        final String id=String.valueOf(listshouye.get(position).id);
        shouYeHolder.llitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    intent.setClass(parent.getContext(), LoginActivity.class);
                }else {
                    intent.setClass(parent.getContext(), WebViewActivity.class);
                    bundle.putString("url", "/mobileui/article?"+id);
                    bundle.putString("id", id);
                    bundle.putString("list","listv2");
                    intent.putExtras(bundle);
                }
                parent.getContext().startActivity(intent);
            }
        });
        return convertView;
    }
    public void  refreshShouYe(List<ResultOne> list){
        listshouye.clear();
        listshouye.addAll(list);
    }
    class ShouYeHolder{
        ImageView iv;
        TextView tvtitle,tvnumber,tvup;
        LinearLayout llitem;
    }
}
