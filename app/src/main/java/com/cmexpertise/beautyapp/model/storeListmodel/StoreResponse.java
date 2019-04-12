package com.cmexpertise.beautyapp.model.storeListmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2/7/2017.
 */

public class StoreResponse implements Parcelable {



    @SerializedName("store_id")
    @Expose
    private String id;

    @SerializedName("store_category_id")
    @Expose
    private String categoryId;

    @SerializedName("store_location_id")
    @Expose
    private String locationId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("open_day")
    @Expose
    private String openDay;

    @SerializedName("close_day")
    @Expose
    private String closeDay;

    @SerializedName("open_time")
    @Expose
    private String openTime;

    @SerializedName("close_time")
    @Expose
    private String closeTime;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("facebook_url")
    @Expose
    private String facebookUrl;

    @SerializedName("youtube_url")
    @Expose
    private String youtubeUrl;

    @SerializedName("instagram_url")
    @Expose
    private String instagramUrl;

    @SerializedName("pinterest_url")
    @Expose
    private String pinterestUrl;

    @SerializedName("facility")
    @Expose
    private String facility;

    @SerializedName("avg_rate")
    @Expose
    private String avgRate;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("dt_added")
    @Expose
    private String dtAdded;

    @SerializedName("dt_updated")
    @Expose
    private String dtUpdated;

    @SerializedName("offer_data")
    @Expose
    private List<StoreOfferData> offerData = null;

    private StoreResponse(Parcel in) {
        this.id = in.readString();
        this.categoryId = in.readString();
        this.locationId = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.openDay = in.readString();
        this.closeDay = in.readString();
        this.openTime = in.readString();
        this.closeTime = in.readString();
        this.discount = in.readString();
        this.image = in.readString();
        this.facebookUrl = in.readString();
        this.youtubeUrl = in.readString();
        this.instagramUrl = in.readString();
        this.pinterestUrl = in.readString();
        this.facility = in.readString();
        this.avgRate = in.readString();
        this.status = in.readString();
        this.dtAdded = in.readString();
        this.dtUpdated = in.readString();
//        offerData = in.readParcelable(StoreOfferData.class.getClassLoader());
        this.offerData = new ArrayList<StoreOfferData>();
        in.readList(this.offerData, StoreOfferData.class.getClassLoader());
        ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOpenDay() {
        return openDay;
    }

    public void setOpenDay(String openDay) {
        this.openDay = openDay;
    }

    public String getCloseDay() {
        return closeDay;
    }

    public void setCloseDay(String closeDay) {
        this.closeDay = closeDay;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getPinterestUrl() {
        return pinterestUrl;
    }

    public void setPinterestUrl(String pinterestUrl) {
        this.pinterestUrl = pinterestUrl;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(String avgRate) {
        this.avgRate = avgRate;
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

    public List<StoreOfferData> getOfferData() {
        return offerData;
    }

    public void setOfferData(List<StoreOfferData> offerData) {
        this.offerData = offerData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(categoryId);
        dest.writeString(locationId);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(openDay);
        dest.writeString(closeDay);
        dest.writeString(openTime);
        dest.writeString(closeTime);
        dest.writeString(discount);
        dest.writeString(image);
        dest.writeString(facebookUrl);
        dest.writeString(youtubeUrl);
        dest.writeString(instagramUrl);
        dest.writeString(pinterestUrl);
        dest.writeString(facility);
        dest.writeString(avgRate);
        dest.writeString(status);
        dest.writeString(dtAdded);
        dest.writeString(dtUpdated);
        dest.writeList(offerData);

    }

    public static final Creator<StoreResponse> CREATOR = new Creator<StoreResponse>() {

        @Override
        public StoreResponse createFromParcel(Parcel source) {
            return new StoreResponse(source);
        }

        @Override
        public StoreResponse[] newArray(int size) {
            return new StoreResponse[size];
        }
    };




}
