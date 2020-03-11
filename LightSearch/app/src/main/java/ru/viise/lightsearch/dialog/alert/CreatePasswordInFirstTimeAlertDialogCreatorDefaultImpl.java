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
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.AuthorizationPreferenceEnum;
import ru.viise.lightsearch.data.CreatePasswordInFirstTimeAlertDialogCreatorDTO;

public class CreatePasswordInFirstTimeAlertDialogCreatorDefaultImpl implements CreatePasswordInFirstTimeAlertDialogCreator {

    private final String FIRST_TIME = AuthorizationPreferenceEnum.FIRST_TIME.stringValue();
    private final String SUPERUSER  = AuthorizationPreferenceEnum.SUPERUSER.stringValue();
    private final String TRUE       = "true";

    private final CreatePasswordInFirstTimeAlertDialogCreatorDTO creatorDTO;
    private final Activity activity;
    private final SharedPreferences sPref;

    public CreatePasswordInFirstTimeAlertDialogCreatorDefaultImpl(
            CreatePasswordInFirstTimeAlertDialogCreatorDTO creatorDTO) {
        this.creatorDTO = creatorDTO;
        activity = this.creatorDTO.alertDialogCreatorDTO().rootActivity();
        sPref = this.creatorDTO.alertDialogCreatorDTO().sharedPreferences();
    }

    @Override
    public AlertDialog create() {
        DialogSettingsContainer dialogSettingsContainer =
                DialogSettingsContainerCreatorInit.dialogSettingsContainerCreator(activity)
                        .createDialogSettingsContainer();

        dialogSettingsContainer.textViewTitle().setText(R.string.create_password_admin);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogSettingsContainer.dialogSettingsView()).create();

        dialogSettingsContainer.buttonOK().setOnClickListener(viewOK -> {
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(SUPERUSER,
                    creatorDTO.hashAlgorithms().digest(dialogSettingsContainer.editText().getText().toString()));
            ed.putString(FIRST_TIME, TRUE);
            ed.apply();
            dialogSettingsContainer.editText().setText("");
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
