package com.qualifeed.teamleader.retrofit;

import com.qualifeed.teamleader.model.BlockedModel;
import com.qualifeed.teamleader.model.DashBoradModel;
import com.qualifeed.teamleader.model.DefectListModel;
import com.qualifeed.teamleader.model.DefectModel;
import com.qualifeed.teamleader.model.InboxModel;
import com.qualifeed.teamleader.model.LoginModel;
import com.qualifeed.teamleader.model.ProductTypeModel;
import com.qualifeed.teamleader.model.RepairModel;
import com.qualifeed.teamleader.model.ScrapModel;
import com.qualifeed.teamleader.model.SentModel;
import com.qualifeed.teamleader.model.TeamListModel;
import com.qualifeed.teamleader.model.TeamModel;
import com.qualifeed.teamleader.model.TimerModel;
import com.qualifeed.teamleader.model.TrainingModel;
import com.qualifeed.teamleader.model.WorkerModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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


    @GET("get_product_type_team")
    Call<ProductTypeModel> getProductType();

    @FormUrlEncoded
    @POST("get_all_product_type_team")
    Call<DashBoradModel> getDashData(@FieldMap Map<String, String> params);

    @GET("get_all_scrap_team")
    Call<ScrapModel> getAllScrap();

    @GET("get_all_product_repair_team")
    Call<RepairModel> getAllRepair();

    @FormUrlEncoded
    @POST("get_all_suspect_defect_team")
    Call<DefectModel> getAllDefect(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_all_blocked_list_team")
    Call<BlockedModel> getAllBlocked(@FieldMap Map<String, String> params);

    @Multipart
    @POST("add_defact_problem_team")
    Call<Map<String,String>> addNewDefect(
            @Part("team_leader_id") RequestBody team_leader_id,
            @Part("product_id") RequestBody product_id,
            @Part("description") RequestBody description,
            @Part("defect_id") RequestBody defect_id,
            @Part("save_defect") RequestBody save_defect,
            @Part MultipartBody.Part file);



    @FormUrlEncoded
    @POST("worker_block_unblock")
    Call<TeamModel> getAllTeam(@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("add_block_unblock")
    Call<Map<String,String>> blockWorker(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_worker_profile")
    Call<WorkerModel> getWorkerDetail(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("inbox_email")
    Call<InboxModel> getAllInboxMail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("send_box")
    Call<SentModel> getAllSentMail(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("product_defects_count")
    Call<DefectListModel> getDefectList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_team")
    Call<TeamListModel> getTeamListApiCall(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_teamleade")
    Call<TimerModel> timerApiCall(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("suspect_defect_to_product_defect")
    Call<ResponseBody> addSuspectDefectApiCall(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("blocked_to_product_defect")
    Call<ResponseBody> addBlockDefectApiCall(@FieldMap Map<String, String> params);



    @Multipart
    @POST("send_mail")
    Call<ResponseBody> sentEmailApiCall(
            @Part("user_id") RequestBody user_id,
            @Part("from_email") RequestBody from_email,
            @Part("to_email") RequestBody to_email,
            @Part("subject") RequestBody subject,
            @Part("message") RequestBody message,
            @Part MultipartBody.Part file);



    @FormUrlEncoded
    @POST("get_worker_by_team_temled")
    Call<ResponseBody> workerDetailApiCall(@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("add_block_unblock")
    Call<ResponseBody> workerBlockedApiCall(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_training")
    Call<TrainingModel> getTrainingApiCall(@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("send_training")
    Call<ResponseBody> workerTrainingApiCall(@FieldMap Map<String, String> params);



}
