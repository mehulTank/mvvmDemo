package com.cmexpertise.beautyapp.fcm;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";


    String deviceId = "";

    String refreshedToken = "";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token in logcat
        Log.e("---Refreshed token: ", "Refreshed token: " + refreshedToken);

        //Preferences.writeString(getApplicationContext(), Preferences.DEVICE_TOKEN, refreshedToken);


        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

//        callApiForDeviceToken();


    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }


    private void callApiForDeviceToken() {


        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String deviceType = "mobile";

        String deviceOs = "android";

        String devicekey = "";


        //String refreshedToken = FirebaseInstanceId.getInstance().getToken();


//        if (InternetConnection.checkConnection(this)) {
//
//
//            ItrackService service = ServiceFactory.getInstance(this).createRetrofitService(ItrackService.class);
//            service.getMyjsonCheckDeviceToken(deviceId, deviceType, refreshedToken + "", deviceOs, devicekey, Preferences.readString(getApplicationContext(), Preferences.USER_ID, Preferences.USER_ID))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<CheckDeviceToken>() {
//                        @Override
//                        public final void onCompleted() {
//                            // do nothing
//                        }
//
//                        @Override
//                        public final void onError(Throwable e) {
//                            Log.e("GithubDemo", e.getMessage());
//                        }
//
//                        @Override
//                        public final void onNext(CheckDeviceToken response) {
//                            //dialog.dismiss();
//                            Log.d("---GithubDemo", new Gson().toJson(response));
//                            if (response.getResponsedata().getSuccess() == 1) {
//
//
//                                //reminderReprtModel = response;
//                                //setReminderListData();
//                                //Snackbar.make(drawer, response.getResponsedata().getMessage(), Snackbar.LENGTH_LONG).show();
//                            } else {
//                                //Snackbar.make(drawer, response.getResponsedata().getMessage(), Snackbar.LENGTH_LONG).show();
//
//                            }
//
//                        }
//                    });
//        } else {
//
//            //Snackbar.make(drawer, R.string.string_internet_connection_not_available, Snackbar.LENGTH_LONG).show();
//        }
    }

}

