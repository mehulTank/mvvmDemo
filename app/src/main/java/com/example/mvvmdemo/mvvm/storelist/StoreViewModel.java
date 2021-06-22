package com.example.mvvmdemo.mvvm.storelist;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.mvvmdemo.BeautyApplication;
import com.example.mvvmdemo.model.storeListmodel.StoreResponse;
import com.example.mvvmdemo.model.storeListmodel.StoreResponseData;
import com.example.mvvmdemo.webservice.UsersService;

import java.util.ArrayList;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by Nishidh Patel
 */

public class StoreViewModel extends Observable {

    static StoreViewModel storeViewModel;
    ArrayList<StoreResponse> productModelArrayList;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private StoreListNavigator storeListNavigator;


    public StoreViewModel(@NonNull Context context, StoreListNavigator storeListNavigator) {
        this.context = context;
        this.storeListNavigator = storeListNavigator;
        storeViewModel = this;
        productModelArrayList = new ArrayList<>();
    }

    @BindingAdapter(value = "android:scrollEvent", requireAll = false)
    public static void setScrollingEventRecylerView(RecyclerView recylerView, int a) {

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recylerView.getLayoutManager();
        Log.d("---", "--" + linearLayoutManager.findLastVisibleItemPosition());

        recylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                Log.d("---", "--" + linearLayoutManager.findLastVisibleItemPosition());

                Log.d("--- state change", "--- state change");


                //storeViewModel.getStoreList("" + 17, "0");


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.d("--- onScrolled", "--- onScrolled");
            }
        });
       /* Picasso.with(imageView.getContext()).load(url)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.placeholder_thumb)
                .resize(400, 300)
                .into(imageView);*/


    }

    public void getStoreList(final String locationId, final String offset) {


        try {
            BeautyApplication appController = BeautyApplication.getmInstance();
            UsersService usersService = appController.getUserService();


            Disposable disposable = usersService.doGetLocation(locationId, offset)
                    .subscribeOn(appController.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userResponse -> storeListNavigator.storeResponce(userResponse),
                            throwable -> storeListNavigator.handleError(throwable));

            compositeDisposable.add(disposable);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getStoreListLatlng(final String lat, final String lng, final String offset) {


        try {
            BeautyApplication appController = BeautyApplication.getmInstance();
            UsersService usersService = appController.getUserService();


            Disposable disposable = usersService.doGetLocationWithLatLng(lat, lng, offset)
                    .subscribeOn(appController.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userResponse -> updateStoreListResponse(userResponse),
                            throwable -> storeListNavigator.handleError(throwable));

            compositeDisposable.add(disposable);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void updateStoreListResponse(StoreResponseData storeResponseData) {


        if (storeResponseData != null) {
            productModelArrayList.addAll(storeResponseData.getResponsedata().getData());
            setChanged();
            notifyObservers();
        }


    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }


}

