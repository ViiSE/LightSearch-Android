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
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;

import ru.viise.lightsearch.R;
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

public class CancelSoftCheckFromCartAlertDialogCreatorDefaultImpl implements CancelSoftCheckFromCartAlertDialogCreator {

    private final String PREF = "pref";

    private final Fragment fragment;
    private final FragmentActivity activity;
    private final ManagerActivityHandler managerActivityHandler;
    private final android.app.AlertDialog queryDialog;

    public CancelSoftCheckFromCartAlertDialogCreatorDefaultImpl(Fragment fragment,
                ManagerActivityHandler managerActivityHandler,
                android.app.AlertDialog queryDialog) {
        this.fragment = fragment;
        this.activity = this.fragment.getActivity();
        this.managerActivityHandler = managerActivityHandler;
        this.queryDialog = queryDialog;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AlertDialog create() {
        DialogOKCancelContainer dialogOKCancelContainer = DialogOKCancelContainerCreatorInit.
                dialogOKCancelContainerCreator(activity).create();

        dialogOKCancelContainer.textViewTitle().setVisibility(View.GONE);
        dialogOKCancelContainer.textViewResult().setText(R.string.dialog_exit_to_cart);

        AlertDialog dialog = new AlertDialog.Builder(activity).setView(dialogOKCancelContainer.dialogOKCancelView()).create();

        dialogOKCancelContainer.buttonOK().setOnClickListener(viewOK -> {
            SharedPreferences sPref = fragment.getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

            Command<CancelSoftCheckPojo> command = new CancelSoftCheckCommandWithCardCode(
                    new CancelSoftCheckCommandWithUserIdentifier(
                            new CancelSoftCheckCommandWithCartSign(
                                    new CancelSoftCheckCommandWithToken(
                                            new CancelSoftCheckCommandSimple(),
                                            prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
                                    ), true
                            ), prefManager.load(PreferencesManagerType.USER_IDENT_MANAGER)
                    ), prefManager.load(PreferencesManagerType.CARD_CODE_MANAGER));

            NetworkAsyncTask<CancelSoftCheckPojo> networkAsyncTask = new NetworkAsyncTask<>(
                    managerActivityHandler,
                    queryDialog);
            networkAsyncTask.execute(command);

            dialog.dismiss();
        });

        dialogOKCancelContainer.buttonCancel().setOnClickListener(viewCancel -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
