package com.example.mvvmdemo.Activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mvvmdemo.R;
import com.example.mvvmdemo.databinding.ActivityRegisterBinding;
import com.example.mvvmdemo.model.ResponseBase;
import com.example.mvvmdemo.mvvm.register.RegisterNavigator;
import com.example.mvvmdemo.mvvm.register.RegisterViewModel;
import com.example.mvvmdemo.util.Utils;

/**
 * Created Nishidh Patel
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
        Utils.hideKeyboard(RegisterActivity.this);
        Utils.hideProgressDialog(this,progress);
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
            viewModel.doRegister(fname, lName, email, phone, passwors, "0");
        }

    }

    @Override
    public void registerResponce(ResponseBase userResponse) {

        Utils.hideProgressDialog(this, progress);
        Utils.hideKeyboard(RegisterActivity.this);
        //Utils.snackbar(llMain, "" + userResponse.getResponsedata().getMessage(), true, RegisterActivity.this);
        Toast.makeText(RegisterActivity.this,""+userResponse.getResponsedata().getMessage(),Toast.LENGTH_SHORT).show();

        if (userResponse.getResponsedata().getSuccess() == 1) {
            finish();
        }

    }


}
