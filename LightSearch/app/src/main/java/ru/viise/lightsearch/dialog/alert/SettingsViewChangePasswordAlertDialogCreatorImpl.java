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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerType;
import ru.viise.lightsearch.security.HashAlgorithm;

public class SettingsViewChangePasswordAlertDialogCreatorImpl implements SettingsViewChangePasswordAlertDialogCreator {

    private final Activity rootActivity;
    private final HashAlgorithm hashAlgorithm;
    private final PreferencesManager prefManager;

    public SettingsViewChangePasswordAlertDialogCreatorImpl(
            Activity rootActivity,
            HashAlgorithm hashAlgorithm,
            PreferencesManager prefManager) {
        this.rootActivity = rootActivity;
        this.hashAlgorithm = hashAlgorithm;
        this.prefManager = prefManager;
    }

    @Override
    public AlertDialog create() {
        DialogSettingsContainer dialogSettingsContainer =
                DialogSettingsContainerCreatorInit.dialogSettingsContainerCreator(rootActivity)
                        .createDialogSettingsContainer();

        dialogSettingsContainer.textViewTitle().setText(R.string.change_password_admin);

        AlertDialog dialog = new AlertDialog.Builder(rootActivity)
                .setView(dialogSettingsContainer.dialogSettingsView()).setCancelable(true).create();

        dialogSettingsContainer.buttonOK().setOnClickListener(viewOK -> {
            prefManager.save(
                    PreferencesManagerType.SUPERUSER,
                    hashAlgorithm.digest(dialogSettingsContainer.editText().getText().toString()));
            dialogSettingsContainer.editText().setText("");
            Toast t =
                    Toast.makeText(rootActivity.getApplicationContext(), R.string.password_changed, Toast.LENGTH_SHORT);
            t.show();
            dialog.dismiss();
        });

        dialogSettingsContainer.buttonCancel().setOnClickListener(viewCancel -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
