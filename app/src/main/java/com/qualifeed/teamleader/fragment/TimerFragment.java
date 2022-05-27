package com.qualifeed.teamleader.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.qualifeed.teamleader.DashboardAct;
import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.adapter.TeamListAdapter;
import com.qualifeed.teamleader.adapter.TypeAdapter;
import com.qualifeed.teamleader.databinding.FragmentTimerBinding;
import com.qualifeed.teamleader.model.ProductTypeModel;
import com.qualifeed.teamleader.model.TeamListModel;
import com.qualifeed.teamleader.model.TimerModel;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;
import com.qualifeed.teamleader.viewModel.TimerFragmentViewModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;

public class TimerFragment extends Fragment {
    private static final String TAG = TimerFragment.class.getSimpleName();
    FragmentTimerBinding binding;
    TimerFragmentViewModel timerFragmentViewModel;
    ArrayList<ProductTypeModel.Result> productTypeArrayList;
    ArrayList<TeamListModel.Result> teamArrayList;
    TypeAdapter adapter;
    TeamListAdapter teamListAdapter;
    String productTypeId ="",teamId="",date="";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timerFragmentViewModel = ViewModelProviders.of(getActivity()).get(TimerFragmentViewModel.class);
        timerFragmentViewModel.init(getActivity());



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productTypeArrayList = new ArrayList<>();
        teamArrayList = new ArrayList<>();

        teamListAdapter = new TeamListAdapter(getActivity(),teamArrayList);
        binding.spinnerTeam.setAdapter(teamListAdapter);

        adapter = new TypeAdapter(getActivity(),productTypeArrayList);
        binding.spinnerType.setAdapter(adapter);


        timerFragmentViewModel.getTeamListAllViewModel().observe(getActivity(), new Observer<TeamListModel>() {
            @Override
            public void onChanged(TeamListModel teamListModel) {
                if (teamListModel != null) {
                    if (teamListModel.getStatus().equals("1")) {
                        teamArrayList.clear();
                        teamArrayList.addAll(teamListModel.getResult());
                        teamId  = teamListModel.getResult().get(0).getId();
                        teamListAdapter.notifyDataSetChanged();
                    } else {
                        teamArrayList.clear();
                        teamArrayList.addAll(null);
                        teamListAdapter.notifyDataSetChanged();

                    }
                }
            }
        });


        timerFragmentViewModel.getProductTypeAllRepoViewModel().observe(getActivity(), new Observer<ProductTypeModel>() {
            @Override
            public void onChanged(ProductTypeModel productTypeModel) {
                if (productTypeModel != null) {
                    if (productTypeModel.status.equals("1")) {
                        productTypeArrayList.clear();
                        productTypeArrayList.addAll(productTypeModel.result);
                        productTypeId  = productTypeModel.result.get(0).id;
                        adapter.notifyDataSetChanged();
                    } else {
                        productTypeArrayList.clear();
                        adapter.notifyDataSetChanged();

                    }
                }

            }
        });

        timerFragmentViewModel.getTimerRpoViewModel().observe(getActivity(), new Observer<TimerModel>() {
            @Override
            public void onChanged(TimerModel timerModel) {
                try {
                    if (timerModel != null) {
                        String responseData =  new Gson().toJson(timerModel,TimerModel.class)   ;// != null ? response.body().toString() : "";
                        Log.e("Timer  Response", responseData);
                        if (timerModel.getStatus().equals("1")) {
                            binding.tvNotFound.setVisibility(View.GONE);
                            binding.layoutChart.setVisibility(View.VISIBLE);
                            binding.layoutTotal.setVisibility(View.VISIBLE);
                            binding.tvTotalTime.setText(timerModel.getResult().getTotal());
                            setChar(String.valueOf(covertintoMinit(timerModel.getResult().getSessionTimer())),
                                    String.valueOf(covertintoMinit(timerModel.getResult().getRepairTime())));

                        } else if (timerModel.getStatus().equals("0")) {
                            binding.tvNotFound.setVisibility(View.VISIBLE);
                            binding.layoutChart.setVisibility(View.GONE);
                            binding.layoutTotal.setVisibility(View.GONE);


                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });





        if(NetworkAvailablity.checkNetworkStatus(getActivity())) timerFragmentViewModel.teamListViewModel(DataManager.getInstance().getUserData(getActivity()).result.id);
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        if(NetworkAvailablity.checkNetworkStatus(getActivity())) timerFragmentViewModel.productTypeRepoViewModel();
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();







        date = DataManager.getCurrent1();
        binding.tvDate.setText(DataManager.convertDateToString3(date));


        binding.tvDate.setOnClickListener(v -> {
            DatePicker(getActivity());

        });

        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productTypeId = productTypeArrayList.get(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.spinnerTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             if(teamArrayList!=null)   teamId = teamArrayList.get(position).getId();
                timerFragmentViewModel.timerViewModel(productTypeId,teamId,date,DataManager.getInstance().getUserData(getActivity()).result.id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });









    }

    private int covertintoMinit(String time) {
        int timeTotal=0;
        String sess []  = time.split(":");
        timeTotal = (Integer.parseInt(sess[0]) *60) + Integer.parseInt(sess[1]) ;
       return  timeTotal % 60;
    }


    public  void DatePicker(Context context) {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; // your format "  dd-MM-yyyy
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                //  listener.SelectedDate(sdf.format(myCalendar.getTime()));
                date = sdf.format(myCalendar.getTime());
                binding.tvDate.setText(DataManager.convertDateToString3(date));

            }

        };
        DatePickerDialog datePickerDialog= new DatePickerDialog(context, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        // datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis()/*+ (1000*60*60*24*2)*/);

        datePickerDialog.show();
    }


    private void setChar(String d1,String d2) {
        BarData data = new BarData(getDataSet(d1,d2));
        binding.chart.setData(data);
        binding.chart.animateXY(2000, 2000);
        binding.chart.invalidate();
        binding.chart.getAxisRight().setDrawGridLines(false);
        binding.chart.getAxisLeft().setDrawGridLines(false);
        binding.chart.getXAxis().setEnabled(false);
        binding.chart.getXAxis().setDrawAxisLine(false);
        binding.chart.getDescription().setEnabled(false);
        binding.chart.getAxisLeft().setDrawLabels(false);
        binding.chart.getAxisRight().setDrawLabels(false);
        //chart.getXAxis().setDrawLabels(false);

        binding.chart.getLegend().setEnabled(false);


        //remove left border
        binding.chart.getAxisLeft().setDrawAxisLine(false);

        //remove right border
        binding.chart.getAxisRight().setDrawAxisLine(false);
    }


    private BarDataSet getDataSet(String d1, String d2) {

        ArrayList<BarEntry> entries = new ArrayList();
        entries.add(new BarEntry(0f, Float.parseFloat(d1)));
        entries.add(new BarEntry(2f, Float.parseFloat(d2)));
        //   entries.add(new BarEntry(12f, 3));
        //    entries.add(new BarEntry(18f, 4));
        //     entries.add(new BarEntry(9f, 5));

        BarDataSet dataset = new BarDataSet(entries,"hi");
        dataset.setColors(new int[]{R.color.color_green , R.color.color_yellow} , getActivity());

        return dataset;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> labels = new ArrayList();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        return labels;
    }




}
