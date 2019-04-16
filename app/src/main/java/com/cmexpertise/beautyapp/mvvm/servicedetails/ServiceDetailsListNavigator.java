

package com.cmexpertise.beautyapp.mvvm.servicedetails;

import com.cmexpertise.beautyapp.model.storeServicemodel.ServicesList;

/**
 * Created by kailash patel
 */

public interface ServiceDetailsListNavigator {

    void handleError(Throwable throwable);

    void storeResponce(ServicesList userResponse);

}
