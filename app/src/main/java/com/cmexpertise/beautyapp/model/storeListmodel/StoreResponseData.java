package com.cmexpertise.beautyapp.model.storeListmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2/7/2017.
 */

public class StoreResponseData {
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
