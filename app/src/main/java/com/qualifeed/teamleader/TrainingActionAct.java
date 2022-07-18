package com.qualifeed.teamleader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qualifeed.teamleader.adapter.TrainingAdapter;
import com.qualifeed.teamleader.adapter.TypeAdapter;
import com.qualifeed.teamleader.databinding.ActivityActionTrainingBinding;
import com.qualifeed.teamleader.model.ProductTypeModel;
import com.qualifeed.teamleader.model.TrainingModel;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingActionAct extends AppCompatActivity {
    public String TAG = "TrainingActionAct";
    ActivityActionTrainingBinding binding;
    TeamLeadInterface apiInterface;
    String id = "", name = "", image = "", teamName = "", productId = "", trainingId = "";
    TrainingAdapter adapter;
    ArrayList<TrainingModel.Result> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_action_training);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new TrainingAdapter(TrainingActionAct.this, arrayList);
        binding.spinnerType.setAdapter(adapter);


        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            teamName = getIntent().getStringExtra("team");
            image = getIntent().getStringExtra("image");
            productId = getIntent().getStringExtra("product_id");

            binding.tvName.setText("" + name);
            binding.tvType.setText("" + teamName);
            binding.tvWorkerId.setText("WorkerID : " + id);
            Glide.with(TrainingActionAct.this).load(image)
                    .override(60, 60)
                    .error(R.drawable.dummy)
                    .into(binding.ivUser);
            if (NetworkAvailablity.checkNetworkStatus(TrainingActionAct.this))
                getTrainingList(productId);
            else
                Toast.makeText(TrainingActionAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();


        }

        binding.btnTraining.setOnClickListener(v -> {
            if (trainingId.equals(""))
                Toast.makeText(TrainingActionAct.this, getString(R.string.please_select_training_type), Toast.LENGTH_SHORT).show();
            else {
                if (NetworkAvailablity.checkNetworkStatus(TrainingActionAct.this))
                    WorkerTrainingByTeamLeader();
                else
                    Toast.makeText(TrainingActionAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }
        });






        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (arrayList.size() > 0) trainingId = arrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void getTrainingList(String productId) {
        DataManager.getInstance().showProgressMessage(TrainingActionAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
       // map.put("product_id", productId);
        Log.e(TAG, "Get Training List Request " + map);
        Call<TrainingModel> loginCall = apiInterface.getTrainingApiCall();
        loginCall.enqueue(new Callback<TrainingModel>() {
            @Override
            public void onResponse(Call<TrainingModel> call, Response<TrainingModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    TrainingModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Get Training List Response " + responseString);
                    if (data.getStatus().equals("1")) {
                        arrayList.clear();
                        arrayList.addAll(data.getResult());
                        trainingId = data.getResult().get(0).getId();
                        adapter.notifyDataSetChanged();
                    } else if (data.getStatus().equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TrainingModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void WorkerTrainingByTeamLeader() {
        DataManager.getInstance().showProgressMessage(TrainingActionAct.this, getString(R.string.please_wait));
            Map<String,String> map = new HashMap<>();
            map.put("worker_id",id);
            map.put("teamleader_id",DataManager.getInstance().getUserData(TrainingActionAct.this).result.id);
            map.put("training_id",trainingId);
            Log.e(TAG, "Worker Training  Request :" + map);
            Call<ResponseBody> loginCall = apiInterface.workerTrainingApiCall(map);
            loginCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    DataManager.getInstance().hideProgressMessage();
                    try {
                        String responseData = response.body() != null ? response.body().string() : "";
                        Log.e(TAG, "Worker Training Response :" + responseData);
                        JSONObject data = new JSONObject(responseData);
                        if (data.get("status").equals("1")) {
                            Toast.makeText(TrainingActionAct.this,getText(R.string.sent_training), Toast.LENGTH_SHORT).show();
                              startActivity(new Intent(TrainingActionAct.this, BlockedAct.class)
                                .putExtra("date", BlockedAct.date)
                                .putExtra("type1", BlockedAct.type1)
                                .putExtra("type2", BlockedAct.type2)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        } else if (data.get("status").equals("0")) {
                            Toast.makeText(TrainingActionAct.this, data.getJSONObject("result").getString("message"), Toast.LENGTH_SHORT).show();
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
