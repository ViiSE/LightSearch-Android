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
import ru.viise.lightsearch.data.creator.SoftCheckRecordCreator;
import ru.viise.lightsearch.data.creator.SoftCheckRecordCreatorInit;

import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.AMOUNT;
import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.ID;
import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.NAME;
import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.PRICE;
import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.SUBDIVISION;
import static ru.viise.lightsearch.cmd.ClientCommandContentEnum.UNIT;

public class CreateSoftCheckRecordCreatorRule implements TestRule {

    private SoftCheckRecordCreator softCheckRecordCreator;

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @SuppressWarnings("unchecked")
            @Override
            public void evaluate() throws Throwable {
                JSONArray data = new JSONArray();

                JSONObject record1 = new JSONObject();
                record1.put(ID.stringValue(), "1111111");
                record1.put(NAME.stringValue(), "Item 11");
                record1.put(PRICE.stringValue(), "4490.0");
                record1.put(UNIT.stringValue(), UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue());
                record1.put(SUBDIVISION.stringValue(), "Sklad 1");
                record1.put(AMOUNT.stringValue(), "30.0");

                JSONObject record2 = new JSONObject();
                record2.put(ID.stringValue(), "1111111");
                record2.put(NAME.stringValue(), "Item 11");
                record2.put(PRICE.stringValue(), "4490.0");
                record2.put(UNIT.stringValue(), UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue());
                record2.put(SUBDIVISION.stringValue(), "Sklad 2");
                record2.put(AMOUNT.stringValue(), "39.4");

                data.add(record1);
                data.add(record2);

                softCheckRecordCreator = SoftCheckRecordCreatorInit.softCheckRecordCreator(data);

                base.evaluate();
            }
        };
    }

    public SoftCheckRecordCreator getSoftCheckRecordCreator() {
        return softCheckRecordCreator;
    }
}
