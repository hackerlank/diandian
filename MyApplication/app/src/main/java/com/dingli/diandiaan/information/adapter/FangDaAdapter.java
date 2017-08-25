package com.dingli.diandiaan.information.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dingli.diandiaan.R;

import java.util.ArrayList;

/**
 * Created by dingliyuangong on 2016/10/31.
 */
public class FangDaAdapter extends PagerAdapter{
      ArrayList<View> url;
    public FangDaAdapter(ArrayList<View> url){
        this.url=url;
    }
    @Override
    public int getCount() {
        return url.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=url.get(position);
        ViewParent vp=view.getParent();
        if (vp!=null){
          ViewGroup vg=(ViewGroup)vp;
            vg.removeView(view);
        }
        ((ViewPager)container).addView(url.get(position));
        return url.get(position);
    }
}
