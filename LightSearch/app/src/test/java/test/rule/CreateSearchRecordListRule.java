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

import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.SubdivisionList;

public class CreateSearchRecordListRule implements TestRule {

    private final List<SearchRecord> records;
    private final SubdivisionList subdivisions;

    public CreateSearchRecordListRule(List<SearchRecord> records, SubdivisionList subdivisions) {
        this.records = records;
        this.subdivisions = subdivisions;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                records.add(SearchRecordInit.searchRecord("Item 1", "111111", "11", "pcs.", subdivisions));
                records.add(SearchRecordInit.searchRecord("Item 2", "222222", "22", "m2", subdivisions));

                base.evaluate();
            }
        };
    }
}
