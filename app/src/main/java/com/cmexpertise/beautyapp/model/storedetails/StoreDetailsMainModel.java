package com.cmexpertise.beautyapp.model.storedetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nishidh patel
 */

public class StoreDetailsMainModel {
    @SerializedName("responsedata")
    @Expose
    private StoreResponseList responsedata;

    public StoreResponseList getResponsedata() {
        return responsedata;
    }

    public void setResponsedata(StoreResponseList responsedata) {
        this.responsedata = responsedata;
    }


}
