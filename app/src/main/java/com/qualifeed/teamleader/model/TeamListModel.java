package com.qualifeed.teamleader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TeamListModel {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("teamleader_id")
        @Expose
        private String teamleaderId;
        @SerializedName("team_name")
        @Expose
        private String teamName;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTeamleaderId() {
            return teamleaderId;
        }

        public void setTeamleaderId(String teamleaderId) {
            this.teamleaderId = teamleaderId;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }


}