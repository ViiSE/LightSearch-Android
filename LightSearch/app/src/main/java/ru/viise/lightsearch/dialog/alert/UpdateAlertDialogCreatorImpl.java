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

import androidx.appcompat.app.AlertDialog;

import java.net.URL;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.util.appupdater.UtilsLibrary;
import ru.viise.lightsearch.util.appupdater.objects.Update;

public class UpdateAlertDialogCreatorImpl implements UpdateAlertDialogCreator {

    private final Activity activity;
    private final String description;
    private final URL apk;

    public UpdateAlertDialogCreatorImpl(Activity activity, Update update) {
        this.activity = activity;
        this.description = update.getReleaseNotes();
        this.apk = update.getUrlToDownload();
    }

    @Override
    public AlertDialog create() {
        DialogOKCancelContainer dialogOKCancelContainer =
                DialogOKCancelContainerCreatorInit.dialogOKCancelContainerCreator(activity)
                        .create();

        dialogOKCancelContainer.textViewTitle().setText(R.string.dialog_update_title);
        dialogOKCancelContainer.textViewResult().setText(description);

        dialogOKCancelContainer.buttonOK().setText(R.string.button_update);
        dialogOKCancelContainer.buttonCancel().setText(R.string.button_update_later);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogOKCancelContainer.dialogOKCancelView()).create();

        dialogOKCancelContainer.buttonOK().setOnClickListener(viewOK -> {
            UtilsLibrary.goToUpdate(activity.getApplicationContext(), apk);
            dialog.dismiss();

            DialogOKContainer dialogOKContainer =
                    DialogOKContainerCreatorInit.dialogOKContainerCreator(activity)
                            .create();

            dialogOKContainer.textViewTitle().setText(R.string.dialog_update_downloading_title);
            dialogOKContainer.textViewResult().setText(R.string.dialog_update_downloading_description);

            dialogOKContainer.buttonOK().setText(R.string.dialog_OK);

            AlertDialog dialogDownloading = new AlertDialog.Builder(activity)
                    .setView(dialogOKContainer.dialogOKView()).create();

            dialogOKContainer.buttonOK().setOnClickListener(view -> dialogDownloading.dismiss());
            AlertDialogUtil.setTransparentBackground(dialogDownloading);
            dialogDownloading.show();
        });

        dialogOKCancelContainer.buttonCancel().setOnClickListener(viewCancel -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
