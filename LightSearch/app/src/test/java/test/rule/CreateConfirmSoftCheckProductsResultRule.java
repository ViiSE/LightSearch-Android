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

import ru.viise.lightsearch.cmd.result.ConfirmSoftCheckProductsResult;
import ru.viise.lightsearch.cmd.result.ConfirmSoftCheckProductsResultInit;
import ru.viise.lightsearch.data.SoftCheckRecord;

public class CreateConfirmSoftCheckProductsResultRule implements TestRule {

    private ConfirmSoftCheckProductsResult confirmSoftCheckProductsResult;
    private final List<SoftCheckRecord> records;

    public CreateConfirmSoftCheckProductsResultRule(List<SoftCheckRecord> records) {
        this.records = records;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                confirmSoftCheckProductsResult =
                        ConfirmSoftCheckProductsResultInit.confirmSoftCheckProductsResult(
                                true, "Products is confirmed!", records, null);

                base.evaluate();
            }
        };
    }

    public ConfirmSoftCheckProductsResult getConfirmSoftCheckProductsResult() {
        return confirmSoftCheckProductsResult;
    }
}
