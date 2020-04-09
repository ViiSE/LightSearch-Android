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
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.fragment.StackFragmentTitle;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManagerImpl;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class ExitToAuthorizationAlertDialogCreatorImpl implements ExitToAuthorizationAlertDialogCreator {

    private final FragmentActivity activity;

    public ExitToAuthorizationAlertDialogCreatorImpl(FragmentActivity activity) {
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
            SharedPreferences sPref = activity.getSharedPreferences("pref", Context.MODE_PRIVATE);
            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);
            prefManager.save(PreferencesManagerType.TOKEN_MANAGER, "");
            prefManager.save(PreferencesManagerType.USER_IDENT_MANAGER, "");
            new FragmentTransactionManagerImpl(activity).doAuthorizationFragmentTransaction(true);

//            activity.getSupportFragmentManager().popBackStack();
        });

        dialogOKCancelContainer.buttonCancel().setOnClickListener(viewCancel -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
