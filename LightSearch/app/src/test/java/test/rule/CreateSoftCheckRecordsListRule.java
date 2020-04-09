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

import java.util.List;

import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.SubdivisionList;

public class CreateSoftCheckRecordsListRule implements TestRule {

    private final String amountUnit = "pcs.";

    private final List<SoftCheckRecord> records;
    private final SubdivisionList subdivList;

    public CreateSoftCheckRecordsListRule(List<SoftCheckRecord> records, SubdivisionList subdivList) {
        this.records = records;
        this.subdivList = subdivList;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                SoftCheckRecord item1 = SoftCheckRecordInit.softCheckRecord(
                        "Item 1", "111111", "35", amountUnit, subdivList);
                item1.setProductsCount(100L);

                SoftCheckRecord item2 = SoftCheckRecordInit.softCheckRecord(
                        "Item 2", "222222", "100", amountUnit, subdivList);
                item2.setProductsCount(10L);

                SoftCheckRecord item3 = SoftCheckRecordInit.softCheckRecord
                        ("Item 3", "3060901", "1000", amountUnit, subdivList);
                item3.setProductsCount(50L);

                records.add(item1);
                records.add(item2);
                records.add(item3);

                base.evaluate();
            }
        };
    }
}
