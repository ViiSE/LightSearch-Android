/*
 *  Copyright 2020 ViiSE.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package ru.viise.lightsearch.data.entity;

import ru.viise.lightsearch.data.pojo.CloseSoftCheckPojo;

public class CloseSoftCheckCommandWithToken implements Command<CloseSoftCheckPojo> {

    private final Command<CloseSoftCheckPojo> command;
    private final String token;

    public CloseSoftCheckCommandWithToken(Command<CloseSoftCheckPojo> command, String token) {
        this.command = command;
        this.token = token;
    }

    @Override
    public CloseSoftCheckPojo formForSend() {
        CloseSoftCheckPojo closeSoftCheckPojo = command.formForSend();
        closeSoftCheckPojo.setToken(token);

        return closeSoftCheckPojo;
    }

    @Override
    public String name() {
        return command.name();
    }
}
