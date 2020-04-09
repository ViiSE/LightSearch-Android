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

import ru.viise.lightsearch.data.pojo.SkladListPojo;
import ru.viise.lightsearch.data.pojo.SkladListPojoRawResult;
import ru.viise.lightsearch.data.pojo.SkladListPojoResult;

public class SkladListCommandResult implements CommandResult<SkladListPojo, SkladListPojoResult> {

    private final Command<SkladListPojo> lastCommand;
    private final SkladListPojoRawResult resultRawPojo;
    private SkladListPojoResult resultPojo;


    public SkladListCommandResult(Command<SkladListPojo> lastCommand, SkladListPojoRawResult resultRawPojo) {
        this.lastCommand = lastCommand;
        this.resultRawPojo = resultRawPojo;
    }

    public SkladListCommandResult(SkladListPojoRawResult resultRawPojo) {
        this.lastCommand = null;
        this.resultRawPojo = resultRawPojo;
    }

    @Override
    public boolean isDone() {
        return resultRawPojo.getIsDone();
    }

    @Override
    public SkladListPojoResult data() {
        if(resultPojo == null) {
            resultPojo = new SkladListPojoResult();
            resultPojo.setIsDone(resultRawPojo.getIsDone());
            resultPojo.setMessage(resultRawPojo.getMessage());
            resultPojo.setSkladList(resultRawPojo.getSkladList() != null ? resultRawPojo.getSkladList().toArray(new String[0]) : new String [0]);
        }

        return resultPojo;
    }

    @Override
    public Command<SkladListPojo> lastCommand() {
        return lastCommand;
    }
}
