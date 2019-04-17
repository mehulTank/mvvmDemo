package com.cmexpertise.beautyapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.databinding.RowGallerylistBinding;
import com.cmexpertise.beautyapp.fragment.GalleryListFragment;
import com.cmexpertise.beautyapp.model.storeGallrymodel.StoreGalleryResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class GalleryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<StoreGalleryResponse> productModelList;
    private OnItemClickListener onItemClickListener;
    private Context mContext;
    private GalleryListFragment galleryListFragment;
    private boolean isLoading;
    private int lastPosition = -1;
    private RowGallerylistBinding binding;


    public GalleryListAdapter(GalleryListFragment productListFragment, Context context, List<StoreGalleryResponse> items) {
        this.productModelList = items;
        this.galleryListFragment = productListFragment;
        this.mContext = context;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_gallerylist, parent, false);
        View v = binding.getRoot();
        v.setOnClickListener(this);
        return new ViewHolderData(v);

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

    public void addRecord(ArrayList<StoreGalleryResponse> sleeptipsModelArrayList) {
        productModelList = sleeptipsModelArrayList;
    }


    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    @Override
    public void onClick(final View v) {
        // Give some time to the ripple to finish the effect
        if (onItemClickListener != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onItemClickListener.onItemClick(v, (StoreGalleryResponse) v.getTag());
                }
            }, 200);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, StoreGalleryResponse viewModel);

    }


    protected class ViewHolderData extends RecyclerView.ViewHolder {


        public ViewHolderData(View itemView) {
            super(itemView);


        }

        public void bindData(StoreGalleryResponse item, int position) {


            //Glide.with(mContext).load(item.getName()).placeholder(R.drawable.ic_placeholder).centerCrop().into(binding.rowGalleryIvGallery);

            Picasso.with(mContext).load(item.getThumb())
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.drawable.placeholder_thumb)
                    .resize(400, 300)
                    .into(binding.rowGalleryIvGallery);

            itemView.setTag(item);
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


}
