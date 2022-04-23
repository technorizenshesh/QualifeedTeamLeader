package com.qualifeed.teamleader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SentModel {

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
        @SerializedName("subject")
        @Expose
        public String subject;
        @SerializedName("message")
        @Expose
        public String message;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("sender")
        @Expose
        public String sender;
        @SerializedName("receiver")
        @Expose
        public String receiver;
        @SerializedName("receiver_name")
        @Expose
        public String receiverName;
        @SerializedName("sender_name")
        @Expose
        public String senderName;



    }

}