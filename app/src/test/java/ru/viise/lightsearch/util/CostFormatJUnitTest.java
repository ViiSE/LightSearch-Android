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

package ru.viise.lightsearch.util;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class CostFormatJUnitTest {

    @Test
    public void format() {
        testBegin("CostFormat", "format()");

        float totalCost = 55.58001f;

        CostFormat costFormat = CostFormatInit.costFormat();
        float formatCost = costFormat.format(totalCost);

        assertThat(formatCost).isEqualTo(55.58f);

        System.out.println("formatCost: " + formatCost);

        testEnd("CostFormat", "format()");
    }
}
