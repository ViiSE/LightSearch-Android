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
import android.widget.Button;
import android.widget.TextView;

import ru.viise.lightsearch.R;

public class DialogOKCancelContainerCreatorDefaultImpl implements DialogOKCancelContainerCreator {

    private final Activity activity;

    public DialogOKCancelContainerCreatorDefaultImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public DialogOKCancelContainer create() {
        View dialogView = this.activity.getLayoutInflater().inflate(R.layout.dialog_ok_cancel, null);
        Button buttonOK = dialogView.findViewById(R.id.buttonOKDialogOKCancel);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancelDialogOKCancel);
        TextView textViewTitle = dialogView.findViewById(R.id.textViewTitleDialogOKCancel);
        TextView textViewResult = dialogView.findViewById(R.id.textViewDialogOKCancel);
        return DialogOKCancelContainerInit.dialogOKCancelContainer(
                dialogView, buttonOK, buttonCancel, textViewTitle, textViewResult);
    }
}
