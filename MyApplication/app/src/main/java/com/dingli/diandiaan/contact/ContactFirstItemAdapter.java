package com.dingli.diandiaan.contact;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.RollCallList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2017/3/17.
 */

public class ContactFirstItemAdapter extends BaseAdapter{

    List<RollCallList> arraylist;
    Context context;
    ListContactView.ContactListener listener;
    String state;
    public ContactFirstItemAdapter(List<RollCallList> listroll,Context context, String str, ListContactView.ContactListener listener){
        this.arraylist=listroll;
        this.context=context;
        this.listener=listener;
        this.state=str;
    }
    @Override
    public int getCount() {
        return arraylist.size();
    }
    @Override
    public RollCallList getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contacts_view,null);
        }
       final  ListContactView listContactView=(ListContactView) convertView;
        listContactView.setContactView(getItem(position),state);
        listContactView.setTag(getItem(position));
        listContactView.setContactListener(listener);
        final String phones=arraylist.get(position).phone;
        final String names=arraylist.get(position).name;
        final String stuIds=arraylist.get(position).stuId;
        if (!TextUtils.isEmpty(phones)) {
            if (!phones.equals("null")) {
                listContactView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onContactListener(listContactView.contactphone.getText().toString(), phones, names, stuIds);
                    }
                });
            }
        }
        if (state.equals("staff")){
            listContactView.ivstaff.setVisibility(View.VISIBLE);
        }else{
            listContactView.ivstaff.setVisibility(View.GONE);
        }
        return listContactView;
    }
    public void refreshListContact(List<RollCallList> arraylists){
        arraylist.clear();
        arraylist.addAll(arraylists);
    }
    public void addListContact(List<RollCallList> arraylists){
        arraylist.addAll(arraylists);
    }
}
