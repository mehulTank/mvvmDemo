package com.example.mvvmdemo.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mvvmdemo.BeautyApplication;
import com.example.mvvmdemo.R;
import com.example.mvvmdemo.util.Utils;


/****************************************************************************
 * SplashActivity
 *
 * @CreatedDate:
 * @ModifiedBy: not yet
 * @ModifiedDate: not yet
 * @purpose:This Class is user for SplashActivity.
 ***************************************************************************/

public class SplashActivity extends AppCompatActivity {
    private int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLanguage(this, BeautyApplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_language)), "en"));
        setContentView(R.layout.activity_splash);
        new SplashTask().execute();
    }

    /**
     * AsycTask for setting splash screen by sleep thread for some time
     */
    private class SplashTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(SPLASH_TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            boolean isLogin = BeautyApplication.getmInstance().getSharedPreferences().getBoolean(getString((R.string.preferances_islogin)), false);
            boolean isIntro = BeautyApplication.getmInstance().getSharedPreferences().getBoolean(getString((R.string.preferances_isIntro)), false);

//            isLogin=true;
//
//            if (!isIntro) {
//                Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//
//                com.cmexpertise.beautyapp.util.Preferences.writeString(SplashActivity.this, com.cmexpertise.beautyapp.util.Preferences.SELECTED_LANGUAGE_ID, "1");
//                com.cmexpertise.beautyapp.util.Preferences.writeString(SplashActivity.this, Preferences.SELECTED_LANGUAGE_PREFIX, "en");
//            }
//            else
//                {
//                if (isLogin)
//                {
//
//                    if (Preferences.readString(SplashActivity.this, Preferences.SELECTED_CATEGORIES_ID, "").isEmpty())
//                    {
////                        Intent intent = new Intent(SplashActivity.this, SelectLocationActivity.class);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        startActivity(intent);
////                        SplashActivity.this.finish();
//                    }
//                    else
//                    {
//                        Intent mMenuIntent = new Intent(SplashActivity.this, MainActivity.class);
//                        startActivity(mMenuIntent);
//                        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
//                        finish();
//                    }
//
//
//                } else {
//
//                }
//
//            }


            Intent mMenuIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mMenuIntent);
            overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
            finish();

        }
    }
}
