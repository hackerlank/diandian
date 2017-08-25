package com.dingli.diandiaan.information;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.information.adapter.FangDaAdapter;

import java.util.ArrayList;

/**
 * Created by dingliyuangong on 2016/10/31.
 */
public class FangDaActivity extends BaseActivity{
    ViewPager viewpop;
    ArrayList<View> list;
    FangDaAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview_popuwindows);
        String url=getIntent().getStringExtra(Constant.URL);
        int yeshu=getIntent().getIntExtra(Constant.YESHU,0);
        viewpop=(ViewPager)findViewById(R.id.viewpop);
        list=new ArrayList<>();
        if (url.contains(";")){
            String[] http=url.split(";");
            switch (http.length){
                case 2:
                    for (int i=0;i<2;i++){
                        View view= LayoutInflater.from(this).inflate(R.layout.viewpager_itempage,null);
                        ImageView ive=(ImageView)view.findViewById(R.id.imageViewda);
                        Glide.with(this).load(http[i]).into(ive);
                        list.add(ive);
                    }
                    adapter=new FangDaAdapter(list);
                    break;
                case 3:
                    for (int i=0;i<3;i++){
                        View view=LayoutInflater.from(this).inflate(R.layout.viewpager_itempage,null);
                        ImageView ive=(ImageView)view.findViewById(R.id.imageViewda);
                        Glide.with(this).load(http[i]).into(ive);
                        list.add(view);
                    }
                   adapter=new FangDaAdapter(list);
                    break;
            }
        }else {
            View view=LayoutInflater.from(this).inflate(R.layout.viewpager_itempage,null);
            ImageView ive=(ImageView)view.findViewById(R.id.imageViewda);
            Glide.with(this).load(url).into(ive);
            list.add(ive);
            adapter=new FangDaAdapter(list);
        }
        viewpop.setAdapter(adapter);
        viewpop.setCurrentItem(yeshu);
    }
    class  MyPageListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            switch (position){
                case 0:
                    viewpop.setCurrentItem(0);
                    break;
                case 1:
                    viewpop.setCurrentItem(1);
                    break;
                case 2:
                    viewpop.setCurrentItem(2);
                    break;
            }
        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    viewpop.setCurrentItem(0);
                    break;
                case 1:
                    viewpop.setCurrentItem(1);
                    break;
                case 2:
                    viewpop.setCurrentItem(2);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
