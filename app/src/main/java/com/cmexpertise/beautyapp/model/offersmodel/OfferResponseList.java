package com.cmexpertise.beautyapp.model.offersmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 2/8/2017.
 */

public class OfferResponseList {
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
    private List<OfferResponse> data = null;

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

    public List<OfferResponse> getData() {
        return data;
    }

    public void setData(List<OfferResponse> data) {
        this.data = data;
    }
}
