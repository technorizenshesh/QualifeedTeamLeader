package com.qualifeed.teamleader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.qualifeed.teamleader.databinding.ActivitySplashBinding;
import com.qualifeed.teamleader.utils.DataManager;

public class SplashAct extends AppCompatActivity {
    ActivitySplashBinding binding;
    public static int SPLASH_TIME_OUT = 3000;
    int PERMISSION_ID = 44;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        processNextActivity();
    }

    private void processNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (DataManager.getInstance().getUserData(getApplicationContext()) != null &&
                        DataManager.getInstance().getUserData(getApplicationContext()).result != null &&
                        !DataManager.getInstance().getUserData(getApplicationContext()).result.id.equals("")) {
                    startActivity(new Intent(SplashAct.this, DashboardAct.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashAct.this, LoginAct.class));
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }


}