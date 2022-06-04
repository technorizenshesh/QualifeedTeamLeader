package com.qualifeed.teamleader;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.qualifeed.teamleader.adapter.MyAdapter;
import com.qualifeed.teamleader.adapter.TypeAdapter;
import com.qualifeed.teamleader.databinding.ActivityDashboradBinding;
import com.qualifeed.teamleader.fragment.DefectFragment;
import com.qualifeed.teamleader.fragment.ProductFragment;
import com.qualifeed.teamleader.fragment.TimerFragment;
import com.qualifeed.teamleader.model.DashBoradModel;
import com.qualifeed.teamleader.model.DefectModel;
import com.qualifeed.teamleader.model.ProductTypeModel;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.DateSetListener;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;
import com.qualifeed.teamleader.utils.SessionManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardAct extends AppCompatActivity {
    public String TAG = "DashboardAct";
    ActivityDashboradBinding binding;
    TypeAdapter adapter;
    ArrayList<ProductTypeModel.Result> arrayList;
    TeamLeadInterface apiInterface;
    String productTypeId = "", date = "";
    String str_image_path = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;
    int tabPos = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashborad);
        initViews();
    }

    private void initViews() {

        arrayList = new ArrayList<>();
        date = DataManager.getCurrent1();
        binding.tvDate.setText(DataManager.convertDateToString3(date));
        binding.btnLogout.setOnClickListener(v -> SessionManager.clearsession(DashboardAct.this));


        binding.layoutDefect.setOnClickListener(v -> {
            if (checkPermisssionForReadStorage())
                openCamera();
        });


        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Product"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Defect"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Timer"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.viewPager.setAdapter(new MyAdapter(DashboardAct.this, getSupportFragmentManager(), binding.tabLayout.getTabCount()));
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
                Log.e("tab position===", tab.getPosition() + "");
                tabPos = tab.getPosition();
                if (tab.getPosition() == 0) {
                    if (!productTypeId.equals("")) {
                        if (!date.equals("")) ProductFragment.ProductTab(productTypeId, date);
                    }
                } else if (tab.getPosition() == 1) {
                    if (!productTypeId.equals("")) {
                        if (!date.equals("")) DefectFragment.DefectTab(productTypeId, date);
                    }


                } else if (tab.getPosition() == 2) {
                    if (!productTypeId.equals("")) {
                        if (!date.equals("")) TimerFragment.TimerTab(productTypeId, date);
                    }

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        adapter = new TypeAdapter(DashboardAct.this, arrayList);
        binding.spinnerType.setAdapter(adapter);

        if (NetworkAvailablity.checkNetworkStatus(DashboardAct.this)) getProductType();
        else
            Toast.makeText(DashboardAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.tvDate.setOnClickListener(v -> {
            DatePicker(DashboardAct.this);

        });


        // binding.tvBlocked.setOnClickListener(v -> startActivity(new Intent(DashboardAct.this, BlockedAct.class)));

        binding.tvScrap.setOnClickListener(v -> startActivity(new Intent(DashboardAct.this, ScrapListAct.class)));

        binding.tvRepair.setOnClickListener(v -> startActivity(new Intent(DashboardAct.this, RepairListAct.class)));

        binding.layoutTeam.setOnClickListener(v -> startActivity(new Intent(DashboardAct.this, TeamListAct.class)));

        binding.layoutCommunication.setOnClickListener(v -> startActivity(new Intent(DashboardAct.this, CommunicationAct.class)));

    }

    private void setChar(String d1, String d2, String d3) {
        BarData data = new BarData(getDataSet(d1, d2, d3));
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


    private BarDataSet getDataSet(String d1, String d2, String d3) {

        ArrayList<BarEntry> entries = new ArrayList();
        entries.add(new BarEntry(0f, Float.parseFloat(d3)));
        entries.add(new BarEntry(2f, Float.parseFloat(d2)));
        entries.add(new BarEntry(4f, Float.parseFloat(d1)));
        //   entries.add(new BarEntry(12f, 3));
        //    entries.add(new BarEntry(18f, 4));
        //     entries.add(new BarEntry(9f, 5));

        BarDataSet dataset = new BarDataSet(entries, "hi");
        dataset.setColors(new int[]{R.color.color_yellow, R.color.color_red, R.color.color_green}, DashboardAct.this);

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


    public void getProductType() {
        DataManager.getInstance().showProgressMessage(DashboardAct.this, getString(R.string.please_wait));
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

                        getDashBoradData(productTypeId, date);

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

    private void getDashBoradData(String productTypeId, String date) {

        DataManager.getInstance().showProgressMessage(DashboardAct.this, getString(R.string.please_wait));
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
                        binding.layoutChart.setVisibility(View.VISIBLE);
                        binding.tvTotal.setVisibility(View.VISIBLE);
                        binding.tvTotal.setText(data.result.total + " Total");
                        setChar(data.result.totalChecked, data.result.totalScrap, data.result.totalProductRepair);

                        if (tabPos == 0) {
                            if (!productTypeId.equals("")) {
                                if (!date.equals(""))
                                    ProductFragment.ProductTab(productTypeId, date);
                            }
                        } else if (tabPos == 1) {
                            if (!productTypeId.equals("")) {
                                if (!date.equals("")) DefectFragment.DefectTab(productTypeId, date);
                            }
                        } else if (tabPos == 2) {
                            if (!productTypeId.equals("")) {
                                if (!date.equals("")) TimerFragment.TimerTab(productTypeId, date);
                            }
                        }


                    } else if (data.status.equals("0")) {
                        binding.layoutChart.setVisibility(View.GONE);
                        binding.tvTotal.setVisibility(View.GONE);


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
                if (NetworkAvailablity.checkNetworkStatus(context))
                    getDashBoradData(productTypeId, date);
                else
                    Toast.makeText(context, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

            }

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        //  datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        // datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis()+ (1000*60*60*24*2));

        datePickerDialog.show();
    }


    private void openCamera() {

        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/QualifeedTeamLeader/Images/");

        if (!dirtostoreFile.exists()) {
            dirtostoreFile.mkdirs();
        }

        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/QualifeedTeamLeader/Images/" + "IMG_" + timestr + ".jpg");

        str_image_path = tostoreFile.getPath();

        uriSavedImage = FileProvider.getUriForFile(DashboardAct.this,
                BuildConfig.APPLICATION_ID + ".provider",
                tostoreFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        startActivityForResult(intent, REQUEST_CAMERA);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == REQUEST_CAMERA) {
                startActivity(new Intent(DashboardAct.this, NewDefectAct.class)
                        .putExtra("defect_image", str_image_path));

            }

        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(DashboardAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(DashboardAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(DashboardAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(DashboardAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(DashboardAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(DashboardAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(DashboardAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        openCamera();
                    } else {
                        Toast.makeText(DashboardAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DashboardAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }


}


