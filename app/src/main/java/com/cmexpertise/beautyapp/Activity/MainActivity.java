package com.cmexpertise.beautyapp.Activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmexpertise.beautyapp.BeautyApplication;
import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.fragment.StoreListFragment;
import com.cmexpertise.beautyapp.util.Preferences;
import com.cmexpertise.beautyapp.util.Utils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private TextView txtDrawerUserName;
    private TextView txtDrawerUserEmail;
    private TextView toolbarTextView;
    private DrawerLayout drawer;
    private RelativeLayout rlLogout;

    private MaterialSearchView searchView;
    private NavigationView navigationView;


    private Toolbar toolbar;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;


    private String fromDtaeCalc = "";
    private String toDateCalc = "";
    private String cityName = "";


    private int MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;


    public static String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        FirebaseApp.initializeApp(MainActivity.this);
        Utils.setLanguage(this, Preferences.readString(this, Preferences.SELECTED_LANGUAGE_PREFIX, "en"));

    }

    @Override
    public void initComponents() {

        BeautyApplication.getmInstance().setActivity(MainActivity.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setOnClickListener(this);
        toolbar.setOnClickListener(this);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbarTextView = (TextView) findViewById(R.id.txtTitle);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        rlLogout = (RelativeLayout) findViewById(R.id.rlLogout);
        rlLogout.setOnClickListener(this);

        setUpDrawerSync();
        setUpHeaderData();
        userChangeListner();

        Preferences.getPreferences(MainActivity.this).registerOnSharedPreferenceChangeListener(listener);
        changeFragment(new StoreListFragment(), false);
        rateUsDialog();
        changeTitle(true, Preferences.readString(this, Preferences.SELECTED_CITY, ""));

    }

    private void userChangeListner() {

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                // Implementation
                if ((key.equals(Preferences.USER_NAME)) || (key.equals(Preferences.USER_EMAIL_ID))) {
                    txtDrawerUserName.setText(Preferences.readString(MainActivity.this, Preferences.USER_NAME, ""));
                    txtDrawerUserEmail.setText(Preferences.readString(MainActivity.this, Preferences.USER_EMAIL_ID, ""));
                }
            }
        };

    }

    private void setUpHeaderData() {

        if (Preferences.readBoolean(getApplicationContext(), Preferences.SELECTEDLOCATIONORCITY, false)) {
            cityName = Preferences.readString(this, Preferences.SELECTED_CURRENT_CITY, "");
        } else {
            cityName = Preferences.readString(this, Preferences.SELECTED_CITY, "");
        }


        View header = navigationView.getHeaderView(0);
        txtDrawerUserName = (TextView) header.findViewById(R.id.txtDrawerUserName);
        txtDrawerUserEmail = (TextView) header.findViewById(R.id.txtDrawerUserEmail);


        if (!Preferences.readString(MainActivity.this, Preferences.USER_ID, "").equals("")) {
            txtDrawerUserName.setText(Preferences.readString(MainActivity.this, Preferences.USER_NAME, ""));
            txtDrawerUserEmail.setText(Preferences.readString(MainActivity.this, Preferences.USER_EMAIL_ID, ""));

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
        } else {
            txtDrawerUserName.setText(getString(R.string.user_name));
            txtDrawerUserEmail.setText(getString(R.string.welcome_guest));
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_logout);
        }

    }

    private void setUpDrawerSync() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                Utils.hideKeyboard(MainActivity.this);

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Utils.hideKeyboard(MainActivity.this);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.closeDrawer(GravityCompat.START);

    }

    private void rateUsDialog() {


//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);
//        RateUsDialogFragment rateUsDialogFragment = new RateUsDialogFragment();
//        int daysDiff = getDateDifferent();
//        String dialogStatus = Preferences.readString(MainActivity.this, Preferences.RATE_US_DIALOG_STATUS, "");
//        if (toDateCalc.isEmpty()) {
//            rateUsDialogFragment.show(ft, "dialog");
//        }
//        if (((daysDiff > 2) && (Integer.parseInt(dialogStatus) == 1)) || (daysDiff == 1)) {
//            rateUsDialogFragment.show(ft, "dialog");
//        }
    }

    public int getDateDifferent() {
        int days = 0;

        int startDate = 0;
        int endDate = 0;
        fromDtaeCalc = getDateTime() + "";
        toDateCalc = Preferences.readString(MainActivity.this, Preferences.RATE_US_DIALOG_SHOW_LAST_DATE, "");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date Date1 = null, Date2 = null;
        try {
            Date1 = sdf.parse(toDateCalc);
            Date2 = sdf.parse(fromDtaeCalc);
            days = (int) ((Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return days;
    }

    @Override
    public void onClick(View v)
    {


        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (v.getId())
        {
            case R.id.toolbar:
                Intent locationIntent = new Intent(MainActivity.this, SelectLocationActivity.class);
                locationIntent.putExtra(Utils.INTENT_FROM_SETTING, getString(R.string.yes));
                startActivity(locationIntent);
                break;

            case R.id.rlLogout:
                onLogOutClicked();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            changeFragment(new StoreListFragment(), false);
            changeTitle(true, Preferences.readString(this, Preferences.SELECTED_CITY, ""));
            toolbar.setOnClickListener(this);

        } else if (id == R.id.nav_profile) {

            // changeFragment(new StoreListFragment(), false);
            changeTitle(false, getString(R.string.Profile));
            toolbar.setOnClickListener(null);


        } else if (id == R.id.nav_offers) {
            //changeFragment(new OfferListFragment(), false);
            changeTitle(false, getString(R.string.Offers));
            toolbar.setOnClickListener(null);

        } else if (id == R.id.nav_booking) {
            // changeFragment(new BookingListFragment(), false);
            changeTitle(false, getString(R.string.Bookings));
            toolbar.setOnClickListener(null);
        } else if (id == R.id.nav_setting) {

            //changeFragment(new SettingFragment(), false);
            changeTitle(false, getString(R.string.action_settings));
            toolbar.setOnClickListener(null);


        } else if (id == R.id.nav_notification) {
            // changeFragment(new NotificationListFragment(), false);
            changeTitle(false, getString(R.string.notification));
            toolbar.setOnClickListener(null);


        } else if (id == R.id.nav_about) {
            //changeFragment(new AboutUsFragment(), false);
            changeTitle(false, getString(R.string.AboutUs));
            toolbar.setOnClickListener(null);


        } else if (id == R.id.nav_feedback) {

//            RateUsDialog cdd = new RateUsDialog(MainActivity.this);
////            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////            cdd.show();
////            toolbar.setOnClickListener(null);

        }
        else if (id == R.id.nav_login) {
            Preferences.writeString(this, Preferences.IS_SKIPED, "");
            startActivity(new Intent(this, LoginActivity.class));
            toolbar.setOnClickListener(null);
        }


        Utils.hideKeyboard(MainActivity.this);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void changeTitle(boolean isComp, String title) {

        getSupportActionBar().setTitle("" + title);
        toolbarTextView.setText(Preferences.readString(this, Preferences.SELECTED_CITY, ""));

        if (isComp) {
            toolbarTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_location_edit, 0);
        } else {
            toolbarTextView.setCompoundDrawables(null, null, null, null);
        }







    }

    private void performLogout() {
        Preferences.writeString(MainActivity.this, Preferences.USER_ID, "");
        Preferences.writeString(MainActivity.this, Preferences.SELECTED_LOCATION_ID, "");
        Preferences.writeString(MainActivity.this, Preferences.SELECTED_CATEGORIES_ID, "");
        Preferences.writeString(MainActivity.this, Preferences.SELECTED_CITY_LATITUDE, "");
        Preferences.writeString(MainActivity.this, Preferences.SELECTED_CITY_LONGITUDE, "");
        Preferences.writeString(MainActivity.this, Preferences.LOGIN_TYPE, "");
        Preferences.writeString(MainActivity.this, Preferences.DEVICE_ID, "");
        Preferences.writeString(MainActivity.this, Preferences.DEVICE_TOKEN, "");
        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
            FirebaseInstanceId.getInstance().getToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void changeFragment(Fragment fragment, boolean isAddtoBackstack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment, fragment.getClass().getSimpleName());
        if (isAddtoBackstack)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();


    }

    public void onLogOutClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(R.string.logout_confirm)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        performLogout();
                        dialogInterface.dismiss();

                    }


                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
        builder.show();
    }

}
