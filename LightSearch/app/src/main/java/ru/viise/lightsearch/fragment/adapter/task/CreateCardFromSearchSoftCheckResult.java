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

package ru.viise.lightsearch.fragment.adapter.task;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.Subdivision;
import ru.viise.lightsearch.data.UnitsEnum;
import ru.viise.lightsearch.fragment.adapter.ResultSearchSoftCheckArrayAdapter;
import ru.viise.lightsearch.fragment.util.ViewFillerInit;
import ru.viise.lightsearch.fragment.util.ViewFillerProxy;
import ru.viise.lightsearch.fragment.util.ViewFillerProxyInit;

public class CreateCardFromSearchSoftCheckResult extends AsyncTask<SoftCheckRecord, Void, View> {

    private final ResultSearchSoftCheckArrayAdapter adapter;

    public CreateCardFromSearchSoftCheckResult(ResultSearchSoftCheckArrayAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected View doInBackground(SoftCheckRecord... records) {
        SoftCheckRecord record = records[0];
        View card = LayoutInflater.from(adapter.getContext()).inflate(R.layout.cardview_row_result_search, null);
        if (record != null) {
            ((TextView) card.findViewById(R.id.textViewCardNameRS)).setText(record.name());
            ((TextView) card.findViewById(R.id.textViewCardIDRS)).setText(record.barcode());
            ((TextView) card.findViewById(R.id.textViewCardAmountRS)).setText(
                    String.format("%s", record.maxAmountWithUnit()));

            final int color = ((TextView) card.findViewById(R.id.textViewCardIDRS)).getCurrentTextColor();

            if (record.subdivisions().collection().size() > 1) {
                ViewFillerProxy viewFillerProxy = ViewFillerProxyInit.viewFillerProxy();
                viewFillerProxy.setViewFiller(ViewFillerInit.viewFiller(card));
                record.subdivisions().collection().forEach(subdivision -> viewFillerProxy.addView(subdivision, record.amountUnit(), color));
            } else if (record.subdivisions().collection().size() == 1) {
                Subdivision subdiv = record.subdivisions().collection().iterator().next();
                ((TextView) card.findViewById(R.id.textViewCardSubdivRS)).setText(subdiv.name());
                ((TextView) card.findViewById(R.id.textViewCardSubdivAmount)).setText(
                        String.format("%s %s", subdiv.productAmount(), record.amountUnit()));
            }

            ((TextView) card.findViewById(R.id.textViewCardPriceRS)).setText(
                    String.format("%s %s", record.price(), UnitsEnum.CURRENT_PRICE_UNIT.stringValue()));

        }
        adapter.addCard(card);

        return card;
    }
}

