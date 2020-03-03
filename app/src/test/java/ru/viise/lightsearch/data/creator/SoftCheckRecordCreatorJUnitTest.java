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

import ru.viise.lightsearch.data.SoftCheckRecord;
import test.rule.CreateSoftCheckRecordCreatorRule;

import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class SoftCheckRecordCreatorJUnitTest {

    private SoftCheckRecordCreator softCheckRecordCreator;

    @Rule
    public final CreateSoftCheckRecordCreatorRule softCheckRecordCreatorRule =
            new CreateSoftCheckRecordCreatorRule();

    @Before
    public void setUp() {
        softCheckRecordCreator = softCheckRecordCreatorRule.getSoftCheckRecordCreator();
    }

    @Test
    public void createSoftCheckRecord() {
        testBegin("SoftCheckRecordCreator", "createSoftCheckRecord()");

        SoftCheckRecord record = softCheckRecordCreator.createSoftCheckRecord();
        System.out.println("SoftCheckRecord: ");
        System.out.println("name: " + record.name());
        System.out.println("barcode: " + record.barcode());
        System.out.println("currentAmount: " + record.currentAmount());
        System.out.println("maxAmount: " + record.maxAmountWithUnit());
        System.out.println("totalCost: " + record.totalCostWithUnit());
        System.out.println("price: " + record.priceWithUnit());
        System.out.println("subdivisions: " + record.subdivisions());

        testEnd("SoftCheckRecordCreator", "createSoftCheckRecord()");
    }
}
