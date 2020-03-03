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

package ru.viise.lightsearch.cmd.result.creator.v2;

import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.UnbindCommandResultInit;
import ru.viise.lightsearch.cmd.result.creator.CommandResultCreator;
import ru.viise.lightsearch.data.pojo.UnbindResultPojo;

public class CommandResultUnbindCreatorV2Impl implements CommandResultCreator {

    private final UnbindResultPojo pojo;

    public CommandResultUnbindCreatorV2Impl(UnbindResultPojo pojo) {
        this.pojo = pojo;
    }

    @Override
    public CommandResult create() {
        return UnbindCommandResultInit
                .unbindCommandResult(
                        pojo.getIsDone(),
                        pojo.getMessage(),
                        null);
    }
}