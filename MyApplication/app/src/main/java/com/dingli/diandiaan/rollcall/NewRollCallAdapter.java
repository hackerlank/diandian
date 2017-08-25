package com.dingli.diandiaan.rollcall;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.CourseMing;
import com.dingli.diandiaan.common.ResultInfoCallOne;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/11/30.
 */
public class NewRollCallAdapter extends BaseAdapter{
      List<CourseMing> arrayList;
      Context context;
    ViewHolder viewHolder;
    PopupWindow popupWindow;
    ViewHolder.onCancelCollectListener listener;
    String quanbu;
    ImageView iv;
    int h;
    int w;
    public NewRollCallAdapter(Context context,PopupWindow popupWindow,ViewHolder.onCancelCollectListener listener,String quanbu,int h,int w){
        this.context=context;
        this.popupWindow=popupWindow;
        this.listener=listener;
        arrayList=new ArrayList<>();
        this.quanbu=quanbu;
        this.h=h;
        this.w=w;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public CourseMing getItem(int position) {
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
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_newcallview,null);
            viewHolder.tvren=(TextView)convertView.findViewById(R.id.tvrens);
            viewHolder.bantv=(TextView)convertView.findViewById(R.id.bantvs);
            viewHolder.lvs=(ListView)convertView.findViewById(R.id.lvs);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.bantv.setText(arrayList.get(position).className);
        String renshu=String.valueOf(arrayList.get(position).rollCallList.size());
        viewHolder.tvren.setText(renshu);
        ArrayList<ResultInfoCallOne> arrayListone=arrayList.get(position).rollCallList;
        ListViewAdapter adapter=new ListViewAdapter(parent.getContext(),arrayListone,popupWindow,listener,quanbu);
        viewHolder.lvs.setAdapter(adapter);
        viewHolder.setmOnCancelInterface(listener);
        viewHolder.lvs.setPressed(true);
        viewHolder.lvs.setEnabled(true);
        viewHolder.lvs.setClickable(true);
        return convertView;
    }
    public void refreshNewCalllist(List<CourseMing> list){
        arrayList.clear();
        arrayList.addAll(list);
    }
}
