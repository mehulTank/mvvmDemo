package com.example.mvvmdemo.mvvm.forgot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.example.mvvmdemo.BeautyApplication;
import com.example.mvvmdemo.R;
import com.example.mvvmdemo.util.Utils;
import com.example.mvvmdemo.webservice.UsersService;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by Nishidh Patel
 */

public class ForgotViewModel extends Observable {


    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ForgotNavigator loginNavigator;

    public ForgotViewModel(@NonNull Context context) {
        this.context = context;
        loginNavigator = (ForgotNavigator) context;

    }

    public String isEmailValid(String email) {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            return context.getString(R.string.str_enter_email);
        } else if (!Utils.isValidEmail(email)) {
            return context.getString(R.string.str_valid_email_enter);
        } else {
            return "";
        }

    }


    public void onClickLogin(View view) {
        loginNavigator.loginClick();
    }


    public void onClickForgot(View view) {
        loginNavigator.forgotClcik();
    }


    public void forgotPsw(String userEmail) {


        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();

        Disposable disposable = usersService.doForgotPasswordDetails(userEmail)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> loginNavigator.forgotResponce(userResponse),throwable -> loginNavigator.handleError(throwable));

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

