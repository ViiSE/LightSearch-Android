
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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.fragment.adapter.RecyclerViewAdapterMore;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class MoreFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        SharedPreferences sPref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

        String username = prefManager.load(PreferencesManagerType.USERNAME_MANAGER);
        TextView usernameTextView = view.findViewById(R.id.tvMoreUsername);
        usernameTextView.setText(username);

        TextView userIdentTextView = view.findViewById(R.id.tvMoreUserIdent);
        String userIdent = prefManager.load(PreferencesManagerType.USER_IDENT_MANAGER);
        if(!userIdent.equals("0"))
            userIdentTextView.setText(userIdent);

        recyclerView = view.findViewById(R.id.rwMore);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.rw_divider_shape));
        recyclerView.addItemDecoration(itemDecorator);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        RecyclerViewAdapterMore adapter = new RecyclerViewAdapterMore(this.getContext(), this.getActivity());
        recyclerView.setAdapter(adapter);
    }
}
