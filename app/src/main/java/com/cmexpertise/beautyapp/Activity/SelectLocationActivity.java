package com.cmexpertise.beautyapp.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.adapter.CityListAdapter;
import com.cmexpertise.beautyapp.databinding.ActivitySelectLocationBinding;
import com.cmexpertise.beautyapp.model.locationModel.LocationModel;
import com.cmexpertise.beautyapp.model.locationModel.LocationResponse;
import com.cmexpertise.beautyapp.model.locationModel.LocationResponseData;
import com.cmexpertise.beautyapp.model.locationModel.LocationResponseList;
import com.cmexpertise.beautyapp.mvvm.locationdata.LocationDataViewModel;
import com.cmexpertise.beautyapp.mvvm.locationdata.LocationNavigator;
import com.cmexpertise.beautyapp.service.CurrentLocation;
import com.cmexpertise.beautyapp.util.Constans;
import com.cmexpertise.beautyapp.util.Preferences;
import com.cmexpertise.beautyapp.util.Utils;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kailash Patel
 */
public class SelectLocationActivity extends BaseActivity implements LocationNavigator {


    //  private Toolbar toolbar;
    private RecyclerView rvCityList;
    private RelativeLayout rlMyLocation;
    private EditText etSearch;
    private LinearLayout llMainView;

    //Goolge Location Componet
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    protected String mLastUpdateTime;
    protected Boolean mRequestingLocationUpdates;

    private CityListAdapter cityListAdapter, fillterData;
    private List<LocationResponse> searchList = new ArrayList<LocationResponse>();

    private int MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;
    private LocationResponseList cityResponseList = new LocationResponseList();
    private String fromSetting;

