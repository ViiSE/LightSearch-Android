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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import ru.viise.lightsearch.data.UnitsEnum;
import ru.viise.lightsearch.data.creator.SearchRecordsCreator;
import ru.viise.lightsearch.data.creator.SearchRecordsCreatorInit;

import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.AMOUNT;
import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.ID;
import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.NAME;
import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.PRICE;
import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.SUBDIVISION;
import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.UNIT;

public class CreateSearchRecordsCreatorRule implements TestRule {

    private SearchRecordsCreator searchRecordsCreator;

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @SuppressWarnings("unchecked")
            @Override
            public void evaluate() throws Throwable {
                JSONArray data = new JSONArray();
                JSONObject record = new JSONObject();
                record.put(ID.stringValue(), "555467");
                record.put(NAME.stringValue(), "Item 1");
                record.put(PRICE.stringValue(), "77.0");
                record.put(UNIT.stringValue(), UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue());
                record.put(SUBDIVISION.stringValue(), "Sklad 2");
                record.put(AMOUNT.stringValue(), "40.2");
                data.add(record);

                searchRecordsCreator = SearchRecordsCreatorInit.searchRecordsCreator(data);

                base.evaluate();
            }
        };
    }

    public SearchRecordsCreator getSearchRecordsCreator() {
        return searchRecordsCreator;
    }
}
