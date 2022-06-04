package com.qualifeed.teamleader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qualifeed.teamleader.databinding.ActivityActionBinding;
import com.qualifeed.teamleader.databinding.ActivityWorkerProfileBinding;
import com.qualifeed.teamleader.model.WorkerModel;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionAct extends AppCompatActivity {
    public String TAG = "ActionAct";
    ActivityActionBinding binding;
    TeamLeadInterface apiInterface;
    String workerId="",id="",name="",image="",teamName="",type="",productId="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_action);
        initViews();
    }

    private void initViews() {
        if(getIntent()!=null) {
            workerId  = getIntent().getStringExtra("worker_id");
            if(NetworkAvailablity.checkNetworkStatus(ActionAct.this)) getWorkerDetail(workerId);
            else Toast.makeText(ActionAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }


        binding.btnBlock.setOnClickListener(v -> {
            startActivity(new Intent(ActionAct.this,BlockActionAct.class)
            .putExtra("id",id).putExtra("name",name).putExtra("team",teamName).putExtra("image",image));
        });


        binding.btnTraining.setOnClickListener(v -> {
            startActivity(new Intent(ActionAct.this,TrainingActionAct.class)
                    .putExtra("id",id).putExtra("name",name).putExtra("team",teamName).putExtra("image",image).putExtra("product_id",productId));
        });




    }




    private void getWorkerDetail(String workerId) {
        DataManager.getInstance().showProgressMessage(ActionAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("worker_id",workerId);
        Log.e(TAG, "Worker Details Request :" + map);
        Call<ResponseBody> loginCall = apiInterface.workerDetailApiCall(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    Log.e(TAG, "Worker Details Response :" + responseData);
                    JSONObject data = new JSONObject(responseData);

                    if (data.get("status").equals("1")) {

                        name = data.getJSONObject("result").getString("name");
                        image = data.getJSONObject("result").getString("image");
                        teamName = data.getJSONObject("result").getString("team_name");
                        id = data.getJSONObject("result").getString("id");
                        type = data.getJSONObject("result").getString("product_type");
                        productId = data.getJSONObject("result").getString("product_id");


                        binding.tvName.setText("" +name );
                        binding.tvType.setText("" + type);
                        binding.tvWorkerId.setText("WorkerID : " + id);
                        //   binding.tvControl.setText("Piece to control : " );
                        //  binding.tvSession.setText("Control Session Time : " );
                        //   binding.tvReportTime.setText("Repair Time : " );

                        Glide.with(ActionAct.this).load(image)
                                .override(60,60)
                                .error(R.drawable.dummy)
                                .into(binding.ivUser);

                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(ActionAct.this, data.getJSONObject("result").getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}
