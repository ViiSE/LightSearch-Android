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
import test.rule.CreateCartRecordRule;
import test.rule.CreateSubdivisionsListRule;

import static com.google.common.truth.Truth.assertThat;
import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class CartRecordJUnitTest {

    private final String amountUnit = UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue();
    private SubdivisionList subdivisionList = SubdivisionListInit.subdivisionList(amountUnit);
    private CartRecord cartRecord;

    @Rule
    public final CreateCartRecordRule cartRecordRule = new CreateCartRecordRule(subdivisionList);

    @Rule
    public final RuleChain chain = RuleChain
            .outerRule(new CreateSubdivisionsListRule(subdivisionList, amountUnit))
            .around(cartRecordRule);


    @Before
    public void setUp() {
        cartRecord = cartRecordRule.getCardRecord();
    }

    @Test
    public void isConfirmed() {
        testBegin("CartRecord", "isConfirmed()");

        boolean isConfirmed = cartRecord.isConfirmed();
        System.out.println("isConfirmed: " + isConfirmed);

        testEnd("CartRecord", "isConfirmed()");
    }

    @Test
    public void oldMaxAmountWithUnit() {
        testBegin("CartRecord", "oldMaxAmountWithUnit()");

        String oldMaxAmountWithUnit = cartRecord.oldMaxAmountWithUnit();
        System.out.println("oldMaxAmountWithUnit: " + oldMaxAmountWithUnit);

        testEnd("CartRecord", "oldMaxAmountWithUnit()");
    }

    @Test
    public void name() {
        testBegin("CartRecord", "name()");

        String name = cartRecord.name();
        assertThat(name).isNotNull();
        assertThat(name).isNotEmpty();

        System.out.println("name: " + name);

        testEnd("CartRecord", "name()");
    }

    @Test
    public void barcode() {
        testBegin("CartRecord", "barcode()");

        String barcode = cartRecord.barcode();
        assertThat(barcode).isNotNull();
        assertThat(barcode).isNotEmpty();
        assertThat(barcode).matches("[0-9]+");

        System.out.println("barcode: " + barcode);

        testEnd("CartRecord", "barcode()");
    }

    @Test
    public void price() {
        testBegin("CartRecord", "price()");

        float price = cartRecord.price();
        System.out.println("price: " + price);

        testEnd("CartRecord", "price()");
    }

    @Test
    public void priceWithUnit() {
        testBegin("CartRecord", "priceWithUnit()");

        String priceWithUnit = cartRecord.priceWithUnit();
        assertThat(priceWithUnit).isNotNull();
        assertThat(priceWithUnit).isNotEmpty();

        System.out.println("priceWithUnit: " + priceWithUnit);

        testEnd("CartRecord", "priceWithUnit()");
    }

    @Test
    public void amountUnit() {
        testBegin("CartRecord", "amountUnit()");

        String amountUnit = cartRecord.amountUnit();
        assertThat(amountUnit).isNotNull();
        assertThat(amountUnit).isNotEmpty();

        System.out.println("amountUnit: " + amountUnit);

        testEnd("CartRecord", "amountUnit()");
    }

    @Test
    public void currentAmount() {
        testBegin("CartRecord", "currentAmount()");

        float currentAmount = cartRecord.currentAmount();
        System.out.println("currentAmount: " + currentAmount);

        testEnd("CartRecord", "currentAmount()");
    }

    @Test
    public void totalCostWithUnit() {
        testBegin("CartRecord", "totalCostWithUnit()");

        String totalCostWithUnit = cartRecord.totalCostWithUnit();
        assertThat(totalCostWithUnit).isNotNull();
        assertThat(totalCostWithUnit).isNotEmpty();

        System.out.println("totalCostWithUnit: " + totalCostWithUnit);

        testEnd("CartRecord", "totalCostWithUnit()");
    }

    @Test
    public void totalCost() {
        testBegin("CartRecord", "totalCost()");

        float totalCost = cartRecord.totalCost();
        System.out.println("totalCost: " + totalCost);

        testEnd("CartRecord", "totalCost()");
    }

    @Test
    public void maxAmount() {
        testBegin("CartRecord", "maxAmount()");

        float maxAmount = cartRecord.maxAmount();
        System.out.println("totalCost: " + maxAmount);

        testEnd("CartRecord", "maxAmount()");
    }

    @Test
    public void maxAmountWithUnit() {
        testBegin("CartRecord", "maxAmountWithUnit()");

        String maxAmountWithUnit = cartRecord.maxAmountWithUnit();
        assertThat(maxAmountWithUnit).isNotNull();
        assertThat(maxAmountWithUnit).isNotEmpty();

        System.out.println("maxAmountWithUnit: " + maxAmountWithUnit);

        testEnd("CartRecord", "maxAmountWithUnit()");
    }

    @Test
    public void subdivisions() {
        testBegin("CartRecord", "subdivisions()");

        SubdivisionList subdivisions = cartRecord.subdivisions();
        assertThat(subdivisions).isNotNull();

        System.out.println("subdivisions: " + subdivisions);

        testEnd("CartRecord", "subdivisions()");
    }

    @Test
    public void setMaxAmount() {
        testBegin("CartRecord", "setMaxAmount()");

        System.out.println("cartRecord:maxAmount before: " + cartRecord.maxAmount());

        ProductAmountFormat pAFormat = ProductAmountFormatInit.productAmountFormat();
        float newMaxAmount = pAFormat.format("40.0");

        cartRecord.setMaxAmount(newMaxAmount);
        assertThat(cartRecord.maxAmount()).isEqualTo(newMaxAmount);

        System.out.println("cartRecord:maxAmount after: " + cartRecord.maxAmount());

        testEnd("CartRecord", "setMaxAmount()");
    }

    @Test
    public void setProductsCount() {
        testBegin("CartRecord", "setProductsCount()");

        System.out.println("cartRecord:currentAmount before: " + cartRecord.currentAmount());

        ProductAmountFormat pAFormat = ProductAmountFormatInit.productAmountFormat();
        float newCurrentAmount = pAFormat.format("10.0");

        cartRecord.setProductsCount(newCurrentAmount);
        assertThat(cartRecord.currentAmount()).isEqualTo(newCurrentAmount);

        System.out.println("cartRecord:currentAmount after: " + cartRecord.currentAmount());

        testEnd("CartRecord", "setProductsCount()");
    }
}
