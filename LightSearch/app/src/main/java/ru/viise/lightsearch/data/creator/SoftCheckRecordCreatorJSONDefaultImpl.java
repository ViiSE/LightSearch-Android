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

import java.util.Objects;

import ru.viise.lightsearch.cmd.ClientCommandContentEnum;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.SoftCheckRecordInit;
import ru.viise.lightsearch.data.Subdivision;
import ru.viise.lightsearch.data.SubdivisionInit;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.SubdivisionListInit;

public class SoftCheckRecordCreatorJSONDefaultImpl implements SoftCheckRecordCreator {

    private final String SUBDIVISION   = ClientCommandContentEnum.SUBDIVISION.stringValue();
    private final String ID            = ClientCommandContentEnum.ID.stringValue();
    private final String NAME          = ClientCommandContentEnum.NAME.stringValue();
    private final String PRICE         = ClientCommandContentEnum.PRICE.stringValue();
    private final String AMOUNT        = ClientCommandContentEnum.AMOUNT.stringValue();
    private final String UNIT          = ClientCommandContentEnum.UNIT.stringValue();

    private final JSONArray data;

    public SoftCheckRecordCreatorJSONDefaultImpl(Object data) {
        this.data = (JSONArray) data;
    }

    @Override
    public SoftCheckRecord createSoftCheckRecord() {
        try {
            JSONObject recJOne = (JSONObject) data.get(0);
            String barcode = Objects.requireNonNull(recJOne.get(ID)).toString();
            String name = Objects.requireNonNull(recJOne.get(NAME)).toString();
            String price = Objects.requireNonNull(recJOne.get(PRICE)).toString();
            String amountUnit = Objects.requireNonNull(recJOne.get(UNIT)).toString();

            SubdivisionList subdivisions = SubdivisionListInit.subdivisionList(amountUnit);

            for (Object rec : data) {
                JSONObject recJ = (JSONObject) rec;
                Subdivision subdivision = SubdivisionInit.subdivision(
                        Objects.requireNonNull(recJ.get(SUBDIVISION)).toString(),
                        Objects.requireNonNull(recJ.get(AMOUNT)).toString());
                subdivisions.addSubdivision(subdivision);
            }
            return SoftCheckRecordInit.softCheckRecord(name, barcode, price, amountUnit, subdivisions);
        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            return null;
        }
    }
}