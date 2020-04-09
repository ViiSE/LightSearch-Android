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

import ru.viise.lightsearch.data.SubdivisionList;

public class CreateSubdivisionsListRule implements TestRule {

    private final SubdivisionList subdivisionList;

    public CreateSubdivisionsListRule(SubdivisionList subdivisionList, String amountUnit) {
        if(subdivisionList == null)
            subdivisionList = SubdivisionListInit.subdivisionList(amountUnit);
        this.subdivisionList = subdivisionList;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                subdivisionList.addSubdivision(SubdivisionInit.subdivision("Sklad 1", "50"));
                subdivisionList.addSubdivision(SubdivisionInit.subdivision("Sklad 2", "10"));
                subdivisionList.addSubdivision(SubdivisionInit.subdivision("TK 1", "90"));
                subdivisionList.addSubdivision(SubdivisionInit.subdivision("TK 2", "3"));

                base.evaluate();
            }
        };
    }
}
