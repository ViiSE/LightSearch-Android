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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.KeyboardHideTool;
import ru.viise.lightsearch.activity.KeyboardHideToolImpl;
import ru.viise.lightsearch.activity.OnBackPressedListener;
import ru.viise.lightsearch.activity.OnBackPressedListenerType;
import ru.viise.lightsearch.cmd.network.task.NetworkCallback;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.CancelSoftCheckPojoResult;
import ru.viise.lightsearch.data.pojo.SendForm;
import ru.viise.lightsearch.dialog.alert.CancelSoftCheckAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.CancelSoftCheckAlertDialogCreatorFragmentImpl;
import ru.viise.lightsearch.dialog.alert.ErrorAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.ReconnectAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromActivityDefaultImpl;
import ru.viise.lightsearch.fragment.adapter.ResultSearchSoftCheckArrayAdapter;

public class SoftCheckContainerFragment extends Fragment implements ISoftCheckContainerFragment, OnBackPressedListener {

    public static final String TAG = "softCheckContainerFragment";

    private static final String SOFT_CHECK_RECORDS = "softCheckRecords";
    private List<SoftCheckRecord> softCheckRecords = new ArrayList<>();

    private static final String SCC_FRAGMENT = "SCCFragment";
    private int selected = 0; //0 - OpenSoftCheckFragment, 1 - SoftCheckFragment, 2 - CartFragment
    private OnBackPressedListenerType onBackPressedListenerType;

    private AlertDialog queryDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            selected = savedInstanceState.getInt(SCC_FRAGMENT);
            if(selected == 1)
                onBackPressedListenerType = OnBackPressedListenerType.SOFT_CHECK_FRAGMENT;
            else if(selected == 0)
                onBackPressedListenerType = OnBackPressedListenerType.OPEN_SOFT_CHECK;
            else if(selected == 2)
                onBackPressedListenerType = OnBackPressedListenerType.CART_FRAGMENT;

            softCheckRecords = savedInstanceState.getParcelableArrayList(SOFT_CHECK_RECORDS);
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(selected == 0) {
            onBackPressedListenerType = OnBackPressedListenerType.OPEN_SOFT_CHECK;
            transaction.replace(R.id.fragment_sc_container, new OpenSoftCheckFragment());
            transaction.addToBackStack(null);
        } else if(selected == 1) {
            onBackPressedListenerType = OnBackPressedListenerType.SOFT_CHECK_FRAGMENT;
            SoftCheckFragment scFragment = new SoftCheckFragment();
            scFragment.init(softCheckRecords);
            transaction.replace(R.id.fragment_sc_container, scFragment);
            transaction.addToBackStack(null);
        } else if(selected == 2) {
            onBackPressedListenerType = OnBackPressedListenerType.CART_FRAGMENT;
            CartFragment cartFragment = new CartFragment();
            cartFragment.init(softCheckRecords);
            transaction.replace(R.id.fragment_sc_container, cartFragment);
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soft_check_container, container, false);

        queryDialog = SpotsDialogCreatorInit.spotsDialogCreator(this.getActivity(), R.string.spots_dialog_query_exec)
                .create();

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
        onBackPressedListenerType = OnBackPressedListenerType.SOFT_CHECK_FRAGMENT;
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
            onBackPressedListenerType = OnBackPressedListenerType.OPEN_SOFT_CHECK;
        }
    }

    @Override
    public void switchToCartFragment(List<SoftCheckRecord> records) {
        CartFragment cartFragment = new CartFragment();
        cartFragment.init(records);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_sc_container, cartFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
        this.getActivity().setTitle(R.string.fragment_cart);
        selected = 2;
        onBackPressedListenerType = OnBackPressedListenerType.CART_FRAGMENT;
    }

