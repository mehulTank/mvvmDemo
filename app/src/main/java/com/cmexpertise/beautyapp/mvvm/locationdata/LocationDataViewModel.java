package com.cmexpertise.beautyapp.mvvm.locationdata;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cmexpertise.beautyapp.BeautyApplication;
import com.cmexpertise.beautyapp.model.locationModel.LocationResponseData;
import com.cmexpertise.beautyapp.webservice.UsersService;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by Kailash Patel
 */

public class LocationDataViewModel extends Observable {


    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LocationNavigator loginNavigator;

    public LocationDataViewModel(@NonNull Context context) {
        this.context = context;
        loginNavigator = (LocationNavigator) context;

    }


    public void getLocationData() {


        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();

        Disposable disposable = usersService.doGetLocation()
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LocationResponseData>() {
                    @Override
                    public void accept(LocationResponseData userResponse) throws Exception {
                        loginNavigator.locationResponce(userResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        loginNavigator.handleError(throwable);
                    }
                });

        compositeDisposable.add(disposable);
    }


    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }


}

