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

import ru.viise.lightsearch.data.pojo.CloseSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.CloseSoftCheckPojoResult;

public class CloseSoftCheckCommandResult implements CommandResult<CloseSoftCheckPojo, CloseSoftCheckPojoResult> {

    private final Command<CloseSoftCheckPojo> lastCommand;
    private final CloseSoftCheckPojoResult resultPojo;

    public CloseSoftCheckCommandResult(
            Command<CloseSoftCheckPojo> lastCommand,
            CloseSoftCheckPojoResult resultPojo) {
        this.lastCommand = lastCommand;
        this.resultPojo = resultPojo;
    }

    public CloseSoftCheckCommandResult(CloseSoftCheckPojoResult resultPojo) {
        this.lastCommand = null;
        this.resultPojo = resultPojo;
    }

    @Override
    public boolean isDone() {
        return resultPojo.getIsDone();
    }

    @Override
    public CloseSoftCheckPojoResult data() {
        return resultPojo;
    }

    @Override
    public Command<CloseSoftCheckPojo> lastCommand() {
        return lastCommand;
    }
}
