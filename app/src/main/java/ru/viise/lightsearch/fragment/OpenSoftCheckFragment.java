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

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.ManagerActivityHandler;
import ru.viise.lightsearch.activity.ManagerActivityUI;
import ru.viise.lightsearch.activity.scan.ScannerInit;
import ru.viise.lightsearch.cmd.manager.task.v2.NetworkAsyncTask;
import ru.viise.lightsearch.data.ScanType;
import ru.viise.lightsearch.data.pojo.OpenSoftCheckPojo;
import ru.viise.lightsearch.data.v2.Command;
import ru.viise.lightsearch.data.v2.OpenSoftCheckCommandSimple;
import ru.viise.lightsearch.data.v2.OpenSoftCheckCommandWithCardCode;
import ru.viise.lightsearch.data.v2.OpenSoftCheckCommandWithToken;
import ru.viise.lightsearch.data.v2.OpenSoftCheckCommandWithUserIdentifier;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class OpenSoftCheckFragment extends Fragment implements IOpenSoftCheckFragment {

    private final String PREF = "pref";

    private ManagerActivityUI managerActivityUI;
    private ManagerActivityHandler managerActivityHandler;

    private AlertDialog queryDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_soft_check, container, false);
        Animation animAlpha = AnimationUtils.loadAnimation(this.getActivity(), R.anim.alpha);
        queryDialog = SpotsDialogCreatorInit.spotsDialogCreator(this.getActivity(), R.string.spots_dialog_query_exec)
                .create();

        Button openSoftCheckButton = view.findViewById(R.id.buttonOpenSoftCheck);
        openSoftCheckButton.setOnClickListener(view1 -> {
            view1.startAnimation(animAlpha);
            managerActivityUI.setScanType(ScanType.OPEN_SOFT_CHECK);
            ScannerInit.scanner(this.getActivity()).scan();
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        managerActivityUI = (ManagerActivityUI) this.getActivity();
        managerActivityHandler = (ManagerActivityHandler) this.getActivity();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setCardCode(String cardCode) {
        SharedPreferences sPref = this.getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);
        prefManager.save(PreferencesManagerType.CARD_CODE_MANAGER, cardCode);

        Command<OpenSoftCheckPojo> command = new OpenSoftCheckCommandWithUserIdentifier(
                new OpenSoftCheckCommandWithCardCode(
                        new OpenSoftCheckCommandWithToken(
                                new OpenSoftCheckCommandSimple(),
                                prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
                        ), prefManager.load(PreferencesManagerType.CARD_CODE_MANAGER)
                ), prefManager.load(PreferencesManagerType.USER_IDENT_MANAGER));

        NetworkAsyncTask<OpenSoftCheckPojo> networkAsyncTask = new NetworkAsyncTask<>(
                managerActivityHandler,
                queryDialog);

        networkAsyncTask.execute(command);
    }
}
