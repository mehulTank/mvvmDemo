package com.cmexpertise.beautyapp.mvvm.category;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cmexpertise.beautyapp.BeautyApplication;
import com.cmexpertise.beautyapp.model.categoryModel.CategoryResponseData;
import com.cmexpertise.beautyapp.webservice.UsersService;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by Kailash Patel
 */

public class CategoryDataViewModel extends Observable {


    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CategoryNavigator loginNavigator;

    public CategoryDataViewModel(@NonNull Context context) {
        this.context = context;
        loginNavigator = (CategoryNavigator) context;

    }


    public void getCategoryList() {


        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();

        Disposable disposable = usersService.doGetCategoryList()
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CategoryResponseData>() {
                    @Override
                    public void accept(CategoryResponseData userResponse) throws Exception {
                        loginNavigator.categoryResponce(userResponse);

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