//    @Override
//    public void switchToSoftCheckFragment(SoftCheckRecord record) {
//        this.getActivity().setTitle(this.getActivity().getString(R.string.fragment_soft_check));
//        getSoftCheckFragment().addSoftCheckRecord(record);
//    }

    private ISoftCheckFragment getSoftCheckFragment() {
        ImplFinder<ISoftCheckFragment> scFinder = new ImplFinderFragmentFromActivityDefaultImpl<>(this.getActivity());
        try {
            return scFinder.findImpl(ISoftCheckFragment.class);
        } catch (FindableException ignore) {
            return null;
        }
    }

    private IOpenSoftCheckFragment getOpenSoftCheckFragment() {
        ImplFinder<IOpenSoftCheckFragment> scFinder = new ImplFinderFragmentFromActivityDefaultImpl<>(this.getActivity());
        try {
            return scFinder.findImpl(IOpenSoftCheckFragment.class);
        } catch (FindableException ignore) {
            return null;
        }
    }

    private ICartFragment getCartFragment() {
        ImplFinder<ICartFragment> scFinder = new ImplFinderFragmentFromActivityDefaultImpl<>(this.getActivity());
        try {
            return scFinder.findImpl(ICartFragment.class);
        } catch (FindableException ignore) {
            return null;
        }
    }

    @Override
    public void addSoftCheckRecord(SoftCheckRecord record) {
        this.getActivity().setTitle(R.string.fragment_soft_check);
        getSoftCheckFragment().addSoftCheckRecord(record);
    }

    @Override
    public void setCardCode(String cardCode) {
        getOpenSoftCheckFragment().setCardCode(cardCode);
    }

    @Override
    public void setSoftCheckBarcode(String barcode) {
        getSoftCheckFragment().setSoftCheckBarcode(barcode);
    }

    @Override
    public void showResultSearchSoftCheckFragment(List<SoftCheckRecord> records) {
        KeyboardHideTool khTool = new KeyboardHideToolImpl(this.getActivity());
        khTool.hideKeyboard();

        View dialogView = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_soft_check_result_search, null);
        ImageButton buttonCancel = dialogView.findViewById(R.id.imageButtonCancel);
        ListView listView = dialogView.findViewById(R.id.listViewSoftCheckResult);
        listView.setAdapter(new ResultSearchSoftCheckArrayAdapter(this.getActivity(), R.id.cardViewRS, records));
        androidx.appcompat.app.AlertDialog dialogResult = new androidx.appcompat.app.AlertDialog
                .Builder(this.getActivity(), R.style.FSDialogTheme)
                .setView(dialogView)
                .create();
        dialogResult.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        listView.setOnItemClickListener((adapterView, v, position, id) -> {
            addSoftCheckRecord(records.get(position));
            dialogResult.dismiss();
        });

        buttonCancel.setOnClickListener(view -> dialogResult.dismiss());
        dialogResult.show();
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
    public void onBackPressed() {
        if(onBackPressedListenerType == OnBackPressedListenerType.SOFT_CHECK_FRAGMENT ||
                onBackPressedListenerType == OnBackPressedListenerType.CART_FRAGMENT) {
            NetworkCallback<CancelSoftCheckPojo, CancelSoftCheckPojoResult> cancelSCCallback =
                    new NetworkCallback<CancelSoftCheckPojo, CancelSoftCheckPojoResult>() {
                        @Override
                        public void handleResult(CommandResult<CancelSoftCheckPojo, CancelSoftCheckPojoResult> resultCancelSC) {
                            if (resultCancelSC.isDone()) {
                                getActivity().setTitle(getString(R.string.fragment_container));
                                callDialogSuccess(resultCancelSC.data().getMessage());
                                switchToOpenSoftCheckFragment();
                            } else if (resultCancelSC.lastCommand() != null) {
                                callReconnectDialog(this, resultCancelSC.lastCommand());
                            } else
                                callDialogError(resultCancelSC.data().getMessage());
                        }
                    };

            CancelSoftCheckAlertDialogCreator cancelSCADCr = new CancelSoftCheckAlertDialogCreatorFragmentImpl(
                    this,
                    cancelSCCallback,
                    queryDialog);
            cancelSCADCr.create().show();
        } else if(onBackPressedListenerType == OnBackPressedListenerType.OPEN_SOFT_CHECK) {
            this.getActivity().getSupportFragmentManager().popBackStack(ContainerFragment.TAG, 0);
            this.getActivity().setTitle(this.getActivity().getString(R.string.fragment_container));
        }
    }

//    @Override
//    public void refreshCartRecords(List<SoftCheckRecord> cartRecords) {
//        getCartFragment().refreshCartRecords(cartRecords);
//    }
}
