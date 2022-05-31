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
import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.RepairListAct;
import com.qualifeed.teamleader.adapter.RepairAdapter;
import com.qualifeed.teamleader.adapter.SuspectAdapter;
import com.qualifeed.teamleader.databinding.FragmentSuspectBinding;
import com.qualifeed.teamleader.model.DefectModel;
import com.qualifeed.teamleader.model.RepairModel;
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

public class SuspectFragment extends Fragment {
    public String TAG = "SuspectFragment";
    FragmentSuspectBinding binding;
    TeamLeadInterface apiInterface;
    SuspectAdapter adapter;
    ArrayList<DefectModel.Result> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_suspect, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        initViews();


    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new SuspectAdapter(getActivity(),arrayList);
        binding.rvDefect.setAdapter(adapter);



    }


    private void suspectListData() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("date","2022-06-06");
        Call<DefectModel> loginCall = apiInterface.getAllDefect(map);
        loginCall.enqueue(new Callback<DefectModel>() {
            @Override
            public void onResponse(Call<DefectModel> call, Response<DefectModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    DefectModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Suspect Defect Response :" + responseString);
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
            public void onFailure(Call<DefectModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if(NetworkAvailablity.checkNetworkStatus(getActivity())) suspectListData();
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }
}
