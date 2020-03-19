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
import android.view.View;
import android.widget.Button;

import ru.viise.lightsearch.R;

public class AboutAlertDialogCreatorDefaultImpl implements SuccessAlertDialogCreator {

    private final Activity activity;

    public AboutAlertDialogCreatorDefaultImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public AlertDialog create() {
        View dialogView = this.activity.getLayoutInflater().inflate(R.layout.dialog_about, null);
        Button buttonOK = dialogView.findViewById(R.id.buttonDialogAbout);

        AlertDialog dialog = new AlertDialog.Builder(activity).setView(dialogView).create();
        buttonOK.setOnClickListener(viewOK -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
