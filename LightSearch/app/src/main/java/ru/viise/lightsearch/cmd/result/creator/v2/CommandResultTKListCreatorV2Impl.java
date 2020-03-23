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
import ru.viise.lightsearch.cmd.result.SkladListCommandResultImpl;
import ru.viise.lightsearch.cmd.result.TKListCommandResultImpl;
import ru.viise.lightsearch.cmd.result.creator.CommandResultCreator;
import ru.viise.lightsearch.data.pojo.TKListResultPojo;

public class CommandResultTKListCreatorV2Impl implements CommandResultCreator {

    private final TKListResultPojo pojo;

    public CommandResultTKListCreatorV2Impl(TKListResultPojo pojo) {
        this.pojo = pojo;
    }

    @Override
    public CommandResult create() {
        return new TKListCommandResultImpl(
                pojo.getIsDone(),
                pojo.getMessage(),
                pojo.getTKList() != null ? pojo.getTKList().toArray(new String[0]) : new String [0]);
    }
}
