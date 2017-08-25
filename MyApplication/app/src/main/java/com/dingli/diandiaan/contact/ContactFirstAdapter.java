package com.dingli.diandiaan.contact;

import android.graphics.ImageFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.ResultInfoCallOne;
import com.dingli.diandiaan.common.ResultOne;
import com.dingli.diandiaan.common.RollCallList;
import com.dingli.diandiaan.login.chuqinlv.CustomListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2017/5/9.
 */

public class ContactFirstAdapter extends BaseAdapter{
    List<ResultOne> firstlist;
    ListContactView.ContactListener listeners;
    public ContactFirstAdapter(ListContactView.ContactListener listener){
        this.listeners=listener;
        firstlist=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return firstlist.size();
    }
    @Override
    public Object getItem(int position) {
        return firstlist.get(position);
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
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contactfirst_view,null);
            viewHolder.ivstaffturn=(ImageView)convertView.findViewById(R.id.ivstaffturn);
            viewHolder.tvclasname=(TextView) convertView.findViewById(R.id.tvclasname);
            viewHolder.tvstaffnum=(TextView) convertView.findViewById(R.id.tvstaffnum);
            viewHolder.listviewzicontact=(CustomListView) convertView.findViewById(R.id.listviewzicontact);
            viewHolder.vishcontact=convertView.findViewById(R.id.vishcontact);
            viewHolder.llfirstcontac=(LinearLayout) convertView.findViewById(R.id.llfirstcontac);
            convertView.setTag(viewHolder);
        }else{
          viewHolder=(ContactViewHolder) convertView.getTag();
        }
        final ContactViewHolder holder=viewHolder;
        viewHolder.tvclasname.setText(firstlist.get(position).className);
        viewHolder.tvstaffnum.setText(String.valueOf(firstlist.get(position).count));
        if (holder.listviewzicontact.getVisibility()==View.VISIBLE){
            holder.ivstaffturn.setBackgroundResource(R.mipmap.icon_lvdown);
            viewHolder.vishcontact.setVisibility(View.VISIBLE);
        }else{
            holder.ivstaffturn.setBackgroundResource(R.mipmap.icon_youjian);
            viewHolder.vishcontact.setVisibility(View.GONE);
        }
        List<RollCallList> listc=firstlist.get(position).data.data;
        ContactFirstItemAdapter itemadapter=new ContactFirstItemAdapter(listc,parent.getContext(),"student",listeners);
        viewHolder.listviewzicontact.setAdapter(itemadapter);
        viewHolder.llfirstcontac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.listviewzicontact.getVisibility()==View.VISIBLE){
                    holder.listviewzicontact.setVisibility(View.GONE);
                    holder.vishcontact.setVisibility(View.GONE);
                    holder.ivstaffturn.setBackgroundResource(R.mipmap.icon_youjian);
                    holder.vishcontact.setVisibility(View.GONE);
                }else{
                    holder.listviewzicontact.setVisibility(View.VISIBLE);
                    holder.vishcontact.setVisibility(View.VISIBLE);
                    holder.ivstaffturn.setBackgroundResource(R.mipmap.icon_lvdown);
                    holder.vishcontact.setVisibility(View.VISIBLE);
                }
            }
        });
        return convertView;
    }
    public void refreshFirstContact(List<ResultOne> listresu){
        firstlist.clear();
        firstlist.addAll(listresu);
    }
    public void addFirstContact(List<ResultOne> listresu){
        firstlist.addAll(listresu);
    }
}
