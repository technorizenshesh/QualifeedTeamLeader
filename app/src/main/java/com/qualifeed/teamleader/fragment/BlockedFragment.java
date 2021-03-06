package com.qualifeed.teamleader.fragment;

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
import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.adapter.BlockedAdapter;
import com.qualifeed.teamleader.adapter.SuspectAdapter;
import com.qualifeed.teamleader.databinding.FragmentBlockedBinding;
import com.qualifeed.teamleader.databinding.FragmentSuspectBinding;
import com.qualifeed.teamleader.model.BlockedModel;
import com.qualifeed.teamleader.model.DefectModel;
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

public class BlockedFragment extends Fragment {
    public String TAG = "BlockedFragment";
    FragmentBlockedBinding binding;
    TeamLeadInterface apiInterface;
    BlockedAdapter adapter;
    ArrayList<BlockedModel.Result> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blocked, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new BlockedAdapter(getActivity(),arrayList);
        binding.rvDefect.setAdapter(adapter);

        if(NetworkAvailablity.checkNetworkStatus(getActivity())) blockedtListData();
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

    }


    private void blockedtListData() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String>map = new HashMap<>();
        map.put("date", DataManager.convertDateToString5(BlockedAct.date));
        map.put("product_type_1", BlockedAct.type1);
        map.put("product_type_2", BlockedAct.type2);
        Log.e(TAG, "Blocked Defect Request :" + map);

        Call<BlockedModel> loginCall = apiInterface.getAllBlocked(map);
        loginCall.enqueue(new Callback<BlockedModel>() {
            @Override
            public void onResponse(Call<BlockedModel> call, Response<BlockedModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    BlockedModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Blocked Defect Response :" + responseString);
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
            public void onFailure(Call<BlockedModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



}