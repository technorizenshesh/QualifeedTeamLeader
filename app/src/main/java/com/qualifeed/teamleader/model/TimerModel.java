package com.qualifeed.teamleader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TimerModel {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
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

        @SerializedName("session_timer")
        @Expose
        private String sessionTimer;
        @SerializedName("repair_time")
        @Expose
        private String repairTime;
        @SerializedName("total")
        @Expose
        private String total;

        public String getSessionTimer() {
            return sessionTimer;
        }

        public void setSessionTimer(String sessionTimer) {
            this.sessionTimer = sessionTimer;
        }

        public String getRepairTime() {
            return repairTime;
        }

        public void setRepairTime(String repairTime) {
            this.repairTime = repairTime;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

    }


}