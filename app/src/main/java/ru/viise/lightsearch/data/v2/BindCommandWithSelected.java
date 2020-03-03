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

package ru.viise.lightsearch.data.v2;

import ru.viise.lightsearch.data.pojo.BindPojo;

public class BindCommandWithSelected implements Command<BindPojo> {

    private final Command<BindPojo> command;
    private final int selected;

    public BindCommandWithSelected(Command<BindPojo> command, int selected) {
        this.command = command;
        this.selected = selected;
    }

    @Override
    public BindPojo formForSend() {
        BindPojo bindPojo = command.formForSend();
        bindPojo.setSelected(selected);

        return bindPojo;
    }

    @Override
    public String name() {
        return command.name();
    }
}
