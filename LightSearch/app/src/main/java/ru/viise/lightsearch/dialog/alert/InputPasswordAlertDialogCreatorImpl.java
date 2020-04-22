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
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.AuthorizationPreferenceEnum;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerType;
import ru.viise.lightsearch.security.HashAlgorithm;

public class InputPasswordAlertDialogCreatorImpl implements InputPasswordAlertDialogCreator {

    private final String SUPERUSER  = AuthorizationPreferenceEnum.SUPERUSER.stringValue();

    private final Activity activity;
    private final PreferencesManager prefManager;
    private final HashAlgorithm hashAlgorithm;

    public InputPasswordAlertDialogCreatorImpl(
            Activity activity,
            PreferencesManager prefManager,
            HashAlgorithm hashAlgorithm) {
        this.activity = activity;
        this.prefManager = prefManager;
        this.hashAlgorithm = hashAlgorithm;
    }

    @Override
    public AlertDialog create() {
        DialogSettingsContainer dialogSettingsContainer =
                DialogSettingsContainerCreatorInit.dialogSettingsContainerCreator(activity)
                        .createDialogSettingsContainer();

        dialogSettingsContainer.textViewTitle().setVisibility(View.GONE);
        dialogSettingsContainer.textViewResult().setText(R.string.input_password);

        AlertDialog dialog = new AlertDialog.Builder(activity).setView(dialogSettingsContainer.dialogSettingsView())
                .create();

        dialogSettingsContainer.buttonOK().setOnClickListener(viewOK -> {
            String password = prefManager.load(PreferencesManagerType.SUPERUSER);
            if (hashAlgorithm.digest(dialogSettingsContainer.editText().getText().toString()).equals(password)) {
                dialogSettingsContainer.editText().setText("");
                dialog.dismiss();

                new AlertDialogSettingsCreatorImpl(activity, hashAlgorithm, prefManager)
                        .create()
                        .show();

            } else {
                ErrorAlertDialogCreator errADCr =
                        new ErrorPassAlertDialogCreatorImpl(activity);
                errADCr.create().show();
                dialogSettingsContainer.editText().setText("");
                dialog.dismiss();
            }
        });

        dialogSettingsContainer.buttonCancel().setOnClickListener(viewCancel -> {
            dialogSettingsContainer.editText().setText("");
            dialog.dismiss();
        });

        AlertDialogUtil.setTransparentBackground(dialog);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
