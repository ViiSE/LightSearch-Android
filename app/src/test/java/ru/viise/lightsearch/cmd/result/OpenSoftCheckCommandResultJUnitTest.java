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
import test.rule.CreateOpenSoftCheckCommandResultRule;

import static test.TestMessage.testBegin;
import static test.TestMessage.testEnd;

public class OpenSoftCheckCommandResultJUnitTest {

    private OpenSoftCheckCommandResult openSoftCheckCommandResult;

    @Rule
    public final CreateOpenSoftCheckCommandResultRule openSoftCheckCommandResultRule =
            new CreateOpenSoftCheckCommandResultRule();

    @Before
    public void setUp() {
        openSoftCheckCommandResult = openSoftCheckCommandResultRule.getOpenSoftCheckCommandResult();
    }

    @Test
    public void isDone() {
        testBegin("OpenSoftCheckCommandResult", "isDone()");

        boolean isDone = openSoftCheckCommandResult.isDone();
        System.out.println("isDone: " + isDone);

        testEnd("OpenSoftCheckCommandResult", "isDone()");
    }

    @Test
    public void isReconnect() {
        testBegin("OpenSoftCheckCommandResult", "isReconnect()");

        boolean isReconnect = openSoftCheckCommandResult.isReconnect();
        System.out.println("isReconnect: " + isReconnect);

        testEnd("OpenSoftCheckCommandResult", "isReconnect()");
    }

    @Test
    public void reconnectDTO() {
        testBegin("OpenSoftCheckCommandResult", "reconnectDTO()");

        ReconnectDTO reconnectDTO = openSoftCheckCommandResult.reconnectDTO();
        System.out.println("reconnectDTO: " + reconnectDTO);

        testEnd("OpenSoftCheckCommandResult", "reconnectDTO()");
    }

    @Test
    public void message() {
        testBegin("OpenSoftCheckCommandResult", "message()");

        String message = openSoftCheckCommandResult.message();
        System.out.println("message: " + message);

        testEnd("OpenSoftCheckCommandResult", "message()");
    }
}
