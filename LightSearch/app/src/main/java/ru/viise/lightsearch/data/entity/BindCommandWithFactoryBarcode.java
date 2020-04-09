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

import ru.viise.lightsearch.data.pojo.BindPojo;

public class BindCommandWithFactoryBarcode implements Command<BindPojo> {

    private final Command<BindPojo> command;
    private final String factoryBarcode;

    public BindCommandWithFactoryBarcode(Command<BindPojo> command, String factoryBarcode) {
        this.command = command;
        this.factoryBarcode = factoryBarcode;
    }

    @Override
    public BindPojo formForSend() {
        BindPojo bp = command.formForSend();
        bp.setFactoryBarcode(factoryBarcode);

        return bp;
    }

    @Override
    public String name() {
        return command.name();
    }
}
