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

import java.util.List;

import ru.viise.lightsearch.data.BindRecord;
import ru.viise.lightsearch.data.ReconnectDTO;

public class BindCommandResultDefaultImpl implements BindCommandResult {

    private final boolean isDone;
    private final String message;
    private final List<BindRecord> records;
    private final ReconnectDTO reconnectDTO;
    private final int selected;
    private final String factoryBarcode;

    public BindCommandResultDefaultImpl(
            boolean isDone,
            String message,
            List<BindRecord> records,
            ReconnectDTO reconnectDTO,
            int selected,
            String factoryBarcode) {
        this.isDone = isDone;
        this.message = message;
        this.records = records;
        this.reconnectDTO = reconnectDTO;
        this.selected = selected;
        this.factoryBarcode = factoryBarcode;
    }

    public BindCommandResultDefaultImpl(
            boolean isDone,
            String message,
            int selected,
            ReconnectDTO reconnectDTO,
            String factoryBarcode) {
        this.isDone = isDone;
        this.message = message;
        this.records = null;
        this.reconnectDTO = reconnectDTO;
        this.selected = selected;
        this.factoryBarcode = factoryBarcode;
    }

    @Override
    public List<BindRecord> records() {
        return records;
    }

    @Override
    public int selected() {
        return selected;
    }

    @Override
    public String factoryBarcode() {
        return factoryBarcode;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public boolean isReconnect() {
        return reconnectDTO != null;
    }

    @Override
    public ReconnectDTO reconnectDTO() {
        return reconnectDTO;
    }

    @Override
    public String message() {
        return message;
    }
}
