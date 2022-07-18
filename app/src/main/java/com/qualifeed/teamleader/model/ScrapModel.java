package com.qualifeed.teamleader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;




public class ScrapModel {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
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
        @SerializedName("product_id")
        @Expose
        public String productId;
        @SerializedName("comment")
        @Expose
        public String comment;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("worker_id")
        @Expose
        public String workerId;
        @SerializedName("product_defects_id")
        @Expose
        public String productDefectsId;

        @SerializedName("productref")
        @Expose
        public String productRef;

        @SerializedName("product_type_1")
        @Expose
        public String productType1;

        @SerializedName("product_type_2")
        @Expose
        public String productType2;


        @SerializedName("team_leader_id")
        @Expose
        public String team_leader_id;



    }



}