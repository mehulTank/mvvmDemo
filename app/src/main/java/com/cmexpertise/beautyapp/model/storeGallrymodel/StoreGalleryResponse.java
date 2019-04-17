package com.cmexpertise.beautyapp.model.storeGallrymodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2/8/2017.
 */

public class StoreGalleryResponse implements Parcelable {

    public static final Creator<StoreGalleryResponse> CREATOR = new Creator<StoreGalleryResponse>() {

        @Override
        public StoreGalleryResponse createFromParcel(Parcel source) {
            return new StoreGalleryResponse(source);
        }

        @Override
        public StoreGalleryResponse[] newArray(int size) {
            return new StoreGalleryResponse[size];
        }
    };
    @SerializedName("gallery_id")
    @Expose
    private String id;
    @SerializedName("gallery_store_id")
    @Expose
    private String storeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dt_added")
    @Expose
    private String dtAdded;
    @SerializedName("dt_updated")
    @Expose
    private String dtUpdated;

    private StoreGalleryResponse(Parcel in) {
        this.id = in.readString();
        this.storeId = in.readString();
        this.name = in.readString();
        this.thumb = in.readString();
        this.status = in.readString();
        this.dtAdded = in.readString();
        this.dtUpdated = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(storeId);
        dest.writeString(name);
        dest.writeString(thumb);
        dest.writeString(status);
        dest.writeString(dtAdded);
        dest.writeString(dtUpdated);
    }
}
