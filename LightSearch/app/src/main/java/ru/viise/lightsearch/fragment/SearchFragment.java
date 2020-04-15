
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.KeyboardHideToolInit;
import ru.viise.lightsearch.activity.ManagerActivityUI;
import ru.viise.lightsearch.activity.scan.ScannerInit;
import ru.viise.lightsearch.cmd.ClientCommands;
import ru.viise.lightsearch.cmd.network.task.FillAdapterAsyncTask;
import ru.viise.lightsearch.cmd.network.task.NetworkAsyncTask;
import ru.viise.lightsearch.cmd.network.task.NetworkCallback;
import ru.viise.lightsearch.data.ScanType;
import ru.viise.lightsearch.data.SearchFragmentContentEnum;
import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.entity.SearchCommandSimple;
import ru.viise.lightsearch.data.entity.SearchCommandWithBarcode;
import ru.viise.lightsearch.data.entity.SearchCommandWithSklad;
import ru.viise.lightsearch.data.entity.SearchCommandWithSubdivision;
import ru.viise.lightsearch.data.entity.SearchCommandWithTK;
import ru.viise.lightsearch.data.entity.SearchCommandWithToken;
import ru.viise.lightsearch.data.pojo.SearchPojo;
import ru.viise.lightsearch.data.pojo.SearchPojoResult;
import ru.viise.lightsearch.dialog.alert.ErrorAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.NoResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.NoResultAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.ReconnectAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManager;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManagerImpl;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;


public class SearchFragment extends Fragment implements ISearchFragment, NetworkCallback<SearchPojo, SearchPojoResult> {

    private final static String MENU_SELECTED = "selected";
    private final static String SKLAD_ARRAY = "sklad_array";
    private final static String TK_ARRAY = "tk_array";
    private int selected = 0; //0-sklad, 1-TK, 2 - ALL

    private AlertDialog queryDialog;
    private SpinnerWithCallback skladSpinner;
    private SpinnerWithCallback TKSpinner;
    private EditText searchEditText;
    private RadioButton skladRadioButton;
    private RadioButton TKRadioButton;
    private RadioButton AllRadioButton;

    private String[] skladArray;
    private String[] TKArray;

