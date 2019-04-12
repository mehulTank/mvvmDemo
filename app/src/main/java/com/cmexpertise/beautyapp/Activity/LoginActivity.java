package com.cmexpertise.beautyapp.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.cmexpertise.beautyapp.BeautyApplication;
import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.databinding.ActivityLoginBinding;
import com.cmexpertise.beautyapp.model.loginModel.LoginResponse;
import com.cmexpertise.beautyapp.model.loginModel.LoginUserData;
import com.cmexpertise.beautyapp.mvvm.login.LoginNavigator;
import com.cmexpertise.beautyapp.mvvm.login.LoginViewModel;
import com.cmexpertise.beautyapp.util.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.cmexpertise.beautyapp.util.Constans.RC_SIGN_IN;


public class LoginActivity extends BaseActivity implements LoginNavigator, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout llContainer;


    private String password;
    private String email;
    private ProgressDialog progress;
    private CallbackManager callbackFbManager;

    private String firstname = "";
    private String lastname = "";
    private String fbID = "";
    private String userName = "";
    private String socialEmail = "";
    private boolean gpLoginbtnclick = false;

    private GoogleApiClient mGoogleApiClient;
    private ActivityLoginBinding activityLoginBinding;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        initComponents();
        initFacebook();
        initGoogleSign();


    }


    @Override
    public void initComponents() {

        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginViewModel(this);
        activityLoginBinding.setLoginViewModel(loginViewModel);
        llContainer = activityLoginBinding.activityLlmain;

        Log.d("initComponents", "TOKEN==" + FirebaseInstanceId.getInstance().getToken());


    }


    @Override
    public void onClick(View v) {


    }


    @Override
    public void handleError(Throwable throwable) {
        Utils.snackbar(llContainer, "" + throwable.getMessage(), true, LoginActivity.this);
    }

    @Override
    public void login() {


        Utils.hideKeyboard(LoginActivity.this);
        email = activityLoginBinding.activityLoginEtUserName.getText().toString();
        password = activityLoginBinding.activityLoginEtPassword.getText().toString();
        final String checkValidation = loginViewModel.isEmailAndPasswordValid(email, password);

        if (!checkValidation.isEmpty()) {
            Utils.snackbar(llContainer, checkValidation, true, LoginActivity.this);
        } else {
            progress = Utils.showProgressDialog(LoginActivity.this);
            loginViewModel.checkLogin(email, password, "0");
        }
    }

    @Override
    public void register() {

        Intent mMenuIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(mMenuIntent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);

    }

    @Override
    public void forgotPassword() {

        Intent mMenuIntent = new Intent(LoginActivity.this, ForgotPswActivity.class);
        startActivity(mMenuIntent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);

    }

    @Override
    public void fbLogin() {

        if (Utils.isOnline(LoginActivity.this, true)) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        } else {
            Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, getApplicationContext());

        }

    }

    @Override
    public void googleLogin() {
        if (Utils.isOnline(LoginActivity.this, true)) {
            if (gpLoginbtnclick) {
                if (!mGoogleApiClient.isConnecting()) {
                    resolveSignInError();
                }
            }
        } else {
            Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, getApplicationContext());

        }


    }


    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void loginResponce(LoginResponse userResponse) {

        Utils.hideProgressDialog(LoginActivity.this, progress);
        Utils.snackbar(llContainer, "" + userResponse.getResponsedata().getMessage(), true, LoginActivity.this);


        if (userResponse.getResponsedata().getSuccess() == 1) {


            LoginUserData loginDetailsModel = userResponse.getResponsedata().getUserData();

            BeautyApplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_userID), loginDetailsModel.getId());
            BeautyApplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_userName), loginDetailsModel.getFname());
            BeautyApplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_userEmail), loginDetailsModel.getEmail());
            BeautyApplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_userPhone), loginDetailsModel.getPhone());
            BeautyApplication.getmInstance().savePreferenceDataBoolean(getString(R.string.preferances_isNormallogin), false);
            goToHome();

        } else {

            Utils.snackbar(llContainer, "" + userResponse.getResponsedata().getMessage(), true, LoginActivity.this);

        }

    }


    private void goToHome() {


        if (Build.VERSION.SDK_INT < 23) {
            openHomeActivity();
            finish();
        } else {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openHomeActivity();
            } else {
                Utils.checkPermitionCameraGaller(LoginActivity.this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openHomeActivity();
                } else {
                    // permission denied
                }
            }
        }
    }

    private void openHomeActivity() {

        BeautyApplication.getmInstance().savePreferenceDataBoolean(getString(R.string.preferances_islogin), true);
       // Intent intent = new Intent(getApplicationContext(), MenuBarActivity.class);
       // startActivity(intent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
        finish();
    }

    private void initFacebook() {

        callbackFbManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackFbManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final Profile profile = Profile.getCurrentProfile();


                final GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if (object.has("email") && object.getString("email") != null) {
                                socialEmail = object.getString("email");
                            }
                            if (profile != null) {
                                firstname = profile.getFirstName();
                                lastname = profile.getLastName();
                                fbID = profile.getId();
                                email = socialEmail;
                                userName = firstname + " " + lastname;
                                String phone = "";

                                progress = Utils.showProgressDialog(LoginActivity.this);
                                loginViewModel.fbLogin(userName, lastname, email, phone, "1", fbID);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                final Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

                Log.d("onError", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("onError", "onError" + error.getMessage());

            }
        });
    }


    private void initGoogleSign() {

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();


        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            gpLoginbtnclick = false;
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (signInResult != null) {
                    handleSignInResult(signInResult);
                }
            }
        } else {
            callbackFbManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    public void handleSignInResult(GoogleSignInResult signInResult) {
        if (signInResult != null) {

            if (signInResult.isSuccess()) {
                GoogleSignInAccount acct = signInResult.getSignInAccount();
                if (acct != null) {


                    String idToken = acct.getId();
                    String name = acct.getDisplayName();
                    String nameSplit[] = name.split(" ");
                    String firstName = nameSplit[0];
                    String lastName = nameSplit[1];
                    String email = acct.getEmail();

                    progress = Utils.showProgressDialog(LoginActivity.this);
                    loginViewModel.googleLogin(firstName, lastName, email, "", "2", idToken);
                }
            }

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }
    }


}
