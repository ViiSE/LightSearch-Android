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

import ru.viise.lightsearch.data.pojo.UnbindCheckPojo;

public class UnbindCheckCommandWithFactoryBarcode implements Command<UnbindCheckPojo> {

    private final Command<UnbindCheckPojo> command;
    private final String factoryBarcode;

    public UnbindCheckCommandWithFactoryBarcode(Command<UnbindCheckPojo> command, String factoryBarcode) {
        this.command = command;
        this.factoryBarcode = factoryBarcode;
    }

    @Override
    public UnbindCheckPojo formForSend() {
        UnbindCheckPojo unbindCheckPojo = command.formForSend();
        unbindCheckPojo.setFactoryBarcode(factoryBarcode);

        return unbindCheckPojo;
    }

    @Override
    public String name() {
        return command.name();
    }
}
