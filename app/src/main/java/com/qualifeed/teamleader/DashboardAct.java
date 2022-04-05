package com.qualifeed.teamleader;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.qualifeed.teamleader.adapter.MyAdapter;
import com.qualifeed.teamleader.databinding.ActivityDashboradBinding;
import com.qualifeed.teamleader.utils.SessionManager;

public class DashboardAct extends AppCompatActivity {
    ActivityDashboradBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dashborad);
        initViews();
    }

    private void initViews() {


        binding.btnLogout.setOnClickListener(v -> SessionManager.clearsession(DashboardAct.this));

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Product"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Defect"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Timer"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.viewPager.setAdapter(new MyAdapter(DashboardAct.this,getSupportFragmentManager(), binding.tabLayout.getTabCount()));
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
