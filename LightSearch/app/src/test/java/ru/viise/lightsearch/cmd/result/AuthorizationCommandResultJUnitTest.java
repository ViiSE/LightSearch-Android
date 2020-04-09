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

package ru.viise.lightsearch.cmd.result;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import test.rule.CreateAuthorizationCommandResultRule;

import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class AuthorizationCommandResultJUnitTest {

    private AuthorizationCommandResult authorizationCommandResult;

    @Rule
    public final CreateAuthorizationCommandResultRule authorizationCommandResultRule =
            new CreateAuthorizationCommandResultRule();

    @Before
    public void setUp() {
        authorizationCommandResult = authorizationCommandResultRule.getAuthorizationCommandResult();
    }

    @Test
    public void isDone() {
        testBegin("AuthorizationCommandResult", "isDone()");

        boolean isDone = authorizationCommandResult.isDone();
        System.out.println("isDone: " + isDone);

        testEnd("AuthorizationCommandResult", "isDone()");
    }

    @Test
    public void isReconnect() {
        testBegin("AuthorizationCommandResult", "isReconnect()");

        boolean isReconnect = authorizationCommandResult.isReconnect();
        System.out.println("isReconnect: " + isReconnect);

        testEnd("AuthorizationCommandResult", "isReconnect()");
    }

    @Test
    public void reconnectDTO() {
        testBegin("AuthorizationCommandResult", "reconnectDTO()");

        ReconnectDTO reconnectDTO = authorizationCommandResult.lastCommand();
        System.out.println("reconnectDTO: " + reconnectDTO);

        testEnd("AuthorizationCommandResult", "reconnectDTO()");
    }

    @Test
    public void message() {
        testBegin("AuthorizationCommandResult", "message()");

        String message = authorizationCommandResult.message();
        System.out.println("message: " + message);

        testEnd("AuthorizationCommandResult", "message()");
    }

    @Test
    public void userIdent() {
        testBegin("AuthorizationCommandResult", "userIdent()");

        String userIdent = authorizationCommandResult.userIdent();
        System.out.println("userIdent: " + userIdent);

        testEnd("AuthorizationCommandResult", "userIdent()");
    }

    @Test
    public void skladList() {
        testBegin("AuthorizationCommandResult", "skladList()");

        String[] skladList = authorizationCommandResult.skladList();
        System.out.println("skladList: " + Arrays.toString(skladList));

        testEnd("AuthorizationCommandResult", "skladList()");
    }

    @Test
    public void TKList() {
        testBegin("AuthorizationCommandResult", "TKList()");

        String[] TKList = authorizationCommandResult.TKList();
        System.out.println("TKList: " + Arrays.toString(TKList));

        testEnd("AuthorizationCommandResult", "TKList()");
    }

}
