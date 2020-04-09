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

package ru.viise.lightsearch.data.entity;

import java.util.ArrayList;
import java.util.List;

import ru.viise.lightsearch.data.CartRecord;
import ru.viise.lightsearch.data.CartRecordImpl;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.UnconfirmedRecord;
import ru.viise.lightsearch.data.UnconfirmedRecordImpl;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojo;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojoRawResult;
import ru.viise.lightsearch.data.pojo.ConfirmSoftCheckProductsPojoResult;
import ru.viise.lightsearch.data.pojo.ProductPojo;

public class ConfirmSoftCheckProductsCommandResult implements CommandResult<ConfirmSoftCheckProductsPojo, ConfirmSoftCheckProductsPojoResult> {

    private final Command<ConfirmSoftCheckProductsPojo> lastCommand;
    private final ConfirmSoftCheckProductsPojoRawResult resultRawPojo;
    private ConfirmSoftCheckProductsPojoResult resultPojo;

    public ConfirmSoftCheckProductsCommandResult(
            Command<ConfirmSoftCheckProductsPojo> lastCommand,
            ConfirmSoftCheckProductsPojoRawResult resultRawPojo) {
        this.lastCommand = lastCommand;
        this.resultRawPojo = resultRawPojo;
    }

    public ConfirmSoftCheckProductsCommandResult(ConfirmSoftCheckProductsPojoRawResult resultRawPojo) {
        this.lastCommand = null;
        this.resultRawPojo = resultRawPojo;
    }

    @Override
    public boolean isDone() {
        return resultRawPojo.getIsDone();
    }

    @Override
    public ConfirmSoftCheckProductsPojoResult data() {
        if(resultPojo == null) {
            List<SoftCheckRecord> softCheckRecords = new ArrayList<>();

            List<UnconfirmedRecord> unconfirmedRecords = new ArrayList<>();
            for (ProductPojo unconfirmedRec : resultRawPojo.getData()) {
                String barcode = unconfirmedRec.getId();
                String amount = unconfirmedRec.getAmount();
                UnconfirmedRecord unconfirmedRecord = new UnconfirmedRecordImpl(barcode, amount);
                unconfirmedRecords.add(unconfirmedRecord);
            }

            for (SoftCheckRecord softCheckRecord : resultRawPojo.getSoftCheckRecords()) {
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

                CartRecord cartRecord = new CartRecordImpl(name, barcodeSCRec, price,
                        amountUnit, subdivisions, currentAmount, oldMaxAmount, newMaxAmount);
                softCheckRecords.add(cartRecord);

                softCheckRecord.setMaxAmount(newMaxAmount);
            }

            resultPojo = new ConfirmSoftCheckProductsPojoResult();
            resultPojo.setIsDone(resultRawPojo.getIsDone());
            resultPojo.setMessage(resultRawPojo.getMessage());
            resultPojo.setRecords(softCheckRecords);
            resultPojo.setSoftCheckRecords(resultRawPojo.getSoftCheckRecords());
        }

        return resultPojo;
    }

    @Override
    public Command<ConfirmSoftCheckProductsPojo> lastCommand() {
        return lastCommand;
    }
}
