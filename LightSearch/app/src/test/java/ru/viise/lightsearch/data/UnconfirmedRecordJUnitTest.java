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

package ru.viise.lightsearch.data;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import test.rule.CreateUnconfirmedRecordRule;

import static com.google.common.truth.Truth.assertThat;
import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class UnconfirmedRecordJUnitTest {

    private UnconfirmedRecord unconfirmedRecord;

    @Rule
    public final CreateUnconfirmedRecordRule unconfirmedRecordRule = new CreateUnconfirmedRecordRule();

    @Before
    public void setUp() {
        unconfirmedRecord = unconfirmedRecordRule.getUnconfirmedRecord();
    }

    @Test
    public void barcode() {
        testBegin("UnconfirmedRecord", "barcode()");

        String barcode = unconfirmedRecord.barcode();
        assertThat(barcode).isNotNull();
        assertThat(barcode).isNotEmpty();
        assertThat(barcode).matches("[0-9]+");

        System.out.println("barcode: " + barcode);

        testEnd("UnconfirmedRecord", "barcode()");
    }

    @Test
    public void amount() {
        testBegin("UnconfirmedRecord", "amount()");

        float amount = unconfirmedRecord.amount();
        assertThat(amount).isGreaterThan(0);

        System.out.println("amount: " + amount);

        testEnd("UnconfirmedRecord", "amount()");
    }
}
