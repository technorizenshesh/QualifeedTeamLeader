package com.qualifeed.teamleader;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.qualifeed.teamleader.adapter.RepairAdapter;
import com.qualifeed.teamleader.adapter.TeamAdapter;
import com.qualifeed.teamleader.databinding.ActivityRepairBinding;
import com.qualifeed.teamleader.databinding.ActivityTeamListBinding;
import com.qualifeed.teamleader.model.RepairModel;
import com.qualifeed.teamleader.model.TeamModel;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.retrofit.onPosListener;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamListAct extends AppCompatActivity implements onPosListener {
    public String TAG = "TeamListAct";
    ActivityTeamListBinding binding;
    TeamLeadInterface apiInterface;
    TeamAdapter adapter;
    ArrayList< TeamModel.Result> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_team_list);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new TeamAdapter(TeamListAct.this,arrayList,TeamListAct.this);
        binding.rvTeam.setAdapter(adapter);

        if(NetworkAvailablity.checkNetworkStatus(TeamListAct.this)) TeamListData();
        else Toast.makeText(TeamListAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }



    private void TeamListData() {
        DataManager.getInstance().showProgressMessage(TeamListAct.this, getString(R.string.please_wait));
        Map<String,String>map = new HashMap<>();
        map.put("team_leader_id",DataManager.getInstance().getUserData(TeamListAct.this).result.id);
        Log.e(TAG, "TeamList Request :" + map);
        Call<TeamModel> loginCall = apiInterface.getAllTeam(map);
        loginCall.enqueue(new Callback<TeamModel>() {
            @Override
            public void onResponse(Call<TeamModel> call, Response<TeamModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    TeamModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "TeamList Response :" + responseString);
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
            public void onFailure(Call<TeamModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }





    private void blockUnblockFun(String workerId,String status) {
        DataManager.getInstance().showProgressMessage(TeamListAct.this, getString(R.string.please_wait));
        Map<String,String>map = new HashMap<>();
        map.put("team_leader_id",DataManager.getInstance().getUserData(TeamListAct.this).result.id);
        map.put("worker_id",workerId);
        Log.e(TAG, "Block Worker Request :" + map);
        Call<Map<String,String>> loginCall = apiInterface.blockWorker(map);
        loginCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Block Worker Response :" + responseString);
                    if (data.get("status").equals("1")) {
                        if(NetworkAvailablity.checkNetworkStatus(TeamListAct.this)) TeamListData();
                        else Toast.makeText(TeamListAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

                    } else if (data.get("status").equals("0")) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }






    @Override
    public void onPos(int pos, String type) {
        if(type.equals("Block")) blockUnblockDialog(arrayList.get(pos).workerId,type,getString(R.string.do_you_wanna_block_this_profile));
        else  if(type.equals("Unblock")) blockUnblockDialog(arrayList.get(pos).workerId,type,getString(R.string.do_you_wanna_unblock_this_profile));
        else  if(type.equals("Next"))    startActivity(new Intent(TeamListAct.this,WorkerProfileAct.class)
        .putExtra("worker_id",arrayList.get(pos).workerId));

    }



    public void blockUnblockDialog(String workerId,String status,String msg){
        final Dialog dialog = new Dialog(TeamListAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_block_unblock);

        TextView tvMsg =  dialog.findViewById(R.id.text_dialog);
        tvMsg.setText(msg);

        TextView tvYes =  dialog.findViewById(R.id.tvYes);
        TextView tvNo =  dialog.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkAvailablity.checkNetworkStatus(TeamListAct.this)) blockUnblockFun(workerId,status);
                else Toast.makeText(TeamListAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }




}
