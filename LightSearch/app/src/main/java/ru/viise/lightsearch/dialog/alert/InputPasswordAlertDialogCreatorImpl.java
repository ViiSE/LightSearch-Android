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
import android.view.View;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.AuthorizationPreferenceEnum;
import ru.viise.lightsearch.data.InputPasswordAlertDialogCreatorDTO;

import static android.view.View.VISIBLE;

public class InputPasswordAlertDialogCreatorImpl implements InputPasswordAlertDialogCreator {

    private final String SUPERUSER  = AuthorizationPreferenceEnum.SUPERUSER.stringValue();

    private final InputPasswordAlertDialogCreatorDTO creatorDTO;
    private final Activity activity;
    private final SharedPreferences sPref;

    public InputPasswordAlertDialogCreatorImpl(InputPasswordAlertDialogCreatorDTO creatorDTO) {
        this.creatorDTO = creatorDTO;
        activity = this.creatorDTO.alertDialogCreatorDTO().rootActivity();
        sPref = this.creatorDTO.alertDialogCreatorDTO().sharedPreferences();
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
            String password = sPref.getString(SUPERUSER, "");
            if (creatorDTO.hashAlgorithms().digest(dialogSettingsContainer.editText().getText().toString()).equals(password)) {
                creatorDTO.twHost().setVisibility(VISIBLE);
                creatorDTO.twPort().setVisibility(VISIBLE);
                creatorDTO.etHost().setVisibility(VISIBLE);
                creatorDTO.etPort().setVisibility(VISIBLE);
                creatorDTO.bChangePassword().setVisibility(VISIBLE);
                dialogSettingsContainer.editText().setText("");
                dialog.dismiss();
            } else {
                ErrorAlertDialogCreator errADCr =
                        new ErrorPassAlertDialogCreatorImpl(activity, creatorDTO);
                errADCr.create().show();
                dialogSettingsContainer.editText().setText("");
                dialog.dismiss();
            }
        });

        dialogSettingsContainer.buttonCancel().setOnClickListener(viewCancel -> {
            creatorDTO.cbSettings().setChecked(false);
            dialog.dismiss();
        });

        AlertDialogUtil.setTransparentBackground(dialog);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
