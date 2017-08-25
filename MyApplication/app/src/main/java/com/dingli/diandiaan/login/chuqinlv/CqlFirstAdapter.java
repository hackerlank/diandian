package com.dingli.diandiaan.login.chuqinlv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.login.chuqinlv.studentCql.StudentCqlActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/21.
 */
public class CqlFirstAdapter extends BaseAdapter{

    private List<JSONObject> list;
    private Context context;
    private SharedPreferences preferences;

    public CqlFirstAdapter(List<JSONObject> list, Context context) {
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
        try {

            int periodName  = list.get(position).optInt("dayOfWeek");
            if(periodName>0){
                switch (list.get(position).getInt("dayOfWeek")){
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
            if(list.get(position)!=null){
                if(list.get(position).getString("courseName")!=null){
                    holder.major.setText(list.get(position).get("courseName").toString());
                    holder.from.setText(list.get(position).get("periodName").toString());
                }

                    int allCount=list.get(position).getInt("allCount");
                    int nCount=list.get(position).getInt("nCount");
                    if(allCount==0||nCount==0){
                        holder.cql.setText("0%");
                    }else{
                        DecimalFormat df = new DecimalFormat("#.00");
                        double d=(double)nCount*100/allCount;
                        holder.cql.setText(df.format(d)+"%");
                    }
                JSONArray jsonArray=list.get(position).getJSONArray("classInfo");
                final List<JSONObject> data=new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++){
                    data.add(jsonArray.getJSONObject(i));
                }
                final List<JSONObject> data1=data;
                CqlSecondAdapter adapter=new CqlSecondAdapter(data1,context);
                holder.cql_second_listview.setAdapter(adapter);
                final ViewHolder hoder=holder;
                holder.dianrl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hoder.cql_second_listview.getVisibility() == View.VISIBLE) {
                            hoder.cql_second_listview.setVisibility(View.GONE);
                            hoder.visha.setVisibility(View.VISIBLE);
                            hoder.vish.setVisibility(View.GONE);
                            hoder.you.setBackgroundResource(R.mipmap.youjian);
                        } else {
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

                        try {
                            String className = data1.get(position).getString("className");
                            String classCql=null;
                            int class_allCount=list.get(position).getInt("allCount");
                            int class_nCount=list.get(position).getInt("nCount");
                            if(class_allCount==0||class_nCount==0){
                                classCql="0%";
                            }else{
                                classCql=class_nCount*100/class_allCount+"%";
                            }

                            preferences = context.getSharedPreferences("aizixin_info",Context.MODE_PRIVATE);
                            boolean isAll = preferences.getBoolean("isAll", false);
                            if (isAll){
                            Intent intent = new Intent(context, StudentCqlActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("className",className);
                            bundle.putString("classCql",classCql);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
