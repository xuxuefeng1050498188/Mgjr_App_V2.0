package com.mgjr.mangofinance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by wim on 16/5/23.
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter{

    private List<Fragment> fragmentList;

    public MainViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList){
        super(fragmentManager);
        this.fragmentList = fragmentList;
    }

    public void setFragment(List<Fragment> fragmentlist) {
        this.fragmentList = fragmentlist;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
//        Log.d("size==",String.valueOf(fragmentList.size()));
        return fragmentList.size();
    }
}
