package com.cmexpertise.beautyapp.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cmexpertise.beautyapp.BeautyApplication;
import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.adapter.ServiceDetailsAdapter;
import com.cmexpertise.beautyapp.databinding.FragmentInfoBinding;
import com.cmexpertise.beautyapp.model.storeListmodel.StoreResponse;
import com.cmexpertise.beautyapp.model.storeServicemodel.ServicesList;
import com.cmexpertise.beautyapp.model.storeServicemodel.ServicesResponse;
import com.cmexpertise.beautyapp.mvvm.servicedetails.ServiceDetailsListNavigator;
import com.cmexpertise.beautyapp.mvvm.servicedetails.ServiceDetailsListViewModel;
import com.cmexpertise.beautyapp.util.Preferences;
import com.cmexpertise.beautyapp.util.Utils;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created kailash Patel
 */

public class InfoFragment extends BaseFragment implements OnMapReadyCallback, ServiceDetailsListNavigator {

    private GoogleMap mMap;
    private StoreResponse storeResponse;

    private Bundle bundle;
    private LinearLayoutManager linearLayoutManager;
    private ServiceDetailsAdapter servicesAdapter;
    private String storeId;
    private List<ServicesResponse> servicesResponseList;

    private FragmentInfoBinding binding;
    private ServiceDetailsListViewModel serviceDetailsListViewModel;
    private ServiceDetailsListNavigator serviceDetailsListNavigator;
    private Activity activity;
    private SkeletonScreen skeletonScreen;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getArguments();

        if (bundle != null)
        {
            storeResponse = bundle.getParcelable(Utils.INTENT_STORE_DETAILS);
            storeId = storeResponse.getId();
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false);
        View rootView = binding.getRoot();
        activity = BeautyApplication.getmInstance().getActivity();
        initComponents(rootView);

        return rootView;
    }

    @Override
    public void initComponents(View rootView) {

        serviceDetailsListNavigator = this;
        serviceDetailsListViewModel = new ServiceDetailsListViewModel(activity, serviceDetailsListNavigator);

        servicesResponseList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.fragmentStoreInfoLvService.setLayoutManager(linearLayoutManager);

        servicesAdapter = new ServiceDetailsAdapter(activity, servicesResponseList);
        binding.fragmentStoreInfoLvService.setAdapter(servicesAdapter);
        binding.fragmentStoreInfoLvService.setNestedScrollingEnabled(false);


        getAminities();
        ServiceList();

    }


    private void ServiceList() {

        storeId = storeResponse.getId();

        skeletonScreen = Skeleton.bind(binding.fragmentStoreInfoLvService)
                .adapter(servicesAdapter)
                .load(R.layout.row_loading_skeleton)
                .shimmer(false)
                .show();


        if (Utils.isOnline(activity, true)) {

            serviceDetailsListViewModel.getServiceList(storeId);


        } else {
            skeletonScreen = Skeleton.bind(binding.fragmentStoreInfoLvService)
                    .load(R.layout.nointernet_skeleton)
                    .adapter(servicesAdapter)
                    .shimmer(false)
                    .show();

        }


    }


    private void getAminities() {

        // SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));


        if (storeResponse != null) {
            String facilityList = storeResponse.getFacility();

            try {

                double distance = distance(Double.parseDouble(Preferences.readString(getActivity(), Preferences.SELECTED_CITY_LATITUDE, "")),
                        Double.parseDouble(Preferences.readString(getActivity(), Preferences.SELECTED_CITY_LONGITUDE, "")),
                        Double.parseDouble(storeResponse.getLatitude()), Double.parseDouble(storeResponse.getLongitude()));
            } catch (Exception e) {

            }
            if ((facilityList == null) || (facilityList.equals(""))) {

            } else {
                String strFacility[] = storeResponse.getFacility().split(",");
                for (int i = 0; i < strFacility.length; i++) {
                    switch (Integer.parseInt(strFacility[i])) {
                        case 1:
                            binding.fragmentStoreInfoImgWifi.setImageResource(R.drawable.ic_wifi_color);
                            binding.fragmentStoreInfoTxtWifi.setTextColor(getResources().getColor(R.color.colorPrimary));

                            break;
                        case 2:
                            binding.fragmentStoreInfoImgAc.setImageResource(R.drawable.ic_ac_color);
                            binding.fragmentStoreInfoTxtAc.setTextColor(getResources().getColor(R.color.colorPrimary));
                            break;
                        case 3:
                            binding.fragmentStoreInfoImgCard.setImageResource(R.drawable.ic_card_color);
                            binding.fragmentStoreInfoTxtCard.setTextColor(getResources().getColor(R.color.colorPrimary));
                            break;
                        case 4:

                            binding.fragmentStoreInfoImgTv.setImageResource(R.drawable.ic_tv_color);
                            binding.fragmentStoreInfoTxtTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            break;
                        case 5:

                            binding.fragmentStoreInfoImgCar.setImageResource(R.drawable.ic_car_color);
                            binding.fragmentStoreInfoTxtCar.setTextColor(getResources().getColor(R.color.colorPrimary));
                            break;
                    }
                }
            }
            //  mapFragment.getMapAsync(this);
        }
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {

            mMap = googleMap;


            LatLng sydney = new LatLng(Double.parseDouble(storeResponse.getLatitude() + ""), Double.parseDouble(storeResponse.getLongitude() + ""));
            mMap.addMarker(new MarkerOptions().position(sydney).title(getString(R.string.str_drop_location)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void handleError(Throwable throwable) {

        Toast.makeText(activity, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void storeResponce(ServicesList userResponse) {

        skeletonScreen.hide();

        if (userResponse.getServiceResponsedata().getSuccess().equalsIgnoreCase("1") && userResponse.getServiceResponsedata().getData().size() > 0) {
            servicesAdapter.addData(userResponse.getServiceResponsedata().getData());
        } else {
            emptyView();
        }
    }


    private void emptyView() {

        skeletonScreen = Skeleton.bind(binding.fragmentStoreInfoLvService)
                .adapter(servicesAdapter)
                .load(R.layout.empty_skeleton)
                .shimmer(false)
                .show();

    }


}
