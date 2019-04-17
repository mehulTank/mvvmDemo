

package com.cmexpertise.beautyapp.mvvm.gallery;

import com.cmexpertise.beautyapp.model.storeGallrymodel.StoreGalleryResponseData;

/**
 * Created by kailash patel
 */

public interface GalleryListNavigator {

    void handleError(Throwable throwable);

    void storeResponce(StoreGalleryResponseData userResponse);

}
