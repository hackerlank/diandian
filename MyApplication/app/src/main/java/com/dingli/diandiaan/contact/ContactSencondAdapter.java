package com.dingli.diandiaan.contact;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.CommInterface;
import com.dingli.diandiaan.common.Data;
import com.dingli.diandiaan.rollcall.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2017/5/9.
 */

public class ContactSencondAdapter extends BaseAdapter{
    List<Data> datalist;
    CommInterface commInterface;
    public ContactSencondAdapter(CommInterface interfaces){
        datalist=new ArrayList<>();
        this.commInterface=interfaces;
    }
    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ContactViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ContactViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contactsecond_view,null);
            viewHolder.tvcode=(TextView) convertView.findViewById(R.id.tvcodecontact);
            viewHolder.tvriqi=(TextView) convertView.findViewById(R.id.tvriqicontact);
            viewHolder.tvname=(TextView) convertView.findViewById(R.id.tvnamecontact);
            viewHolder.itemcontact=(LinearLayout) convertView.findViewById(R.id.itemcontact);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ContactViewHolder)convertView.getTag();
        }
        viewHolder.tvriqi.setText(datalist.get(position).code);
        viewHolder.tvname.setText(datalist.get(position).courseName);
        final int courseId=datalist.get(position).courseId;
        viewHolder.itemcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commInterface.KaiOrGuan(null,courseId,"sencond","",0);
            }
        });
        return convertView;
    }
    public void refreshClass(List<Data> listdata){
       datalist.clear();
        datalist.addAll(listdata);
    }
}
