package com.dingli.diandiaan.instruc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.CommInterface;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.ResultOne;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2017/4/14.
 */

public class KaiSuiAdapter extends BaseAdapter{

    List<ResultOne> resultOnes;
    CommInterface commInterface;
    public KaiSuiAdapter(CommInterface listener){
        resultOnes=new ArrayList<>();
        this.commInterface=listener;
    }
    @Override
    public int getCount() {
        return resultOnes.size() % 2 == 0 ? resultOnes.size() / 2 : resultOnes.size() / 2 + 1;
    }

    @Override
    public ResultOne getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewSui viewSui=null;
        if (convertView==null){
            viewSui=new ViewSui();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kaisui_view,null);
            viewSui.llsuione=(LinearLayout) convertView.findViewById(R.id.llsuione);
            viewSui.llsuitwo=(LinearLayout) convertView.findViewById(R.id.llsuitwo);
            viewSui.ivsuione=(ImageView) convertView.findViewById(R.id.ivsuione);
            viewSui.ivsuionewei=(ImageView) convertView.findViewById(R.id.ivsuionewei);
            viewSui.ivsuitwo=(ImageView) convertView.findViewById(R.id.ivsuitwo);
            viewSui.ivsuitwowei=(ImageView) convertView.findViewById(R.id.ivsuitwowei);
            viewSui.tvsuione=(TextView) convertView.findViewById(R.id.tvsuione);
            viewSui.tvsuitwo=(TextView) convertView.findViewById(R.id.tvsuitwo);
            convertView.setTag(viewSui);
        }else{
            viewSui=(ViewSui) convertView.getTag();
        }
        ResultOne itemone = null;
        ResultOne itemtwo = null;
        if(position * 2 + 1 < resultOnes.size()){
            itemone = resultOnes.get(position * 2);
            itemtwo = resultOnes.get(position * 2 + 1);
        }else if(position * 2 + 1 == resultOnes.size()){
            itemone = resultOnes.get(position * 2);
            itemtwo = null;
        }
        if (itemone!=null){
            viewSui.llsuione.setVisibility(View.VISIBLE);
            viewSui.ivsuione.setVisibility(View.GONE);
            viewSui.ivsuionewei.setVisibility(View.VISIBLE);
            viewSui.tvsuione.setText(itemone.name);
            viewSui.llsuione.setTag(itemone);
            if (DianApplication.user.listid.size()!=0) {
                for (int i = 0; i < DianApplication.user.listid.size(); i++) {
                    if (DianApplication.user.listid.get(i).equals(String.valueOf(itemone.id))) {
                        viewSui.ivsuione.setVisibility(View.VISIBLE);
                        viewSui.ivsuionewei.setVisibility(View.GONE);
                    }
                }
            }
        }else{
            viewSui.llsuione.setVisibility(View.GONE);
        }
        if (itemtwo!=null){
            viewSui.llsuitwo.setVisibility(View.VISIBLE);
            viewSui.ivsuitwo.setVisibility(View.GONE);
            viewSui.ivsuitwowei.setVisibility(View.VISIBLE);
            viewSui.tvsuitwo.setText(itemtwo.name);
            viewSui.llsuitwo.setTag(itemtwo);
            if (DianApplication.user.listid.size()!=0) {
                for (int i = 0; i < DianApplication.user.listid.size(); i++) {
                    if (DianApplication.user.listid.get(i).equals(String.valueOf(itemtwo.id))) {
                        viewSui.ivsuitwo.setVisibility(View.VISIBLE);
                        viewSui.ivsuitwowei.setVisibility(View.GONE);
                    }
                }
            }
        }else{
            viewSui.llsuitwo.setVisibility(View.GONE);
        }
        final ResultOne itemleft=itemone;
        final ResultOne itemright=itemtwo;
        final  ImageView igsuione=viewSui.ivsuione;
        final  ImageView igsuitwo=viewSui.ivsuitwo;
        viewSui.llsuione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commInterface.select(itemleft.id,igsuione);
            }
        });
        viewSui.llsuitwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commInterface.select(itemright.id,igsuitwo);
            }
        });
        return convertView;
    }
    public void refreshKaisui(List<ResultOne> resultOnesd){
        resultOnes.clear();
        resultOnes.addAll(resultOnesd);
    }
    class ViewSui{
        LinearLayout llsuitwo,llsuione;
        TextView tvsuitwo,tvsuione;
        ImageView ivsuione,ivsuitwo,ivsuionewei,ivsuitwowei;
    }
}
