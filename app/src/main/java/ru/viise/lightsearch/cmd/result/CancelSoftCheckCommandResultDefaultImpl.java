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

import ru.viise.lightsearch.data.ReconnectDTO;

public class CancelSoftCheckCommandResultDefaultImpl implements CancelSoftCheckCommandResult {

    private final boolean isDone;
    private final String message;
    private final ReconnectDTO reconnectDTO;
    private final boolean isCart;

    public CancelSoftCheckCommandResultDefaultImpl(boolean isDone, String message, ReconnectDTO reconnectDTO,
               boolean isCart) {
        this.isDone = isDone;
        this.message = message;
        this.reconnectDTO = reconnectDTO;
        this.isCart = isCart;
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

    @Override
    public boolean isCart() {
        return isCart;
    }
}
