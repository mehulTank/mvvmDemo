package com.example.mvvmdemo.mvvm.login;

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

public class LoginViewModel extends Observable {


    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LoginNavigator loginNavigator;


    public LoginViewModel(@NonNull Context context) {
        this.context = context;
        loginNavigator = (LoginNavigator) context;

    }

    public String isEmailAndPasswordValid(String email, String password) {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            return context.getString(R.string.str_enter_email);
        } else if (!Utils.isValidEmail(email)) {
            return context.getString(R.string.str_valid_email_enter);
        } else if (TextUtils.isEmpty(password)) {
            return context.getString(R.string.str_enter_password);
        } else {
            return "";
        }

    }



    public void onClickLogin(View view) {
        loginNavigator.login();
    }

    public void onClickSkip(View view) {
        loginNavigator.skip();
    }

    public void onClickRegister(View view) {
        loginNavigator.register();
    }

    public void onClickForgot(View view) {
        loginNavigator.forgotPassword();
    }

    public void onClickFbLogin(View view) {
        loginNavigator.fbLogin();
    }

    public void onClickGoogleLogin(View view) {
        loginNavigator.googleLogin();
    }

    public void checkLogin(String userEmail, String password, String type) {


        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.doLogin(userEmail, password, type)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> loginNavigator.loginResponce(userResponse),throwable -> loginNavigator.handleError(throwable));

        compositeDisposable.add(disposable);
    }


    public void addDeviceLocation(String uID, String deviceId,String fbToken,String deviceType) {


        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.addDeviceToken(uID,deviceId,fbToken,deviceType)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> loginNavigator.addDeviceResponce(userResponse),throwable -> loginNavigator.handleError(throwable));

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


    public void fbLogin(String userName, String lastname, String email, String phone, String type, String fbID)
    {
        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.doFBSocialLogin(userName,lastname,email,phone,type,fbID)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> loginNavigator.loginResponce(userResponse),throwable -> loginNavigator.handleError(throwable));

        compositeDisposable.add(disposable);

    }

    public void googleLogin(String userName, String lastname, String email, String phone, String type, String fbID)
    {
        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.doGoogleSocialLogin(userName,lastname,email,phone,type,fbID)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> loginNavigator.loginResponce(userResponse),throwable -> loginNavigator.handleError(throwable));

        compositeDisposable.add(disposable);

    }
}

