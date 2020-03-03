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

package ru.viise.lightsearch.cmd.result;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import java.util.ArrayList;
import java.util.List;

import ru.viise.lightsearch.data.ReconnectDTO;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.SubdivisionListInit;
import ru.viise.lightsearch.data.UnitsEnum;
import test.rule.CreateConfirmSoftCheckProductsResultRule;
import test.rule.CreateSoftCheckRecordsListRule;
import test.rule.CreateSubdivisionsListRule;

import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class ConfirmSoftCheckProductsResultJUnitTest {

    private final String amountUnit = UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue();
    private final List<SoftCheckRecord> records = new ArrayList<>();
    private final SubdivisionList subdivisions = SubdivisionListInit.subdivisionList(amountUnit);

    private ConfirmSoftCheckProductsResult confirmSoftCheckProductsResult;

    @Rule
    public final CreateConfirmSoftCheckProductsResultRule confirmSoftCheckProductsResultRule =
            new CreateConfirmSoftCheckProductsResultRule(records);

    @Rule
    public final RuleChain chain = RuleChain
            .outerRule(new CreateSubdivisionsListRule(subdivisions, amountUnit))
            .around(new CreateSoftCheckRecordsListRule(records, subdivisions))
            .around(confirmSoftCheckProductsResultRule);

    @Before
    public void setUp() {
        confirmSoftCheckProductsResult = confirmSoftCheckProductsResultRule.getConfirmSoftCheckProductsResult();
    }

    @Test
    public void isDone() {
        testBegin("ConfirmSoftCheckProductsResult", "isDone()");

        boolean isDone = confirmSoftCheckProductsResult.isDone();
        System.out.println("isDone: " + isDone);

        testEnd("ConfirmSoftCheckProductsResult", "isDone()");
    }

    @Test
    public void isReconnect() {
        testBegin("ConfirmSoftCheckProductsResult", "isReconnect()");

        boolean isReconnect = confirmSoftCheckProductsResult.isReconnect();
        System.out.println("isReconnect: " + isReconnect);

        testEnd("ConfirmSoftCheckProductsResult", "isReconnect()");
    }

    @Test
    public void reconnectDTO() {
        testBegin("ConfirmSoftCheckProductsResult", "reconnectDTO()");

        ReconnectDTO reconnectDTO = confirmSoftCheckProductsResult.reconnectDTO();
        System.out.println("reconnectDTO: " + reconnectDTO);

        testEnd("ConfirmSoftCheckProductsResult", "reconnectDTO()");
    }

    @Test
    public void message() {
        testBegin("ConfirmSoftCheckProductsResult", "message()");

        String message = confirmSoftCheckProductsResult.message();
        System.out.println("message: " + message);

        testEnd("ConfirmSoftCheckProductsResult", "message()");
    }

    @Test
    public void cartRecords() {
        testBegin("ConfirmSoftCheckProductsResult", "cartRecords()");

        System.out.println("cartRecords: ");
        confirmSoftCheckProductsResult.cartRecords().forEach(record -> {
            System.out.println("-----------------------------------------------------------------");
            System.out.println("name: " + record.name());
            System.out.println("barcode: " + record.barcode());
            System.out.println("amount: " + record.maxAmountWithUnit());
            System.out.println("price: " + record.priceWithUnit());
            System.out.println("subdivisions: " + record.subdivisions());
        });

        testEnd("ConfirmSoftCheckProductsResult", "cartRecords()");
    }
}
