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
import org.junit.rules.RuleChain;

import ru.viise.lightsearch.util.ProductAmountFormat;
import ru.viise.lightsearch.util.ProductAmountFormatInit;
import test.rule.CreateSoftCheckRecordRule;
import test.rule.CreateSubdivisionsListRule;

import static com.google.common.truth.Truth.assertThat;
import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class SoftCheckRecordJUnitTest {

    private final String amountUnit = UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue();

    private SubdivisionList subdivisions = SubdivisionListInit.subdivisionList(amountUnit);
    private SoftCheckRecord softCheckRecord;

    @Rule
    public final CreateSoftCheckRecordRule softCheckRecordRule =
            new CreateSoftCheckRecordRule(subdivisions);

    @Rule
    public final RuleChain chain = RuleChain
            .outerRule(new CreateSubdivisionsListRule(subdivisions, amountUnit))
            .around(softCheckRecordRule);

    @Before
    public void setUp() {
        softCheckRecord = softCheckRecordRule.getSoftCheckRecord();
    }

    @Test
    public void name() {
        testBegin("SoftCheckRecord", "name()");

        String name = softCheckRecord.name();
        assertThat(name).isNotNull();
        assertThat(name).isNotEmpty();

        System.out.println("name: " + name);

        testEnd("SoftCheckRecord", "name()");
    }

    @Test
    public void barcode() {
        testBegin("SoftCheckRecord", "barcode()");

        String barcode = softCheckRecord.barcode();
        assertThat(barcode).isNotNull();
        assertThat(barcode).isNotEmpty();
        assertThat(barcode).matches("[0-9]+");

        System.out.println("barcode: " + barcode);

        testEnd("SoftCheckRecord", "barcode()");
    }

    @Test
    public void price() {
        testBegin("SoftCheckRecord", "price()");

        float price = softCheckRecord.price();
        assertThat(price).isGreaterThan(0);

        System.out.println("price: " + price);

        testEnd("SoftCheckRecord", "price()");
    }

    @Test
    public void setProductsCount() {
        testBegin("SoftCheckRecord", "setProductsCount()");

        System.out.println("cartRecord:currentAmount before: " + softCheckRecord.currentAmount());

        ProductAmountFormat pAFormat = ProductAmountFormatInit.productAmountFormat();
        float newCurrentAmount = pAFormat.format("10.0");

        softCheckRecord.setProductsCount(newCurrentAmount);
        assertThat(softCheckRecord.currentAmount()).isEqualTo(newCurrentAmount);

        System.out.println("cartRecord:currentAmount after: " + softCheckRecord.currentAmount());

        testEnd("SoftCheckRecord", "setProductsCount()");
    }

    @Test
    public void totalCostWithUnit() {
        testBegin("SoftCheckRecord", "totalCostWithUnit()");

        String totalCostWithUnit = softCheckRecord.totalCostWithUnit();
        assertThat(totalCostWithUnit).isNotNull();
        assertThat(totalCostWithUnit).isNotEmpty();

        System.out.println("totalCostWithUnit: " + totalCostWithUnit);

        testEnd("SoftCheckRecord", "totalCostWithUnit()");
    }

    @Test
    public void totalCost() {
        testBegin("SoftCheckRecord", "totalCost()");

        float totalCost = softCheckRecord.totalCost();
        assertThat(totalCost).isGreaterThan(0);

        System.out.println("totalCost: " + totalCost);

        testEnd("SoftCheckRecord", "totalCost()");
    }

    @Test
    public void maxAmount() {
        testBegin("SoftCheckRecord", "maxAmount()");

        float maxAmount = softCheckRecord.maxAmount();
        assertThat(maxAmount).isGreaterThan(0);

        System.out.println("maxAmount: " + maxAmount);

        testEnd("SoftCheckRecord", "maxAmount()");
    }

    @Test
    public void setMaxAmount() {
        testBegin("SoftCheckRecord", "setMaxAmount()");

        System.out.println("cartRecord:maxAmount before: " + softCheckRecord.maxAmount());

        ProductAmountFormat pAFormat = ProductAmountFormatInit.productAmountFormat();
        float newMaxAmount = pAFormat.format("70.0");

        softCheckRecord.setMaxAmount(newMaxAmount);
        assertThat(softCheckRecord.maxAmount()).isEqualTo(newMaxAmount);

        System.out.println("cartRecord:maxAmount after: " + softCheckRecord.maxAmount());

        testEnd("SoftCheckRecord", "setMaxAmount()");
    }

    @Test
    public void priceWithUnit() {
        testBegin("SoftCheckRecord", "priceWithUnit()");

        String priceWithUnit = softCheckRecord.priceWithUnit();
        assertThat(priceWithUnit).isNotNull();
        assertThat(priceWithUnit).isNotEmpty();

        System.out.println("priceWithUnit: " + priceWithUnit);

        testEnd("SoftCheckRecord", "priceWithUnit()");
    }

    @Test
    public void amountUnit() {
        testBegin("SoftCheckRecord", "amountUnit()");

        String amountUnit = softCheckRecord.amountUnit();
        assertThat(amountUnit).isNotNull();
        assertThat(amountUnit).isNotEmpty();

        System.out.println("amountUnit: " + amountUnit);

        testEnd("SoftCheckRecord", "amountUnit()");
    }

    @Test
    public void currentAmount() {
        testBegin("SoftCheckRecord", "currentAmount()");

        float currentAmount = softCheckRecord.currentAmount();
        assertThat(currentAmount).isGreaterThan(0);

        System.out.println("currentAmount: " + currentAmount);

        testEnd("SoftCheckRecord", "currentAmount()");
    }

    @Test
    public void maxAmountWithUnit() {
        testBegin("SoftCheckRecord", "maxAmountWithUnit()");

        String maxAmountWithUnit = softCheckRecord.maxAmountWithUnit();
        assertThat(maxAmountWithUnit).isNotNull();
        assertThat(maxAmountWithUnit).isNotEmpty();

        System.out.println("maxAmountWithUnit: " + maxAmountWithUnit);

        testEnd("SoftCheckRecord", "maxAmountWithUnit()");
    }

    @Test
    public void subdivisions() {
        testBegin("SoftCheckRecord", "subdivisions()");

        SubdivisionList subdivisions = softCheckRecord.subdivisions();
        assertThat(subdivisions).isNotNull();

        System.out.println("subdivisions: " + subdivisions);

        testEnd("SoftCheckRecord", "subdivisions()");
    }
}
