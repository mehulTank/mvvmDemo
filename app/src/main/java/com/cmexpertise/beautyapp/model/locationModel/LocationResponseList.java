package com.cmexpertise.beautyapp.model.locationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 2/7/2017.
 */

public class LocationResponseList {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("data")
    @Expose
    private List<LocationResponse> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<LocationResponse> getData() {
        return data;
    }

    public void setData(List<LocationResponse> data) {
        this.data = data;
    }


}
