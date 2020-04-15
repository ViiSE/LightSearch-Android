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

package ru.viise.lightsearch.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import java.util.List;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreatorImpl;
import ru.viise.lightsearch.fragment.adapter.ResultSearchArrayAdapter;

public class ResultSearchFragment extends ListFragment {

    private List<SearchRecord> searchRecords;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_search, container, false);
        initAdapter();

        return view;
    }

    private void initAdapter() {
        ResultSearchArrayAdapter adapter = new ResultSearchArrayAdapter(this.getActivity(),
                R.layout.cardview_row_result_search, searchRecords);
        setListAdapter(adapter);
    }

    public void init(List<SearchRecord> searchRecords) {
        this.searchRecords = searchRecords;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        OneResultAlertDialogCreator oneResADCr =
                new OneResultAlertDialogCreatorImpl(
                        this.getActivity(),
                        searchRecords.get(position));
        oneResADCr.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null)
            return;

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                getActivity().setTitle(StackFragmentTitle.pop());
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
            return false;
        });
    }
}
