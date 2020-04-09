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

import ru.viise.lightsearch.data.pojo.TKListPojo;
import ru.viise.lightsearch.data.pojo.TKListPojoRawResult;
import ru.viise.lightsearch.data.pojo.TKListPojoResult;

public class TKListCommandResult implements CommandResult<TKListPojo, TKListPojoResult> {

    private final Command<TKListPojo> lastCommand;
    private final TKListPojoRawResult resultRawPojo;
    private TKListPojoResult resultPojo;

    public TKListCommandResult(Command<TKListPojo> lastCommand, TKListPojoRawResult resultRawPojo) {
        this.lastCommand = lastCommand;
        this.resultRawPojo = resultRawPojo;
    }

    public TKListCommandResult(TKListPojoRawResult resultRawPojo) {
        this.lastCommand = null;
        this.resultRawPojo = resultRawPojo;
    }

    @Override
    public boolean isDone() {
        return resultRawPojo.getIsDone();
    }

    @Override
    public TKListPojoResult data() {
        if(resultPojo == null) {
            resultPojo = new TKListPojoResult();
            resultPojo.setIsDone(resultRawPojo.getIsDone());
            resultPojo.setMessage(resultRawPojo.getMessage());
            resultPojo.setTKList(resultRawPojo.getTKList() != null ? resultRawPojo.getTKList().toArray(new String[0]) : new String [0]);
        }

        return resultPojo;
    }

    @Override
    public Command<TKListPojo> lastCommand() {
        return lastCommand;
    }
}
