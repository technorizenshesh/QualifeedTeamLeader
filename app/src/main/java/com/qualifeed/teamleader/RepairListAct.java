package com.qualifeed.teamleader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.qualifeed.teamleader.adapter.RepairAdapter;
import com.qualifeed.teamleader.adapter.ScrapAdapter;
import com.qualifeed.teamleader.adapter.SuspectAdapter;
import com.qualifeed.teamleader.databinding.ActivityRepairBinding;
import com.qualifeed.teamleader.model.RepairModel;
import com.qualifeed.teamleader.model.ScrapModel;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairListAct extends AppCompatActivity {
    public String TAG = "RepairListAct";
    ActivityRepairBinding binding;
    TeamLeadInterface apiInterface;
    RepairAdapter adapter;
    ArrayList<RepairModel.Result>arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_repair);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new RepairAdapter(RepairListAct.this,arrayList);
        binding.rvRepair.setAdapter(adapter);

        if(NetworkAvailablity.checkNetworkStatus(RepairListAct.this)) repairListData();
        else Toast.makeText(RepairListAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

    }


    private void repairListData() {
        DataManager.getInstance().showProgressMessage(RepairListAct.this, getString(R.string.please_wait));
        Call<RepairModel> loginCall = apiInterface.getAllRepair();
        loginCall.enqueue(new Callback<RepairModel>() {
            @Override
            public void onResponse(Call<RepairModel> call, Response<RepairModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    RepairModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Repair defect Response :" + responseString);
                    if (data.status.equals("1")) {
                        binding.tvNotFound.setVisibility(View.GONE);

                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvNotFound.setVisibility(View.VISIBLE);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RepairModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}
