package com.cmexpertise.beautyapp.Activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.databinding.ActivityRegisterBinding;
import com.cmexpertise.beautyapp.model.ResponseBase;
import com.cmexpertise.beautyapp.mvvm.register.RegisterNavigator;
import com.cmexpertise.beautyapp.mvvm.register.RegisterViewModel;
import com.cmexpertise.beautyapp.util.Utils;

/**
 * Created Kailash Patel
 */

public class RegisterActivity extends BaseActivity implements RegisterNavigator {


    private static final long MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;

    private LinearLayout llMain;
    private ProgressDialog progress;
    private RegisterViewModel viewModel;
    private ActivityRegisterBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();


    }

    @Override
    public void initComponents() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        viewModel = new RegisterViewModel(this);
        binding.setRegisterViewModel(viewModel);
        llMain = binding.activityLlmain;

    }


    @Override
    public void handleError(Throwable throwable) {
        Utils.snackbar(llMain, "" + throwable.getMessage(), true, RegisterActivity.this);
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
    public void register() {

        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();


        Utils.hideKeyboard(RegisterActivity.this);
        final String email = binding.activityRegisterEtEmail.getText().toString();
        final String passwors = binding.activityRegisterEtPassword.getText().toString();
        final String fname = binding.activityRegisterEtFirstName.getText().toString();
        final String lName = binding.activityRegisterEtLastName.getText().toString();
        final String cnfPsw = binding.activityRegisterEtConfirmPass.getText().toString();
        final String phone = binding.activityRegisterEtMobile.getText().toString();


        final String checkValidation = viewModel.checkvalidation(fname, lName, email, phone, passwors, cnfPsw);

        if (!checkValidation.isEmpty()) {
            Utils.snackbar(llMain, checkValidation, true, RegisterActivity.this);
        } else {
            progress = Utils.showProgressDialog(RegisterActivity.this);
            viewModel.doRegister(fname, lName, email, phone, passwors, cnfPsw);
        }

    }

    @Override
    public void registerResponce(ResponseBase userResponse) {

        Utils.hideProgressDialog(this, progress);
        Utils.snackbar(llMain, "" + userResponse.getResponsedata().getMessage(), true, RegisterActivity.this);

        if (userResponse.getResponsedata().getSuccess() == 1) {
            finish();
        }

    }


}
