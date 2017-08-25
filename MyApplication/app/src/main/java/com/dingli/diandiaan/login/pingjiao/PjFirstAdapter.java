package com.dingli.diandiaan.login.pingjiao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.Coursecenter;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.evaluation.EvaluationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/21.
 */
public class PjFirstAdapter extends BaseAdapter{

    private List<Course> list;
    private Context context;
    private SharedPreferences preferences;

    public PjFirstAdapter(List<Course> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        int ret=0;
        if(list!=null){
            ret=list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view=null;
        if(convertView==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_chuqinlv_first,null);
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
        holder.major= (TextView) view.findViewById(R.id.major);
        holder.cql= (TextView) view.findViewById(R.id.cql);
        holder.cql_second_listview= (ListView) view.findViewById(R.id.cql_second_listview);
        holder.you= (ImageView) view.findViewById(R.id.you);
        holder.you.setBackgroundResource(R.mipmap.youjian);
        holder.dianrl=(RelativeLayout)view.findViewById(R.id.dianrl);
        holder.visha=view.findViewById(R.id.visha);
        holder.vish=view.findViewById(R.id.vish);
        //这三项需要判断是否显示
        holder.day= (TextView) view.findViewById(R.id.day);
        holder.from= (TextView) view.findViewById(R.id.from);
//        try {
            String dayOfWeek  = list.get(position).dayOfWeek;
            int dayofw=Integer.parseInt(dayOfWeek);
            if(dayofw>0){
                switch (dayofw){
                    case 1:
                        holder.day.setText("星期一");
                        break;
                    case 2:
                        holder.day.setText("星期二");
                        break;
                    case 3:
                        holder.day.setText("星期三");
                        break;
                    case 4:
                        holder.day.setText("星期四");
                        break;
                    case 5:
                        holder.day.setText("星期五");
                        break;
                    case 6:
                        holder.day.setText("星期六");
                        break;
                    case 7:
                        holder.day.setText("星期日");
                        break;
                }
            }
         //   holder.day.setText(list.get(position).get("dayOfWeek").toString());

            holder.from.setText(list.get(position).periodName);
            holder.major.setText(list.get(position).courseName);
            final Course object=list.get(position);

            DecimalFormat df = new DecimalFormat("#0.00");
            double d=list.get(position).avgScore;
                holder.cql.setText(df.format(d));
            List<Coursecenter> data=new ArrayList<>();
//            for(int i=0; i <list.get(position).classInfo.size();i++){
                data.addAll(list.get(position).classInfo);
//            }
            final List<Coursecenter> data1=data;
            final int sched=list.get(position).scheduleId;
            PjSecondAdapter adapter=new PjSecondAdapter(data1,context);
            holder.cql_second_listview.setAdapter(adapter);
        final ViewHolder hoder=holder;
           holder.dianrl.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (hoder.cql_second_listview.getVisibility()==View.VISIBLE){
                       hoder.cql_second_listview.setVisibility(View.GONE);
                       hoder.visha.setVisibility(View.VISIBLE);
                       hoder.vish.setVisibility(View.GONE);
                       hoder.you.setBackgroundResource(R.mipmap.youjian);
                   }else{
                       hoder.cql_second_listview.setVisibility(View.VISIBLE);
                       hoder.visha.setVisibility(View.GONE);
                       hoder.vish.setVisibility(View.VISIBLE);
                       hoder.you.setBackgroundResource(R.mipmap.icon_zhankai);
                   }
               }
           });
            holder.cql_second_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String className = null;
                    String classCql=null;
//                    int scheduleId=0;
//                    int classId=0;
//                    int allCount=0;
//                    int count=0;
                    Coursecenter object1=data1.get(position);
//                    try {
                        classCql=object1.avgScore+"";
                       int classId=object1.classId;
                        className = object1.className;
//                        int scheduleId= data1.get(position).scheduleId;
                        int count=data1.get(position).count;
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    preferences = context.getSharedPreferences("aizixin_info",Context.MODE_PRIVATE);
                    boolean isAll = preferences.getBoolean("isAll", false);
                    if (!isAll){
                        //单独周可以跳转
                        // Intent intent = new Intent(context, TestActivity .class);
                        Intent intent = new Intent(context, EvaluationActivity.class);
                       // Intent intent = new Intent(context, EvaluationActivity.class);
//                        Bundle bundle = new Bundle();
                        intent.putExtra("className", className);
                        intent.putExtra("classCql", classCql);
                        intent.putExtra("classId", classId);
                        intent.putExtra("scheduleId", sched);
//                        intent.putExtras(bundle);
//                        DianApplication.user.classId=classId;
//                        DianApplication.user.scheduleId=scheduleId;
//                        try {
//                            int courseId=object.getInt("courseId");
//                            scheduleId=object.getInt("scheduleId");
//                            bundle.putInt("courseId",courseId);
//                            bundle.putInt("scheduleId", scheduleId);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                         context.startActivity(intent);
                    }
                }
            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return view;
    }
    class ViewHolder{
        private TextView major;
        private TextView cql;
        private TextView day;
        private TextView from;
        private ListView cql_second_listview;
        private ImageView you;
        private RelativeLayout dianrl;
        private View visha;
        private View vish;
    }
}
