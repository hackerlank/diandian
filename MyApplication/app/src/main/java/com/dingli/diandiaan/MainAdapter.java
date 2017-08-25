package com.dingli.diandiaan;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dingli.diandiaan.contact.ContactFragment;
import com.dingli.diandiaan.firstpage.ShouyeFragment;
import com.dingli.diandiaan.information.InformationFragment;
import com.dingli.diandiaan.login.PersoncenterFragment;

/**
 * Created by dingliyuangong on 2016/11/11.
 */
public class MainAdapter extends FragmentPagerAdapter{
    public MainAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
//                HybridShouyeFragment shouyeFragment=HybridShouyeFragment.newInstance();
                ShouyeFragment shouyeFragment=ShouyeFragment.newInstance();
                return shouyeFragment;
            case 2:
                ContactFragment syllFragment=ContactFragment.newInstance();
                return  syllFragment;
            case 1:
                InformationFragment informationFragment=InformationFragment.newInstance();
                return  informationFragment;
            case 3:
                PersoncenterFragment personcenterFragment=PersoncenterFragment.newInstance();
                return  personcenterFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
