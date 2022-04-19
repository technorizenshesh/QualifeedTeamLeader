package com.qualifeed.teamleader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.qualifeed.teamleader.adapter.ScrapAdapter;
import com.qualifeed.teamleader.databinding.ActivityScrapBinding;
import com.qualifeed.teamleader.model.DashBoradModel;
import com.qualifeed.teamleader.model.ScrapModel;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrapListAct extends AppCompatActivity {
    public String TAG = "ScrapListAct";
    ActivityScrapBinding binding;
    TeamLeadInterface apiInterface;
    ScrapAdapter adapter;
    ArrayList<ScrapModel.Result>arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_scrap);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new ScrapAdapter(ScrapListAct.this,arrayList);
        binding.rvScrap.setAdapter(adapter);

        if(NetworkAvailablity.checkNetworkStatus(ScrapListAct.this)) scrapListData();
        else Toast.makeText(ScrapListAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

    }


    private void scrapListData() {
        DataManager.getInstance().showProgressMessage(ScrapListAct.this, getString(R.string.please_wait));
        Call<ScrapModel> loginCall = apiInterface.getAllScrap();
        loginCall.enqueue(new Callback<ScrapModel>() {
            @Override
            public void onResponse(Call<ScrapModel> call, Response<ScrapModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    ScrapModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Scrap Dashboard Response :" + responseString);
                    if (data.status.equals("1")) {
                      arrayList.clear();
                      arrayList.addAll(data.result);
                      adapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ScrapModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}
