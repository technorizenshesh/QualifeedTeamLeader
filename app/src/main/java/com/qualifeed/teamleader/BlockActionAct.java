package com.qualifeed.teamleader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.qualifeed.teamleader.databinding.ActivityActionBlockBinding;
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

public class BlockActionAct extends AppCompatActivity {
    public String TAG = "BlockActionAct";
    ActivityActionBlockBinding binding;
    TeamLeadInterface apiInterface;
    String id="",name="",image="",teamName="",type="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_action_block);
        initViews();
    }

    private void initViews() {
        if(getIntent()!=null){
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            teamName = getIntent().getStringExtra("team");
            image = getIntent().getStringExtra("image");
            binding.tvName.setText("" +name );
            binding.tvType.setText("" + teamName);
            binding.tvWorkerId.setText("WorkerID : " + id);
            Glide.with(BlockActionAct.this).load(image)
                    .override(60,60)
                    .error(R.drawable.dummy)
                    .into(binding.ivUser);


        }

        binding.btnBlock.setOnClickListener(v -> {
            if(NetworkAvailablity.checkNetworkStatus(BlockActionAct.this)) WorkerBlockByTeamLeader();
            else Toast.makeText(BlockActionAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        });
    }


    private void WorkerBlockByTeamLeader() {
        DataManager.getInstance().showProgressMessage(BlockActionAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("worker_id",id);
        map.put("team_leader_id",DataManager.getInstance().getUserData(BlockActionAct.this).result.id);
        map.put("comment",binding.edComment.getText().toString());
        map.put("status","Block");
        Log.e(TAG, "Worker Block  Request :" + map);
        Call<ResponseBody> loginCall = apiInterface.workerBlockedApiCall(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    Log.e(TAG, "Worker Block Response :" + responseData);
                    JSONObject data = new JSONObject(responseData);
                    if (data.get("status").equals("1")) {
                        Toast.makeText(BlockActionAct.this,getText(R.string.block_worker), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BlockActionAct.this, BlockedAct.class)
                                .putExtra("date", BlockedAct.date)
                                .putExtra("type1", BlockedAct.type1)
                                .putExtra("type2", BlockedAct.type2)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));                        finish();
                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(BlockActionAct.this, data.getJSONObject("result").getString("message"), Toast.LENGTH_SHORT).show();
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
