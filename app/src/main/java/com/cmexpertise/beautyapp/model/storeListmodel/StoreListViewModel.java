package com.cmexpertise.beautyapp.model.storeListmodel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmexpertise.beautyapp.R;
import com.cmexpertise.beautyapp.util.Constans;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import static com.cmexpertise.beautyapp.util.Utils.distance;

public class StoreListViewModel extends BaseObservable {
    private static DecimalFormat df;
    private StoreResponse storeResponse;

    public StoreListViewModel(StoreResponse storeResponse) {
        this.storeResponse = storeResponse;
        df = new DecimalFormat("#.0");

    }

    @BindingAdapter("android:srcImage")
    public static void setImageResource(ImageView imageView, String url) {

        Picasso.with(imageView.getContext()).load(url)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.placeholder_thumb)
                .resize(400, 300)
                .into(imageView);


    }

    @BindingAdapter(value = {"android:distanceLat", "android:distanceLng"})
    public static void setDistance(TextView textView, String lat, String lng) {

        double distance = distance(Constans.CURRENT_LATITUDE, Constans.CURRENT_LONGITUDE, Double.parseDouble(lat), Double.parseDouble(lng));
        String dis = df.format((distance));
        textView.setText(dis + " " + textView.getContext().getString(R.string.kmaway));


    }

    public StoreResponse getStoreResponse() {
        return storeResponse;
    }


}
