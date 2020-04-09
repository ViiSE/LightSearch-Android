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
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.SoftCheckRecord;

public class InfoProductAlertDialogCreatorSoftCheckImpl implements InfoProductAlertDialogCreator {

    private final Activity activity;
    private final SoftCheckRecord record;

    public InfoProductAlertDialogCreatorSoftCheckImpl(Activity activity, SoftCheckRecord record) {
        this.record = record;
        this.activity = activity;
    }

    @Override
    public AlertDialog create() {
        String id = "<b>" + activity.getString(R.string.dialog_res_prod_id) + "</b>";
        String name = "<b>" + activity.getString(R.string.dialog_res_prod_name) + "</b>";
        String price = "<b>" + activity.getString(R.string.dialog_res_prod_price) + "</b>";
        String max_amount = "<b>" + activity.getString(R.string.dialog_res_prod_max_amount) + "</b>";
        String subdivisions = "<b>" + activity.getString(R.string.dialog_res_prod_subdivisions) + "</b>";

        String result = id + ": " + record.barcode() + "<br>"+
                name + ": " + record.name() + "<br>" +
                price + ": " + record.priceWithUnit() + "<br>" +
                max_amount + ": " + record.maxAmountWithUnit() + "<br>" +
                subdivisions + ": " + "<br>" + record.subdivisions().toString();

        DialogOKContainer dialogOKContainer =
                DialogOKContainerCreatorInit.dialogOKContainerCreator(activity).create();

        dialogOKContainer.textViewTitle().setVisibility(View.GONE);
        dialogOKContainer.textViewResult().setText(HtmlCompat.fromHtml(result, HtmlCompat.FROM_HTML_MODE_LEGACY));

        AlertDialog dialog =
                new AlertDialog.Builder(activity).setView(dialogOKContainer.dialogOKView()).create();

        dialogOKContainer.buttonOK().setOnClickListener(viewOK -> dialog.dismiss());
        AlertDialogUtil.setTransparentBackground(dialog);

        return dialog;
    }
}
