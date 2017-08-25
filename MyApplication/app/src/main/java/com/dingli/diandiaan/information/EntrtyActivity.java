package com.dingli.diandiaan.information;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.information.adapter.EntyAdapter;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class EntrtyActivity extends BaseActivity implements View.OnClickListener{

    TextView tvweidone,yitvdone,tvyiback;
    View vwweidone,vwyidone,vwyiback;
    EntyAdapter adapter;
    ViewPager vpinfom;
    ImageView entrtyback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrty);
        initview();
        adapter=new EntyAdapter(getSupportFragmentManager());
        vpinfom.setAdapter(adapter);
    }
    void initview(){
       tvweidone=(TextView)findViewById(R.id.tvweidone);
        yitvdone=(TextView)findViewById(R.id.yitvdone);
        tvyiback=(TextView)findViewById(R.id.tvyiback);
        vwweidone=findViewById(R.id.vwweidone);
        vwyidone=findViewById(R.id.vwyidone);
        vwyiback=findViewById(R.id.vwyiback);
        vpinfom=(ViewPager)findViewById(R.id.vpinfom);
        entrtyback=(ImageView)findViewById(R.id.entrtyback);
        entrtyback.setOnClickListener(this);
        tvweidone.setOnClickListener(this);
        yitvdone.setOnClickListener(this);
        tvyiback.setOnClickListener(this);
        vpinfom.setOffscreenPageLimit(2);
        vpinfom.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                 switch (position){
                     case 0:
                         weidone();
                         break;
                     case 1:
                         yidone();
                         break;
                     case 2:
                        yiback();
                         break;
                 }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.tvweidone:
              vpinfom.setCurrentItem(0);
              weidone();
              break;
          case R.id.yitvdone:
              vpinfom.setCurrentItem(1);
              yidone();
              break;
          case R.id.tvyiback:
              vpinfom.setCurrentItem(2);
              yiback();
              break;
          case R.id.entrtyback:
              this.finish();
              overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
              break;
      }
    }
    void weidone(){
        tvweidone.setTextColor(getResources().getColor(R.color.qianblue));
        vwweidone.setBackgroundColor(getResources().getColor(R.color.qianblue));
        yitvdone.setTextColor(getResources().getColor(R.color.xiangqin));
        vwyidone.setBackgroundColor(getResources().getColor(R.color.bg_White));
        tvyiback.setTextColor(getResources().getColor(R.color.xiangqin));
        vwyiback.setBackgroundColor(getResources().getColor(R.color.bg_White));
        tvweidone.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        yitvdone.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvyiback.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }
    void yidone(){
        tvweidone.setTextColor(getResources().getColor(R.color.xiangqin));
        vwweidone.setBackgroundColor(getResources().getColor(R.color.bg_White));
        yitvdone.setTextColor(getResources().getColor(R.color.qianblue));
        vwyidone.setBackgroundColor(getResources().getColor(R.color.qianblue));
        tvyiback.setTextColor(getResources().getColor(R.color.xiangqin));
        vwyiback.setBackgroundColor(getResources().getColor(R.color.bg_White));
        tvweidone.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        yitvdone.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvyiback.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }
    void yiback(){
        tvweidone.setTextColor(getResources().getColor(R.color.xiangqin));
        vwweidone.setBackgroundColor(getResources().getColor(R.color.bg_White));
        yitvdone.setTextColor(getResources().getColor(R.color.xiangqin));
        vwyidone.setBackgroundColor(getResources().getColor(R.color.bg_White));
        tvyiback.setTextColor(getResources().getColor(R.color.qianblue));
        vwyiback.setBackgroundColor(getResources().getColor(R.color.qianblue));
        tvweidone.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        yitvdone.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvyiback.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }
}
