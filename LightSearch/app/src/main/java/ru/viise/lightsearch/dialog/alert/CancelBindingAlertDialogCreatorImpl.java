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

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.fragment.IBindingContainerFragment;

public class CancelBindingAlertDialogCreatorImpl implements CancelBindingAlertDialogCreator {

    private final Fragment fragment;

    public CancelBindingAlertDialogCreatorImpl(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public AlertDialog create() {
        DialogOKCancelContainer dialogOKCancelContainer = DialogOKCancelContainerCreatorInit
                        .dialogOKCancelContainerCreator(fragment.getActivity())
                        .create();

        dialogOKCancelContainer.textViewResult().setText(R.string.dialog_cancel_binding);
        dialogOKCancelContainer.textViewTitle().setText(R.string.dialog_message);

        AlertDialog dialog = new AlertDialog.Builder(fragment.getActivity()).setView(
                dialogOKCancelContainer.dialogOKCancelView()).create();

        AlertDialog successAD = new SuccessAlertDialogCreatorImpl(
                fragment.getActivity(),
                fragment.getString(R.string.dialog_bind_cancel))
                .create();

        dialogOKCancelContainer.buttonOK().setOnClickListener(viewOKCancel -> {
            ((IBindingContainerFragment) fragment).switchToCheckBind();
            dialog.dismiss();
            successAD.show();
        });

        dialogOKCancelContainer.buttonCancel().setOnClickListener(viewCancel -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
