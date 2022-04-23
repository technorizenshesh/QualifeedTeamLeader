package com.qualifeed.teamleader;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qualifeed.teamleader.databinding.ActivityWorkerProfileBinding;
import com.qualifeed.teamleader.model.WorkerModel;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerProfileAct extends AppCompatActivity {
    public String TAG = "WorkerProfileAct";
    ActivityWorkerProfileBinding binding;
    TeamLeadInterface apiInterface;
    String workerId="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_worker_profile);
        initViews();
    }

    private void initViews() {
        if(getIntent()!=null) {
            workerId  = getIntent().getStringExtra("worker_id");
            if(NetworkAvailablity.checkNetworkStatus(WorkerProfileAct.this)) getWorkerDetail(workerId);
            else Toast.makeText(WorkerProfileAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }




    private void getWorkerDetail(String workerId) {
        DataManager.getInstance().showProgressMessage(WorkerProfileAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("worker_id",workerId);
        Log.e(TAG, "Worker Details Request :" + map);
        Call<WorkerModel> loginCall = apiInterface.getWorkerDetail(map);
        loginCall.enqueue(new Callback<WorkerModel>() {
            @Override
            public void onResponse(Call<WorkerModel> call, Response<WorkerModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    WorkerModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Worker Details Response :" + responseString);
                    if (data.status.equals("1")) {
                        binding.tvName.setText("" + data.result.userName);
                        binding.tvType.setText("" + data.result.type);
                        binding.tvWorkerId.setText("WorkerID : " + data.result.id);
                     //   binding.tvControl.setText("Piece to control : " );
                      //  binding.tvSession.setText("Control Session Time : " );
                     //   binding.tvReportTime.setText("Repair Time : " );

                        Glide.with(WorkerProfileAct.this).load(data.result.image)
                                .override(60,60)
                                .error(R.drawable.dummy)
                                .into(binding.ivUser);

                    } else if (data.status.equals("0")) {
                        Toast.makeText(WorkerProfileAct.this, data.message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WorkerModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}
