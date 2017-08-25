package com.dingli.diandiaan.login.pingjiao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Coursecenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by admin on 2016/8/16.
 */
public class PjSecondAdapter extends BaseAdapter {

    private List<Coursecenter> list;
    private Context context;
    public PjSecondAdapter(List<Coursecenter> list, Context context ) {
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
    public Coursecenter getItem(int position) {
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
//        try {
            Coursecenter object=list.get(position);
            holder.class_name.setText(object.className);
            DecimalFormat df = new DecimalFormat("#0.00");
            double d=object.avgScore;
                holder.class_cql.setText(df.format(d));
//            String s=object.getInt("classid")+"";
//            EventBus.getDefault().post(new Event(4,s));

//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return view;
    }
    class ViewHolder{
        private TextView class_name;
        private TextView class_cql;
    }
}