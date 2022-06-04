package com.qualifeed.teamleader.fragment;

import android.app.Activity;
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

import com.google.gson.Gson;
import com.qualifeed.teamleader.DashboardAct;
import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.adapter.DefectProductAdapter;
import com.qualifeed.teamleader.adapter.TypeAdapter;
import com.qualifeed.teamleader.databinding.FragmentDefectBinding;
import com.qualifeed.teamleader.model.DefectListModel;
import com.qualifeed.teamleader.model.ProductTypeModel;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefectFragment extends Fragment {
    public static String TAG = "DefectFragment";
    private static FragmentDefectBinding binding;
    TypeAdapter adapter;
    private static DefectProductAdapter defectProductAdapter;

    public static ArrayList<ProductTypeModel.Result> arrayList;
    public static ArrayList<DefectListModel.Result> defecArrayList;

    private static TeamLeadInterface apiInterface;
    String productTypeId = "", date = "";
    private static Context context;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_defect, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);

        arrayList = new ArrayList<>();
        defecArrayList = new ArrayList<>();


        date = DataManager.getCurrent1();
        binding.tvDate.setText(DataManager.convertDateToString3(date));

        adapter = new TypeAdapter(getActivity(), arrayList);
        binding.spinnerType.setAdapter(adapter);

        defectProductAdapter = new DefectProductAdapter(getActivity(), defecArrayList);
        binding.rvDefect.setAdapter(defectProductAdapter);


        if (NetworkAvailablity.checkNetworkStatus(getActivity())) getProductType();
        else
            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.tvDate.setOnClickListener(v -> {
            DatePicker(getActivity());

        });

    }


    public void DatePicker(Context context) {
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
                if (NetworkAvailablity.checkNetworkStatus(context))
                    getDefectData(productTypeId, date);
                else
                    Toast.makeText(context, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

            }

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        // datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis()/*+ (1000*60*60*24*2)*/);

        datePickerDialog.show();
    }

    public void getProductType() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Call<ProductTypeModel> loginCall = apiInterface.getProductType();
        loginCall.enqueue(new Callback<ProductTypeModel>() {
            @Override
            public void onResponse(Call<ProductTypeModel> call, Response<ProductTypeModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    ProductTypeModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Get Product Type Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        productTypeId = data.result.get(0).id;
                        adapter.notifyDataSetChanged();

                        getDefectData(productTypeId, date);

                    } else if (data.status.equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductTypeModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private static void getDefectData(String productTypeId, String date) {
        DataManager.getInstance().showProgressMessage((Activity) context, context.getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("type", "SE");
        map.put("date", date);
        Log.e(TAG, " Product Defect List Request :" + map);
        Call<DefectListModel> loginCall = apiInterface.getDefectList(map);
        loginCall.enqueue(new Callback<DefectListModel>() {
            @Override
            public void onResponse(Call<DefectListModel> call, Response<DefectListModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    DefectListModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Get Product Defect List Response :" + responseString);
                    if (data.getStatus().equals("1")) {
                        binding.tvNotFound.setVisibility(View.GONE);
                        defecArrayList.clear();
                        defecArrayList.addAll(data.getResult());
                        defectProductAdapter.notifyDataSetChanged();
                        // getDefectData(productTypeId,date);

                    } else if (data.getStatus().equals("0")) {
                        binding.tvNotFound.setVisibility(View.VISIBLE);
                        defecArrayList.clear();
                        defectProductAdapter.notifyDataSetChanged();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DefectListModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    public static void DefectTab(String productId, String date) {
        if (NetworkAvailablity.checkNetworkStatus(context)) getDefectData(productId, date);
        else
            Toast.makeText(context, context.getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

    }


}