    private LocationDataViewModel viewModel;
    private SkeletonScreen skeletonScreen;
    private ActivitySelectLocationBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initComponents();
        updateValuesFromBundle(savedInstanceState);
        createLocationRequest();

    }

    @Override
    public void initComponents() {


        viewModel = new LocationDataViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_location);
        binding.setLocationDataViewModel(viewModel);


        rvCityList = binding.activitySelectLocationRvCity;
        rlMyLocation = binding.activitySelectLocationRlSelectLocation;
        etSearch = binding.activitySelectLocationEtSearchLocation;
        llMainView = binding.activitySelectLocationLlMain;

        rlMyLocation.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        rvCityList.setLayoutManager(new LinearLayoutManager(SelectLocationActivity.this));


        getLocation();


        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";


    }


    private void getLocation() {


        Bundle intentLocation = getIntent().getExtras();

        if (intentLocation != null) {

            rlMyLocation.setOnClickListener(this);
            getCityList();


        } else {
            if ((!Preferences.readString(this, Preferences.SELECTED_LOCATION_ID, "").equals(""))
                    && ((!Preferences.readString(this, Preferences.SELECTED_CITY_LATITUDE, "").equals(""))
                    || (!Preferences.readString(this, Preferences.SELECTED_CITY_LONGITUDE, "").equals("")))) {

                Intent intent = new Intent(this, SelectCategoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
            } else {
                rlMyLocation.setOnClickListener(this);
                getCityList();
            }

        }

    }

    private void getCityList() {

        if (!Utils.isOnline(SelectLocationActivity.this, true)) {
            showErrorWithInternet();
        } else {

            skeletonScreen = Skeleton.bind(binding.activitySelectLocationRvCity)
                    .adapter(cityListAdapter)
                    .load(R.layout.row_loading_skeleton)
                    .shimmer(false)
                    .show();


            viewModel.getLocationData();

        }


    }

    @Override
    public void onClick(View v) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (v == rlMyLocation) {
            setPermision();
        } else if (v == etSearch) {
            searchClickHandle();
            performSearch();
        }


    }

    private void searchClickHandle() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    performSearch();
                    return true;
                }
                return false;
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                performSearch();
            }
        });


        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
    }


    private void performSearch() {

        String strText = etSearch.getText().toString().trim();

        if (strText.equals("")) {
            fillterData = new CityListAdapter(SelectLocationActivity.this, cityResponseList.getData());
            rvCityList.setAdapter(fillterData);
        } else {
            searchList.clear();
            for (int i = 0; i < cityResponseList.getData().size(); i++) {

                if (cityResponseList.getData().get(i).getName().toLowerCase().contains(strText)) {
                    searchList.add(cityResponseList.getData().get(i));

                }
            }
            fillterData = new CityListAdapter(SelectLocationActivity.this, searchList);
            cityListAdapter = null;
            fillterData.notifyDataSetChanged();
            rvCityList.setAdapter(fillterData);

        }
        fillterData.SetOnItemClickListener(new CityListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, LocationResponse locationResponse, int position) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                locationSelection(locationResponse);
            }
        });
    }

    private void locationSelection(LocationResponse locationResponse) {

        if (!Utils.isOnline(SelectLocationActivity.this, true)) {
            showErrorWithInternet();
        } else {

            distanceCalculate(locationResponse);

        }
    }

    private void distanceCalculate(LocationResponse locationResponse) {

        Preferences.writeString(SelectLocationActivity.this, Preferences.SELECTED_CITY_LATITUDE, "");
        Preferences.writeString(SelectLocationActivity.this, Preferences.SELECTED_CITY_LONGITUDE, "");

        String latitude = Preferences.readString(SelectLocationActivity.this, Preferences.SELECTED_CITY_LATITUDE, "");
        String longitude = Preferences.readString(SelectLocationActivity.this, Preferences.SELECTED_CITY_LONGITUDE, "");

        if (latitude.isEmpty() || longitude.isEmpty()) {

            if (Geocoder.isPresent()) {
                try {
                    String location = locationResponse.getName();
                    Geocoder gc = new Geocoder(this);
                    List<Address> addresses = gc.getFromLocationName(location, 1); // get the found Address Objects
                    if (addresses != null && addresses.size() > 0) {
                        List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                        for (Address a : addresses) {
                            if (a.hasLatitude() && a.hasLongitude()) {

                                Preferences.writeString(SelectLocationActivity.this, Preferences.SELECTED_CITY_LATITUDE, a.getLatitude() + "");
                                Preferences.writeString(SelectLocationActivity.this, Preferences.SELECTED_CITY_LONGITUDE, a.getLongitude() + "");
                                Preferences.writeString(SelectLocationActivity.this, Preferences.SELECTED_LOCATION_ID, locationResponse.getId());
                                Preferences.writeString(SelectLocationActivity.this, Preferences.SELECTED_CITY, locationResponse.getName());

                                if (fromSetting != null) {
                                    fromSetting = null;
                                    Preferences.writeBoolean(getApplicationContext(), Preferences.SELECTEDLOCATIONORCITY, false);
                                    Intent intent = new Intent(this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    SelectLocationActivity.this.finish();
                                } else {
                                    startActivity(new Intent(SelectLocationActivity.this, SelectCategoryActivity.class));
                                    finish();
                                }
                                break;
                            }
                        }
                    } else {
                        return;
                    }
                } catch (IOException e) {

                }
            }
        }


    }


    private void setPermision() {
        if (Build.VERSION.SDK_INT < 23) {

            startService(new Intent(getApplicationContext(), CurrentLocation.class));
        } else {
            if (ContextCompat.checkSelfPermission(SelectLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(SelectLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startService(new Intent(getApplicationContext(), CurrentLocation.class));
                updateLocationUI();
            } else {
                Utils.checkPermitionCameraGaller(SelectLocationActivity.this);
            }
        }

    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case 1050: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startService(new Intent(getApplicationContext(), CurrentLocation.class));

                } else {
                }
            }
        }

    }


    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            if (savedInstanceState.keySet().contains(Constans.KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        Constans.KEY_REQUESTING_LOCATION_UPDATES);
            }

            if (savedInstanceState.keySet().contains(Constans.KEY_LOCATION)) {

                mCurrentLocation = savedInstanceState.getParcelable(Constans.KEY_LOCATION);
            }

            if (savedInstanceState.keySet().contains(Constans.KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(Constans.KEY_LAST_UPDATED_TIME_STRING);
            }

        }
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(Constans.UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(Constans.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void updateLocationUI() {
        if (Constans.CURRENT_LATITUDE != 0.0) {


            if (fromSetting != null) {
                fromSetting = null;
                Preferences.writeBoolean(getApplicationContext(), Preferences.SELECTEDLOCATIONORCITY, true);
                Intent intent = new Intent(SelectLocationActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                SelectLocationActivity.this.finish();
            } else {
                startActivity(new Intent(SelectLocationActivity.this, SelectCategoryActivity.class));
                finish();
            }
            return;
        }
    }

    private void showErrorWithInternet() {

        skeletonScreen = Skeleton.bind(binding.activitySelectLocationRvCity)
                .load(R.layout.nointernet_skeleton)
                .shimmer(false)
                .show();
    }


    @Override
    public void handleError(Throwable throwable) {

        skeletonScreen.hide();
        Snackbar.make(llMainView, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void locationResponce(LocationResponseData responseBase) {


        skeletonScreen.hide();

        if (responseBase != null) {
            if (Integer.parseInt(responseBase.getResponsedata().getSuccess()) == 1 && responseBase.getResponsedata().getData().size() > 0) {
                cityResponseList = responseBase.getResponsedata();
                cityListAdapter = new CityListAdapter(SelectLocationActivity.this, responseBase.getResponsedata().getData());
                rvCityList.setAdapter(cityListAdapter);
                cityListAdapter.notifyDataSetChanged();

                cityListAdapter.SetOnItemClickListener(new CityListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, LocationResponse locationResponse, int position) {
                        locationSelection(locationResponse);
                    }
                });
            } else {

                skeletonScreen = Skeleton.bind(binding.activitySelectLocationRvCity)
                        .load(R.layout.no_datafound_skeleton)
                        .shimmer(false)
                        .show();
            }
        }


    }
}


