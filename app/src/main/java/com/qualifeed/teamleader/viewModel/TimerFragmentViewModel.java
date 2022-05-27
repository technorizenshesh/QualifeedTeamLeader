package com.qualifeed.teamleader.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.qualifeed.teamleader.model.ProductTypeModel;
import com.qualifeed.teamleader.model.TeamListModel;
import com.qualifeed.teamleader.model.TimerModel;
import com.qualifeed.teamleader.repository.TimerFregRepository;

import okhttp3.ResponseBody;

public class TimerFragmentViewModel  extends AndroidViewModel {
    TimerFregRepository timerFregRepository;
    private LiveData<TeamListModel> teamListModelLiveData;
    private LiveData<ProductTypeModel> productTypeModelLiveData;
    private LiveData<TimerModel> responseBodyLiveData;


    private Context context;

    public TimerFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(Context context){
        this.context = context;
        timerFregRepository = new TimerFregRepository(context);
        teamListModelLiveData = timerFregRepository.getAllTeamList();
        productTypeModelLiveData = timerFregRepository.getAllProductTypeRepo();
        responseBodyLiveData = timerFregRepository.getAllTimerDataRepo();



    }


    public void teamListViewModel (String teamLeadId){
        timerFregRepository.teamList(teamLeadId);
    }

    public LiveData<TeamListModel> getTeamListAllViewModel(){
        return teamListModelLiveData;
    }



    public void productTypeRepoViewModel(){ timerFregRepository.getProductTypeRepo(); }

    public LiveData<ProductTypeModel> getProductTypeAllRepoViewModel(){ return productTypeModelLiveData; }


    public void timerViewModel(String productType, String teamId, String date, String teamLeadId){ timerFregRepository.timerDataRepo(productType,teamId,date,teamLeadId); }

    public LiveData<TimerModel> getTimerRpoViewModel(){ return responseBodyLiveData; }



}

