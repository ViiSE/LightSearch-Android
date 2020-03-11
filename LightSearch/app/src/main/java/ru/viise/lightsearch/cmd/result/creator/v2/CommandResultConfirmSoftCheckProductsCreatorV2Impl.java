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

package ru.viise.lightsearch.cmd.result.creator.v2;

import java.util.ArrayList;
import java.util.List;

import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.ConfirmSoftCheckProductsResultInit;
import ru.viise.lightsearch.cmd.result.creator.CommandResultCreator;
import ru.viise.lightsearch.data.CartRecord;
import ru.viise.lightsearch.data.CartRecordInit;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.UnconfirmedRecord;
import ru.viise.lightsearch.data.UnconfirmedRecordInit;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsResultPojo;
import ru.viise.lightsearch.data.pojo.ProductPojo;

public class CommandResultConfirmSoftCheckProductsCreatorV2Impl implements CommandResultCreator {

    private ConfirmSoftCheckProductsResultPojo pojo;

    public CommandResultConfirmSoftCheckProductsCreatorV2Impl(ConfirmSoftCheckProductsResultPojo pojo) {
        this.pojo = pojo;
    }

    @Override
    public CommandResult create() {
        if(pojo.getIsDone()) {

            List<SoftCheckRecord> cartRecords = new ArrayList<>();

            List<UnconfirmedRecord> unconfirmedRecords = new ArrayList<>();
            for (ProductPojo unconfirmedRec : pojo.getData()) {
                String barcode = unconfirmedRec.getId();
                String amount = unconfirmedRec.getAmount();
                UnconfirmedRecord unconfirmedRecord =
                        UnconfirmedRecordInit.unconfirmedRecord(barcode, amount);
                unconfirmedRecords.add(unconfirmedRecord);
            }

            for (SoftCheckRecord softCheckRecord : pojo.getSoftCheckRecords()) {
                String barcodeSCRec = softCheckRecord.barcode();
                String name = softCheckRecord.name();
                float price = softCheckRecord.price();
                String amountUnit = softCheckRecord.amountUnit();
                SubdivisionList subdivisions = softCheckRecord.subdivisions();
                float currentAmount = softCheckRecord.currentAmount();
                float oldMaxAmount = softCheckRecord.maxAmount();
                float newMaxAmount = softCheckRecord.maxAmount();

                for (UnconfirmedRecord unconfirmedRecord : unconfirmedRecords) {
                    if (barcodeSCRec.equals(unconfirmedRecord.barcode()))
                        newMaxAmount = unconfirmedRecord.amount();
                }

                CartRecord cartRecord = CartRecordInit.cartRecord(name, barcodeSCRec, price,
                        amountUnit, subdivisions, currentAmount, oldMaxAmount, newMaxAmount);
                cartRecords.add(cartRecord);

                softCheckRecord.setMaxAmount(newMaxAmount);
            }

            return ConfirmSoftCheckProductsResultInit.confirmSoftCheckProductsResult(
                    pojo.getIsDone(),
                    null,
                    cartRecords,
                    null);
        } else {
            return ConfirmSoftCheckProductsResultInit.confirmSoftCheckProductsResult(
                    pojo.getIsDone(),
                    null,
                    null,
                    null);
        }
    }
}
