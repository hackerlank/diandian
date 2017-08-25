package com.dingli.diandiaan;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.DianTool;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2015/9/16.
 */
public class YiDaoTuActivity  extends FragmentActivity {
    ViewPager viewPageryidaotu;
    private List<Fragment> data;
    Bitmap[] bm=new Bitmap[4];
    ViewPagerAdapter adapter;
    ImageView ivguideone,ivguidetwo,ivguidethree,ivguidefour;
    ImageView[] guide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yidaotu);
        init();
    }
    void init(){
        viewPageryidaotu=(ViewPager)findViewById(R.id.viewPageryidaotu);
        ivguideone=(ImageView) findViewById(R.id.ivguideone);
        ivguidetwo=(ImageView) findViewById(R.id.ivguidetwo);
        ivguidethree=(ImageView) findViewById(R.id.ivguithree);
        ivguidefour=(ImageView) findViewById(R.id.ivguidefour);
        ivguideone.setBackgroundResource(R.mipmap.dot_selected);
        ivguidetwo.setBackgroundResource(R.mipmap.dot_unselected);
        ivguidethree.setBackgroundResource(R.mipmap.dot_unselected);
        ivguidefour.setBackgroundResource(R.mipmap.dot_unselected);
        guide=new ImageView[]{ivguideone,ivguidetwo,ivguidethree,ivguidefour};
        data=getdata();
        adapter=
                new ViewPagerAdapter(
                        getSupportFragmentManager());
        viewPageryidaotu.setAdapter(adapter);
        viewPageryidaotu.setOnPageChangeListener(new PageChangListener());
    }
    private List<Fragment> getdata(){
        List<Fragment> list=new ArrayList<Fragment>();
        list.add(new Fragment1());
        list.add(new Fragment2());
        list.add(new Fragment3());
        list.add(new Fragment4());
        bm[0]= DianTool.readBitMap(this,R.mipmap.teaone);
        bm[1]= DianTool.readBitMap(this,R.mipmap.teatwo);
        bm[2]= DianTool.readBitMap(this,R.mipmap.teathree);
        bm[3]= DianTool.readBitMap(this,R.mipmap.teafour);
        return list;
    }
    class PageChangListener
            implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(
                int position) {
          switch (position){
              case 0:
                  selected(0);
                  break;
              case 1:
                  selected(1);
                  break;
              case 2:
                  selected(2);
                  break;
              case 3:
                  selected(3);
                  break;
          }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
    void selected(int  positon){
        for (int i=0;i<guide.length;i++){
            if (i==positon){
                guide[i].setBackgroundResource(R.mipmap.dot_selected);
            }else{
                guide[i].setBackgroundResource(R.mipmap.dot_unselected);
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                YiDaoTuActivity.this.finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPageryidaotu=null;
        data=null;
        adapter=null;
        for (int i=0;i<bm.length;i++){
            bm[i].recycle();
            bm[i]=null;
        }
        System.gc();
    }
}
