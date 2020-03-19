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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.SearchCommandResultInit;
import ru.viise.lightsearch.cmd.result.SearchSoftCheckCommandResultInit;
import ru.viise.lightsearch.cmd.result.creator.CommandResultCreator;
import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.SearchRecordInit;
import ru.viise.lightsearch.data.SearchRecordList;
import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.SoftCheckRecordDefaultImpl;
import ru.viise.lightsearch.data.SoftCheckRecordInit;
import ru.viise.lightsearch.data.Subdivision;
import ru.viise.lightsearch.data.SubdivisionInit;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.SubdivisionListInit;
import ru.viise.lightsearch.data.pojo.ProductPojo;
import ru.viise.lightsearch.data.pojo.SearchResultPojo;

public class CommandResultSearchSoftCheckCreatorV2Impl implements CommandResultCreator {

    private final SearchResultPojo pojo;

    public CommandResultSearchSoftCheckCreatorV2Impl(SearchResultPojo pojo) {
        this.pojo = pojo;
    }

    @Override
    public CommandResult create() {
        List<SoftCheckRecord> records = new ArrayList<>();
        if (pojo.getIsDone()) {

            for (ProductPojo rec : pojo.getData()) {
                String barcode = rec.getId();
                String name = rec.getName();
                String price = rec.getPrice();
                String amountUnit = rec.getEi();

                Subdivision subdivision = SubdivisionInit.subdivision(
                        rec.getSubdiv(),
                        rec.getAmount());

                SubdivisionList subdivisions = SubdivisionListInit.subdivisionList(amountUnit);
                subdivisions.addSubdivision(subdivision);

                add(records, new SoftCheckRecordDefaultImpl(name, barcode, price, amountUnit, subdivisions), subdivision);
            }
        }

        return SearchSoftCheckCommandResultInit.searchSoftCheckCommandResult(
                pojo.getIsDone(),
                null,
                records,
                null);
    }

    private void add(List<SoftCheckRecord> records, SoftCheckRecord record, Subdivision subdivision) {
        for(int i = 0; i < records.size(); i++) {
            if(records.get(i).barcode().equals(record.barcode())) {
                records.get(i).subdivisions().addSubdivision(subdivision);
                records.get(i).refreshMaxAmount();
                return;
            }
        }
        records.add(record);
    }
}
