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

import java.util.List;

import ru.viise.lightsearch.cmd.ClientCommandContentEnum;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.creator.CartRecordsCreator;
import ru.viise.lightsearch.data.creator.CartRecordsCreatorInit;

public class CreateCartRecordsCreatorRule implements TestRule {

    private final List<SoftCheckRecord> records;

    private CartRecordsCreator cartRecordsCreator;

    public CreateCartRecordsCreatorRule(List<SoftCheckRecord> records) {
        this.records = records;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {

            @SuppressWarnings({"unchecked"})
            @Override
            public void evaluate() throws Throwable {
                JSONArray data = new JSONArray();
                JSONObject unconfRec = new JSONObject();
                unconfRec.put(ClientCommandContentEnum.ID.stringValue(), "8800500");
                unconfRec.put(ClientCommandContentEnum.AMOUNT.stringValue(), "20.0");
                data.add(unconfRec);

                cartRecordsCreator = CartRecordsCreatorInit.cartRecordsCreator(records, data);

                base.evaluate();
            }
        };
    }

    public CartRecordsCreator getCartRecordsCreator() {
        return cartRecordsCreator;
    }
}
