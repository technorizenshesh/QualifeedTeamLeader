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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qualifeed.teamleader.databinding.ActivitySuspectDetailsBinding;
import com.qualifeed.teamleader.model.DefectModel;
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

public class SuspectDetailsAct extends AppCompatActivity {
    public String TAG = "SuspectDetailsAct";
    ActivitySuspectDetailsBinding binding;
    DefectModel.Result result;
    TeamLeadInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_suspect_details);
        initViews();
    }

    private void initViews() {
        if (getIntent() != null) {
            result = (DefectModel.Result) getIntent().getSerializableExtra("suspect_defect");
            binding.tvTitle.setText(result.comment);
            binding.tvProductId.setText("ProductRef : " + result.productRef);
            binding.tvDate.setText("Date : " + result.dateTime);
            binding.tvWorkerId.setText("Worker Id : " + result.workerId);
            binding.tvDiscription.setText(result.comment);

            Glide.with(SuspectDetailsAct.this)
                    .load(result.image)
                    .override(150, 150)
                    .placeholder(R.drawable.dummy)
                    .error(R.drawable.dummy)
                    .into(binding.ivImg);


        }


        binding.ivCancel.setOnClickListener(v -> {
            startActivity(new Intent(SuspectDetailsAct.this, ActionAct.class).putExtra("worker_id", result.workerId)
                    .putExtra("product_ref",result.productRef)
                    .putExtra("type1",result.productType1)
                    .putExtra("type2",result.productType2));
        });

        binding.ivAccept.setOnClickListener(v -> blockUnblockDialog(result.id, "Do you wanna add this defect to the defect list"));
    }


    public void blockUnblockDialog(String id, String msg) {
        final Dialog dialog = new Dialog(SuspectDetailsAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_block_unblock);

        TextView tvMsg = dialog.findViewById(R.id.text_dialog);
        tvMsg.setText(msg);

        TextView tvYes = dialog.findViewById(R.id.tvYes);
        TextView tvNo = dialog.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkAvailablity.checkNetworkStatus(SuspectDetailsAct.this))
                    blockUnblockFun(id);
                else
                    Toast.makeText(SuspectDetailsAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
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


    public void blockUnblockFun(String id) {
        DataManager.getInstance().showProgressMessage(SuspectDetailsAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("suspect_id", id);
        map.put("team_leader_id", DataManager.getInstance().getUserData(SuspectDetailsAct.this).result.id);
        Call<ResponseBody> loginCall = apiInterface.addSuspectDefectApiCall(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    //    String responseString = new Gson().toJson(response.body());
                    JSONObject obj = new JSONObject(responseData);
                    Log.e(TAG, "Suspect Defect Response :" + responseData);
                    if (obj.get("status").equals("1")) {
                     /*   startActivity(new Intent(SuspectDetailsAct.this, BlockedAct.class)
                                .putExtra("date", BlockedAct.date)
                                .putExtra("type1", BlockedAct.type1)
                                .putExtra("type2", BlockedAct.type2)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));*/
                        finish();
                    } else if (obj.get("status").equals("0")) {
                        Toast.makeText(SuspectDetailsAct.this, "not added", Toast.LENGTH_SHORT).show();
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
