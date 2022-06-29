package com.qualifeed.teamleader;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.qualifeed.teamleader.adapter.MyAdapter;
import com.qualifeed.teamleader.adapter.MyAdapter2;
import com.qualifeed.teamleader.databinding.ActivityBlockedBinding;

public class BlockedAct extends AppCompatActivity {
    ActivityBlockedBinding binding;
    public static String type1="",type2="",date="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_blocked);
        initViews();
    }

    private void initViews() {
        if(getIntent()!=null){
            type1 = getIntent().getStringExtra("type1");
            type2 = getIntent().getStringExtra("type2");
            date = getIntent().getStringExtra("date");

        }

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Suspect Defect"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Blocked"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.viewPager.setAdapter(new MyAdapter2(BlockedAct.this,getSupportFragmentManager(), binding.tabLayout.getTabCount()));
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
