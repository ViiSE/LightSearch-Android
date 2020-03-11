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

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreator;
import ru.viise.lightsearch.dialog.alert.OneResultAlertDialogCreatorInit;
import ru.viise.lightsearch.fragment.adapter.ResultSearchArrayAdapter;

public class ResultSearchFragment extends ListFragment {

    private List<SearchRecord> searchRecords;

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
        ResultSearchArrayAdapter adapter = new ResultSearchArrayAdapter(this.getActivity(),
                R.layout.cardview_row_result_search, searchRecords);
        setListAdapter(adapter);
    }

    public void init(List<SearchRecord> searchRecords) {
        this.searchRecords = searchRecords;
//        SubdivisionList sl = SubdivisionListInit.subdivisionList("шт.");
//        sl.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "10.0"));
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 1 С Очень длинным НАЗВАНИЕМ",
//                "1",
//                "1366.50",
//                "шт.",
//                sl));
//
//        SubdivisionList sl2 = SubdivisionListInit.subdivisionList("шт.");
//        sl2.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl2.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl2.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl2.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl2.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 2 С Очень длинным НАЗВАНИЕМ",
//                "2",
//                "10.50",
//                "шт.",
//                sl2));
//
//        SubdivisionList sl3 = SubdivisionListInit.subdivisionList("шт.");
//        sl3.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl3.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 3 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "3",
//                "45.0",
//                "шт.",
//                sl3));
//
//
//        SubdivisionList sl4 = SubdivisionListInit.subdivisionList("шт.");
//        sl4.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 4 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "4",
//                "200000.0",
//                "шт.",
//                sl4));
//
//        SubdivisionList sl5 = SubdivisionListInit.subdivisionList("шт.");
//        sl5.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl5.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl5.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl5.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl5.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 5 С Очень длинным списком ПОДРАЗДЕЛЕНИЙ",
//                "5",
//                "10.50",
//                "шт.",
//                sl5));
//
//        SubdivisionList sl6 = SubdivisionListInit.subdivisionList("шт.");
//        sl6.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl6.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl6.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 6 (Довольно типичный товар и тут много букв чтобы добиться большой длины да это круто)",
//                "6",
//                "45.0",
//                "шт.",
//                sl6));
//
//        SubdivisionList s7 = SubdivisionListInit.subdivisionList("шт.");
//        s7.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "10.0"));
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 7 С Очень длинным НАЗВАНИЕМ",
//                "7",
//                "1366.50",
//                "шт.",
//                s7));
//
//        SubdivisionList sl8 = SubdivisionListInit.subdivisionList("шт.");
//        sl8.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl8.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl8.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl8.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl8.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 8 С Очень длинным НАЗВАНИЕМ",
//                "8",
//                "10.50",
//                "шт.",
//                sl8));
//
//        SubdivisionList sl9 = SubdivisionListInit.subdivisionList("шт.");
//        sl9.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl9.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 9 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "9",
//                "45.0",
//                "шт.",
//                sl9));
//
//
//        SubdivisionList sl10 = SubdivisionListInit.subdivisionList("шт.");
//        sl10.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 10 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "10",
//                "200000.0",
//                "шт.",
//                sl10));
//
//        SubdivisionList sl11 = SubdivisionListInit.subdivisionList("шт.");
//        sl11.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl11.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl11.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl11.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl11.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 11 С Очень длинным списком ПОДРАЗДЕЛЕНИЙ",
//                "11",
//                "10.50",
//                "шт.",
//                sl11));
//
//        SubdivisionList sl12 = SubdivisionListInit.subdivisionList("шт.");
//        sl12.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl12.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl12.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 12 (Довольно типичный товар и тут много букв чтобы добиться большой длины да это круто)",
//                "12",
//                "45.0",
//                "шт.",
//                sl12));
//
//        SubdivisionList sl13 = SubdivisionListInit.subdivisionList("шт.");
//        sl13.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "10.0"));
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 13 С Очень длинным НАЗВАНИЕМ",
//                "13",
//                "1366.50",
//                "шт.",
//                sl13));
//
//        SubdivisionList sl14 = SubdivisionListInit.subdivisionList("шт.");
//        sl14.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl14.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl14.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl14.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl14.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 14 С Очень длинным НАЗВАНИЕМ",
//                "14",
//                "10.50",
//                "шт.",
//                sl14));
//
//        SubdivisionList sl15 = SubdivisionListInit.subdivisionList("шт.");
//        sl15.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl15.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 15 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "15",
//                "45.0",
//                "шт.",
//                sl15));
//
//
//        SubdivisionList sl16 = SubdivisionListInit.subdivisionList("шт.");
//        sl16.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 16 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "16",
//                "200000.0",
//                "шт.",
//                sl16));
//
//        SubdivisionList sl17 = SubdivisionListInit.subdivisionList("шт.");
//        sl17.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl17.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl17.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl17.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl17.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 17 С Очень длинным списком ПОДРАЗДЕЛЕНИЙ",
//                "17",
//                "10.50",
//                "шт.",
//                sl17));
//
//        SubdivisionList sl18 = SubdivisionListInit.subdivisionList("шт.");
//        sl18.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl18.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl18.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 18 (Довольно типичный товар и тут много букв чтобы добиться большой длины да это круто)",
//                "18",
//                "45.0",
//                "шт.",
//                sl18));
//
//        SubdivisionList sl19 = SubdivisionListInit.subdivisionList("шт.");
//        sl19.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "10.0"));
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 19 С Очень длинным НАЗВАНИЕМ",
//                "19",
//                "1366.50",
//                "шт.",
//                sl19));
//
//        SubdivisionList sl20 = SubdivisionListInit.subdivisionList("шт.");
//        sl20.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl20.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl20.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl20.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl20.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 20 С Очень длинным НАЗВАНИЕМ",
//                "20",
//                "10.50",
//                "шт.",
//                sl20));
//
//        SubdivisionList sl21 = SubdivisionListInit.subdivisionList("шт.");
//        sl21.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl21.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 21 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "21",
//                "45.0",
//                "шт.",
//                sl21));
//
//
//        SubdivisionList sl22 = SubdivisionListInit.subdivisionList("шт.");
//        sl22.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 22 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "22",
//                "200000.0",
//                "шт.",
//                sl22));
//
//        SubdivisionList sl23 = SubdivisionListInit.subdivisionList("шт.");
//        sl23.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl23.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl23.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl23.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl23.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 23 С Очень длинным списком ПОДРАЗДЕЛЕНИЙ",
//                "23",
//                "10.50",
//                "шт.",
//                sl23));
//
//        SubdivisionList sl24 = SubdivisionListInit.subdivisionList("шт.");
//        sl24.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl24.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl24.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 24 (Довольно типичный товар и тут много букв чтобы добиться большой длины да это круто)",
//                "24",
//                "45.0",
//                "шт.",
//                sl24));
//
//        SubdivisionList sl25 = SubdivisionListInit.subdivisionList("шт.");
//        sl25.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl25.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl25.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl25.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl25.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 25 С Очень длинным НАЗВАНИЕМ",
//                "25",
//                "10.50",
//                "шт.",
//                sl25));
//
//        SubdivisionList sl26 = SubdivisionListInit.subdivisionList("шт.");
//        sl26.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl26.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 26 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "26",
//                "45.0",
//                "шт.",
//                sl26));
//
//
//        SubdivisionList sl27 = SubdivisionListInit.subdivisionList("шт.");
//        sl27.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 27 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "27",
//                "200000.0",
//                "шт.",
//                sl27));
//
//        SubdivisionList sl28 = SubdivisionListInit.subdivisionList("шт.");
//        sl28.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl28.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl28.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl28.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl28.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 28 С Очень длинным списком ПОДРАЗДЕЛЕНИЙ",
//                "28",
//                "10.50",
//                "шт.",
//                sl28));
//
//        SubdivisionList sl29 = SubdivisionListInit.subdivisionList("шт.");
//        sl29.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl29.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl29.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 29 (Довольно типичный товар и тут много букв чтобы добиться большой длины да это круто)",
//                "29",
//                "45.0",
//                "шт.",
//                sl29));
//
//        SubdivisionList s30 = SubdivisionListInit.subdivisionList("шт.");
//        s30.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "10.0"));
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 30 С Очень длинным НАЗВАНИЕМ",
//                "30",
//                "1366.50",
//                "шт.",
//                s30));
//
//        SubdivisionList sl31 = SubdivisionListInit.subdivisionList("шт.");
//        sl31.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl31.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl31.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl31.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl31.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 31 С Очень длинным НАЗВАНИЕМ",
//                "31",
//                "10.50",
//                "шт.",
//                sl31));
//
//        SubdivisionList sl32 = SubdivisionListInit.subdivisionList("шт.");
//        sl32.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl32.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 32 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "32",
//                "45.0",
//                "шт.",
//                sl32));
//
//        SubdivisionList sl33 = SubdivisionListInit.subdivisionList("шт.");
//        sl33.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 33 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "33",
//                "200000.0",
//                "шт.",
//                sl33));
//
//        SubdivisionList sl34 = SubdivisionListInit.subdivisionList("шт.");
//        sl34.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl34.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl34.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl34.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl34.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 34 С Очень длинным списком ПОДРАЗДЕЛЕНИЙ",
//                "34",
//                "10.50",
//                "шт.",
//                sl34));
//
//        SubdivisionList sl35 = SubdivisionListInit.subdivisionList("шт.");
//        sl35.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl35.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl35.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 35 (Довольно типичный товар и тут много букв чтобы добиться большой длины да это круто)",
//                "35",
//                "45.0",
//                "шт.",
//                sl35));
//
//        SubdivisionList sl36 = SubdivisionListInit.subdivisionList("шт.");
//        sl36.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "10.0"));
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 36 С Очень длинным НАЗВАНИЕМ",
//                "36",
//                "1366.50",
//                "шт.",
//                sl36));
//
//        SubdivisionList sl37 = SubdivisionListInit.subdivisionList("шт.");
//        sl37.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl37.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl37.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl37.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl37.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 37 С Очень длинным НАЗВАНИЕМ",
//                "37",
//                "10.50",
//                "шт.",
//                sl37));
//
//        SubdivisionList sl38 = SubdivisionListInit.subdivisionList("шт.");
//        sl38.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl38.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 38 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "38",
//                "45.0",
//                "шт.",
//                sl38));
//
//
//        SubdivisionList sl39 = SubdivisionListInit.subdivisionList("шт.");
//        sl39.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 39 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "39",
//                "200000.0",
//                "шт.",
//                sl39));
//
//        SubdivisionList sl40 = SubdivisionListInit.subdivisionList("шт.");
//        sl40.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl40.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl40.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl40.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl40.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 40 С Очень длинным списком ПОДРАЗДЕЛЕНИЙ",
//                "40",
//                "10.50",
//                "шт.",
//                sl40));
//
//        SubdivisionList sl41 = SubdivisionListInit.subdivisionList("шт.");
//        sl41.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl41.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl41.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 41 (Довольно типичный товар и тут много букв чтобы добиться большой длины да это круто)",
//                "41",
//                "45.0",
//                "шт.",
//                sl41));
//
//        SubdivisionList sl42 = SubdivisionListInit.subdivisionList("шт.");
//        sl42.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "10.0"));
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 19 С Очень длинным НАЗВАНИЕМ",
//                "42",
//                "1366.50",
//                "шт.",
//                sl42));
//
//        SubdivisionList sl43 = SubdivisionListInit.subdivisionList("шт.");
//        sl43.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl43.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl43.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl43.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl43.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 43 С Очень длинным НАЗВАНИЕМ",
//                "43",
//                "10.50",
//                "шт.",
//                sl43));
//
//        SubdivisionList sl44 = SubdivisionListInit.subdivisionList("шт.");
//        sl44.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl44.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 44 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "44",
//                "45.0",
//                "шт.",
//                sl44));
//
//
//        SubdivisionList sl45 = SubdivisionListInit.subdivisionList("шт.");
//        sl45.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 45 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "45",
//                "200000.0",
//                "шт.",
//                sl45));
//
//        SubdivisionList sl46 = SubdivisionListInit.subdivisionList("шт.");
//        sl46.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl46.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl46.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl46.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl46.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 46 С Очень длинным списком ПОДРАЗДЕЛЕНИЙ",
//                "46",
//                "10.50",
//                "шт.",
//                sl46));
//
//        SubdivisionList sl47 = SubdivisionListInit.subdivisionList("шт.");
//        sl47.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl47.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl47.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 47 (Довольно типичный товар и тут много букв чтобы добиться большой длины да это круто)",
//                "47",
//                "45.0",
//                "шт.",
//                sl47));
//
//        SubdivisionList sl48 = SubdivisionListInit.subdivisionList("шт.");
//        sl48.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl48.addSubdivision(SubdivisionInit.subdivision("ТК 8 (Проспект 60-лет октября)", "90.0"));
//        sl48.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//        sl48.addSubdivision(SubdivisionInit.subdivision("Еще один склад на какой-то улице", "10.0"));
//        sl48.addSubdivision(SubdivisionInit.subdivision("Склад 200", "15.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 48 С Очень длинным НАЗВАНИЕМ",
//                "48",
//                "10.50",
//                "шт.",
//                sl48));
//
//        SubdivisionList sl49 = SubdivisionListInit.subdivisionList("шт.");
//        sl49.addSubdivision(SubdivisionInit.subdivision("Супер склад на выборе", "15.0"));
//        sl49.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 49 С НЕ ТАКИМ УЖ и Очень длинным НАЗВАНИЕМ",
//                "49",
//                "45.0",
//                "шт.",
//                sl49));
//
//
//        SubdivisionList sl50 = SubdivisionListInit.subdivisionList("шт.");
//        sl50.addSubdivision(SubdivisionInit.subdivision("Склад 1000 (ВЫБОР)", "22.0"));
//
//        this.searchRecords.add(SearchRecordInit.searchRecord(
//                "Тестовый Товар 50 С У Которого Только Одно ПОДРАЗДЕЛЕНИЕ",
//                "50",
//                "200000.0",
//                "шт.",
//                sl50));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        OneResultAlertDialogCreator oneResADCr =
                OneResultAlertDialogCreatorInit.oneResultSearchAlertDialogCreator(this.getActivity(),
                        searchRecords.get(position));
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
                getActivity().setTitle(StackFragmentTitle.pop());
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
            return false;
        });
    }
}
