package com.dingli.diandiaan.login.chuqinlv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dingli.diandiaan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by admin on 2016/7/21.
 */
public class CqlSecondAdapter extends BaseAdapter{
    private List<JSONObject> list;
    private Context context;

    public CqlSecondAdapter(List<JSONObject> list, Context context ) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null;
        if(convertView==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_chuqinlv_second,null);
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
        holder.class_name= (TextView) view.findViewById(R.id.class_name);
        holder.class_cql= (TextView) view.findViewById(R.id.class_cql);
        try {
            //新修改
            int allCount=list.get(position).getInt("allCount");
            int nCount=list.get(position).getInt("nCount");
            if(allCount==0||nCount==0){
                holder.class_cql.setText("0%");
            }else{
                holder.class_cql.setText(nCount*100/allCount+"%");
            }
            holder.class_name.setText(list.get(position).getString("className"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
    class ViewHolder{
        private TextView class_name;
        private TextView class_cql;
    }
}
