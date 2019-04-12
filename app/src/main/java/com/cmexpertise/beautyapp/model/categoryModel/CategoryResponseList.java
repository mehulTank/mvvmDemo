package com.cmexpertise.beautyapp.model.categoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kailash Patel
 */

public class CategoryResponseList {
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
    private List<CategoryResponse> data = null;

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

    public List<CategoryResponse> getData() {
        return data;
    }

    public void setData(List<CategoryResponse> data) {
        this.data = data;
    }


}
