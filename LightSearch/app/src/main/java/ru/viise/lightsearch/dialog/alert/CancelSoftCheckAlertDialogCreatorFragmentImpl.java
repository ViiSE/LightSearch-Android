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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.cmd.network.task.NetworkAsyncTask;
import ru.viise.lightsearch.cmd.network.task.NetworkCallback;
import ru.viise.lightsearch.data.entity.CancelSoftCheckCommandSimple;
import ru.viise.lightsearch.data.entity.CancelSoftCheckCommandWithCardCode;
import ru.viise.lightsearch.data.entity.CancelSoftCheckCommandWithCartSign;
import ru.viise.lightsearch.data.entity.CancelSoftCheckCommandWithToken;
import ru.viise.lightsearch.data.entity.CancelSoftCheckCommandWithUserIdentifier;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojoResult;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class CancelSoftCheckAlertDialogCreatorFragmentImpl implements CancelSoftCheckAlertDialogCreator {

    private final Fragment fragment;
    private final NetworkCallback<CancelSoftCheckPojo, CancelSoftCheckPojoResult> networkCallback;
    private final android.app.AlertDialog queryDialog;

    public CancelSoftCheckAlertDialogCreatorFragmentImpl(
            Fragment fragment,
            NetworkCallback<CancelSoftCheckPojo, CancelSoftCheckPojoResult> networkCallback,
            android.app.AlertDialog queryDialog) {
        this.fragment = fragment;
        this.networkCallback = networkCallback;
        this.queryDialog = queryDialog;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AlertDialog create() {
        DialogOKCancelContainer dialogOKCancelContainer =
                DialogOKCancelContainerCreatorInit.dialogOKCancelContainerCreator(fragment.getActivity())
                        .create();

        dialogOKCancelContainer.textViewResult().setText(R.string.dialog_cancel_soft_check);
        dialogOKCancelContainer.textViewTitle().setText(R.string.dialog_message);

        AlertDialog dialog = new AlertDialog.Builder(fragment.getActivity()).setView(
                dialogOKCancelContainer.dialogOKCancelView()).create();

        dialogOKCancelContainer.buttonOK().setOnClickListener(viewOK -> {
            SharedPreferences sPref = fragment.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
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

            NetworkAsyncTask<CancelSoftCheckPojo, CancelSoftCheckPojoResult> networkAsyncTask = new NetworkAsyncTask<>(
                    networkCallback,
                    queryDialog);
            networkAsyncTask.execute(command);

            dialog.dismiss();
        });

        dialogOKCancelContainer.buttonCancel().setOnClickListener(viewCancel -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