    private ManagerActivityUI managerActivityUI;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            selected = savedInstanceState.getInt(MENU_SELECTED);
            TKArray = savedInstanceState.getStringArray(TK_ARRAY);
            skladArray = savedInstanceState.getStringArray(SKLAD_ARRAY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        skladSpinner = view.findViewById(R.id.spinnerSklad);
        TKSpinner = view.findViewById(R.id.spinnerTK);
        searchEditText = view.findViewById(R.id.editTextSearch);
        FloatingActionButton barcodeButton = view.findViewById(R.id.floatingActionButtonBarcode);
        skladRadioButton = view.findViewById(R.id.radioButtonSklad);
        TKRadioButton = view.findViewById(R.id.radioButtonTK);
        AllRadioButton = view.findViewById(R.id.radioButtonAll);

        TKSpinner.afterSetAdapterCallback(dataTK -> TKArray = dataTK);
        skladSpinner.afterSetAdapterCallback(dataSklad -> skladArray = dataSklad);

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String barcode = searchEditText.getText().toString();

                if(barcode.length() < 2) {
                    Toast t = Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                            "Введите не менее двух символов!", Toast.LENGTH_LONG);
                    t.show();
                } else {
                    KeyboardHideToolInit.keyboardHideTool(getActivity()).hideKeyboard();
                    run(barcode);
                }
                searchEditText.clearFocus();
                v.requestFocus();
                return true;
            }
            return false;
        });

        barcodeButton.setOnClickListener(view1 -> {
            KeyboardHideToolInit.keyboardHideTool(this.getActivity()).hideKeyboard();
            searchEditText.clearFocus();
            view1.requestFocus();
            managerActivityUI.setScanType(ScanType.SEARCH);
            ScannerInit.scanner(this.getActivity()).scan();
        });

        switch(selected) {
            case 0:
                skladRadioButton.setChecked(true);
                TKSpinner.setEnabled(false);
                skladSpinner.setEnabled(true);
                break;
            case 1:
                TKRadioButton.setChecked(true);
                TKSpinner.setEnabled(true);
                skladSpinner.setEnabled(false);
                break;
            case 2:
                AllRadioButton.setChecked(true);
                TKSpinner.setEnabled(false);
                skladSpinner.setEnabled(false);
                break;
        }

        ProgressBar pBarSpinnerSklad = view.findViewById(R.id.pBarSpinnerSklad);
        ProgressBar pBarSpinnerTK = view.findViewById(R.id.pBarSpinnerTK);

        fillSpinner(pBarSpinnerSklad, ClientCommands.SKLAD_LIST, skladSpinner, skladArray);
        fillSpinner(pBarSpinnerTK, ClientCommands.TK_LIST, TKSpinner, TKArray);

        skladRadioButton.setOnClickListener(view1 -> {
            skladSpinner.setEnabled(true);
            System.out.println("I am here");
            TKSpinner.setEnabled(false);
            selected = 0;
        });
        TKRadioButton.setOnClickListener(view2 -> {
            skladSpinner.setEnabled(false);
            TKSpinner.setEnabled(true);
            selected = 1;
        });

        AllRadioButton.setOnClickListener(view3 -> {
            skladSpinner.setEnabled(false);
            TKSpinner.setEnabled(false);
            selected = 2;
        });

        queryDialog = SpotsDialogCreatorInit.spotsDialogCreator(this.getActivity(), R.string.spots_dialog_query_exec)
                .create();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(MENU_SELECTED, selected);
        outState.putStringArray(SKLAD_ARRAY, skladArray);
        outState.putStringArray(TK_ARRAY, TKArray);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        managerActivityUI = (ManagerActivityUI) this.getActivity();
    }

    public void init(String[] skladArray, String[] TKArray) {
        this.skladArray = skladArray;
        this.TKArray = TKArray;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void fillSpinner(ProgressBar progressBar, String commandName, SpinnerWithCallback spinner, String[] dataArray) {
        FillAdapterAsyncTask fillTask = new FillAdapterAsyncTask(
                new WeakReference<>(spinner),
                new WeakReference<>(progressBar),
                new WeakReference<>(this.getActivity()),
                commandName);
        fillTask.execute(dataArray);
    }

    private SearchFragmentContentEnum getSubdivision() {
        if(skladRadioButton.isChecked())
            return SearchFragmentContentEnum.SKLAD;
        else if(TKRadioButton.isChecked())
            return SearchFragmentContentEnum.TK;
        else if(AllRadioButton.isChecked())
            return SearchFragmentContentEnum.ALL;
        else
            return SearchFragmentContentEnum.NULL;
    }

    private String getSelectedSklad() {
        String selectedSklad = skladSpinner.getSelectedItem().toString();
        SearchFragmentContentEnum type = getSubdivision();
        if(type == SearchFragmentContentEnum.ALL)
            return SearchFragmentContentEnum.ALL.stringValue();
        else if(type == SearchFragmentContentEnum.SKLAD)
            return selectedSklad.equals(SearchFragmentContentEnum.ALL_UI.stringValue())
                    ? SearchFragmentContentEnum.ALL.stringValue()
                    : selectedSklad;
        else
            return SearchFragmentContentEnum.NULL.stringValue();
    }

    private String getSelectedTK() {
        String selectedTK = TKSpinner.getSelectedItem().toString();
        SearchFragmentContentEnum type = getSubdivision();
        if(type == SearchFragmentContentEnum.ALL)
            return SearchFragmentContentEnum.ALL.stringValue();
        else if(type == SearchFragmentContentEnum.TK)
            return selectedTK.equals(SearchFragmentContentEnum.ALL_UI.stringValue())
                ? SearchFragmentContentEnum.ALL.stringValue()
                : selectedTK;
        else
            return SearchFragmentContentEnum.NULL.stringValue();
    }

    @Override
    public void setSearchBarcode(String barcode, boolean isRun) {
        searchEditText.setText(barcode);
        if(isRun)
            run(barcode);
    }

    private void run(String barcode) {
        SharedPreferences sPref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

        Command<SearchPojo> command = new SearchCommandWithBarcode(
                new SearchCommandWithSklad(
                        new SearchCommandWithTK(
                                new SearchCommandWithSubdivision(
                                        new SearchCommandWithToken(
                                                new SearchCommandSimple(),
                                                prefManager.load(PreferencesManagerType.TOKEN_MANAGER)
                                        ), getSubdivision().uiValue()
                                ), getSelectedTK()
                        ), getSelectedSklad()
                ), barcode);

        NetworkAsyncTask<SearchPojo, SearchPojoResult> networkAsyncTask = new NetworkAsyncTask<>(
                this,
                queryDialog);

        networkAsyncTask.execute((Command) command);
    }

    @Override
    public void handleResult(CommandResult<SearchPojo, SearchPojoResult> result) {
        if(result.isDone()) {
            List<SearchRecord> records = result.data().getRecords();
            if (records.size() != 0) {
                String title = this.getString(R.string.search_result) + " " + result.data().getSubdivision();
                if (records.size() == 1) {
                    OneResultAlertDialogCreator oneResADCr =
                            new OneResultAlertDialogCreatorImpl(this.getActivity(), records.get(0));
                    androidx.appcompat.app.AlertDialog oneResAD = oneResADCr.create();
                    oneResAD.setCanceledOnTouchOutside(false);
                    oneResAD.show();
                } else {
                    FragmentTransactionManager fragmentTransactionManager =
                            new FragmentTransactionManagerImpl(this.getActivity());
                    fragmentTransactionManager.doResultSearchFragmentTransaction(title, records);
                }
            } else {
                NoResultAlertDialogCreator noResADCr =
                        new NoResultAlertDialogCreatorImpl(this.getActivity());
                noResADCr.create().show();
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
}
