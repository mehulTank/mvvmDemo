package com.cmexpertise.beautyapp.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.databinding.RowSelectLocationBinding;
import com.cmexpertise.beautyapp.model.locationModel.LocationResponse;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    private OnItemClickListener mItemClickListener;
    private Context mContext;
    private List<LocationResponse> mDataSet;
    private RowSelectLocationBinding binding;

    public CityListAdapter(Context context, List<LocationResponse> dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View v = LayoutInflater.from(mContext).inflate(R.layout.row_select_location, parent, false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_select_location, parent, false);
        View v = binding.getRoot();
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        binding.rowSelectLocationTvLocationName.setText(mDataSet.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void remove(int position) {
        mDataSet.remove(mDataSet.get(position));
        notifyDataSetChanged();
    }

    public void add(LocationResponse text, int position) {
        mDataSet.add(position, text);
        notifyItemInserted(position);

    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, LocationResponse locationResponse, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolder(View itemView) {
            super(itemView);

            binding.llLocationRow.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, mDataSet.get(getPosition()), getPosition());
            }
        }
    }

}