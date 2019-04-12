package com.cmexpertise.beautyapp.model.locationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2/7/2017.
 */

public class LocationResponse {
    @SerializedName("location_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dt_added")
    @Expose
    private String dtAdded;
    @SerializedName("dt_updated")
    @Expose
    private String dtUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDtAdded() {
        return dtAdded;
    }

    public void setDtAdded(String dtAdded) {
        this.dtAdded = dtAdded;
    }

    public String getDtUpdated() {
        return dtUpdated;
    }

    public void setDtUpdated(String dtUpdated) {
        this.dtUpdated = dtUpdated;
    }

}
