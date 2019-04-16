package com.cmexpertise.beautyapp.mvvm.servicedetails;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cmexpertise.beautyapp.BeautyApplication;
import com.cmexpertise.beautyapp.model.storeServicemodel.ServicesList;
import com.cmexpertise.beautyapp.webservice.UsersService;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by Kailash Patel
 */

public class ServiceDetailsListViewModel extends Observable {

    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ServiceDetailsListNavigator storeListNavigator;

    public ServiceDetailsListViewModel(@NonNull Context context, ServiceDetailsListNavigator storeListNavigator) {
        this.context = context;
        this.storeListNavigator = storeListNavigator;
    }


    public void getServiceList(final String storeId) {

        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.getServiceList(storeId)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ServicesList>() {
                    @Override
                    public void accept(ServicesList userResponse) throws Exception {
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

