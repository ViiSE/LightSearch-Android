/*
 * Copyright 2019 ViiSE.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.viise.lightsearch.request;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

import ru.viise.lightsearch.dialog.alert.RequestPhonePermissionAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.RequestPhonePermissionAlertDialogCreatorInit;

public class PhonePermissionDefaultImpl implements PhonePermission {

    private final int PHONE_STATE_CODE = 1;
    private final Activity activity;

    public PhonePermissionDefaultImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void requestPhonePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            RequestPhonePermissionAlertDialogCreator reqPPADCr =
                    RequestPhonePermissionAlertDialogCreatorInit.requestPhonePermissionAlertDialogCreator(activity);
            reqPPADCr.createAlertDialog().show();
        } else
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    PHONE_STATE_CODE);
    }
}
