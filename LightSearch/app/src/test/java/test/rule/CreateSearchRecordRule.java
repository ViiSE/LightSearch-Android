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

package test.rule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.UnitsEnum;

public class CreateSearchRecordRule implements TestRule {

    private final SubdivisionList subdivisions;

    private SearchRecord searchRecord;

    public CreateSearchRecordRule(SubdivisionList subdivisions) {
        this.subdivisions = subdivisions;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                String name = "Item 1";
                String barcode = "12334278";
                String price = "55.5";
                String amountUnit = UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue();

                searchRecord = SearchRecordInit.searchRecord(name, barcode, price, amountUnit, subdivisions);

                base.evaluate();
            }
        };
    }

    public SearchRecord getSearchRecord() {
        return searchRecord;
    }
}
