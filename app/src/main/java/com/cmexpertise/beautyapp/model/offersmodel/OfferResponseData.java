package com.cmexpertise.beautyapp.model.offersmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferResponseData {

    @SerializedName("responsedata")
    @Expose
    private OfferResponseList responsedata;

    public OfferResponseList getResponsedata() {
        return responsedata;
    }

    public void setResponsedata(OfferResponseList responsedata) {
        this.responsedata = responsedata;
    }

}