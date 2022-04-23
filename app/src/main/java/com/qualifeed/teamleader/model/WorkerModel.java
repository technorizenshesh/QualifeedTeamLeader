package com.qualifeed.teamleader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class WorkerModel {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_name")
        @Expose
        public String userName;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("social_id")
        @Expose
        public String socialId;
        @SerializedName("register_id")
        @Expose
        public String registerId;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("sub_admin_id")
        @Expose
        public String subAdminId;
        @SerializedName("sup_id")
        @Expose
        public String supId;
        @SerializedName("type")
        @Expose
        public String type;


    }

}