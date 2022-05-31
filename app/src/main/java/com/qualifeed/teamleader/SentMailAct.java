package com.qualifeed.teamleader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.qualifeed.teamleader.databinding.ActivitySentMailBinding;
import com.qualifeed.teamleader.retrofit.ApiClient;
import com.qualifeed.teamleader.retrofit.TeamLeadInterface;
import com.qualifeed.teamleader.utils.DataManager;
import com.qualifeed.teamleader.utils.NetworkAvailablity;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SentMailAct extends AppCompatActivity {
    public String TAG = "SentMailAct";
    ActivitySentMailBinding binding;
    TeamLeadInterface apiInterface;
    String str_image_path = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(TeamLeadInterface.class);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_sent_mail);
        initViews();
    }

    private void initViews() {

        binding.btnSent.setOnClickListener(v -> {
            validation();
        });

        binding.ivPic.setOnClickListener(v -> {
            if(checkPermisssionForReadStorage())
                openCamera();
        });


    }

    private void validation() {
        if(binding.edTo.getText().toString().equals(""))
            Toast.makeText(SentMailAct.this, getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
       else if(binding.edSubject.getText().toString().equals(""))
            Toast.makeText(SentMailAct.this, getString(R.string.please_enter_subject), Toast.LENGTH_SHORT).show();
       else if(binding.edMsg.getText().toString().equals(""))
            Toast.makeText(SentMailAct.this, getString(R.string.please_enter_message), Toast.LENGTH_SHORT).show();
         else {
             if(NetworkAvailablity.checkNetworkStatus(SentMailAct.this)) sentEmail();
             else Toast.makeText(SentMailAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }

    }

    private void sentEmail() {
     /*   DataManager.getInstance().showProgressMessage(SentMailAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",);
        map.put("from_email", DataManager.getInstance().getUserData(SentMailAct.this).result.email);
        map.put("to_email", binding.edTo.getText().toString());
        map.put("subject", binding.edSubject.getText().toString());
        map.put("message", );
        Log.e(TAG, "Sent Email Request " + map);
        Call<ResponseBody> loginCall = apiInterface.sentEmailApiCall(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject obj = new JSONObject(responseData);
                    Log.e(TAG, "Sent Email Response :" + responseData);
                    if (obj.get("status").equals("1")) {
                        Toast.makeText(SentMailAct.this, "Sent mail Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (obj.get("status").equals("0"))  Toast.makeText(SentMailAct.this, obj.getString("message"), Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });*/





        DataManager.getInstance().showProgressMessage(SentMailAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(SentMailAct.this).result.id);
        RequestBody from = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(SentMailAct.this).result.email);
        RequestBody to = RequestBody.create(MediaType.parse("text/plain"), binding.edTo.getText().toString());
        RequestBody subject = RequestBody.create(MediaType.parse("text/plain"), binding.edSubject.getText().toString());
        RequestBody msg = RequestBody.create(MediaType.parse("text/plain"), binding.edMsg.getText().toString());



        Call<ResponseBody> loginCall = apiInterface.sentEmailApiCall(userId,from,to,subject,msg, filePart);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject obj = new JSONObject(responseData);
                    Log.e(TAG, "Sent Email Response :" + responseData);
                    if (obj.get("status").equals("1")) {
                        Toast.makeText(SentMailAct.this, "Sent mail Successfully", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(SentMailAct.this,CommunicationAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else if (obj.get("status").equals("0"))  Toast.makeText(SentMailAct.this, obj.getString("message"), Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    private void openCamera() {

        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/QualifeedTeamLeader/Images/");

        if (!dirtostoreFile.exists()) {
            dirtostoreFile.mkdirs();
        }

        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/QualifeedTeamLeader/Images/" + "IMG_" + timestr + ".jpg");

        str_image_path = tostoreFile.getPath();

        uriSavedImage = FileProvider.getUriForFile(SentMailAct.this,
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
                binding.ivImg.setVisibility(View.VISIBLE);
                Glide.with(SentMailAct.this)
                        .load(str_image_path)
                        .override(150,150)
                        .error(R.drawable.dummy)
                        .into(binding.ivImg);

            }

        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(SentMailAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(SentMailAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(SentMailAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SentMailAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(SentMailAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SentMailAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(SentMailAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(SentMailAct.this,
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
                        Toast.makeText(SentMailAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SentMailAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }

}
