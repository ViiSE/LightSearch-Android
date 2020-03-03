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

import java.util.Collection;

import test.rule.CreateSubdivisionRule;
import test.rule.CreateSubdivisionsListRule;

import static com.google.common.truth.Truth.assertThat;
import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class SubdivisionListJUnitTest {

    private final String amountUnit = UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue();

    private SubdivisionList subdivisions = SubdivisionListInit.subdivisionList(amountUnit);
    private Subdivision subdivision;

    @Rule
    public final CreateSubdivisionsListRule subdivisionsListRule =
            new CreateSubdivisionsListRule(subdivisions, amountUnit);

    @Rule
    public final CreateSubdivisionRule subdivisionRule = new CreateSubdivisionRule();

    @Before
    public void setUp() {
        subdivision = subdivisionRule.getSubdivision();
    }

    @Test
    public void addSubdivision() {
        testBegin("SubdivisionList", "addSubdivision()");

        assertThat(subdivision).isNotNull();

        System.out.println("Subdivisions: before: " + subdivisions);
        subdivisions.addSubdivision(subdivision);
        System.out.println("Subdivisions: before: " + subdivisions);

        testEnd("SubdivisionList", "addSubdivision()");
    }

    @Test
    public void collection() {
        testBegin("SubdivisionList", "collection()");

        Collection subdivCollection = subdivisions.collection();
        assertThat(subdivCollection).isNotNull();

        System.out.println("Subdivision collection: " + subdivCollection);

        testEnd("SubdivisionList", "collection()");
    }
}
