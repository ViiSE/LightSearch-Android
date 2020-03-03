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

public class BindCommandResultInit {

    public static BindCommandResult bindCheckCommandResult(
            boolean isDone,
            String message,
            List<BindRecord> records,
            ReconnectDTO reconnectDTO,
            int selected,
            String factoryBarcode) {
        return new BindCommandResultDefaultImpl(
                isDone,
                message,
                records,
                reconnectDTO,
                selected,
                factoryBarcode);
    }

    public static BindCommandResult bindCommandResult(
            boolean isDone,
            String message,
            int selected,
            ReconnectDTO reconnectDTO,
            String factoryBarcode) {
        return new BindCommandResultDefaultImpl(isDone, message, selected, reconnectDTO, factoryBarcode);
    }
}
