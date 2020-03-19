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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.activity.KeyboardHideTool;
import ru.viise.lightsearch.activity.KeyboardHideToolDefaultImpl;
import ru.viise.lightsearch.activity.ManagerActivityHandler;
import ru.viise.lightsearch.activity.OnBackPressedListener;
import ru.viise.lightsearch.activity.OnBackPressedListenerType;
import ru.viise.lightsearch.data.CartRecord;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.dialog.alert.AlertDialogUtil;
import ru.viise.lightsearch.dialog.alert.CancelSoftCheckAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.CancelSoftCheckAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.alert.DialogOKContainer;
import ru.viise.lightsearch.dialog.alert.DialogOKContainerCreatorInit;
import ru.viise.lightsearch.dialog.alert.DialogOKContainerInit;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromActivityDefaultImpl;
import ru.viise.lightsearch.find.ImplFinderFragmentFromFragmentDefaultImpl;
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

//    @Override
//    public void switchToSoftCheckFragment() {
//        SoftCheckFragment scFragment = new SoftCheckFragment();
//        scFragment.init(softCheckRecords);
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_up, R.anim.enter_from_up, R.anim.exit_to_down);
//        transaction.replace(R.id.fragment_sc_container, scFragment);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.addToBackStack(null);
//        transaction.commit();
//        selected = 1;
//        onBackPressedListenerType = OnBackPressedListenerType.SOFT_CHECK_FRAGMENT;
//    }
//
//    @Override
//    public void switchToOpenSoftCheckFragment() {
//        if (selected != 0) {
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_down, R.anim.enter_from_down, R.anim.exit_to_up);
//            transaction.replace(R.id.fragment_sc_container, new OpenSoftCheckFragment());
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            transaction.addToBackStack(null);
//            transaction.commit();
//            softCheckRecords.clear();
//            selected = 0;
//            onBackPressedListenerType = OnBackPressedListenerType.OPEN_SOFT_CHECK;
//        }
//    }

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
        selected = 2;
        onBackPressedListenerType = OnBackPressedListenerType.CART_FRAGMENT;
    }

    @Override
    public void switchToSoftCheckFragment(SoftCheckRecord record) {
        this.getActivity().setTitle(this.getActivity().getString(R.string.fragment_soft_check));
        getSoftCheckFragment().addSoftCheckRecord(record);
    }

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

//    @Override
//    public void showResultSearchSoftCheckFragment(List<SoftCheckRecord> records) {
//        ResultSearchSoftCheckFragment resultFragment = new ResultSearchSoftCheckFragment();
//        resultFragment.init(records);
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_up, R.anim.enter_from_up, R.anim.exit_to_down);
//        transaction.replace(R.id.fragment_sc_container, resultFragment);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.addToBackStack(null);
//        transaction.commit();
//        StackFragmentTitle.push(getString(R.string.fragment_soft_check));
//        this.getActivity().setTitle(R.string.fragment_result_soft_check);
//        KeyboardHideTool khTool = new KeyboardHideToolDefaultImpl(this.getActivity());
//        khTool.hideKeyboard();
//    }

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
        KeyboardHideTool khTool = new KeyboardHideToolDefaultImpl(this.getActivity());
        khTool.hideKeyboard();
//        ResultSearchDialogFragment dialogFragment = new ResultSearchDialogFragment();
//        dialogFragment.init(records);
//        dialogFragment.show(getFragmentManager(), ResultSearchDialogFragment.TAG);
//        this.getActivity().setTitle(R.string.fragment_result_soft_check);

        View dialogView = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_soft_check_result_search, null);
        ImageButton buttonCancel = dialogView.findViewById(R.id.imageButtonCancel);
        ListView listView = dialogView.findViewById(R.id.listViewSoftCheckResult);
        listView.setAdapter(new ResultSearchSoftCheckArrayAdapter(this.getActivity(), R.id.cardViewRS, records));
        android.support.v7.app.AlertDialog dialogResult = new android.support.v7.app.AlertDialog
                .Builder(this.getActivity(), R.style.FSDialogTheme)
                .setView(dialogView)
                .create();
        listView.setOnItemClickListener((adapterView, v, position, id) -> {
            addSoftCheckRecord(records.get(position));
            dialogResult.dismiss();
        });

        buttonCancel.setOnClickListener(view -> dialogResult.dismiss());
        AlertDialogUtil.setTransparentBackground(dialogResult);
        dialogResult.show();
//        ResultSearchSoftCheckFragment resultFragment = new ResultSearchSoftCheckFragment();
//        resultFragment.init(records);
//
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_up, R.anim.enter_from_up, R.anim.exit_to_down);
//        transaction.replace(R.id.fragment_open_sc, resultFragment);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.addToBackStack(ResultSearchSoftCheckFragment.TAG);
//        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(onBackPressedListenerType == OnBackPressedListenerType.SOFT_CHECK_FRAGMENT) {
            CancelSoftCheckAlertDialogCreator cancelSCADCr =
                    CancelSoftCheckAlertDialogCreatorInit.cancelSoftCheckAlertDialogCreator(
                            this, (ManagerActivityHandler) this.getActivity(), queryDialog);
            cancelSCADCr.create().show();
        } else if(onBackPressedListenerType == OnBackPressedListenerType.OPEN_SOFT_CHECK) {
            this.getActivity().getSupportFragmentManager().popBackStack(ContainerFragment.TAG, 0);
            this.getActivity().setTitle(this.getActivity().getString(R.string.fragment_container));
        } else if(onBackPressedListenerType == OnBackPressedListenerType.CART_FRAGMENT) {
            CancelSoftCheckAlertDialogCreator cancelSCADCr =
                    CancelSoftCheckAlertDialogCreatorInit.cancelSoftCheckAlertDialogCreator(
                            this, (ManagerActivityHandler) this.getActivity(), queryDialog);
            cancelSCADCr.create().show();
        }
    }

    @Override
    public void refreshCartRecords(List<SoftCheckRecord> cartRecords) {
        getCartFragment().refreshCartRecords(cartRecords);
    }
}
