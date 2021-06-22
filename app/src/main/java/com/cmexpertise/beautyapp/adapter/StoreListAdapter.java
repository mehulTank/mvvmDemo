package com.cmexpertise.beautyapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.databinding.RowStorelistBinding;
import com.cmexpertise.beautyapp.fragment.StoreListFragment;
import com.cmexpertise.beautyapp.model.storeListmodel.StoreListViewModel;
import com.cmexpertise.beautyapp.model.storeListmodel.StoreResponse;

import java.text.DecimalFormat;
import java.util.List;


public class StoreListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<StoreResponse> productModelList;
    private OnItemClickListener onItemClickListener;
    private Context mContext;
    private StoreListFragment productListFragment;
    private boolean isLoading;
    private int lastPosition = -1;
    private RowStorelistBinding binding;
    private DecimalFormat df;


    public StoreListAdapter(StoreListFragment productListFragment, Context context, List<StoreResponse> items) {
        this.productModelList = items;
        this.productListFragment = productListFragment;
        this.mContext = context;
        df = new DecimalFormat("#.0");
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_storelist, parent, false);
        View v = binding.getRoot();
        v.setOnClickListener(this);
        return new ViewHolderData(binding);

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        lastPosition = position;
        ((ViewHolderData) holder).bindData(productModelList.get(position), position);


    }


    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    @Override
    public void onClick(final View v) {
        if (onItemClickListener != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onItemClickListener.onItemClick(v, (StoreResponse) v.getTag());
                }
            }, 200);
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setLoading() {
        isLoading = true;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, StoreResponse viewModel);

    }

    protected class ViewHolderData extends RecyclerView.ViewHolder {


        private RowStorelistBinding binding;
        private StoreListViewModel storeListViewModel;


        public ViewHolderData(RowStorelistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

        public void bindData(StoreResponse item, int position) {

            itemView.setTag(item);
            storeListViewModel = new StoreListViewModel(item);
            binding.setData(storeListViewModel);

        }
    }

}
