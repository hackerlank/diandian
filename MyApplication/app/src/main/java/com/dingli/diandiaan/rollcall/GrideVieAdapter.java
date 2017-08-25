package com.dingli.diandiaan.rollcall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.ResultInfoCallOne;
import com.dingli.diandiaan.login.CircleImageView;

import java.util.ArrayList;

/**
 * Created by dingliyuangong on 2016/4/2.
 */
public class GrideVieAdapter extends BaseAdapter{

  public Context context;
    public ArrayList<ResultInfoCallOne> arrayList;
    ViewHolderOne viewHolderOne;
    PopupWindow popupWindow;
    ViewHolder.onCancelCollectListener listener;
    public GrideVieAdapter(Context context,ArrayList<ResultInfoCallOne> arrayList,PopupWindow popupWindow,ViewHolder.onCancelCollectListener listener){
        this.context=context;
        this.arrayList=arrayList;
        this.popupWindow=popupWindow;
        this.listener=listener;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public ResultInfoCallOne getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            viewHolderOne=new ViewHolderOne();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_name_view,null);
            viewHolderOne.igvone = (CircleImageView) convertView.findViewById(R.id.igvone);
            viewHolderOne.tvnameone = (TextView) convertView.findViewById(R.id.tvnameone);
            viewHolderOne.rlbgone = (RelativeLayout) convertView.findViewById(R.id.rlbgone);
            convertView.setTag(viewHolderOne);
        }else{
            viewHolderOne=(ViewHolderOne)convertView.getTag();
        }
        if(!arrayList.get(position).avatar.equals("null")) {
            Glide.with(context).load(arrayList.get(position).avatar).into(viewHolderOne.igvone);
        }
        viewHolderOne.tvnameone.setText(arrayList.get(position).userName);
        if(arrayList.get(position).type.equals("2")) {
        viewHolderOne.rlbgone.setBackgroundResource(R.mipmap.kuangke);
        }else if(arrayList.get(position).type.equals("3")) {
            viewHolderOne.rlbgone.setBackgroundResource(R.mipmap.chidao);
        }else if(arrayList.get(position).type.equals("4")) {
            viewHolderOne.rlbgone.setBackgroundResource(R.mipmap.qingjia);
        }else if(arrayList.get(position).type.equals("5")) {
            viewHolderOne.rlbgone.setBackgroundResource(R.mipmap.zaotui);
        }else if(arrayList.get(position).type.equals("1")){
            viewHolderOne.rlbgone.setBackgroundResource(R.color.bg_White);
        }else if(arrayList.get(position).type.equals("0")){

        }
        return convertView;
    }

}
