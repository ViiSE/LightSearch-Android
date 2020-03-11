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

import ru.viise.lightsearch.data.ReconnectDTO;
import test.rule.CreateCloseSoftCheckCommandResultRule;

import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class CloseSoftCheckCommandResultJUnitTest {

    private CloseSoftCheckCommandResult closeSoftCheckCommandResult;

    @Rule
    public final CreateCloseSoftCheckCommandResultRule closeSoftCheckCommandResultRule =
            new CreateCloseSoftCheckCommandResultRule();

    @Before
    public void setUp() {
        closeSoftCheckCommandResult = closeSoftCheckCommandResultRule.getCloseSoftCheckCommandResult();
    }

    @Test
    public void isDone() {
        testBegin("CloseSoftCheckCommandResult", "isDone()");

        boolean isDone = closeSoftCheckCommandResult.isDone();
        System.out.println("isDone: " + isDone);

        testEnd("CloseSoftCheckCommandResult", "isDone()");
    }

    @Test
    public void isReconnect() {
        testBegin("CloseSoftCheckCommandResult", "isReconnect()");

        boolean isReconnect = closeSoftCheckCommandResult.isReconnect();
        System.out.println("isReconnect: " + isReconnect);

        testEnd("CloseSoftCheckCommandResult", "isReconnect()");
    }

    @Test
    public void reconnectDTO() {
        testBegin("CloseSoftCheckCommandResult", "reconnectDTO()");

        ReconnectDTO reconnectDTO = closeSoftCheckCommandResult.reconnectDTO();
        System.out.println("reconnectDTO: " + reconnectDTO);

        testEnd("CloseSoftCheckCommandResult", "reconnectDTO()");
    }

    @Test
    public void message() {
        testBegin("CloseSoftCheckCommandResult", "message()");

        String message = closeSoftCheckCommandResult.message();
        System.out.println("message: " + message);

        testEnd("CloseSoftCheckCommandResult", "message()");
    }
}
