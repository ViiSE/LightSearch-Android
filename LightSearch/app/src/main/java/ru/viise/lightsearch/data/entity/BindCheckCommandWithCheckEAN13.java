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

import ru.viise.lightsearch.data.pojo.BindCheckPojo;

public class BindCheckCommandWithCheckEAN13 implements Command<BindCheckPojo> {

    private final Command<BindCheckPojo> command;
    private final boolean isCheckEAN13;

    public BindCheckCommandWithCheckEAN13(Command<BindCheckPojo> command, boolean isCheckEAN13) {
        this.command = command;
        this.isCheckEAN13 = isCheckEAN13;
    }

    @Override
    public BindCheckPojo formForSend() {
        BindCheckPojo bindCheckPojo = command.formForSend();
        bindCheckPojo.setCheckEan13(isCheckEAN13);

        return bindCheckPojo;
    }

    @Override
    public String name() {
        return command.name();
    }
}
