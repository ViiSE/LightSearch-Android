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
import android.support.v7.app.AlertDialog;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.InputPasswordAlertDialogCreatorDTO;

public class ErrorPassAlertDialogCreatorImpl implements ErrorAlertDialogCreator {

    private final Activity activity;
    private final InputPasswordAlertDialogCreatorDTO creatorDTO;


    public ErrorPassAlertDialogCreatorImpl(Activity activity, InputPasswordAlertDialogCreatorDTO creatorDTO) {
        this.activity = activity;
        this.creatorDTO = creatorDTO;
    }

    @Override
    public AlertDialog create() {
        DialogOKContainer dialogOKContainer =
                DialogOKContainerCreatorInit.dialogOKContainerCreator(activity).create();
        dialogOKContainer.textViewTitle().setText(R.string.dialog_error);
        dialogOKContainer.textViewResult().setText(R.string.dialog_pass_error);

        AlertDialog dialog = new AlertDialog.Builder(activity).setView(dialogOKContainer.dialogOKView()).create();
        dialogOKContainer.buttonOK().setOnClickListener(viewOK -> {
            creatorDTO.cbSettings().setChecked(false);
            dialog.dismiss();
        });

        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
