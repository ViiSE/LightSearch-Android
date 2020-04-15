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

package ru.viise.lightsearch.fragment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.fragment.adapter.task.CreateCardFromSearchResult;

public class ResultSearchArrayAdapter extends ArrayAdapter<SearchRecord> {

    private List<View> cards = new ArrayList<>();

    public ResultSearchArrayAdapter(@NonNull Context context, int resource, @NonNull List<SearchRecord> records) {
        super(context, resource, records);

        for(SearchRecord record: records) {
            CreateCardFromSearchResult crCard = new CreateCardFromSearchResult(this);
            crCard.execute(record);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (cards.size() <= position) {
            try { Thread.sleep(100); } catch (InterruptedException ignore) { }
        }
        notifyDataSetChanged();
        convertView = cards.get(position);

        return convertView;
    }

    public void addCard(View card) {
        cards.add(card);
    }
}
