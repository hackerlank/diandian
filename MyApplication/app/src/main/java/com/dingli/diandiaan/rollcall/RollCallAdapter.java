package com.dingli.diandiaan.rollcall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.CourseMing;
import com.dingli.diandiaan.common.ResultInfoCallOne;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/3/23.
 */
public class RollCallAdapter  extends BaseAdapter{

    Context context;
    List<CourseMing> arrayList;
    ViewHolder viewHolder;
    PopupWindow popupWindow;
    ViewHolder.onCancelCollectListener listener;
     int id;
    public RollCallAdapter(Context context,PopupWindow popupWindow,ViewHolder.onCancelCollectListener listener){
        this.context=context;
        arrayList=new ArrayList<CourseMing>();
        this.popupWindow=popupWindow;
        this.listener=listener;
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_callview, null);
//            viewHolder.igvone = (CircleImageView) convertView.findViewById(R.id.igvone);
//            viewHolder.tvnameone = (TextView) convertView.findViewById(R.id.tvnameone);
//            viewHolder.rlbgone = (RelativeLayout) convertView.findViewById(R.id.rlbgone);
            viewHolder.bantv=(TextView)convertView.findViewById(R.id.bantv);
            viewHolder.gdv=(GridView)convertView.findViewById(R.id.gdv);
            viewHolder.tvren=(TextView)convertView.findViewById(R.id.tvren);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bantv.setText(arrayList.get(position).className);
        String renshu=arrayList.get(position).rollCallList.size()+"";
        viewHolder.tvren.setText(renshu+context.getResources().getString(R.string.ren));
        ArrayList<ResultInfoCallOne> arrayListone=arrayList.get(position).rollCallList;
         GrideVieAdapter adapter=new GrideVieAdapter(parent.getContext(),arrayListone,popupWindow,listener);
         viewHolder.gdv.setAdapter(adapter);
        viewHolder.setmOnCancelInterface(listener);
        viewHolder.gdv.setPressed(true);
        viewHolder.gdv.setClickable(true);
        viewHolder.gdv.setEnabled(true);
        viewHolder.gdv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResultInfoCallOne i=(ResultInfoCallOne)parent.getItemAtPosition(position);
                if (listener != null) {
//                    listener.onCancelCollect(1,view,i.classId,i.className,i.courseId,i.id,i.scheduleId,i.studentScheduleId,
//                            i.teacherId,i.type,i.userId,i.userName);
                }
            }
        });
        return convertView;
    }
    public void addCalllist(List<CourseMing> list){
            arrayList.addAll(list);
    }
    public void refreshCalllist(List<CourseMing> list){
        arrayList.clear();
            arrayList.addAll(list);
    }
}
