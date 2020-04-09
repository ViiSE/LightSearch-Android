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

import ru.viise.lightsearch.data.BindRecord;
import ru.viise.lightsearch.data.BindRecordImpl;
import ru.viise.lightsearch.data.pojo.BindCheckPojo;
import ru.viise.lightsearch.data.pojo.BindCheckPojoRawResult;
import ru.viise.lightsearch.data.pojo.BindCheckPojoResult;

public class BindCheckCommandResult implements CommandResult<BindCheckPojo, BindCheckPojoResult> {

    private final Command<BindCheckPojo> lastCommand;
    private final BindCheckPojoRawResult resultRawPojo;
    private BindCheckPojoResult resultPojo;

    public BindCheckCommandResult(Command<BindCheckPojo> lastCommand, BindCheckPojoRawResult resultRawPojo) {
        this.lastCommand = lastCommand;
        this.resultRawPojo = resultRawPojo;
    }

    public BindCheckCommandResult(BindCheckPojoRawResult resultRawPojo) {
        this.lastCommand = null;
        this.resultRawPojo = resultRawPojo;
    }

    @Override
    public boolean isDone() {
        return resultRawPojo.getIsDone();
    }

    @Override
    public BindCheckPojoResult data() {
        if(resultPojo == null) {
            List<BindRecord> bindRecords;

            if (resultRawPojo.getData() != null)
                bindRecords = resultRawPojo.getData()
                        .stream()
                        .map(product -> new BindRecordImpl(product.getName(), product.getId()))
                        .collect(Collectors.toList());
            else
                bindRecords = new ArrayList<>();

            BindCheckPojoResult result = new BindCheckPojoResult();
            result.setIsDone(resultRawPojo.getIsDone());
            result.setMessage(resultRawPojo.getMessage());

            resultPojo = new BindCheckPojoResult();
            resultPojo.setIsDone(resultRawPojo.getIsDone());
            resultPojo.setMessage(resultRawPojo.getMessage());
            resultPojo.setFactoryBarcode(resultRawPojo.getFactoryBarcode());
            resultPojo.setSelected(resultRawPojo.getSelected());
            resultPojo.setRecords(bindRecords);
        }

        return resultPojo;
    }

    @Override
    public Command<BindCheckPojo> lastCommand() {
        return lastCommand;
    }
}
