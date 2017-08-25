package com.dingli.diandiaan.contact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.CommInterface;
import com.dingli.diandiaan.common.RollCallList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2017/5/9.
 */

public class ContactInstrucAdapter extends BaseAdapter{
   List<RollCallList> instruContact;
    CommInterface com;
    public ContactInstrucAdapter(CommInterface ineter){
        instruContact=new ArrayList<>();
        this.com=ineter;
    }
    @Override
    public int getCount() {
        return instruContact.size();
    }
    @Override
    public Object getItem(int position) {
        return instruContact.get(position);
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
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contactintruc_view,null);
            viewHolder.tvcontaclass=(TextView) convertView.findViewById(R.id.tvcontaclass);
            viewHolder.itemintruc=(LinearLayout) convertView.findViewById(R.id.itemintruc);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ContactViewHolder) convertView.getTag();
        }
        viewHolder.tvcontaclass.setText(instruContact.get(position).name);
        final int id=instruContact.get(position).id;
        final String name=instruContact.get(position).name;
        viewHolder.itemintruc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.KaiOrGuan(null,id,"instruc",name,0);
            }
        });
        return convertView;
    }
    public void refreInsContact(List<RollCallList> listcontins){
        instruContact.clear();
        instruContact.addAll(listcontins);
    }
}
