package com.qualifeed.teamleader.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.qualifeed.teamleader.fragment.BlockedFragment;
import com.qualifeed.teamleader.fragment.InboxFragment;
import com.qualifeed.teamleader.fragment.SentFragment;
import com.qualifeed.teamleader.fragment.SuspectFragment;

public class MyAdapter3 extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter3(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                InboxFragment inboxFragment = new InboxFragment();
                return inboxFragment;
            case 1:
                SentFragment sentFragment = new SentFragment();
                return sentFragment;

            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
