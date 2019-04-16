package com.cmexpertise.beautyapp.mvvm.storelist;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cmexpertise.beautyapp.BeautyApplication;
import com.cmexpertise.beautyapp.model.storeListmodel.StoreResponseData;
import com.cmexpertise.beautyapp.webservice.UsersService;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by Kailash Patel
 */

public class StoreViewModel extends Observable {

    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private StoreListNavigator storeListNavigator;

    public StoreViewModel(@NonNull Context context,StoreListNavigator storeListNavigator) {
        this.context = context;
        this.storeListNavigator = storeListNavigator;
    }


    public void getStoreList(final String categoryId, final String locationId, final String offset) {

        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.doGetLocation(categoryId, locationId, offset)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StoreResponseData>() {
                    @Override
                    public void accept(StoreResponseData userResponse) throws Exception {
                        storeListNavigator.storeResponce(userResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        storeListNavigator.handleError(throwable);
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void getStoreListLatlng(final String categoryId, final String lat,final String lng, final String offset) {

        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.doGetLocationWithLatLng(categoryId, lat,lng, offset)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StoreResponseData>() {
                    @Override
                    public void accept(StoreResponseData userResponse) throws Exception {
                        storeListNavigator.storeResponce(userResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        storeListNavigator.handleError(throwable);
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

