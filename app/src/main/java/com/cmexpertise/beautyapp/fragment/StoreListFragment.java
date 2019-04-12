package com.cmexpertise.beautyapp.fragment;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.cmexpertise.beautyapp.BeautyApplication;
import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.adapter.StoreListAdapter;
import com.cmexpertise.beautyapp.databinding.FragmentProductlistBinding;
import com.cmexpertise.beautyapp.model.storeListmodel.StoreResponse;
import com.cmexpertise.beautyapp.model.storeListmodel.StoreResponseData;
import com.cmexpertise.beautyapp.mvvm.storelist.StoreListNavigator;
import com.cmexpertise.beautyapp.mvvm.storelist.StoreViewModel;
import com.cmexpertise.beautyapp.util.Utils;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;

import java.util.ArrayList;


public class StoreListFragment extends BaseFragment implements StoreListAdapter.OnItemClickListener, StoreListNavigator {


    //Declaration
    
    private SearchView searchView;
    private String searchKeyWord = "";
    private boolean isFromSearch = false;
    private boolean isDataLoadingFromServer = false;


    private ArrayList<StoreResponse> modelArrayList = new ArrayList<>();
    private StoreListAdapter productListAdapter;

    private static int MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;
    private int lastVisibleItem;
    private int totalItemCount;
    private int visibleThreshold = 1;
    private int pageItemCount = 0;
    private int pageIndex = 0;
    private Activity activity;


    private StoreListNavigator storeListNavigator;
    private StoreViewModel storeViewModel;
    private FragmentProductlistBinding fragmentProductlistBinding;


