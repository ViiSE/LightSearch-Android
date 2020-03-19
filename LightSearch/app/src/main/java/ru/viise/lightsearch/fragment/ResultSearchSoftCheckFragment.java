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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromActivityDefaultImpl;
import ru.viise.lightsearch.fragment.adapter.ResultSearchSoftCheckArrayAdapter;

public class ResultSearchSoftCheckFragment extends ListFragment {

    public static final String TAG = "resultSearchSoftCheckFragment";

    private List<SoftCheckRecord> softCheckRecords;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_result_search, container, false);
        initAdapter();

        return view;
    }

    private void initAdapter() {
        ResultSearchSoftCheckArrayAdapter adapter = new ResultSearchSoftCheckArrayAdapter(this.getActivity(),
                R.layout.cardview_row_result_search, softCheckRecords);
        setListAdapter(adapter);
    }

    public void init(List<SoftCheckRecord> searchRecords) {
        this.softCheckRecords = searchRecords;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        getActivity().getSupportFragmentManager().popBackStackImmediate();
        getSoftCheckFragment().addSoftCheckRecord(softCheckRecords.get(position));
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
                getActivity().setTitle(getString(R.string.fragment_soft_check));
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
            return false;
        });
    }

    private ISoftCheckFragment getSoftCheckFragment() {
        ImplFinder<ISoftCheckFragment> finder = new ImplFinderFragmentFromActivityDefaultImpl<>(this.getActivity());
        try { return finder.findImpl(ISoftCheckFragment.class); }
        catch(FindableException ignore) { return null; }
    }
}
