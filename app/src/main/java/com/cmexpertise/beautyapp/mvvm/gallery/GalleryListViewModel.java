package com.cmexpertise.beautyapp.mvvm.gallery;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cmexpertise.beautyapp.BeautyApplication;
import com.cmexpertise.beautyapp.model.storeGallrymodel.StoreGalleryResponseData;
import com.cmexpertise.beautyapp.webservice.UsersService;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by Kailash Patel
 */

public class GalleryListViewModel extends Observable {

    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private GalleryListNavigator storeListNavigator;

    public GalleryListViewModel(@NonNull Context context, GalleryListNavigator storeListNavigator) {
        this.context = context;
        this.storeListNavigator = storeListNavigator;
    }


    public void getGalleryList(final String storeId, final String offSet) {

        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.doGetStroreGalleryImages(storeId, offSet)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StoreGalleryResponseData>() {
                    @Override
                    public void accept(StoreGalleryResponseData userResponse) throws Exception {
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

