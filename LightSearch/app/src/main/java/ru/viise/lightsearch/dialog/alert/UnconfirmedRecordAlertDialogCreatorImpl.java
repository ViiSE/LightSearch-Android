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

public class UnconfirmedRecordAlertDialogCreatorImpl implements UnconfirmedRecordAlertDialogCreator {

    private final Activity activity;

    public UnconfirmedRecordAlertDialogCreatorImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public AlertDialog create() {
        DialogOKContainer dialogOKContainer =
                DialogOKContainerCreatorInit.dialogOKContainerCreator(activity).create();

        dialogOKContainer.textViewTitle().setVisibility(View.GONE);
        dialogOKContainer.textViewResult().setText(R.string.unconfirmed_record);

        AlertDialog dialog = new AlertDialog.Builder(activity).setView(dialogOKContainer.dialogOKView())
                .create();

        dialogOKContainer.buttonOK().setOnClickListener(viewOK -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
