package com.qualifeed.teamleader.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.qualifeed.teamleader.BlockedAct;
import com.qualifeed.teamleader.DashboardAct;
import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.RepairListAct;
import com.qualifeed.teamleader.ScrapListAct;
import com.qualifeed.teamleader.databinding.FragmentProductBinding;
import com.qualifeed.teamleader.model.DashBoradModel;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {
    public static String TAG = "ProductFragment";
    private static FragmentProductBinding binding;
    private static TeamLeadInterface apiInterface;
    private static Context context;
   public static String type1="",type2="",date="";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);

        if (NetworkAvailablity.checkNetworkStatus(getActivity())) getDashBoradData("", "");
        else
            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.layoutBlock.setOnClickListener(v -> startActivity(new Intent(getActivity(), BlockedAct.class)
                .putExtra("type1",type1)
                .putExtra("type2",type2)
                .putExtra("date",date))
        );

        binding.layoutScrap.setOnClickListener(v -> startActivity(new Intent(getActivity(), ScrapListAct.class)));

        binding.layoutRepair.setOnClickListener(v -> startActivity(new Intent(getActivity(), RepairListAct.class)));


    }

    private static void getDashBoradData(String productTypeId, String date) {
        DataManager.getInstance().showProgressMessage((Activity) context, context.getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("date", "2022-04-05");
        //  map.put("product_type",productTypeId);
        Log.e(TAG, "Get Dashboard Request : " + map.toString());
        Call<DashBoradModel> loginCall = apiInterface.getDashData(map);
        loginCall.enqueue(new Callback<DashBoradModel>() {
            @Override
            public void onResponse(Call<DashBoradModel> call, Response<DashBoradModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    DashBoradModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Get Dashboard Response :" + responseString);
                    if (data.status.equals("1")) {
                        binding.tvNotFound.setVisibility(View.GONE);
                        binding.layoutOne.setVisibility(View.VISIBLE);
                        binding.layoutTwo.setVisibility(View.VISIBLE);

                        binding.tvChecked.setText(data.result.totalChecked);
                        binding.tvRepair.setText(data.result.totalProductRepair);
                        binding.tvScrap.setText(data.result.totalScrap);
                        binding.tvBlocked.setText(data.result.totalBlocked);


                    } else if (data.status.equals("0")) {
                        binding.tvNotFound.setVisibility(View.VISIBLE);
                        binding.layoutOne.setVisibility(View.GONE);
                        binding.layoutTwo.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DashBoradModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public static void ProductTab(String Protype1,String Protype2, String date11) {
        type1 = Protype1;
        type2 = Protype2;
        date = date11;

        if (NetworkAvailablity.checkNetworkStatus(context)) getDashBoradData("", "");
        else
            Toast.makeText(context, context.getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

    }

}
