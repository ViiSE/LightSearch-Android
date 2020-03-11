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

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.fragment.StackFragmentTitle;

public class ExitToAuthorizationAlertDialogCreatorDefaultImpl implements ExitToAuthorizationAlertDialogCreator {

    private final FragmentActivity activity;

    public ExitToAuthorizationAlertDialogCreatorDefaultImpl(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public AlertDialog create() {
        DialogOKCancelContainer dialogOKCancelContainer =
                DialogOKCancelContainerCreatorInit.dialogOKCancelContainerCreator(activity)
                        .create();

        dialogOKCancelContainer.textViewTitle().setText(R.string.dialog_exit);
        dialogOKCancelContainer.textViewResult().setText(R.string.dialog_exit_to_auth);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogOKCancelContainer.dialogOKCancelView()).create();

        dialogOKCancelContainer.buttonOK().setOnClickListener(viewOK -> {
            dialog.dismiss();
            activity.setTitle(StackFragmentTitle.pop());
            activity.getSupportFragmentManager().popBackStack();
        });

        dialogOKCancelContainer.buttonCancel().setOnClickListener(viewCancel -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
