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
import android.support.v4.content.ContextCompat;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AlertDialog;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.BindRecord;

public class OneResultAlertDialogCreatorBindCheckImpl implements OneResultAlertDialogCreator {

    private final Activity activity;
    private final BindRecord bindRecord;

    public OneResultAlertDialogCreatorBindCheckImpl(Activity activity, BindRecord bindRecord) {
        this.activity = activity;
        this.bindRecord = bindRecord;
    }

    @Override
    public AlertDialog create() {
        String id = "<b>" + activity.getString(R.string.dialog_res_prod_id) + "</b>";
        String name = "<b>" + activity.getString(R.string.dialog_res_prod_name) + "</b>";

        String result = id + ": " + bindRecord.barcode() + "<br>"+
                name + ": " + bindRecord.name() + "<br>";

        DialogOKContainer dialogOKContainer = DialogOKContainerCreatorInit
                .dialogOKContainerCreator(activity)
                .create();

        dialogOKContainer.textViewResult().setText(HtmlCompat.fromHtml(result, HtmlCompat.FROM_HTML_MODE_LEGACY));

        dialogOKContainer.textViewTitle().setText(R.string.dialog_bind_check_one_result_title);
        dialogOKContainer.textViewTitle().setTextColor(ContextCompat
                .getColor(activity.getApplicationContext(), R.color.colorChange));


        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogOKContainer.dialogOKView())
                .create();

        dialogOKContainer.buttonOK().setOnClickListener(viewOK -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
