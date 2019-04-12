package com.cmexpertise.beautyapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.cmexpertise.beautyapp.sqlite.DatabaseHelper;
import com.cmexpertise.beautyapp.util.WsConstants;
import com.cmexpertise.beautyapp.webservice.UsersService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * *************************************************************************
 * BeautyApplication
 *
 * @CreatedDate:
 * @ModifiedBy: not yet
 * @ModifiedDate: not yet
 * @purpose:This application class to set application level variable and method which
 * used through-out application
 * <p/>
 * *************************************************************************
 */

public class BeautyApplication extends Application {


    private static BeautyApplication mInstance;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper dbHelper;
    private UsersService usersService;
    private Scheduler scheduler;
    private String currentLatLong;
    private Activity activity;

    public static BeautyApplication getmInstance() {
        return mInstance;
    }

    public static void setmInstance(BeautyApplication mInstance) {
        BeautyApplication.mInstance = mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        sharedPreferences = getSharedPreferences(getString(R.string.app_name_new), Context.MODE_PRIVATE);
        dbHelper = new DatabaseHelper(this);
        dbHelper.openDataBase();


    }


    public String getCurrentLatLong() {
        return currentLatLong;
    }

    public void setCurrentLatLong(String currentLatLong) {
        this.currentLatLong = currentLatLong;
    }


    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void savePreferenceDataString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePreferenceDataBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void savePreferenceDataLong(String key, Long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void savePreferenceDataInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static BeautyApplication get(Context context) {
        return (BeautyApplication) context.getApplicationContext();
    }

    public static BeautyApplication create(Context context) {
        return BeautyApplication.get(context);
    }


    /**
     * Call when application is close
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mInstance != null) {
            mInstance = null;
        }

        if (dbHelper != null) {
            dbHelper.close();
        }

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public UsersService getUserService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WsConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return usersService = retrofit.create(UsersService.class);

    }


    public void clearePreferenceData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }

    public void setUserService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


}
