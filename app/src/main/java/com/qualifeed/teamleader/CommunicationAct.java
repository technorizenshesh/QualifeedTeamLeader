package com.qualifeed.teamleader;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.qualifeed.teamleader.adapter.MyAdapter2;
import com.qualifeed.teamleader.adapter.MyAdapter3;
import com.qualifeed.teamleader.databinding.ActivityCommunicationBinding;

public class CommunicationAct extends AppCompatActivity {
    ActivityCommunicationBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_communication);
        initViews();
    }

    private void initViews() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Inbox"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sent"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.viewPager.setAdapter(new MyAdapter3(CommunicationAct.this,getSupportFragmentManager(), binding.tabLayout.getTabCount()));
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

}
