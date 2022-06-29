package com.qualifeed.teamleader.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefectModel implements Serializable {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public class Result implements Serializable{

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


        @SerializedName("description")
        @Expose
        public String description;

        @SerializedName("title")
        @Expose
        public String title;


        @SerializedName("productref")
        @Expose
        public String productRef;


        @SerializedName("product_type_1")
        @Expose
        public String productType1;


        @SerializedName("product_type_2")
        @Expose
        public String productType2;


    }

}

