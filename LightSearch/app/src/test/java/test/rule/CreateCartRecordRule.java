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

import ru.viise.lightsearch.data.CartRecord;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.UnitsEnum;

public class CreateCartRecordRule implements TestRule {

    private CartRecord record;

    private final SubdivisionList subdivisionList;

    public CreateCartRecordRule(SubdivisionList subdivisionList) {
        this.subdivisionList = subdivisionList;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                String name = "Item 1";
                String barcode = "5554381";
                float price = 77f;
                String amountUnit = UnitsEnum.CURRENT_AMOUNT_CART_UNIT.stringValue();
                float currentAmount = 40f;
                float oldMaxAmount = 50f;
                float newMaxAmount = 60f;

                record = CartRecordInit.cartRecord(
                        name, barcode, price, amountUnit, subdivisionList, currentAmount,
                        oldMaxAmount, newMaxAmount);

                base.evaluate();
            }
        };
    }

    public CartRecord getCardRecord() {
        return record;
    }
}
