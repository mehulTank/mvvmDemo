/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version stra2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.cmexpertise.beautyapp.mvvm.register;

import com.cmexpertise.beautyapp.model.ResponseBase;

/**
 * Created by Nishidh patel
 */

public interface RegisterNavigator {

    void handleError(Throwable throwable);

    void loginClick();

    void register();

    void registerResponce(ResponseBase userResponse);

}
