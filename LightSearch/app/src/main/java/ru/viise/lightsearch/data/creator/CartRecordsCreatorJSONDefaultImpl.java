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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.viise.lightsearch.cmd.ClientCommandContentEnum;
import ru.viise.lightsearch.data.CartRecord;
import ru.viise.lightsearch.data.CartRecordInit;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.UnconfirmedRecord;
import ru.viise.lightsearch.data.UnconfirmedRecordInit;

public class CartRecordsCreatorJSONDefaultImpl implements CartRecordsCreator {

    private final String ID     = ClientCommandContentEnum.ID.stringValue();
    private final String AMOUNT = ClientCommandContentEnum.AMOUNT.stringValue();

    private final List<SoftCheckRecord> softCheckRecords;
    private final JSONArray unconfirmedRecordsJ;

    public CartRecordsCreatorJSONDefaultImpl(List<SoftCheckRecord> softCheckRecords, Object unconfirmedRecordsJ) {
        this.softCheckRecords = softCheckRecords;
        this.unconfirmedRecordsJ = (JSONArray) unconfirmedRecordsJ;
    }

    @Override
    public List<SoftCheckRecord> createCartRecords() {
        List<SoftCheckRecord> cartRecords = new ArrayList<>();

        List<UnconfirmedRecord> unconfirmedRecords = new ArrayList<>();
        for(Object unconfirmedRecObj : unconfirmedRecordsJ) {
            JSONObject unconfirmedRecJ = (JSONObject) unconfirmedRecObj;
            String barcode = Objects.requireNonNull(unconfirmedRecJ.get(ID)).toString();
            String amount  = Objects.requireNonNull(unconfirmedRecJ.get(AMOUNT)).toString();
            UnconfirmedRecord unconfirmedRecord =
                    UnconfirmedRecordInit.unconfirmedRecord(barcode, amount);
            unconfirmedRecords.add(unconfirmedRecord);
        }

        for(SoftCheckRecord softCheckRecord : softCheckRecords) {
            String barcodeSCRec = softCheckRecord.barcode();
            String name = softCheckRecord.name();
            float price = softCheckRecord.price();
            String amountUnit = softCheckRecord.amountUnit();
            SubdivisionList subdivisions = softCheckRecord.subdivisions();
            float currentAmount = softCheckRecord.currentAmount();
            float oldMaxAmount = softCheckRecord.maxAmount();
            float newMaxAmount = softCheckRecord.maxAmount();

            for(UnconfirmedRecord unconfirmedRecord : unconfirmedRecords) {
                if(barcodeSCRec.equals(unconfirmedRecord.barcode()))
                    newMaxAmount = unconfirmedRecord.amount();
            }

            CartRecord cartRecord = CartRecordInit.cartRecord(name, barcodeSCRec, price,
                    amountUnit, subdivisions, currentAmount, oldMaxAmount, newMaxAmount);
            cartRecords.add(cartRecord);

            softCheckRecord.setMaxAmount(newMaxAmount);
        }

        return cartRecords;
    }
}
