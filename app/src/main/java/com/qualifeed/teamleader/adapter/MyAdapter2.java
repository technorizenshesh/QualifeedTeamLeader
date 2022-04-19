package com.qualifeed.teamleader.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.qualifeed.teamleader.fragment.BlockedFragment;
import com.qualifeed.teamleader.fragment.DefectFragment;
import com.qualifeed.teamleader.fragment.ProductFragment;
import com.qualifeed.teamleader.fragment.SuspectFragment;
import com.qualifeed.teamleader.fragment.TimerFragment;

public class MyAdapter2  extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter2(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SuspectFragment suspectFragment = new SuspectFragment();
                return suspectFragment;
            case 1:
                BlockedFragment blockedFragment = new BlockedFragment();
                return blockedFragment;

                default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
