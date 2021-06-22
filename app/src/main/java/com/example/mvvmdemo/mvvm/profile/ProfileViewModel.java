package com.example.mvvmdemo.mvvm.profile;

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

public class ProfileViewModel extends Observable {


    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProfileNavigator navigator;

    public ProfileViewModel(@NonNull Context context,ProfileNavigator profileNavigator) {
        this.context = context;
        navigator = profileNavigator;

    }

    public String checkvalidation(final String fname, final String lname, final String email, final String mobile)
    {
        if (TextUtils.isEmpty(fname)) {
            return context.getString(R.string.str_enter_first_name);
        } else if (TextUtils.isEmpty(lname)) {
            return context.getString(R.string.str_enter_last_name);
        } else if (TextUtils.isEmpty(email)) {
            return context.getString(R.string.str_enter_email);
        } else if (!Utils.isValidEmail(email)) {
            return context.getString(R.string.str_valid_email_enter);
        } else if (TextUtils.isEmpty(mobile)) {
            return context.getString(R.string.str_enter_mobile_number);
        } else if (mobile.length() < 8 || mobile.length() > 13) {
            return context.getString(R.string.str_valid_mobile_number);
        }else {
            return "";
        }

    }


    public void onClickSubmit(View view) {
        navigator.SubmitClick();
    }



    public void doProfileUpfate(final String fname, final String lname, final String email, final String mobile) {


        BeautyApplication appController = BeautyApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.getUserProfileDetailsResponse(fname, lname, email, mobile)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> navigator.profileUpdateResponce(userResponse),throwable -> navigator.handleError(throwable));

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

