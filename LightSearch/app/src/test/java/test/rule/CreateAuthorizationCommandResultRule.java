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

import ru.viise.lightsearch.cmd.result.AuthorizationCommandResult;
import ru.viise.lightsearch.cmd.result.AuthorizationCommandResultInit;

public class CreateAuthorizationCommandResultRule implements TestRule {

    private AuthorizationCommandResult authorizationCommandResult;

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                boolean isDone = true;
                String message = "Connect!";
                String userIdent = "007";
                String[] skladList = new String[] {"Sklad 1", "Sklad 2"};
                String[] TKList = new String[] {"TK 1", "TK 2"};

                authorizationCommandResult = AuthorizationCommandResultInit.authorizationCommandResult(
                        isDone, message, userIdent, null, skladList, TKList);

                base.evaluate();
            }
        };
    }

    public AuthorizationCommandResult getAuthorizationCommandResult() {
        return authorizationCommandResult;
    }
}
