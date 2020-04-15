/*
 *  Copyright 2020 ViiSE.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package ru.viise.lightsearch.dialog.alert;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.cmd.network.task.NetworkAsyncTask;
import ru.viise.lightsearch.cmd.network.task.NetworkCallback;
import ru.viise.lightsearch.data.UnbindRecord;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.UnbindCommandSimple;
import ru.viise.lightsearch.data.entity.UnbindCommandWithFactoryBarcode;
import ru.viise.lightsearch.data.entity.UnbindCommandWithToken;
import ru.viise.lightsearch.data.entity.UnbindCommandWithUserIdentifier;
import ru.viise.lightsearch.data.pojo.UnbindPojo;
import ru.viise.lightsearch.data.pojo.UnbindPojoResult;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class OneResultAlertDialogCreatorUnbindImpl implements OneResultAlertDialogCreator {

    private final Activity activity;
    private final NetworkCallback<UnbindPojo, UnbindPojoResult> networkCallback;
    private final UnbindRecord unbindRecord;
    private final android.app.AlertDialog queryDialog;
    private final String factoryBarcode;

    public OneResultAlertDialogCreatorUnbindImpl(
            Activity activity,
            NetworkCallback<UnbindPojo, UnbindPojoResult> networkCallback,
            UnbindRecord unbindRecord,
            android.app.AlertDialog queryDialog,
            String factoryBarcode) {
        this.activity = activity;
        this.networkCallback = networkCallback;
        this.unbindRecord = unbindRecord;
        this.queryDialog = queryDialog;
        this.factoryBarcode = factoryBarcode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AlertDialog create() {
        String id = "<b>" + activity.getString(R.string.dialog_res_prod_id) + "</b>";
        String name = "<b>" + activity.getString(R.string.dialog_res_prod_name) + "</b>";

        String result = id + ": " + unbindRecord.barcode() + "<br>"+
                        name + ": " + unbindRecord.name() + "<br>";

        DialogOKCancelContainer okCancelContainer = DialogOKCancelContainerCreatorInit
                .dialogOKCancelContainerCreator(activity)
                .create();

        okCancelContainer.textViewResult().setText(HtmlCompat.fromHtml(result, HtmlCompat.FROM_HTML_MODE_LEGACY));
        okCancelContainer.textViewTitle().setText(activity.getString(R.string.dialog_res_unbind));

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(okCancelContainer.dialogOKCancelView())
                .create();

        okCancelContainer.buttonOK().setOnClickListener(viewOK -> {
            SharedPreferences sPref = activity.getSharedPreferences("pref", Context.MODE_PRIVATE);
            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

            Command<UnbindPojo> command = new UnbindCommandWithUserIdentifier(
                    new UnbindCommandWithFactoryBarcode(
                            new UnbindCommandWithToken(
                                    new UnbindCommandSimple(),
                                    prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
                            ), factoryBarcode),
                    prefManager.load(PreferencesManagerType.USER_IDENT_MANAGER));

            NetworkAsyncTask<UnbindPojo, UnbindPojoResult> networkAsyncTask = new NetworkAsyncTask<>(
                    networkCallback,
                    queryDialog);
            networkAsyncTask.execute(command);
            dialog.dismiss();
        });

        okCancelContainer.buttonCancel().setOnClickListener(viewOK -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
