package com.qualifeed.teamleader.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.qualifeed.teamleader.fragment.DefectFragment;
import com.qualifeed.teamleader.fragment.ProductFragment;
import com.qualifeed.teamleader.fragment.TimerFragment;

public class MyAdapter  extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ProductFragment productFragment = new ProductFragment();
                return productFragment;
            case 1:
                DefectFragment defectFragment = new DefectFragment();
                return defectFragment;

            case 2:
                TimerFragment timerFragment = new TimerFragment();
                return timerFragment;

                default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
