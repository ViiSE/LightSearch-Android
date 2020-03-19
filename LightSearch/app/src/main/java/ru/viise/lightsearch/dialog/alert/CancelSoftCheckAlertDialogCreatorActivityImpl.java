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

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.ManagerActivity;
import ru.viise.lightsearch.activity.ManagerActivityHandler;
import ru.viise.lightsearch.cmd.manager.task.v2.NetworkAsyncTask;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojo;
import ru.viise.lightsearch.data.v2.CancelSoftCheckCommandSimple;
import ru.viise.lightsearch.data.v2.CancelSoftCheckCommandWithCardCode;
import ru.viise.lightsearch.data.v2.CancelSoftCheckCommandWithCartSign;
import ru.viise.lightsearch.data.v2.CancelSoftCheckCommandWithToken;
import ru.viise.lightsearch.data.v2.CancelSoftCheckCommandWithUserIdentifier;
import ru.viise.lightsearch.data.v2.Command;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class CancelSoftCheckAlertDialogCreatorActivityImpl implements CancelSoftCheckAlertDialogCreator {

    private final String PREF = "pref";

    private final ManagerActivity activity;
    private final android.app.AlertDialog queryDialog;
    private final String message;

    public CancelSoftCheckAlertDialogCreatorActivityImpl(
            ManagerActivity activity,
            android.app.AlertDialog queryDialog,
            String message) {
        this.activity = activity;
        this.queryDialog = queryDialog;
        this.message = message;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AlertDialog create() {
        DialogOKCancelContainer dialogOKCancelContainer =
                DialogOKCancelContainerCreatorInit.dialogOKCancelContainerCreator(activity)
                        .create();

        String fullMessage = message + "\n" + activity.getString(R.string.dialog_cancel_soft_check_warn);

        dialogOKCancelContainer.textViewResult().setText(fullMessage);
        dialogOKCancelContainer.textViewTitle().setText(R.string.dialog_message);

        AlertDialog dialog = new AlertDialog.Builder(activity).setView(
                dialogOKCancelContainer.dialogOKCancelView()).create();

        dialogOKCancelContainer.buttonOK().setOnClickListener(viewOK -> {
            SharedPreferences sPref = activity.getSharedPreferences(PREF, Context.MODE_PRIVATE);
            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

            Command<CancelSoftCheckPojo> command = new CancelSoftCheckCommandWithCardCode(
                    new CancelSoftCheckCommandWithUserIdentifier(
                            new CancelSoftCheckCommandWithCartSign(
                                    new CancelSoftCheckCommandWithToken(
                                            new CancelSoftCheckCommandSimple(),
                                            prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
                                    ), false
                            ), prefManager.load(PreferencesManagerType.USER_IDENT_MANAGER)
                    ), prefManager.load(PreferencesManagerType.CARD_CODE_MANAGER));

            NetworkAsyncTask<CancelSoftCheckPojo> networkAsyncTask = new NetworkAsyncTask<>(
                    activity,
                    queryDialog);
            networkAsyncTask.execute(command);

            dialog.dismiss();
        });

        dialogOKCancelContainer.buttonCancel().setOnClickListener(viewCancel -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
