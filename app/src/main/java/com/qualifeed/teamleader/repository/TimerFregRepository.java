package com.qualifeed.teamleader.repository;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.model.ProductTypeModel;
import com.qualifeed.teamleader.model.TeamListModel;
import com.qualifeed.teamleader.model.TimerModel;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.utils.DataManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerFregRepository {
    public static final String TAG = "TimerFregRepository";
    private TeamLeadInterface apiInterface;
    Context context;
    private MutableLiveData<TeamListModel> teamListModelMutableLiveData;
    private MutableLiveData<ProductTypeModel> productTypeModelMutableLiveData;
    private MutableLiveData<TimerModel> timeBodyMutableLiveData;


    public TimerFregRepository(Context context) {
        this.context = context;
        teamListModelMutableLiveData = new MutableLiveData<>();
        productTypeModelMutableLiveData = new MutableLiveData<>();
        timeBodyMutableLiveData = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
    }


    public void teamList(String teamLeadId) {
        DataManager.getInstance().showProgressMessage((Activity) context, context.getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", teamLeadId);
        Log.e(TAG, "get Team Lisa Request " + map);
        apiInterface.getTeamListApiCall(map)
                .enqueue(new Callback<TeamListModel>() {
                    @Override
                    public void onResponse(Call<TeamListModel> call, Response<TeamListModel> response) {
                        DataManager.getInstance().hideProgressMessage();
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("1"))
                                teamListModelMutableLiveData.postValue(response.body());
                        } else if (response.body().getStatus().equals("0")) {
                            teamListModelMutableLiveData.postValue(null);
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TeamListModel> call, Throwable t) {
                        DataManager.getInstance().hideProgressMessage();
                        teamListModelMutableLiveData.postValue(null);
                    }
                });


    }


    public LiveData<TeamListModel> getAllTeamList() {
        return teamListModelMutableLiveData;
    }


    public void getProductTypeRepo() {
        DataManager.getInstance().showProgressMessage((Activity) context, context.getString(R.string.please_wait));
        apiInterface.getProductType()
                .enqueue(new Callback<ProductTypeModel>() {
                    @Override
                    public void onResponse(Call<ProductTypeModel> call, Response<ProductTypeModel> response) {
                        DataManager.getInstance().hideProgressMessage();
                        if (response.body() != null) {
                            if (response.body().status.equals("1"))
                                productTypeModelMutableLiveData.postValue(response.body());
                        } else if (response.body().status.equals("0")) {
                            productTypeModelMutableLiveData.postValue(null);
                            Toast.makeText(context, response.body().message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductTypeModel> call, Throwable t) {
                        DataManager.getInstance().hideProgressMessage();
                        productTypeModelMutableLiveData.postValue(null);
                    }
                });
    }


    public LiveData<ProductTypeModel> getAllProductTypeRepo() {
        return productTypeModelMutableLiveData;
    }


    public void timerDataRepo(String productType, String teamId, String date, String teamLeadId) {
        DataManager.getInstance().showProgressMessage((Activity) context, context.getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("pro_id", productType);
        map.put("team_id", teamId);
        map.put("date", date);
        map.put("teamleader_id", teamLeadId);
        Log.e(TAG, "get Timer Request " + map);
        apiInterface.timerApiCall(map)
                .enqueue(new Callback<TimerModel>() {
                    @Override
                    public void onResponse(Call<TimerModel> call, Response<TimerModel> response) {
                        DataManager.getInstance().hideProgressMessage();
                        try {
                            if (response.body() != null) {
                                String responseData =  new Gson().toJson(response.body(),TimerModel.class)   ;// != null ? response.body().toString() : "";
                                Log.e("Accept Reject Response", responseData);
                                if (response.body().getStatus().equals("1")) {
                                    timeBodyMutableLiveData.postValue(response.body());
                                } else if (response.body().getStatus().equals("0")) {
                                    timeBodyMutableLiveData.postValue(null);
                                }

                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<TimerModel> call, Throwable t) {
                        DataManager.getInstance().hideProgressMessage();
                        timeBodyMutableLiveData.postValue(null);
                    }
                });


    }


    public LiveData<TimerModel> getAllTimerDataRepo() {
        return timeBodyMutableLiveData;
    }


}
