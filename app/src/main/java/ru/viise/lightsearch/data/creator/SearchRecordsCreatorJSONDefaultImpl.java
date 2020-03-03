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

package ru.viise.lightsearch.data.creator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Objects;

import ru.viise.lightsearch.cmd.ClientCommandContentEnum;
import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.SearchRecordInit;
import ru.viise.lightsearch.data.SearchRecordList;
import ru.viise.lightsearch.data.Subdivision;
import ru.viise.lightsearch.data.SubdivisionInit;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.SubdivisionListInit;

public class SearchRecordsCreatorJSONDefaultImpl implements SearchRecordsCreator {

    private final String SUBDIVISION   = ClientCommandContentEnum.SUBDIVISION.stringValue();
    private final String ID            = ClientCommandContentEnum.ID.stringValue();
    private final String NAME          = ClientCommandContentEnum.NAME.stringValue();
    private final String PRICE         = ClientCommandContentEnum.PRICE.stringValue();
    private final String AMOUNT        = ClientCommandContentEnum.AMOUNT.stringValue();
    private final String UNIT          = ClientCommandContentEnum.UNIT.stringValue();

    private final JSONArray data;

    public SearchRecordsCreatorJSONDefaultImpl(Object data) {
        this.data = (JSONArray) data;
    }

    @Override
    public List<SearchRecord> createSearchRecords() {
        List<SearchRecord> records = new SearchRecordList();
        try {
            for(Object rec : data) {
                JSONObject recJ = (JSONObject) rec;

                String barcode = Objects.requireNonNull(recJ.get(ID)).toString();
                String name = Objects.requireNonNull(recJ.get(NAME)).toString();
                String price = Objects.requireNonNull(recJ.get(PRICE)).toString();
                String amountUnit = Objects.requireNonNull(recJ.get(UNIT)).toString();

                Subdivision subdivision = SubdivisionInit.subdivision(
                        Objects.requireNonNull(recJ.get(SUBDIVISION)).toString(),
                        Objects.requireNonNull(recJ.get(AMOUNT)).toString());

                SubdivisionList subdivisions = SubdivisionListInit.subdivisionList(amountUnit);
                subdivisions.addSubdivision(subdivision);

                records.add(SearchRecordInit.searchRecord(name, barcode, price, amountUnit, subdivisions));
            }

            return records;
        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            return null;
        }
    }
}
