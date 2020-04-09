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
import ru.viise.lightsearch.data.pojo.BindPojoResult;

public class BindCommandResult implements CommandResult<BindPojo, BindPojoResult> {

    private final Command<BindPojo> lastCommand;
    private final BindPojoResult resultPojo;

    public BindCommandResult(Command<BindPojo> lastCommand, BindPojoResult resultPojo) {
        this.lastCommand = lastCommand;
        this.resultPojo = resultPojo;
    }

    public BindCommandResult(BindPojoResult resultPojo) {
        this.lastCommand = null;
        this.resultPojo = resultPojo;
    }

    @Override
    public boolean isDone() {
        return resultPojo.getIsDone();
    }

    @Override
    public BindPojoResult data() {
        return resultPojo;
    }

    @Override
    public Command<BindPojo> lastCommand() {
        return lastCommand;
    }
}
