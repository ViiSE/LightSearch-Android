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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import java.util.ArrayList;
import java.util.List;

import ru.viise.lightsearch.data.CartRecord;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.SubdivisionListInit;
import ru.viise.lightsearch.data.UnitsEnum;
import test.rule.CreateCartRecordsCreatorRule;
import test.rule.CreateSoftCheckRecordsListRule;
import test.rule.CreateSubdivisionsListRule;

import static com.google.common.truth.Truth.assertThat;
import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class CartRecordsCreatorJUnitTest {

    private final String amountUnit = UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue();

    private List<SoftCheckRecord> scRecords = new ArrayList<>();
    private SubdivisionList subdivisions = SubdivisionListInit.subdivisionList(amountUnit);

    private CartRecordsCreator cartRecordsCreator;

    @Rule
    public final CreateCartRecordsCreatorRule cartRecordsCreatorRule =
            new CreateCartRecordsCreatorRule(scRecords);

    @Rule
    public final RuleChain chain = RuleChain
            .outerRule(new CreateSubdivisionsListRule(subdivisions, amountUnit))
            .around(new CreateSoftCheckRecordsListRule(scRecords, subdivisions))
            .around(cartRecordsCreatorRule);

    @Before
    public void setUp() {
        cartRecordsCreator = cartRecordsCreatorRule.getCartRecordsCreator();
    }

    @Test
    public void createCardRecords() {
        testBegin("CartRecordsCreator", "createCardRecords()");

        List<SoftCheckRecord> records = cartRecordsCreator.createCartRecords();
        assertThat(records).isNotNull();

        System.out.println("Cart records list: ");
        records.forEach( record -> {
            CartRecord cartRecord = (CartRecord) record;
            System.out.println("-----------------------------------------------------------------");
            System.out.println("name: " + cartRecord.name());
            System.out.println("barcode: " + cartRecord.barcode());
            System.out.println("maxAmount: " + cartRecord.maxAmountWithUnit());
            System.out.println("price: " + cartRecord.priceWithUnit());
            System.out.println("currentAmount: " + cartRecord.currentAmount());
            System.out.println("totalCost: " + cartRecord.totalCostWithUnit());
            System.out.println("subdivisions: " + cartRecord.subdivisions());
            System.out.println("isConfirmed: " + cartRecord.isConfirmed());
            System.out.println("oldMaxAmount: " + cartRecord.oldMaxAmountWithUnit());
        });

        testEnd("CartRecordsCreator", "createCardRecords()");
    }
}
