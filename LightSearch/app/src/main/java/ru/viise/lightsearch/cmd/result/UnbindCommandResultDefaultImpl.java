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

package ru.viise.lightsearch.cmd.result;

import java.util.List;

import ru.viise.lightsearch.data.ReconnectDTO;
import ru.viise.lightsearch.data.UnbindRecord;

public class UnbindCommandResultDefaultImpl implements UnbindCommandResult {

    private final boolean isDone;
    private final String message;
    private final List<UnbindRecord> records;
    private final ReconnectDTO reconnectDTO;
    private final String factoryBarcode;

    public UnbindCommandResultDefaultImpl(
            boolean isDone,
            String message,
            List<UnbindRecord> records,
            String factoryBarcode,
            ReconnectDTO reconnectDTO) {
        this.isDone = isDone;
        this.message = message;
        this.records = records;
        this.factoryBarcode = factoryBarcode;
        this.reconnectDTO = reconnectDTO;
    }

    public UnbindCommandResultDefaultImpl(
            boolean isDone,
            String message,
            ReconnectDTO reconnectDTO) {
        this.isDone = isDone;
        this.message = message;
        this.records = null;
        this.factoryBarcode = null;
        this.reconnectDTO = reconnectDTO;
    }

    @Override
    public List<UnbindRecord> records() {
        return records;
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
