package com.cmexpertise.beautyapp.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmexpertise.beautyapp.Activity.SelectCategoryActivity;
import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.customecomponent.SmoothCheckBox;
import com.cmexpertise.beautyapp.databinding.RowSelectCategoryBinding;
import com.cmexpertise.beautyapp.model.categoryModel.CategoryResponse;
import com.cmexpertise.beautyapp.model.storeListmodel.StoreResponse;

import java.util.List;


public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<CategoryResponse> categoryResponseList;
    private RowSelectCategoryBinding binding;
    private SelectCategoryActivity selectCategoryActivity;


    public CategoriesAdapter(Context context, List<CategoryResponse> dataSet, final SelectCategoryActivity selectCategoryActivity) {
        mContext = context;
        categoryResponseList = dataSet;
        this.selectCategoryActivity = selectCategoryActivity;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_select_category, parent, false);
        View v = binding.getRoot();
        return new ViewHolderData(v);

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ((ViewHolderData) holder).bindData(categoryResponseList.get(position), position);


    }


    @Override
    public int getItemCount() {
        return categoryResponseList.size();
    }


    public void addAll(List<CategoryResponse> currentSelectedItems) {
        categoryResponseList = currentSelectedItems;
        notifyDataSetChanged();
    }


    protected class ViewHolderData extends RecyclerView.ViewHolder {


        public ViewHolderData(View itemView) {
            super(itemView);


        }

        public void bindData(CategoryResponse item, int position) {


            final CategoryResponse currentCategory = categoryResponseList.get(position);
            binding.rowSelectCategoryTvCatname.setText(categoryResponseList.get(position).getName());
            binding.rowSelectCategoryCbSelect.setChecked(currentCategory.getIsChecked().equalsIgnoreCase("true") ? true : false, false);

            binding.rowSelectCategoryCbSelect.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {

                    selectCategoryActivity.checkedListner(isChecked, position);
                }

            });

        }
    }
}