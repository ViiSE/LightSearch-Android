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
import ru.viise.lightsearch.data.BindRecord;
import ru.viise.lightsearch.fragment.adapter.ResultBindArrayAdapter;

public class CreateCardFromBindResult extends AsyncTask<BindRecord, Void, View> {

    private final ResultBindArrayAdapter adapter;

    public CreateCardFromBindResult(ResultBindArrayAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected View doInBackground(BindRecord... records) {
        BindRecord record = records[0];
        View card = LayoutInflater.from(adapter.getContext()).inflate(R.layout.cardview_row_result_bind, null);
        if (record != null) {
            ((TextView) card.findViewById(R.id.textViewCardNameRB)).setText(record.name());
            ((TextView) card.findViewById(R.id.textViewCardIDRB)).setText(record.barcode());
        }
        adapter.addCard(card);

        return card;
    }
}
