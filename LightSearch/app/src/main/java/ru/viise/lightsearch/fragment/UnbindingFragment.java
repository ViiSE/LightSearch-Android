
/*
 *  Copyright 2020 ViiSE.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package ru.viise.lightsearch.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.KeyboardHideToolInit;
import ru.viise.lightsearch.activity.ManagerActivityUI;
import ru.viise.lightsearch.activity.scan.ScannerInit;
import ru.viise.lightsearch.cmd.network.task.NetworkAsyncTask;
import ru.viise.lightsearch.cmd.network.task.NetworkCallback;
import ru.viise.lightsearch.data.ScanType;
import ru.viise.lightsearch.data.UnbindRecord;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.entity.UnbindCheckCommandSimple;
import ru.viise.lightsearch.data.entity.UnbindCheckCommandWithBarcode;
import ru.viise.lightsearch.data.entity.UnbindCheckCommandWithFactoryBarcode;
import ru.viise.lightsearch.data.entity.UnbindCheckCommandWithToken;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojo;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojoResult;
import ru.viise.lightsearch.data.pojo.UnbindPojo;
import ru.viise.lightsearch.data.pojo.UnbindPojoResult;
import ru.viise.lightsearch.dialog.alert.ErrorAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.NoResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.NoResultAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreatorUnbindImpl;
import ru.viise.lightsearch.dialog.alert.ReconnectAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromActivityDefaultImpl;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManager;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManagerImpl;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class UnbindingFragment extends Fragment implements IUnbindingFragment, NetworkCallback<UnbindCheckPojo, UnbindCheckPojoResult> {

    private final static String SEARCH_MODE_U = "searchModeU";
    private final static String FACTORY_BARCODE_U = "factoryBarcodeU";

    private String factoryBarcode = "";
    private int searchMode = 0; // 0 - Keyboard typing, 1 - barcode

    private AlertDialog queryDialog;
    private EditText searchEditText;

    private ManagerActivityUI managerActivityUI;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            searchMode = savedInstanceState.getInt(SEARCH_MODE_U);
            factoryBarcode = savedInstanceState.getString(FACTORY_BARCODE_U);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unbinding, container, false);

        searchEditText = view.findViewById(R.id.editTextSearchUnbinding);
        FloatingActionButton barcodeButton = view.findViewById(R.id.floatingActionButtonUnbindingBarcode);

        queryDialog = SpotsDialogCreatorInit.spotsDialogCreator(this.getActivity(), R.string.spots_dialog_query_exec)
                .create();

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String input = searchEditText.getText().toString();

                if(input.length() < 2) {
                    Toast t = Toast.makeText(Objects.requireNonNull(this.getActivity()).getApplicationContext(),
                            "Введите не менее двух символов!", Toast.LENGTH_LONG);
                    t.show();
                } else {
                    run();
                }
                v.requestFocus();

                return true;
            }
            return false;
        });

        barcodeButton.setOnClickListener(view2 -> {
            KeyboardHideToolInit.keyboardHideTool(this.getActivity()).hideKeyboard();

            searchEditText.clearFocus();
            view2.requestFocus();

            managerActivityUI.setScanType(ScanType.UNBIND);
            ScannerInit.scanner(this.getActivity()).scan();
        });

        searchMode = 1;

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (searchMode == 0) {
                    if (searchEditText.getText().toString().length() == 13)
                        run();
                } else
                    searchMode = 0;
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SEARCH_MODE_U, searchMode);
        outState.putString(FACTORY_BARCODE_U, factoryBarcode);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        managerActivityUI = (ManagerActivityUI) this.getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setSearchBarcode(String barcode) {
        searchMode = 1;
        searchEditText.setText(barcode);
    }

    @Override
    public void setSearchBarcodeAndRun(String barcode) {
        searchMode = 1;
        searchEditText.setText(barcode);
        run();
    }

    private void run() {
        String input = searchEditText.getText().toString();

        if(input.length() < 2) {
            Toast t = Toast.makeText(Objects.requireNonNull(this.getActivity()).getApplicationContext(),
                    "Введите не менее двух символов!", Toast.LENGTH_LONG);
            t.show();
        } else {
            factoryBarcode = input;

            SharedPreferences sPref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

            KeyboardHideToolInit.keyboardHideTool(this.getActivity()).hideKeyboard();

            Command<UnbindCheckPojo> command = new UnbindCheckCommandWithFactoryBarcode(
                    new UnbindCheckCommandWithBarcode(
                            new UnbindCheckCommandWithToken(
                                    new UnbindCheckCommandSimple(),
                                    prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
                            ), input
                    ), factoryBarcode);

            NetworkAsyncTask<UnbindCheckPojo, UnbindCheckPojoResult> networkAsyncTask = new NetworkAsyncTask<>(
                    this,
                    queryDialog);

            networkAsyncTask.execute((Command) command);
        }
        searchEditText.clearFocus();
    }

    @Override
    public void showResult(UnbindRecord record) {
        NetworkCallback<UnbindPojo, UnbindPojoResult> unbindCallback = new NetworkCallback<UnbindPojo, UnbindPojoResult>() {
            @Override
            public void handleResult(CommandResult<UnbindPojo, UnbindPojoResult> result) {
                if(result.isDone()) {
                    if (getBindingContainerFragment() == null)
                        doBindingContainerFragmentTransactionFromResultBind();

                    SuccessAlertDialogCreator successADCr =
                            new SuccessAlertDialogCreatorImpl(getActivity(), result.data().getMessage());
                    successADCr.create().show();
                } else if(result.lastCommand() != null) {
                    new ReconnectAlertDialogCreatorImpl(
                            getActivity(),
                            this,
                            result.lastCommand()
                    ).create().show();
                } else
                    new ErrorAlertDialogCreatorImpl(
                            getActivity(),
                            result.data().getMessage()
                    ).create().show();
            }
        };

        OneResultAlertDialogCreator dialogCreator = new OneResultAlertDialogCreatorUnbindImpl(
                this.getActivity(),
                unbindCallback,
                record,
                queryDialog,
                factoryBarcode);
        dialogCreator.create().show();
    }

    @Override
    public void handleResult(CommandResult<UnbindCheckPojo, UnbindCheckPojoResult> result) {
        if(result.isDone()) {
            List<UnbindRecord> records = result.data().getRecords();
            if(records != null) { // check unbind
                if (records.size() != 0) {
                    if (records.size() == 1)
                        showResult(records.get(0));
                    else {
                        String title = this.getString(R.string.fragment_result_bind);
                        doResultUnbindFragmentTransaction(title, result);
                    }
                } else {
                    NoResultAlertDialogCreator noResADCr =
                            new NoResultAlertDialogCreatorImpl(this.getActivity());
                    noResADCr.create().show();
                }
            } else { //unbind done
                if(getBindingContainerFragment() == null)
                    doBindingContainerFragmentTransactionFromResultBind();

                SuccessAlertDialogCreator successADCr =
                        new SuccessAlertDialogCreatorImpl(this.getActivity(), result.data().getMessage());
                successADCr.create().show();
            }
        } else if(result.lastCommand() != null) {
            new ReconnectAlertDialogCreatorImpl(
                    this.getActivity(),
                    this,
                    result.lastCommand()
            ).create().show();
        } else
            new ErrorAlertDialogCreatorImpl(
                    this.getActivity(),
                    result.data().getMessage()
            ).create().show();
    }

    private IBindingContainerFragment getBindingContainerFragment() {
        try {
            ImplFinder<IBindingContainerFragment> bcfFinder = new ImplFinderFragmentFromActivityDefaultImpl<>(this.getActivity());
            return bcfFinder.findImpl(IBindingContainerFragment.class);
        } catch (FindableException ignore) {
            return null;
        }
    }

    private void doResultUnbindFragmentTransaction(String title, CommandResult<UnbindCheckPojo, UnbindCheckPojoResult> result) {
        FragmentTransactionManager fragmentTransactionManager =
                new FragmentTransactionManagerImpl(this.getActivity());
        fragmentTransactionManager.doResultUnbindFragmentTransaction(title, result);
    }

    private void doBindingContainerFragmentTransactionFromResultBind() {
        FragmentTransactionManager fragmentTransactionManager =
                new FragmentTransactionManagerImpl(this.getActivity());
        fragmentTransactionManager.doBindingContainerFragmentTransactionFromResultBind();
    }
}
