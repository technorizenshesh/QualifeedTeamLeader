package com.qualifeed.teamleader.retrofit;










import com.qualifeed.teamleader.model.LoginModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface TeamLeadInterface {

    @FormUrlEncoded
    @POST("teamleader_login")
    Call<LoginModel> userLogin (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("forgot_password")
    Call<Map<String, String>> forgotPassword(@FieldMap Map<String, String> params);




}
