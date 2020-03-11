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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.SoftCheckRecord;

public class SoftCheckContainerFragment extends Fragment implements ISoftCheckContainerFragment {

    private static final String SOFT_CHECK_RECORDS = "softCheckRecords";
    private List<SoftCheckRecord> softCheckRecords = new ArrayList<>();

    private static final String SCC_FRAGMENT = "SCCFragment";
    private int selected = 0; //0 - OpenSoftCheckFragment, 1 - SoftCheckFragment

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            selected = savedInstanceState.getInt(SCC_FRAGMENT);
            softCheckRecords = savedInstanceState.getParcelableArrayList(SOFT_CHECK_RECORDS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soft_check_container, container, false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(selected == 0)
            transaction.replace(R.id.fragment_sc_container, new OpenSoftCheckFragment());
        else if(selected == 1) {
            SoftCheckFragment scFragment = new SoftCheckFragment();
            scFragment.init(softCheckRecords);
            transaction.replace(R.id.fragment_sc_container, scFragment);
        }
        transaction.commit();

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SCC_FRAGMENT, selected);
        outState.putParcelableArrayList(SOFT_CHECK_RECORDS, new ArrayList<>(softCheckRecords));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void switchToSoftCheckFragment() {
        SoftCheckFragment scFragment = new SoftCheckFragment();
        scFragment.init(softCheckRecords);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_up, R.anim.enter_from_up, R.anim.exit_to_down);
        transaction.replace(R.id.fragment_sc_container, scFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
        selected = 1;
    }

    @Override
    public void switchToOpenSoftCheckFragment() {
        if (selected != 0) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_down, R.anim.enter_from_down, R.anim.exit_to_up);
            transaction.replace(R.id.fragment_sc_container, new OpenSoftCheckFragment());
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
            softCheckRecords.clear();
            selected = 0;
        }
    }
}
