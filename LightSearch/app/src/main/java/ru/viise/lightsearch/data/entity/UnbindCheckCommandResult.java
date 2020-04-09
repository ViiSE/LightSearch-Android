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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ru.viise.lightsearch.data.UnbindRecord;
import ru.viise.lightsearch.data.UnbindRecordImpl;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojo;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojoRawResult;
import ru.viise.lightsearch.data.pojo.UnbindCheckPojoResult;

public class UnbindCheckCommandResult implements CommandResult<UnbindCheckPojo, UnbindCheckPojoResult> {

    private final Command<UnbindCheckPojo> lastCommand;
    private final UnbindCheckPojoRawResult resultRawPojo;
    private UnbindCheckPojoResult resultPojo;

    public UnbindCheckCommandResult(
            Command<UnbindCheckPojo> lastCommand,
            UnbindCheckPojoRawResult resultRawPojo) {
        this.lastCommand = lastCommand;
        this.resultRawPojo = resultRawPojo;
    }

    public UnbindCheckCommandResult(UnbindCheckPojoRawResult resultRawPojo) {
        this.lastCommand = null;
        this.resultRawPojo = resultRawPojo;
    }

    @Override
    public boolean isDone() {
        return resultRawPojo.getIsDone();
    }

    @Override
    public UnbindCheckPojoResult data() {
        if(resultPojo == null) {
            List<UnbindRecord> unbindRecords;

            if(resultRawPojo.getData() != null)
                unbindRecords = resultRawPojo.getData()
                        .stream()
                        .map(product -> new UnbindRecordImpl(product.getName(), product.getId()))
                        .collect(Collectors.toList());
            else
                unbindRecords = new ArrayList<>();

            resultPojo = new UnbindCheckPojoResult();
            resultPojo.setIsDone(resultRawPojo.getIsDone());
            resultPojo.setMessage(resultRawPojo.getMessage());
            resultPojo.setFactoryBarcode(resultRawPojo.getFactoryBarcode());
            resultPojo.setRecords(unbindRecords);
        }

        return resultPojo;
    }

    @Override
    public Command<UnbindCheckPojo> lastCommand() {
        return lastCommand;
    }
}
