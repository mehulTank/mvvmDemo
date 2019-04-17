package com.cmexpertise.beautyapp.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmexpertise.beautyapp.BeautyApplication;
import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.adapter.GalleryListAdapter;
import com.cmexpertise.beautyapp.databinding.FragmentGallerylistBinding;
import com.cmexpertise.beautyapp.model.storeGallrymodel.StoreGalleryResponse;
import com.cmexpertise.beautyapp.model.storeGallrymodel.StoreGalleryResponseData;
import com.cmexpertise.beautyapp.model.storeListmodel.StoreResponse;
import com.cmexpertise.beautyapp.mvvm.gallery.GalleryListNavigator;
import com.cmexpertise.beautyapp.mvvm.gallery.GalleryListViewModel;
import com.cmexpertise.beautyapp.util.Utils;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;

import java.util.ArrayList;


public class GalleryListFragment extends BaseFragment implements GalleryListNavigator, GalleryListAdapter.OnItemClickListener {


    //Declaration
    private ArrayList<StoreGalleryResponse> modelArrayList = new ArrayList<>();
    private GalleryListAdapter galleryListAdapter;

    private static int MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;
    private int lastVisibleItem;
    private int totalItemCount;
    private int visibleThreshold = 1;
    private int pageItemCount = 0;
    private int pageIndex = 0;
    private Activity activity;

    private GalleryListNavigator galleryListNavigator;
    private GalleryListViewModel galleryListViewModel;
    private FragmentGallerylistBinding binding;


    private View rootView;
    private SkeletonScreen skeletonScreen;
    private StoreResponse storeResponse;
    private Bundle bundle;
    private String storeId = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        bundle = getArguments();

        if (bundle != null) {
            storeResponse = bundle.getParcelable(Utils.INTENT_STORE_DETAILS);
            storeId = storeResponse.getId();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallerylist, container, false);
        rootView = binding.getRoot();
        activity = BeautyApplication.getmInstance().getActivity();
        initComponents(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void initComponents(View rootView) {

        galleryListNavigator = this;
        galleryListViewModel = new GalleryListViewModel(activity, galleryListNavigator);
        binding.setGalleryListViewModel(galleryListViewModel);
        binding.fragmentGallerylistRvList.setHasFixedSize(true);


        setUpAdapater(modelArrayList);
        setUpSkelotView();


    }

    private void setUpSkelotView() {


        skeletonScreen = Skeleton.bind(binding.fragmentGallerylistRvList)
                .adapter(galleryListAdapter)
                .load(R.layout.row_loading_skeleton)
                .shimmer(false)
                .show();

        getGalleryListData(false);


    }


    public void getGalleryListData(final boolean isLoadmore) {

        if (Utils.isOnline(activity, true)) {

            binding.fragmentGallerylistLlLoadMoreProgress.setVisibility(isLoadmore ? View.VISIBLE : View.GONE);
            galleryListViewModel.getGalleryList(storeId, "" + pageIndex);


        } else {
            skeletonScreen = Skeleton.bind(binding.fragmentGallerylistRvList)
                    .load(R.layout.nointernet_skeleton)
                    .adapter(galleryListAdapter)
                    .shimmer(false)
                    .show();

        }
    }


    private void emptyView() {

        skeletonScreen = Skeleton.bind(binding.fragmentGallerylistRvList)
                .adapter(galleryListAdapter)
                .load(R.layout.empty_skeleton)
                .shimmer(false)
                .show();

    }

    private void setUpAdapater(final ArrayList<StoreGalleryResponse> guidelineModelArrayList) {


        binding.fragmentGallerylistRvList.removeAllViews();
        binding.fragmentGallerylistRvList.setHasFixedSize(true);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(activity, 2);
        binding.fragmentGallerylistRvList.setLayoutManager(mLayoutManager);

        galleryListAdapter = new GalleryListAdapter(GalleryListFragment.this, activity, guidelineModelArrayList);
        galleryListAdapter.setOnItemClickListener(this);
        binding.fragmentGallerylistRvList.setAdapter(galleryListAdapter);

        binding.fragmentGallerylistRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                if (totalItemCount > 0) {

                    if (!galleryListAdapter.isLoading() && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (pageItemCount > totalItemCount) {
                            galleryListAdapter.setLoading();
                            pageIndex++;
                            getGalleryListData(true);
                        }
                    }
                }
            }
        });


    }


    @Override
    public void onClick(View v) {


    }


    /**
     * Called when coming back from next screen
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHasOptionsMenu(true);

        }
    }


    @Override
    public void handleError(Throwable throwable) {
        emptyView();
        Utils.snackbar(binding.fragmentGallerylistLlContainer, "" + throwable.getMessage(), true, activity);
    }

    @Override
    public void storeResponce(StoreGalleryResponseData userResponse) {

        skeletonScreen.hide();

        if (userResponse.getResponsedata().getSuccess().equalsIgnoreCase("1")) {

            ArrayList<StoreGalleryResponse> productModelArrayList = (ArrayList<StoreGalleryResponse>) userResponse.getResponsedata().getData();
            pageItemCount = Integer.parseInt(userResponse.getResponsedata().getCount());
            galleryListAdapter.setLoaded();
            binding.fragmentGallerylistLlLoadMoreProgress.setVisibility(View.GONE);

            if (productModelArrayList != null && productModelArrayList.size() > 0) {
                modelArrayList.addAll(productModelArrayList);
                galleryListAdapter.addRecord(modelArrayList);
                galleryListAdapter.notifyDataSetChanged();


            } else {

                if (modelArrayList.size() < 0) {
                    emptyView();
                }
            }
        } else {

            emptyView();
        }


    }


    @Override
    public void onItemClick(View view, StoreGalleryResponse viewModel) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        Utils.hideKeyboard(activity);

    }
}
