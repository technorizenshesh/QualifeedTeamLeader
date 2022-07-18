package com.qualifeed.teamleader;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qualifeed.teamleader.adapter.TypeAdapter;
import com.qualifeed.teamleader.databinding.ActivityNewDefectBinding;
import com.qualifeed.teamleader.model.ProductTypeModel;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewDefectAct extends AppCompatActivity {
    public String TAG = "NewDefectAct";
    ActivityNewDefectBinding binding;
    TypeAdapter adapter;
    ArrayList<ProductTypeModel.Result> arrayList;
    TeamLeadInterface apiInterface;
    String productTypeId="",defectImage="",type1="",type2="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_new_defect);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        if(getIntent()!=null) {
            defectImage = getIntent().getStringExtra("defect_image");
            Glide.with(NewDefectAct.this)
                    .load(defectImage)
                    .centerCrop()
                    .into(binding.ivPic);
        }


        adapter = new TypeAdapter(NewDefectAct.this,arrayList);
        binding.spinnerType.setAdapter(adapter);

        if(NetworkAvailablity.checkNetworkStatus(NewDefectAct.this)) getProductType();
        else Toast.makeText(NewDefectAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();


        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productTypeId = arrayList.get(position).id;
                if(!type1.equals("")) {
                    type1 = arrayList.get(position).productType1;
                    type2 = arrayList.get(position).productType2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        binding.btnAdd.setOnClickListener(v -> {
            if(type1.equals(""))
                Toast.makeText(NewDefectAct.this, "Please select product type", Toast.LENGTH_SHORT).show();

            if(binding.etDefectId.getText().toString().equals(""))
                Toast.makeText(NewDefectAct.this, "Please add product ref", Toast.LENGTH_SHORT).show();


            if(binding.etDefectTitle.getText().toString().equals(""))
                Toast.makeText(NewDefectAct.this, "Please add title", Toast.LENGTH_SHORT).show();


            if(binding.etDes.getText().toString().equals(""))
                Toast.makeText(NewDefectAct.this, "Please add Defect Description", Toast.LENGTH_SHORT).show();

             else {
                 CheckDefectAlertDialog();

            }


        });


    }

    private void CheckDefectAlertDialog() {
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(NewDefectAct.this);
        builder1.setMessage(getResources().getString(R.string.msg_alert));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if(NetworkAvailablity.checkNetworkStatus(NewDefectAct.this)) AddDefect("Yes");
                        else Toast.makeText(NewDefectAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    }
                });

        builder1.setNegativeButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if(NetworkAvailablity.checkNetworkStatus(NewDefectAct.this)) AddDefect("No");
                        else Toast.makeText(NewDefectAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    private void AddDefect(String SaveDefect) {
        DataManager.getInstance().showProgressMessage(NewDefectAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!defectImage.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(defectImage));
            filePart = MultipartBody.Part.createFormData("product_defects_image", file.getName(), RequestBody.create(MediaType.parse("product_defects_image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody team_lead_id = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(NewDefectAct.this).result.id);
        RequestBody typ1 = RequestBody.create(MediaType.parse("text/plain"), type1);
        RequestBody typ2 = RequestBody.create(MediaType.parse("text/plain"), type2);
        RequestBody proRef = RequestBody.create(MediaType.parse("text/plain"), binding.etDefectId.getText().toString());
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), binding.etDefectTitle.getText().toString());
        RequestBody dess = RequestBody.create(MediaType.parse("text/plain"), binding.etDes.getText().toString());
        RequestBody save_defect = RequestBody.create(MediaType.parse("text/plain"), SaveDefect);



        Call<Map<String, String>> loginCall = apiInterface.addNewDefect(team_lead_id,typ1,typ2,proRef,title,dess,save_defect, filePart);
        loginCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Add Defect Response :" + responseString);
                    if (data.get("status").equals("1")) {
                        Toast.makeText(NewDefectAct.this, "Add successfully.", Toast.LENGTH_SHORT).show();
                       finish();
                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(NewDefectAct.this, data.get("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    public void getProductType(){
        DataManager.getInstance().showProgressMessage(NewDefectAct.this, getString(R.string.please_wait));
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
                        productTypeId  = data.result.get(0).id;
                        type1 = data.result.get(0).productType1;
                        type2 = data.result.get(0).productType2;

                        adapter.notifyDataSetChanged();
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


}
