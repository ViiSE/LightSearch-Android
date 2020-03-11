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

import test.rule.CreateSearchRecordRule;
import test.rule.CreateSubdivisionsListRule;

import static com.google.common.truth.Truth.assertThat;
import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class SearchRecordJUnitTest {

    private final String amountUnit = UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue();

    private SubdivisionList subdivisions = SubdivisionListInit.subdivisionList(amountUnit);
    private SearchRecord searchRecord;

    @Rule
    public final CreateSearchRecordRule searchRecordRule = new CreateSearchRecordRule(subdivisions);

    @Rule
    public final RuleChain chain = RuleChain
            .outerRule(new CreateSubdivisionsListRule(subdivisions, amountUnit))
            .around(searchRecordRule);

    @Before
    public void setUp() {
        searchRecord = searchRecordRule.getSearchRecord();
    }

    @Test
    public void name() {
        testBegin("SearchRecord", "name()");

        String name = searchRecord.name();
        assertThat(name).isNotNull();
        assertThat(name).isNotEmpty();

        System.out.println("name: " + name);

        testEnd("SearchRecord", "name()");
    }

    @Test
    public void barcode() {
        testBegin("SearchRecord", "barcode()");

        String barcode = searchRecord.barcode();
        assertThat(barcode).isNotNull();
        assertThat(barcode).isNotEmpty();
        assertThat(barcode).matches("[0-9]+");

        System.out.println("barcode: " + barcode);

        testEnd("SearchRecord", "barcode()");
    }

    @Test
    public void price() {
        testBegin("SearchRecord", "price()");

        float price = searchRecord.price();
        assertThat(price).isGreaterThan(0);

        System.out.println("price: " + price);

        testEnd("SearchRecord", "price()");
    }

    @Test
    public void priceWithUnit() {
        testBegin("SearchRecord", "priceWithUnit()");

        String priceWithUnit = searchRecord.priceWithUnit();
        assertThat(priceWithUnit).isNotNull();
        assertThat(priceWithUnit).isNotEmpty();

        System.out.println("priceWithUnit: " + priceWithUnit);

        testEnd("SearchRecord", "priceWithUnit()");
    }

    @Test
    public void amountUnit() {
        testBegin("SearchRecord", "amountUnit()");

        String amountUnit = searchRecord.amountUnit();
        assertThat(amountUnit).isNotNull();
        assertThat(amountUnit).isNotEmpty();

        System.out.println("amountUnit: " + amountUnit);

        testEnd("SearchRecord", "amountUnit()");
    }

    @Test
    public void maxAmountWithUnit() {
        testBegin("SearchRecord", "maxAmountWithUnit()");

        String maxAmountWithUnit = searchRecord.maxAmountWithUnit();
        assertThat(maxAmountWithUnit).isNotNull();
        assertThat(maxAmountWithUnit).isNotEmpty();

        System.out.println("maxAmountWithUnit: " + maxAmountWithUnit);

        testEnd("SearchRecord", "maxAmountWithUnit()");
    }

    @Test
    public void subdivisions() {
        testBegin("SearchRecord", "subdivisions()");

        SubdivisionList subdivisions = searchRecord.subdivisions();
        assertThat(subdivisions).isNotNull();

        System.out.println("subdivisions: " + subdivisions);

        testEnd("SearchRecord", "subdivisions()");
    }

    @Test
    public void getSubdivision() {
        testBegin("SearchRecord", "getSubdivision()");

        Subdivision subdivision = searchRecord.getSubdivision(0);
        assertThat(subdivision).isNotNull();

        System.out.println("subdivision: " + subdivision);

        testEnd("SearchRecord", "getSubdivision()");
    }

    @Test
    public void isRender() {
        testBegin("SearchRecord", "isRender()");

        boolean isRender = searchRecord.isRender();

        System.out.println("isRender: " + isRender);

        testEnd("SearchRecord", "isRender()");
    }

    @Test
    public void setIsRender() {
        testBegin("SearchRecord", "setIsRender()");

        System.out.println("setIsRender: before: " + searchRecord.isRender());
        searchRecord.setIsRender(true);
        System.out.println("setIsRender: after: " + searchRecord.isRender());

        testEnd("SearchRecord", "setIsRender()");
    }

}
