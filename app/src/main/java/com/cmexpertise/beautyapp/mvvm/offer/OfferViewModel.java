package com.cmexpertise.beautyapp.mvvm.offer;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cmexpertise.beautyapp.BeautyApplication;
import com.cmexpertise.beautyapp.model.offersmodel.OfferResponseData;
import com.cmexpertise.beautyapp.webservice.UsersService;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by Kailash Patel
 */

public class OfferViewModel extends Observable {

    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private OfferListNavigator storeListNavigator;

    public OfferViewModel(@NonNull Context context, OfferListNavigator storeListNavigator) {
        this.context = context;
        this.storeListNavigator = storeListNavigator;
    }


    public void getOfferList(String offset) {

        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.doGetOffer(offset)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OfferResponseData>() {
                    @Override
                    public void accept(OfferResponseData userResponse) throws Exception {
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


}

