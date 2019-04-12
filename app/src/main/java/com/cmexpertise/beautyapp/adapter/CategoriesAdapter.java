package com.cmexpertise.beautyapp.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.customecomponent.SmoothCheckBox;
import com.cmexpertise.beautyapp.databinding.RowSelectCategoryBinding;
import com.cmexpertise.beautyapp.model.categoryModel.CategoryResponse;

import java.util.List;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private Context mContext;
    private List<CategoryResponse> categoryResponseList;
    private RowSelectCategoryBinding binding;


    @NonNull
    private OnItemCheckListener onItemClick;

    public CategoriesAdapter(Context context, List<CategoryResponse> dataSet, OnItemCheckListener onItemCheckListener) {
        mContext = context;
        categoryResponseList = dataSet;
        this.onItemClick = onItemCheckListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View v = LayoutInflater.from(mContext).inflate(R.layout.row_select_category, parent, false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_select_category, parent, false);
        View v = binding.getRoot();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final CategoryResponse currentCategory = categoryResponseList.get(position);
        binding.rowSelectCategoryTvCatname.setText(categoryResponseList.get(position).getName());
        binding.rowSelectCategoryCbSelect.setOnCheckedChangeListener(null);
        binding.rowSelectCategoryCbSelect.setChecked(currentCategory.getIsChecked().equalsIgnoreCase("true"), false);

        binding.rowSelectCategoryCbSelect.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                categoryResponseList.get(position).setIsChecked(isChecked ? "true" : "false");
                if (isChecked) {
                    onItemClick.onItemCheck(categoryResponseList.get(position));
                } else {
                    onItemClick.onItemUncheck(categoryResponseList.get(position));
                }
            }

        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.rowSelectCategoryCbSelect.performClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryResponseList.size();
    }

    public void remove(int position) {
        categoryResponseList.remove(categoryResponseList.get(position));
        notifyDataSetChanged();
    }

    public void add(CategoryResponse text, int position) {
        categoryResponseList.add(position, text);
        notifyItemInserted(position);

    }


    public interface OnItemCheckListener {
        void onItemCheck(CategoryResponse item);

        void onItemUncheck(CategoryResponse item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;


        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }
}