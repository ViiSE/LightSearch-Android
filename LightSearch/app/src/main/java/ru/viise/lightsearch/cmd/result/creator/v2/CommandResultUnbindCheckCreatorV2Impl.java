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

import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.UnbindCommandResultInit;
import ru.viise.lightsearch.cmd.result.creator.CommandResultCreator;
import ru.viise.lightsearch.data.UnbindRecord;
import ru.viise.lightsearch.data.UnbindRecordDefaultImpl;
import ru.viise.lightsearch.data.pojo.UnbindCheckResultPojo;

public class CommandResultUnbindCheckCreatorV2Impl implements CommandResultCreator {

    private final UnbindCheckResultPojo pojo;

    public CommandResultUnbindCheckCreatorV2Impl(UnbindCheckResultPojo pojo) {
        this.pojo = pojo;
    }

    @Override
    public CommandResult create() {
        List<UnbindRecord> unbindRecords;

        if(pojo.getData() != null)
            unbindRecords = pojo.getData()
                    .stream()
                    .map(product -> new UnbindRecordDefaultImpl(product.getName(), product.getId()))
                    .collect(Collectors.toList());
        else
            unbindRecords = new ArrayList<>();

        return UnbindCommandResultInit.unbindCheckCommandResult(
                pojo.getIsDone(),
                pojo.getMessage(),
                unbindRecords,
                pojo.getFactoryBarcode(),
                null);
    }
}