    private View rootView;
    private SkeletonScreen skeletonScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentProductlistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_productlist, container, false);
        rootView = fragmentProductlistBinding.getRoot();
        activity= BeautyApplication.getmInstance().getActivity();
        initToolbar();
        initComponents(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void initComponents(View rootView)
    {

        storeListNavigator=this;
        storeViewModel = new StoreViewModel(activity,storeListNavigator);
        fragmentProductlistBinding.setStoreViewModel(storeViewModel);
        fragmentProductlistBinding.fragmentProductlistRvProductList.setHasFixedSize(true);


        setUpAdapater(modelArrayList);
        setUpSkelotView();


    }

    private void setUpSkelotView() {


        if (Utils.isOnline(activity, true)) {

            skeletonScreen = Skeleton.bind(fragmentProductlistBinding.fragmentProductlistRvProductList)
                    .adapter(productListAdapter)
                    .load(R.layout.row_loading_skeleton)
                    .shimmer(false)
                    .show();

            getProductListData(false);

        } else {
            skeletonScreen = Skeleton.bind(fragmentProductlistBinding.fragmentProductlistRvProductList)
                    .load(R.layout.nointernet_skeleton)
                    .shimmer(false)
                    .show();

        }


    }


    public void initToolbar() {
      //  ((MenuBarActivity) activity).setUpToolbar(getString(R.string.menu_product), false);

    }

    public void getProductListData(final boolean isLoadmore) {

        if (Utils.isOnline(activity, true)) {

            isDataLoadingFromServer = true;
            fragmentProductlistBinding.fragmentProductlistLlLoadMoreProgress.setVisibility(isLoadmore ? View.VISIBLE : View.GONE);
            storeViewModel.getStoreList("19,2,4,5", "1", "" + pageIndex);

        } else {
            skeletonScreen = Skeleton.bind(fragmentProductlistBinding.fragmentProductlistRvProductList)
                    .load(R.layout.nointernet_skeleton)
                    .shimmer(false)
                    .show();

        }
    }

    @Override
    public void onItemClick(View view, StoreResponse viewModel) {

        /**
         * Logic to Prevent the Launch of the Fragment Twice if User makes
         * the Tap(Click) very Fast.
         */
        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        Utils.hideKeyboard(activity);

    }

    /**
     * Option menu for Searchview
     *
     * @param menu
     * @param inflater
     */

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem menuItem = menu.findItem(R.id.menu_search);


        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.actionbar_search_hint));
        searchView.setGravity(Gravity.END);
        final LinearLayout searchBar = (LinearLayout) searchView.findViewById(R.id.search_bar);
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setStartDelay(LayoutTransition.APPEARING, 100);
        searchBar.setLayoutTransition(layoutTransition);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchKeyWord = "";
                return true;
            }
        });


        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchKeyWord = "";
                isFromSearch = false;

                if (Utils.isNetworkAvailable(activity)) {
                    modelArrayList.clear();
                    pageIndex = 0;
                    getProductListData(false);
                } else {
                    Utils.snackbar(fragmentProductlistBinding.fragmentProductlistLlContainer, "" + getString(R.string.check_connection), true, activity);
                }

                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                if (!isDataLoadingFromServer) {
                    isFromSearch = true;
                    searchKeyWord = query;

                    //Clear list data before search
                    pageIndex = 0;
                    modelArrayList.clear();
                    getProductListData(false);
                    return false;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (isFromSearch) {
            menuItem.expandActionView();
            if (!TextUtils.isEmpty(searchKeyWord))
                searchView.setQuery(searchKeyWord, false);
        }

    }


    private void emptyView() {

        skeletonScreen = Skeleton.bind(fragmentProductlistBinding.fragmentProductlistRvProductList)
                .load(R.layout.empty_skeleton)
                .shimmer(false)
                .show();

    }

    private void setUpAdapater(final ArrayList<StoreResponse> guidelineModelArrayList) {


        fragmentProductlistBinding.fragmentProductlistRvProductList.removeAllViews();
        fragmentProductlistBinding.fragmentProductlistRvProductList.setHasFixedSize(true);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(activity, 2);
        fragmentProductlistBinding.fragmentProductlistRvProductList.setLayoutManager(mLayoutManager);

        productListAdapter = new StoreListAdapter(StoreListFragment.this, activity, guidelineModelArrayList);
        productListAdapter.setOnItemClickListener(this);
        fragmentProductlistBinding.fragmentProductlistRvProductList.setAdapter(productListAdapter);

        fragmentProductlistBinding.fragmentProductlistRvProductList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                if (totalItemCount > 0) {

                    if (!productListAdapter.isLoading() && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (pageItemCount > totalItemCount) {
                            productListAdapter.setLoading();
                            pageIndex++;
                            getProductListData(true);
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
            initToolbar();

        }
    }


    private void hideKeyBoard() {
        if (searchView != null) {
            searchView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }

            }, 300);
        }


    }


    @Override
    public void handleError(Throwable throwable) {
        Utils.snackbar(fragmentProductlistBinding.fragmentProductlistLlContainer, "" + throwable.getMessage(), true, activity);
    }

    @Override
    public void storeResponce(StoreResponseData userResponse) {

        isDataLoadingFromServer = false;
        skeletonScreen.hide();

        if (searchView != null) {
            hideKeyBoard();
        }


        if (userResponse.getResponsedata().getSuccess().equalsIgnoreCase("1")) {

            ArrayList<StoreResponse> productModelArrayList = (ArrayList<StoreResponse>) userResponse.getResponsedata().getData();
            pageItemCount = userResponse.getResponsedata().getCount();
            productListAdapter.setLoaded();
            fragmentProductlistBinding.fragmentProductlistLlLoadMoreProgress.setVisibility(View.GONE);

            if (productModelArrayList != null && productModelArrayList.size() > 0) {

                if (productListAdapter != null) {
                    modelArrayList.addAll(productModelArrayList);
                    productListAdapter.addRecord(modelArrayList);
                    productListAdapter.notifyDataSetChanged();

                } else {
                    modelArrayList.addAll(productModelArrayList);
                    setUpAdapater(modelArrayList);
                }


            } else {

                if (modelArrayList.size() < 0) {
                    emptyView();
                }
            }
        } else {

            emptyView();
        }


    }
}
