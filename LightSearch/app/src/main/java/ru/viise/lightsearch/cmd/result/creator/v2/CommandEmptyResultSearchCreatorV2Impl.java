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

import java.util.List;

import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.SearchCommandResultInit;
import ru.viise.lightsearch.cmd.result.creator.CommandResultCreator;
import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.SearchRecordInit;
import ru.viise.lightsearch.data.SearchRecordList;
import ru.viise.lightsearch.data.SubdivisionListInit;

public class CommandEmptyResultSearchCreatorV2Impl implements CommandResultCreator {

    @Override
    public CommandResult create() {

        List<SearchRecord> records = new SearchRecordList();
        records.add(SearchRecordInit.searchRecord(
                "",
                "",
                "0.0",
                "",
                SubdivisionListInit.subdivisionList("")));

        return SearchCommandResultInit.searchCommandResult(
                true,
                null,
                records,
                "",
                null);
    }
}
