package com.cmexpertise.beautyapp.model.offersmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2/8/2017.
 */

public class OfferResponse {



    @SerializedName("offer_id")
    @Expose
    private String id;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;

    @SerializedName("offer_store_id")
    @Expose
    private String storeId;
    @SerializedName("offer_service_id")
    @Expose
    private String serviceId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;
    @SerializedName("discount_price")
    @Expose
    private String discountPrice;
    @SerializedName("discount_per")
    @Expose
    private String discountPer;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String imageName;
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String image) {
        this.imageName = image;
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
    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

//
//    @SerializedName("id")
//    @Expose
//    private String id;
//    @SerializedName("store_id")
//    @Expose
//    private String storeId;
//    @SerializedName("title")
//    @Expose
//    private String title;
//    @SerializedName("description")
//    @Expose
//    private String description;
//    @SerializedName("actual_price")
//    @Expose
//    private String actualPrice;
//    @SerializedName("discount_price")
//    @Expose
//    private String discountPrice;
//    @SerializedName("discount_per")
//    @Expose
//    private String discountPer;
//    @SerializedName("image")
//    @Expose
//    private String imageName;
//
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getStoreId() {
//        return storeId;
//    }
//
//    public void setStoreId(String storeId) {
//        this.storeId = storeId;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getActualPrice() {
//        return actualPrice;
//    }
//
//    public void setActualPrice(String actualPrice) {
//        this.actualPrice = actualPrice;
//    }
//
//    public String getDiscountPrice() {
//        return discountPrice;
//    }
//
//    public void setDiscountPrice(String discountPrice) {
//        this.discountPrice = discountPrice;
//    }
//
//    public String getDiscountPer() {
//        return discountPer;
//    }
//
//    public void setDiscountPer(String discountPer) {
//        this.discountPer = discountPer;
//    }
//
//    public String getImageName() {
//        return imageName;
//    }
//
//    public void setImageName(String imageName) {
//        this.imageName = imageName;
//    }
}
