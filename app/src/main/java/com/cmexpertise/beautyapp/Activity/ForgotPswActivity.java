package com.cmexpertise.beautyapp.Activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.databinding.ActivityForgotBinding;
import com.cmexpertise.beautyapp.model.ResponseBase;
import com.cmexpertise.beautyapp.mvvm.forgot.ForgotNavigator;
import com.cmexpertise.beautyapp.mvvm.forgot.ForgotViewModel;
import com.cmexpertise.beautyapp.util.Utils;

/**
 * Created Kailash Patel
 */

public class ForgotPswActivity extends BaseActivity implements ForgotNavigator {


    private static final long MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;

    private LinearLayout llMain;
    private ProgressDialog progress;
    private ForgotViewModel forgotViewModel;
    private ActivityForgotBinding activityForgotBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();


    }

    @Override
    public void initComponents() {

        activityForgotBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot);
        forgotViewModel = new ForgotViewModel(this);
        activityForgotBinding.setForgotViewModel(forgotViewModel);
        llMain = activityForgotBinding.activityLlMain;

    }


    @Override
    public void handleError(Throwable throwable) {
        Utils.snackbar(llMain, "" + throwable.getMessage(), true, ForgotPswActivity.this);
    }

    @Override
    public void loginClick() {

        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        finish();

    }

    @Override
    public void forgotClcik() {

        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();


        Utils.hideKeyboard(ForgotPswActivity.this);
        String email = activityForgotBinding.activityForgotPassEtEmail.getText().toString();
        final String checkValidation = forgotViewModel.isEmailValid(email);

        if (!checkValidation.isEmpty()) {
            Utils.snackbar(llMain, checkValidation, true, ForgotPswActivity.this);
        } else {
            progress = Utils.showProgressDialog(ForgotPswActivity.this);
            forgotViewModel.forgotPsw(email);
        }


    }

    @Override
    public void forgotResponce(ResponseBase userResponse) {

        Utils.hideProgressDialog(this,progress);

        Utils.snackbar(llMain, "" + userResponse.getResponsedata().getMessage(), true, ForgotPswActivity.this);

        if (userResponse.getResponsedata().getSuccess() == 1) {
            finish();
        }

    }
}
