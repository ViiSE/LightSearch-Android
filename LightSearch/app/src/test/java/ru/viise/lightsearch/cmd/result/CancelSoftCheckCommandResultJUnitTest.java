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

import test.rule.CreateCancelSoftCheckCommandResultRule;

import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class CancelSoftCheckCommandResultJUnitTest {

    private CancelSoftCheckCommandResult cancelSoftCheckCommandResult;

    @Rule
    public final CreateCancelSoftCheckCommandResultRule cancelSoftCheckCommandResultRule =
            new CreateCancelSoftCheckCommandResultRule();

    @Before
    public void setUp() {
        cancelSoftCheckCommandResult = cancelSoftCheckCommandResultRule.getCancelSoftCheckCommandResult();
    }

    @Test
    public void isDone() {
        testBegin("CancelSoftCheckCommandResult", "isDone()");

        boolean isDone = cancelSoftCheckCommandResult.isDone();
        System.out.println("isDone: " + isDone);

        testEnd("CancelSoftCheckCommandResult", "isDone()");
    }

    @Test
    public void isReconnect() {
        testBegin("CancelSoftCheckCommandResult", "isReconnect()");

        boolean isReconnect = cancelSoftCheckCommandResult.isReconnect();
        System.out.println("isReconnect: " + isReconnect);

        testEnd("CancelSoftCheckCommandResult", "isReconnect()");
    }

    @Test
    public void reconnectDTO() {
        testBegin("CancelSoftCheckCommandResult", "reconnectDTO()");

        ReconnectDTO reconnectDTO = cancelSoftCheckCommandResult.lastCommand();
        System.out.println("reconnectDTO: " + reconnectDTO);

        testEnd("CancelSoftCheckCommandResult", "reconnectDTO()");
    }

    @Test
    public void message() {
        testBegin("CancelSoftCheckCommandResult", "message()");

        String message = cancelSoftCheckCommandResult.message();
        System.out.println("message: " + message);

        testEnd("CancelSoftCheckCommandResult", "message()");
    }

    @Test
    public void isCart() {
        testBegin("CancelSoftCheckCommandResult", "isCart()");

        boolean isCart = cancelSoftCheckCommandResult.isCart();
        System.out.println("isCart: " + isCart);

        testEnd("CancelSoftCheckCommandResult", "isCart()");
    }
}
