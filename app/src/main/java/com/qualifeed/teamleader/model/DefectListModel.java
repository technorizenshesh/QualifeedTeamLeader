package com.qualifeed.teamleader.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefectListModel {

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

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("defects_count")
        @Expose
        private Integer defectsCount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getDefectsCount() {
            return defectsCount;
        }

        public void setDefectsCount(Integer defectsCount) {
            this.defectsCount = defectsCount;
        }

    }

}

