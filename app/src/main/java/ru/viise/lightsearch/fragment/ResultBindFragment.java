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
import java.util.Objects;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.BindRecord;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreatorInit;
import ru.viise.lightsearch.dialog.spots.SpotsDialogCreatorInit;
import ru.viise.lightsearch.fragment.adapter.ResultBindArrayAdapter;

public class ResultBindFragment extends ListFragment {

    private String factoryBarcode;
    private List<BindRecord> bindRecords;

    private int selected; // 0 - bindCheck, 1 - bind

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
        ResultBindArrayAdapter adapter = new ResultBindArrayAdapter(
                Objects.requireNonNull(this.getActivity()),
                R.layout.cardview_row_result_bind, bindRecords);
        setListAdapter(adapter);
    }

    public void init(List<BindRecord> bindRecords, String factoryBarcode, int selected) {
        this.bindRecords = bindRecords;
        this.factoryBarcode = factoryBarcode;
        this.selected = selected;

//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 1 С Очень длинным НАЗВАНИЕМ",
//                "1"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 2 С Очень длинным НАЗВАНИЕМ",
//                "2"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 3 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "3"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 4 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "4"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 5 С Очень длинным списком ПОДРАЗДЕЛЕНИЙ",
//                "5"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 6 (Довольно типичный товар и тут много букв чтобы добиться большой длины да это круто)",
//                "6"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 7 С Очень длинным НАЗВАНИЕМ",
//                "7"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 8 С Очень длинным НАЗВАНИЕМ",
//                "8"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 9 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "9"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 10 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "10"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 11 С Очень длинным списком ПОДРАЗДЕЛЕНИЙ",
//                "11"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 12 (Довольно типичный товар и тут много букв чтобы добиться большой длины да это круто)",
//                "12"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 13 С Очень длинным НАЗВАНИЕМ",
//                "13"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 14 С Очень длинным НАЗВАНИЕМ",
//                "14"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 15 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "15"));
//
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 16 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "16"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 17 С Очень длинным списком ПОДРАЗДЕЛЕНИЙ",
//                "17"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 18 (Довольно типичный товар и тут много букв чтобы добиться большой длины да это круто)",
//                "18"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 19 С Очень длинным НАЗВАНИЕМ",
//                "19"));
//
//        this.bindRecords.add(BindRecordInit.bindRecord(
//                "Тестовый Товар 20 С Очень длинным НАЗВАНИЕМ",
//                "20"));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        if(selected == 0) {
            OneResultAlertDialogCreator oneResADCr =
                    OneResultAlertDialogCreatorInit.oneResultBindCheckAlertDialogCreator(
                            this.getActivity(),
                            bindRecords.get(position));
            oneResADCr.create().show();
        } else if(selected == 1) {
            OneResultAlertDialogCreator oneResADCr =
                    OneResultAlertDialogCreatorInit.oneResultBindAlertDialogCreator(
                            this.getActivity(),
                            bindRecords.get(position),
                            SpotsDialogCreatorInit
                                    .spotsDialogCreator(this.getActivity(), R.string.spots_dialog_query_exec)
                                    .create(),
                            factoryBarcode);
            oneResADCr.create().show();
        }
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
}
