package com.dingli.diandiaan.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.QingJiaSty;
import com.dingli.diandiaan.information.EntrtyActivity;
import com.dingli.diandiaan.information.QingJiaMessActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class InfomationAdapter extends BaseAdapter {
    Context context;
    List<QingJiaSty> arrlist;
    public InfomationAdapter(Context context){
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
        View view=null;
        if (convertView==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.information_item,null);
        }else{
            view=convertView;
        }
        ViewHolder holder=null;
        if(holder==null){
            holder=new ViewHolder();
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.tvyuedu=(TextView)view.findViewById(R.id.tvyuedu);
        holder.tvshenqing=(TextView)view.findViewById(R.id.tvshenqing);
        holder.rlqingjia=(RelativeLayout)view.findViewById(R.id.rlqingjia);
        holder.rltongzhi=(RelativeLayout)view.findViewById(R.id.rltongzhi);
        holder.tvshuzi=(TextView)view.findViewById(R.id.tvshuzi);
        holder.tvshuzis=(TextView)view.findViewById(R.id.tvshuzis);
        holder.tvtzsj=(TextView)view.findViewById(R.id.tvtzsj);
        holder.tvtzsjs=(TextView)view.findViewById(R.id.tvtzsjs);
        return view;
    }
    public void addWeiDone(List<QingJiaSty> list){
        arrlist.addAll(list);
    }
    public void refreshWeiDone(List<QingJiaSty> list){
        arrlist.clear();
        arrlist.addAll(list);
    }
    class ViewHolder{
        TextView tvyuedu;
        TextView tvshenqing;
       RelativeLayout rlqingjia;
        RelativeLayout rltongzhi;
        TextView tvshuzi;
        TextView tvshuzis;
        TextView tvtzsj;
        TextView tvtzsjs;
    }
}
