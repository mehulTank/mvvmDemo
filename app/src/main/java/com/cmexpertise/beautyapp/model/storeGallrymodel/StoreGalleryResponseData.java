package com.cmexpertise.beautyapp.model.storeGallrymodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreGalleryResponseData {

    @SerializedName("responsedata")
    @Expose
    private StoreGalleryResponseList responsedata;

    public StoreGalleryResponseList getResponsedata() {
        return responsedata;
    }

    public void setResponsedata(StoreGalleryResponseList responsedata) {
        this.responsedata = responsedata;
    }

}