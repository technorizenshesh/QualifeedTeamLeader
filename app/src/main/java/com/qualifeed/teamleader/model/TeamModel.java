package com.qualifeed.teamleader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TeamModel {

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
        @SerializedName("team_leader_id")
        @Expose
        public String teamLeaderId;
        @SerializedName("worker_id")
        @Expose
        public String workerId;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("worker_name")
        @Expose
        public String workerName;
        @SerializedName("team_leader_name")
        @Expose
        public String teamLeaderName;



    }

}