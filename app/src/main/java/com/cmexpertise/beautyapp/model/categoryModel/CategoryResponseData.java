package com.cmexpertise.beautyapp.model.categoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryResponseData {

    @SerializedName("responsedata")
    @Expose
    private CategoryResponseList responsedata;

    public CategoryResponseList getResponsedata() {
        return responsedata;
    }

    public void setResponsedata(CategoryResponseList responsedata) {
        this.responsedata = responsedata;
    }

}