package com.cmexpertise.beautyapp.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.adapter.CategoriesAdapter;
import com.cmexpertise.beautyapp.databinding.ActivitySelectCategoryBinding;
import com.cmexpertise.beautyapp.model.categoryModel.Categories;
import com.cmexpertise.beautyapp.model.categoryModel.CategoryResponse;
import com.cmexpertise.beautyapp.model.categoryModel.CategoryResponseData;
import com.cmexpertise.beautyapp.mvvm.category.CategoryDataViewModel;
import com.cmexpertise.beautyapp.mvvm.category.CategoryNavigator;
import com.cmexpertise.beautyapp.util.Preferences;
import com.cmexpertise.beautyapp.util.Utils;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kailash Patel
 */
public class SelectCategoryActivity extends AppCompatActivity implements CategoryNavigator, View.OnClickListener {


    private List<CategoryResponse> currentSelectedItems = new ArrayList<>();
    private CategoriesAdapter categoriesAdapter;
    private int MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;


    private ActivitySelectCategoryBinding binding;
    private CategoryDataViewModel viewModel;
    private SkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponet();
    }


    public void initComponet() {
        viewModel = new CategoryDataViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_category);
        binding.setCategoryDataViewModel(viewModel);


        binding.activitySelectCategoryRvCategory.setLayoutManager(new LinearLayoutManager(SelectCategoryActivity.this));
        binding.activitySelectCategoryTvNext.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);


        categoriesAdapter = new CategoriesAdapter(SelectCategoryActivity.this, currentSelectedItems, SelectCategoryActivity.this);
        binding.activitySelectCategoryRvCategory.setAdapter(categoriesAdapter);
        getData();


    }

    public void getData() {
        Bundle intentLocation = getIntent().getExtras();


        if (intentLocation != null) {

            getCategoryList();


        } else {
            if ((!Preferences.readString(this, Preferences.SELECTED_CATEGORIES_ID, "").equals(""))) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                SelectCategoryActivity.this.finish();
            } else {
                getCategoryList();
            }
        }

    }

    private void getCategoryList() {

        if (!Utils.isOnline(SelectCategoryActivity.this, true)) {
            showErrorWithInternet();
        } else {

            skeletonScreen = Skeleton.bind(binding.activitySelectCategoryRvCategory)
                    .adapter(categoriesAdapter)
                    .load(R.layout.row_loading_skeleton)
                    .shimmer(false)
                    .show();


            viewModel.getCategoryList();

        }
    }

    private void showErrorWithInternet() {

        skeletonScreen = Skeleton.bind(binding.activitySelectCategoryRvCategory)
                .load(R.layout.nointernet_skeleton)
                .shimmer(false)
                .show();

    }


    @Override
    public void onClick(View v) {


        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (v == binding.activitySelectCategoryTvNext) {
            if (currentSelectedItems.equals("")) {
                Snackbar snack = Snackbar.make(binding.mainView, R.string.atleast_one_category, Snackbar.LENGTH_LONG);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.show();
            } else {
                if (currentSelectedItems.size() > 0) {

                    String strSelectedID = "";
                    for (CategoryResponse c : currentSelectedItems) {
                        strSelectedID += c.getId() + ",";
                    }
                    Preferences.writeString(SelectCategoryActivity.this, Preferences.SELECTED_CATEGORIES_ID, (strSelectedID.substring(0, (strSelectedID.length() - 1))));
                    Intent intent = new Intent(SelectCategoryActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();


                } else {
                    Snackbar snack = Snackbar.make(binding.mainView, R.string.atleast_one_category, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }

            }

        } else if (v == binding.ivBack) {
            finish();
        }
    }

    @Override
    public void handleError(Throwable throwable) {

        Snackbar.make(binding.mainView, throwable.getMessage(), Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void categoryResponce(CategoryResponseData responseBase) {

        skeletonScreen.hide();

        if (responseBase.getResponsedata().getSuccess().equalsIgnoreCase("1")) {
            if (responseBase.getResponsedata().getData().size() > 0) {
                ArrayList<CategoryResponse> categoryResponses = (ArrayList<CategoryResponse>) responseBase.getResponsedata().getData();
                currentSelectedItems.addAll(categoryResponses);


                if (!Preferences.readString(this, Preferences.SELECTED_CATEGORIES_ID, "").equals("")) {
                    String strSelected[] = Preferences.readString(this, Preferences.SELECTED_CATEGORIES_ID, "").split(",");
                    selectedData(strSelected, currentSelectedItems);

                } else {
                    categoriesAdapter.addAll(currentSelectedItems);

                }
            } else {
                skeletonScreen = Skeleton.bind(binding.activitySelectCategoryRvCategory)
                        .load(R.layout.no_datafound_skeleton)
                        .shimmer(false)
                        .show();
            }
        } else {
            Snackbar.make(binding.mainView, responseBase.getResponsedata().getMessage(), Snackbar.LENGTH_LONG).show();
        }


    }


    private void selectedData(String[] strSelected, List<CategoryResponse> responseBase) {
        for (int i = 0; i < strSelected.length; i++) {
            for (int j = 0; j < responseBase.size(); j++) {
                if (Integer.parseInt(responseBase.get(j).getId()) == Integer.parseInt(strSelected[i])) {
                    currentSelectedItems.get(j).setIsChecked("true");

                }
            }
        }

        categoriesAdapter.addAll(currentSelectedItems);


    }


    public void checkedListner(boolean isChecked, int position) {

        if (currentSelectedItems != null && currentSelectedItems.size() > 0) {
            currentSelectedItems.get(position).setIsChecked(isChecked ? "true" : "false");
            //categoriesAdapter.addAll(currentSelectedItems);
        }
    }
}
