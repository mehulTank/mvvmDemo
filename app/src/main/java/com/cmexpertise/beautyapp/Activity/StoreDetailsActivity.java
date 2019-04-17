package com.cmexpertise.beautyapp.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.databinding.ActivityStoreDetailBinding;
import com.cmexpertise.beautyapp.fragment.GalleryListFragment;
import com.cmexpertise.beautyapp.fragment.InfoFragment;
import com.cmexpertise.beautyapp.model.storeListmodel.StoreResponse;
import com.cmexpertise.beautyapp.util.Constans;
import com.cmexpertise.beautyapp.util.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2/2/2017.
 */
public class StoreDetailsActivity extends AppCompatActivity {


    private StoreResponse storeIntentResponse;
    private DecimalFormat decimalFormat;
    private String storeIndex = "";

    private Intent bundle;
    private String offerName;
    private String OfferPrice;
    private static int currentPage = 0;

    private ActivityStoreDetailBinding binding;
    private Fragment fragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_store_detail);
        bundle = getIntent();

        if (bundle != null) {
            storeIntentResponse = bundle.getParcelableExtra(Utils.INTENT_STORE_DETAILS);
        }


        initComponet();


    }


    private void initComponet() {


        decimalFormat = new DecimalFormat("#.00");
        setupToolbar();
        setUpData();

        binding.activitySettingToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }


    private void setupToolbar() {
        setSupportActionBar(binding.activitySettingToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }


    private void setUpData() {


        if (storeIntentResponse != null) {
            binding.fragmentStoreInfoTxtStoreName.setText("" + storeIntentResponse.getName());
            binding.fragmentStoreInfoTxtStoreAdd.setText("" + storeIntentResponse.getAddress());
            binding.fragmentStoreInfoTxtOpnTime.setText(getString(R.string.open_till) + " " + storeIntentResponse.getCloseTime());
            binding.fragmentStoreRowTxtStoreRbStore.setRating(Float.parseFloat(storeIntentResponse.getAvgRate()));


            try {

                double startLat = Constans.CURRENT_LATITUDE;
                double startLng = Constans.CURRENT_LONGITUDE;
                double endLat = Double.parseDouble(storeIntentResponse.getLatitude());
                double endLng = Double.parseDouble(storeIntentResponse.getLongitude());


                double distanceCal = distance(startLat, startLng, endLat, endLng);
                binding.fragmentStoreInfoTxtDistance.setText(decimalFormat.format((distanceCal)) + " " + getString(R.string.kmaway));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            fragment = new InfoFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Utils.INTENT_STORE_DETAILS, storeIntentResponse);
            fragment.setArguments(bundle);
            replaceFragment(fragment);


            ArrayList<String> imgArr = new ArrayList<>();
            imgArr.add(storeIntentResponse.getImage());

            SlidingImage_Adapter slidingImage_adapter = new SlidingImage_Adapter(StoreDetailsActivity.this, imgArr);
            binding.vpGallery.setAdapter(slidingImage_adapter);
            binding.indicator.setViewPager(binding.vpGallery);

            //autoStartVIewPager(imgArr);
            binding.appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = false;
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + verticalOffset == 0) {

                        binding.collapsingToolbar.setTitle(storeIntentResponse.getName());
                        isShow = true;
                    } else if (isShow) {
                        binding.collapsingToolbar.setTitle(" ");
                        isShow = false;
                    }

                }
            });


            setUpTab();


        }


    }

    private void setUpTab() {

        binding.activitySettingTlMain.addTab(binding.activitySettingTlMain.newTab().setText(getString(R.string.menu_details)));
        binding.activitySettingTlMain.addTab(binding.activitySettingTlMain.newTab().setText(getString(R.string.menu_gallery)));
        binding.activitySettingTlMain.addTab(binding.activitySettingTlMain.newTab().setText(getString(R.string.menu_offers)));
        binding.activitySettingTlMain.setTabGravity(TabLayout.GRAVITY_FILL);


        binding.activitySettingTlMain.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    setUpTitle("", true);
                    fragment = new InfoFragment();

                } else if (tab.getPosition() == 1) {
                    setUpTitle(storeIntentResponse.getName(), true);
                    fragment = new GalleryListFragment();
                }


                Bundle bundle = new Bundle();
                bundle.putParcelable(Utils.INTENT_STORE_DETAILS, storeIntentResponse);
                fragment.setArguments(bundle);
                replaceFragment(fragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void autoStartVIewPager(ArrayList<String> imgArr) {

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == imgArr.size()) {
                    currentPage = 0;
                }
                binding.vpGallery.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        binding.indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {

        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lon1);
        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lon2);
        return (locationA.distanceTo(locationB) / 1000);

    }


    private void handleBottomNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_info:

                setUpTitle("", true);
                fragment = new InfoFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Utils.INTENT_STORE_DETAILS, storeIntentResponse);
                replaceFragment(fragment);


                break;
            case R.id.action_gallery:
                setUpTitle(storeIntentResponse.getName(), true);
                break;
            case R.id.action_offer:
                setUpTitle(storeIntentResponse.getName(), true);
                break;
        }
    }

    private void setUpTitle(String title, boolean isExpand) {
        binding.collapsingToolbar.setTitle("" + title);
        binding.activitySettingToolbar.setTitle("");
        binding.appbarlayout.setExpanded(isExpand);
    }


