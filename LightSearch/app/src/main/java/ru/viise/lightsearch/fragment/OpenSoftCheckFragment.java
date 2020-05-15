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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.ManagerActivity;
import ru.viise.lightsearch.activity.ManagerActivityUI;
import ru.viise.lightsearch.activity.scan.ScannerInit;
import ru.viise.lightsearch.cmd.network.task.NetworkAsyncTask;
import ru.viise.lightsearch.cmd.network.task.NetworkCallback;
import ru.viise.lightsearch.data.ScanType;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.entity.OpenSoftCheckCommandSimple;
import ru.viise.lightsearch.data.entity.OpenSoftCheckCommandWithCardCode;
import ru.viise.lightsearch.data.entity.OpenSoftCheckCommandWithToken;
import ru.viise.lightsearch.data.entity.OpenSoftCheckCommandWithUserIdentifier;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojoResult;
import ru.viise.lightsearch.data.pojo.OpenSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.OpenSoftCheckPojoResult;
import ru.viise.lightsearch.data.pojo.SendForm;
import ru.viise.lightsearch.dialog.alert.CancelSoftCheckAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.CancelSoftCheckAlertDialogCreatorActivityImpl;
import ru.viise.lightsearch.dialog.alert.ErrorAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.ReconnectAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromActivityDefaultImpl;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class OpenSoftCheckFragment extends Fragment implements IOpenSoftCheckFragment, NetworkCallback<OpenSoftCheckPojo, OpenSoftCheckPojoResult> {

    private ManagerActivityUI managerActivityUI;

    private AlertDialog queryDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.toolbar_open_soft_check);
    }

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

        EditText scanEditText = view.findViewById(R.id.editTextOpenSC);
        scanEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String input = scanEditText.getText().toString();

                if(input.length() < 2) {
                    Toast t = Toast.makeText(Objects.requireNonNull(this.getActivity()).getApplicationContext(),
                            "Введите не менее двух символов!", Toast.LENGTH_LONG);
                    t.show();
                } else {
                    ((ManagerActivity) getActivity()).getSoftCheckContainerFragment().setCardCode(input);
                    return true;
                }
                v.requestFocus();
            }
            return false;
        });

        scanEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = scanEditText.getText().toString();
                if (scanEditText.getText().toString().length() == 13)
                    ((ManagerActivity) getActivity()).getSoftCheckContainerFragment().setCardCode(input);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.toolbar_open_soft_check);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        managerActivityUI = (ManagerActivityUI) this.getActivity();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setCardCode(String cardCode) {
        SharedPreferences sPref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);
        prefManager.save(PreferencesManagerType.CARD_CODE_MANAGER, cardCode);

        Command<OpenSoftCheckPojo> command = new OpenSoftCheckCommandWithUserIdentifier(
                new OpenSoftCheckCommandWithCardCode(
                        new OpenSoftCheckCommandWithToken(
                                new OpenSoftCheckCommandSimple(),
                                prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
                        ), prefManager.load(PreferencesManagerType.CARD_CODE_MANAGER)
                ), prefManager.load(PreferencesManagerType.USER_IDENT_MANAGER));

        NetworkAsyncTask<OpenSoftCheckPojo, OpenSoftCheckPojoResult> networkAsyncTask = new NetworkAsyncTask<>(
                this,
                queryDialog);

        networkAsyncTask.execute(command);
    }

    private ISoftCheckContainerFragment getSoftCheckContainerFragment() {
        ImplFinder<ISoftCheckContainerFragment> finder = new ImplFinderFragmentFromActivityDefaultImpl<>(this.getActivity());
        try { return finder.findImpl(ISoftCheckContainerFragment.class); }
        catch(FindableException ignore) { return null; }
    }

    private void callDialogSuccess(String message) {
        new SuccessAlertDialogCreatorImpl(this.getActivity(), message).create().show();
    }

    private void callDialogError(String message) {
        new ErrorAlertDialogCreatorImpl(this.getActivity(), message).create().show();
    }

    private void callReconnectDialog(
            NetworkCallback<? extends SendForm, ? extends SendForm> callback,
            Command<? extends SendForm> lastCommand) {
        new ReconnectAlertDialogCreatorImpl(
                this.getActivity(),
                callback,
                lastCommand
        ).create().show();
    }

    @Override
    public void handleResult(CommandResult<OpenSoftCheckPojo, OpenSoftCheckPojoResult> result) {
        if (result.isDone()) {
            callDialogSuccess(result.data().getMessage());

            ISoftCheckContainerFragment softCheckContainerFragment = getSoftCheckContainerFragment();
            if (softCheckContainerFragment != null)
                softCheckContainerFragment.switchToSoftCheckFragment();
        } else if (result.lastCommand() != null) {
            callReconnectDialog(this, result.lastCommand());
        } else {
            if (result.data().getMessage().contains("уже открыт")) {
                NetworkCallback<CancelSoftCheckPojo, CancelSoftCheckPojoResult> cancelSCCallback =
                        new NetworkCallback<CancelSoftCheckPojo, CancelSoftCheckPojoResult>() {
                    @Override
                    public void handleResult(CommandResult<CancelSoftCheckPojo, CancelSoftCheckPojoResult> resultCancelSC) {
                        if (resultCancelSC.isDone()) {
                            getActivity().setTitle(getString(R.string.fragment_container));
                            callDialogSuccess(resultCancelSC.data().getMessage());
                            ISoftCheckContainerFragment containerFragment = getSoftCheckContainerFragment();
                            if (containerFragment != null)
                                containerFragment.switchToOpenSoftCheckFragment();
                        } else if (resultCancelSC.lastCommand() != null) {
                            callReconnectDialog(this, resultCancelSC.lastCommand());
                        } else
                            callDialogError(resultCancelSC.data().getMessage());
                    }
                };

                CancelSoftCheckAlertDialogCreator cscADCr =
                        new CancelSoftCheckAlertDialogCreatorActivityImpl(
                                this.getActivity(),
                                cancelSCCallback,
                                queryDialog,
                                result.data().getMessage());
                cscADCr.create().show();
            } else
                callDialogError(result.data().getMessage());
        }
    }
}
