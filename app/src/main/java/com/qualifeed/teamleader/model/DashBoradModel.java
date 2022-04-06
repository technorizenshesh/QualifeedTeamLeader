package com.qualifeed.teamleader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashBoradModel {

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
        @SerializedName("product_id")
        @Expose
        public String productId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("total_product_repair")
        @Expose
        public String totalProductRepair;
        @SerializedName("total_scrap")
        @Expose
        public String totalScrap;
        @SerializedName("total_checked")
        @Expose
        public String totalChecked;
        @SerializedName("total_blocked")
        @Expose
        public String totalBlocked;
        @SerializedName("total")
        @Expose
        public Integer total;









    }

}


