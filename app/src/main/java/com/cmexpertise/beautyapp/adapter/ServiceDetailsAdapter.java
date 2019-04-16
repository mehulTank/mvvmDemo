package com.cmexpertise.beautyapp.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.databinding.RowServicelistDetailsBinding;
import com.cmexpertise.beautyapp.model.storeServicemodel.ServicesResponse;

import java.util.List;


public class ServiceDetailsAdapter extends RecyclerView.Adapter<ServiceDetailsAdapter.ViewHolderData> {

    private Context mContext;
    private List<ServicesResponse> servicesResponseList;
    private RowServicelistDetailsBinding binding;


    public ServiceDetailsAdapter(Context context, List<ServicesResponse> dataSet) {
        mContext = context;
        servicesResponseList = dataSet;

    }

    @Override
    public ViewHolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_servicelist_details, parent, false);
        View v = binding.getRoot();
        return new ViewHolderData(v);

    }

    public void setList(List<ServicesResponse> dataSet) {
        this.servicesResponseList = dataSet;
    }

    @Override
    public void onBindViewHolder(final ViewHolderData holder, final int position) {

        ((ViewHolderData) holder).bindData(servicesResponseList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return servicesResponseList.size();
    }

    public void addData(List<ServicesResponse> dataSet) {

        servicesResponseList = dataSet;
        notifyDataSetChanged();
    }


    protected class ViewHolderData extends RecyclerView.ViewHolder {


        public ViewHolderData(View itemView) {
            super(itemView);


        }

        public void bindData(ServicesResponse item, int position) {

            binding.rowServiceTvName.setText("" + item.getName());
            binding.rowServiceTvPrice.setText("" + item.getPrice());

        }
    }
}