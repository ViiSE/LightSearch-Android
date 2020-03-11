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

package ru.viise.lightsearch.dialog.alert;

import android.app.Activity;

import ru.viise.lightsearch.data.BindRecord;
import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.UnbindRecord;

public class OneResultAlertDialogCreatorInit {

    public static OneResultAlertDialogCreator oneResultSearchAlertDialogCreator(
            Activity activity, SearchRecord searchRecord) {
        return new OneResultAlertDialogCreatorDefaultImpl(activity, searchRecord);
    }

    //-------------------------------------------------------------------------------------------//
    public static OneResultAlertDialogCreator oneResultBindCheckAlertDialogCreator(
            Activity activity, BindRecord bindRecord) {
        return new OneResultAlertDialogCreatorBindCheckImpl(activity, bindRecord);
    }

    public static OneResultAlertDialogCreator oneResultBindAlertDialogCreator(
            Activity activity,
            BindRecord bindRecord,
            android.app.AlertDialog queryDialog,
            String factoryBarcode) {
        return new OneResultAlertDialogCreatorBindImpl(activity, bindRecord, queryDialog, factoryBarcode);
    }

    public static OneResultAlertDialogCreator oneResultUnbindAlertDialogCreator(
            Activity activity,
            UnbindRecord unbindRecord,
            android.app.AlertDialog queryDialog,
            String factoryBarcode) {
        return new OneResultAlertDialogCreatorUnbindImpl(activity,unbindRecord, queryDialog, factoryBarcode);
    }
}
