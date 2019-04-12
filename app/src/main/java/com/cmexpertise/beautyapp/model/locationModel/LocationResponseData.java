package com.cmexpertise.beautyapp.model.locationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationResponseData {

    @SerializedName("responsedata")
    @Expose
    private LocationResponseList responsedata;

    public LocationResponseList getResponsedata() {
        return responsedata;
    }

    public void setResponsedata(LocationResponseList responsedata) {
        this.responsedata = responsedata;
    }

}