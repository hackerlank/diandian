package com.dingli.diandiaan.instruc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.CourseMing;
import com.dingli.diandiaan.common.ResultInfoCall;
import com.dingli.diandiaan.common.ResultInfoCallOne;
import com.dingli.diandiaan.rollcall.ListViewAdapter;
import com.dingli.diandiaan.rollcall.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/11/30.
 */
public class InstrucAdapter extends BaseAdapter{
      List<ResultInfoCall> arrayList;
      Context context;
    ViewHolder viewHolder;
    ViewHolder.onCancelCollectListener listener;
    ImageView iv;
    public InstrucAdapter(Context context,ViewHolder.onCancelCollectListener listener){
        this.context=context;
        this.listener=listener;
        arrayList=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public ResultInfoCall getItem(int position) {
        return arrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_instr_view,null);
            viewHolder.bantv=(TextView)convertView.findViewById(R.id.bantvs);
            viewHolder.lvs=(ListView)convertView.findViewById(R.id.lvs);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.bantv.setText(arrayList.get(position).className);
        ArrayList<ResultInfoCallOne> arrayListone=arrayList.get(position).classList;
        ListDetailViewAdapter adapter=new ListDetailViewAdapter(parent.getContext(),arrayListone,listener);
        viewHolder.lvs.setAdapter(adapter);
        viewHolder.setmOnCancelInterface(listener);
        viewHolder.lvs.setPressed(true);
        viewHolder.lvs.setEnabled(true);
        viewHolder.lvs.setClickable(true);
        return convertView;
    }
    public void refreshInstruclist(List<ResultInfoCall> list){
        arrayList.clear();
        arrayList.addAll(list);
    }
}
