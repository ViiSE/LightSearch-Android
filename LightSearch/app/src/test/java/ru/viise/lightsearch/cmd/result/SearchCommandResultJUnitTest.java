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

import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.UnitsEnum;
import test.rule.CreateSearchCommandResultRule;
import test.rule.CreateSearchRecordListRule;
import test.rule.CreateSubdivisionsListRule;

import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class SearchCommandResultJUnitTest {

    private final String amountUnit = UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue();
    private SubdivisionList subdivisions = SubdivisionListInit.subdivisionList(amountUnit);
    private List<SearchRecord> records = new ArrayList<>();
    private SearchCommandResult searchCommandResult;

    @Rule
    public final CreateSearchCommandResultRule searchCommandResultRule =
            new CreateSearchCommandResultRule(records);

    @Rule
    public final RuleChain chain = RuleChain
            .outerRule(new CreateSubdivisionsListRule(subdivisions, amountUnit))
            .around(new CreateSearchRecordListRule(records, subdivisions))
            .around(searchCommandResultRule);

    @Before
    public void setUp() {
        searchCommandResult = searchCommandResultRule.getSearchCommandResult();
    }

    @Test
    public void isDone() {
        testBegin("SearchCommandResult", "isDone()");

        boolean isDone = searchCommandResult.isDone();
        System.out.println("isDone: " + isDone);

        testEnd("SearchCommandResult", "isDone()");
    }

    @Test
    public void isReconnect() {
        testBegin("SearchCommandResult", "isReconnect()");

        boolean isReconnect = searchCommandResult.isReconnect();
        System.out.println("isReconnect: " + isReconnect);

        testEnd("SearchCommandResult", "isReconnect()");
    }

    @Test
    public void reconnectDTO() {
        testBegin("SearchCommandResult", "reconnectDTO()");

        ReconnectDTO reconnectDTO = searchCommandResult.lastCommand();
        System.out.println("reconnectDTO: " + reconnectDTO);

        testEnd("SearchCommandResult", "reconnectDTO()");
    }

    @Test
    public void message() {
        testBegin("SearchCommandResult", "message()");

        String message = searchCommandResult.message();
        System.out.println("message: " + message);

        testEnd("SearchCommandResult", "message()");
    }

    @Test
    public void subdivision() {
        testBegin("SearchCommandResult", "subdivision()");

        String subdivision = searchCommandResult.subdivision();
        System.out.println("subdivision: " + subdivision);

        testEnd("SearchCommandResult", "subdivision()");
    }

    @Test
    public void records() {
        testBegin("SearchCommandResult", "records()");

        System.out.println("records: ");
        searchCommandResult.records().forEach(record -> {
            System.out.println("-----------------------------------------------------------------");
            System.out.println("name: " + record.name());
            System.out.println("amount: " + record.maxAmountWithUnit());
            System.out.println("price: " + record.priceWithUnit());
            System.out.println("barcode: " + record.barcode());
            System.out.println("subdivisions: " + record.subdivisions());
        });

        testEnd("SearchCommandResult", "records()");
    }
}
