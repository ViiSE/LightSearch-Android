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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ru.viise.lightsearch.cmd.result.BindCommandResultInit;
import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.creator.CommandResultCreator;
import ru.viise.lightsearch.data.BindRecord;
import ru.viise.lightsearch.data.BindRecordDefaultImpl;
import ru.viise.lightsearch.data.pojo.BindCheckResultPojo;

public class CommandResultBindCheckCreatorV2Impl implements CommandResultCreator {

    private final BindCheckResultPojo pojo;

    public CommandResultBindCheckCreatorV2Impl(BindCheckResultPojo pojo) {
        this.pojo = pojo;
    }

    @Override
    public CommandResult create() {
        List<BindRecord> bindRecords;

        if(pojo.getData() != null)
            bindRecords = pojo.getData()
                    .stream()
                    .map(product -> new BindRecordDefaultImpl(product.getName(), product.getId()))
                    .collect(Collectors.toList());
        else
            bindRecords = new ArrayList<>();

        return BindCommandResultInit.bindCheckCommandResult(
                pojo.getIsDone(),
                pojo.getMessage(),
                bindRecords,
                null,
                pojo.getSelected(),
                pojo.getFactoryBarcode());
    }
}
