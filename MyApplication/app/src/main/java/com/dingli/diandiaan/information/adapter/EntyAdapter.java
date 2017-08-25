package com.dingli.diandiaan.information.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dingli.diandiaan.information.WeiDonFragment;
import com.dingli.diandiaan.information.YiBackFragment;
import com.dingli.diandiaan.information.YiDoneFragment;

/**
 * Created by dingliyuangong on 2016/8/1.
 */
public class EntyAdapter extends FragmentPagerAdapter{
    String[] enty={"未处理","已处理","已撤回"};
    public EntyAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                WeiDonFragment weiDonFragment=new WeiDonFragment();
                return weiDonFragment;

            case 1:
                YiDoneFragment yiDoneFragment=new YiDoneFragment();
                return yiDoneFragment;
            case 2:
                YiBackFragment yiBackFragment=new YiBackFragment();
                return yiBackFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return enty.length;
    }
}