//    public void displayView(int i)
//    {
//
//        Fragment fragment = null;
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        switch (i) {
//            case 0:
//                if (fragmentManager.findFragmentByTag(InfoFragment.class.getName()) == null)
//                {
//                    fragment = new InfoFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(Utility.INTENT_STORE_DETAILS, storeIntentResponse);
//                    fragment.setArguments(bundle);
//                } else {
//                    fragment = fragmentManager.findFragmentByTag(InfoFragment.class.getName());
//                }
//                replaceFragment(fragment, InfoFragment.class.getName());
//                break;
//
//
//            case 1:
//                if (fragmentManager.findFragmentByTag(GalleryFragment.class.getName()) == null)
//                {
//                    fragment = new GalleryFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(Utility.INTENT_STORE_DETAILS, storeIntentResponse);
//                    fragment.setArguments(bundle);
//                } else {
//                    fragment = fragmentManager.findFragmentByTag(GalleryFragment.class.getName());
//                }
//                replaceFragment(fragment, GalleryFragment.class.getName());
//                break;
//
//
//            case 2:
//
//                if (fragmentManager.findFragmentByTag(StaffListFragment.class.getName()) == null)
//                {
//                    fragment = StaffListFragment.newInstance(storeIntentResponse.getId(), storeIntentResponse);
//
//                } else {
//                    fragment = fragmentManager.findFragmentByTag(StaffListFragment.class.getName());
//                }
//                replaceFragment(fragment, StaffListFragment.class.getName());
//                break;
//
//
//            case 3:
//
//                if (fragmentManager.findFragmentByTag(OfferListFragment.class.getName()) == null)
//                {
//                    fragment = new OfferListFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(Utility.INTENT_STORE_DETAILS, storeIntentResponse);
//                    bundle.putString("StoreId", storeIntentResponse.getId());
//
//                    fragment.setArguments(bundle);
//                } else {
//                    fragment = fragmentManager.findFragmentByTag(OfferListFragment.class.getName());
//                }
//                replaceFragment(fragment, OfferListFragment.class.getName());
//
//                break;
//
//
//
//            case 4:
//                if (fragmentManager.findFragmentByTag(BookAppointmentFragment.class.getName()) == null)
//                {
//                    fragment = new BookAppointmentFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("StoreId", storeIntentResponse.getId());
//                    bundle.putParcelable(Utility.INTENT_STORE_DETAILS, storeIntentResponse);
//                    bundle.putString("StaffId", "-1");
//                    bundle.putString("StaffFName","Any");
//
//
//                    if (this.bundle.getString(Utility.INTENT_OFFER_ID) != null && this.bundle.getString(Utility.INTENT_OFFER_TOTAL_AMT) != null) {
//                        bundle.putString("Services", this.bundle.getString(Utility.INTENT_SERVICES));
//                        bundle.putString("OfferName", offerName);
//                        bundle.putString("OfferPrice", OfferPrice);
//                        bundle.putString("offferID", this.bundle.getString(Utility.INTENT_OFFER_ID));
//                        bundle.putString("totalAmount", this.bundle.getString(Utility.INTENT_OFFER_TOTAL_AMT));
//                    }
//                    bundle.putParcelable(Utility.INTENT_STORE_DETAILS, storeIntentResponse);
//                    fragment.setArguments(bundle);
//                } else {
//                    fragment = fragmentManager.findFragmentByTag(BookAppointmentFragment.class.getName());
//                }
//                replaceFragment(fragment, BookAppointmentFragment.class.getName());
//
//                break;
//        }
//
//    }


    public void replaceFragment(Fragment fragment) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.commit();
        manager.executePendingTransactions();

    }


}
