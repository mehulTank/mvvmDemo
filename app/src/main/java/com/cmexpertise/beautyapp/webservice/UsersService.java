package com.cmexpertise.beautyapp.webservice;



import com.cmexpertise.beautyapp.model.ResponseBase;
import com.cmexpertise.beautyapp.model.categoryModel.CategoryResponseData;
import com.cmexpertise.beautyapp.model.locationModel.LocationResponseData;
import com.cmexpertise.beautyapp.model.loginModel.LoginResponse;
import com.cmexpertise.beautyapp.model.offersmodel.OfferResponseData;
import com.cmexpertise.beautyapp.model.storeGallrymodel.StoreGalleryResponseData;
import com.cmexpertise.beautyapp.model.storeListmodel.StoreResponseData;
import com.cmexpertise.beautyapp.model.storeServicemodel.ServicesList;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Kailash Patel
 */

public interface UsersService
{

    @FormUrlEncoded
    @POST("store_list")
    Observable<StoreResponseData> doGetLocation(@Field("category_id") String category_id,
                                                @Field("location_id") String location_id,
                                                @Field("offset") String offset);

    @FormUrlEncoded
    @POST("store_list")
    Observable<StoreResponseData> doGetLocationWithLatLng(@Field("category_id") String category_id,
                                                             @Field("latitude") String latitude,
                                                             @Field("longitude") String longitude,
                                                             @Field("offset") String offset);

    @FormUrlEncoded
    @POST("user_login")
    Observable<LoginResponse> doLogin(@Field("email") String email, @Field("password") String password, @Field("login_type") String login_type);

    @FormUrlEncoded
    @POST("add_device")
    Observable<ResponseBase> addDeviceToken(@Field("user_id") String user_id, @Field("device_id") String device_id, @Field("device_token") String device_token, @Field("device_type") String device_type);



    @FormUrlEncoded
    @POST("user_login")
    Observable<LoginResponse> doFBSocialLogin(@Field("fname") String fname, @Field("lname") String lname, @Field("email") String email, @Field("phone") String strPhoneNo,
                                                 @Field("login_type") String login_type, @Field("facebook_token_id") String facebook_token_id);

    @FormUrlEncoded
    @POST("user_login")
    Observable<LoginResponse> doGoogleSocialLogin(@Field("fname") String fname, @Field("lname") String lname, @Field("email") String email, @Field("phone") String strPhoneNo,
                                                     @Field("login_type") String login_type, @Field("gmail_token_id") String gmail_token_id);

    @FormUrlEncoded
    @POST("user_forgot_password")
    Observable<ResponseBase> doForgotPasswordDetails(@Field("email") String email);

    @FormUrlEncoded
    @POST("user_register")
    Observable<ResponseBase> doRegister(@Field("fname") String fname, @Field("lname") String lname, @Field("email") String email, @Field("password") String password, @Field("phone") String phone, @Field("login_type") String login_type);

    @POST("location_list")
    Observable<LocationResponseData> doGetLocation();

    @POST("category_list")
    Observable<CategoryResponseData> doGetCategoryList();

    @GET("store_service_list")
    Observable<ServicesList> getServiceList(@Query("store_id") String storeId);

    @FormUrlEncoded
    @POST("store_gallery_list")
    Observable<StoreGalleryResponseData> doGetStroreGalleryImages(@Field("store_id") String store_id, @Field("offset") String offset);

    @GET("offer_list")
    Observable<OfferResponseData> doGetOffer(@Query("offset") String offset);

}
