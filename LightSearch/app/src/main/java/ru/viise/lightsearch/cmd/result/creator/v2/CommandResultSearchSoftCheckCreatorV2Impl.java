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

import ru.viise.lightsearch.cmd.result.CommandResult;
import ru.viise.lightsearch.cmd.result.SearchSoftCheckCommandResultInit;
import ru.viise.lightsearch.cmd.result.creator.CommandResultCreator;
import ru.viise.lightsearch.data.SoftCheckRecord;
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
        if(pojo.getIsDone()) {

            ProductPojo product = pojo.getData().get(0);
            String barcode = product.getId();
            String name = product.getName();
            String price = product.getPrice();
            String amountUnit = product.getEi();

            SubdivisionList subdivisions = SubdivisionListInit.subdivisionList(amountUnit);

            for (ProductPojo rec : pojo.getData()) {
                Subdivision subdivision = SubdivisionInit.subdivision(
                        rec.getSubdiv(),
                        rec.getAmount());
                subdivisions.addSubdivision(subdivision);
            }
            SoftCheckRecord record = SoftCheckRecordInit.softCheckRecord(
                    name,
                    barcode,
                    price,
                    amountUnit,
                    subdivisions);

            return SearchSoftCheckCommandResultInit.searchSoftCheckCommandResult(
                    pojo.getIsDone(),
                    null,
                    record,
                    null);
        } else
            return SearchSoftCheckCommandResultInit.searchSoftCheckCommandResult(
                            pojo.getIsDone(),
                            pojo.getMessage(),
                            null,
                            null);
    }
}
