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
import java.util.Objects;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.cmd.network.task.NetworkCallback;
import ru.viise.lightsearch.data.UnbindRecord;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.pojo.UnbindPojo;
import ru.viise.lightsearch.data.pojo.UnbindPojoResult;
import ru.viise.lightsearch.dialog.alert.ErrorAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreatorUnbindImpl;
import ru.viise.lightsearch.dialog.alert.ReconnectAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.SuccessAlertDialogCreatorImpl;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.exception.FindableException;
import ru.viise.lightsearch.find.ImplFinder;
import ru.viise.lightsearch.find.ImplFinderFragmentFromActivityDefaultImpl;
import ru.viise.lightsearch.fragment.adapter.ResultUnbindArrayAdapter;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManager;
import ru.viise.lightsearch.fragment.transaction.FragmentTransactionManagerImpl;

public class ResultUnbindFragment extends ListFragment {

    private String factoryBarcode;

    private List<UnbindRecord> unbindRecords;

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
        ResultUnbindArrayAdapter adapter = new ResultUnbindArrayAdapter(
                Objects.requireNonNull(this.getActivity()),
                R.layout.cardview_row_result_bind, unbindRecords);
        setListAdapter(adapter);
    }

    public void init(List<UnbindRecord> bindRecords, String factoryBarcode) {
        this.unbindRecords = bindRecords;
        this.factoryBarcode = factoryBarcode;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

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

        OneResultAlertDialogCreator oneResADCr =
                new OneResultAlertDialogCreatorUnbindImpl(
                            this.getActivity(),
                            unbindCallback,
                            unbindRecords.get(position),
                            SpotsDialogCreatorInit
                                    .spotsDialogCreator(this.getActivity(), R.string.spots_dialog_query_exec)
                                    .create(),
                            factoryBarcode);
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
                Objects.requireNonNull(getActivity()).setTitle(StackFragmentTitle.pop());
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
            return false;
        });
    }

    private IBindingContainerFragment getBindingContainerFragment() {
        try {
            ImplFinder<IBindingContainerFragment> bcfFinder = new ImplFinderFragmentFromActivityDefaultImpl<>(this.getActivity());
            return bcfFinder.findImpl(IBindingContainerFragment.class);
        } catch (FindableException ignore) {
            return null;
        }
    }

    private void doBindingContainerFragmentTransactionFromResultBind() {
        FragmentTransactionManager fragmentTransactionManager =
                new FragmentTransactionManagerImpl(this.getActivity());
        fragmentTransactionManager.doBindingContainerFragmentTransactionFromResultBind();
    }
}
