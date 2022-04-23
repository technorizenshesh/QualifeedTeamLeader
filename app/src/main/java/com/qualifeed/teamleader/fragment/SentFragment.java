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
import com.qualifeed.teamleader.adapter.InboxAdapter;
import com.qualifeed.teamleader.adapter.SentAdapter;
import com.qualifeed.teamleader.databinding.FragmentInboxBinding;
import com.qualifeed.teamleader.model.InboxModel;
import com.qualifeed.teamleader.model.SentModel;
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

public class SentFragment extends Fragment {
    public String TAG = "SentFragment";
    FragmentInboxBinding binding;
    TeamLeadInterface apiInterface;
    SentAdapter adapter;
    ArrayList<SentModel.Result> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new SentAdapter(getActivity(),arrayList);
        binding.rvInbox.setAdapter(adapter);

        if(NetworkAvailablity.checkNetworkStatus(getActivity())) inboxListData();
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

    }


    private void inboxListData() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email","team@gmail.com"/*DataManager.getInstance().getUserData(getActivity()).result.email*/);
        Call<SentModel> loginCall = apiInterface.getAllSentMail(map);
        loginCall.enqueue(new Callback<SentModel>() {
            @Override
            public void onResponse(Call<SentModel> call, Response<SentModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SentModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Inbox List Response :" + responseString);
                    if (data.status.equals("1")) {
                        binding.tvNotFound.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        binding.tvNotFound.setVisibility(View.VISIBLE);
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SentModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}
