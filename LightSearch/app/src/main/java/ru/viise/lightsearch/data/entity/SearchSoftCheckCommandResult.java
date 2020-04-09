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

import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.data.SoftCheckRecordImpl;
import ru.viise.lightsearch.data.Subdivision;
import ru.viise.lightsearch.data.SubdivisionImpl;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.SubdivisionListImpl;
import ru.viise.lightsearch.data.pojo.ProductPojo;
import ru.viise.lightsearch.data.pojo.SearchPojoRawResult;
import ru.viise.lightsearch.data.pojo.SearchSoftCheckPojo;
import ru.viise.lightsearch.data.pojo.SearchSoftCheckPojoResult;

public class SearchSoftCheckCommandResult implements CommandResult<SearchSoftCheckPojo, SearchSoftCheckPojoResult> {

    private final Command<SearchSoftCheckPojo> lastCommand;
    private final SearchPojoRawResult resultRawPojo;
    private SearchSoftCheckPojoResult resultPojo;

    public SearchSoftCheckCommandResult(Command<SearchSoftCheckPojo> lastCommand, SearchPojoRawResult resultRawPojo) {
        this.lastCommand = lastCommand;
        this.resultRawPojo = resultRawPojo;
    }

    public SearchSoftCheckCommandResult(SearchPojoRawResult resultRawPojo) {
        this.lastCommand = null;
        this.resultRawPojo = resultRawPojo;
    }

    @Override
    public boolean isDone() {
        return resultRawPojo.getIsDone();
    }

    @Override
    public SearchSoftCheckPojoResult data() {
        if(resultPojo == null) {
            List<SoftCheckRecord> records = new ArrayList<>();
            if (resultRawPojo.getIsDone()) {

                for (ProductPojo rec : resultRawPojo.getData()) {
                    String barcode = rec.getId();
                    String name = rec.getName();
                    String price = rec.getPrice();
                    String amountUnit = rec.getEi();

                    Subdivision subdivision = new SubdivisionImpl(
                            rec.getSubdiv(),
                            rec.getAmount());

                    SubdivisionList subdivisions = new SubdivisionListImpl(amountUnit);
                    subdivisions.addSubdivision(subdivision);

                    add(records, new SoftCheckRecordImpl(name, barcode, price, amountUnit, subdivisions), subdivision);
                }
            }

            // Убрать последнюю запись постольку, поскольку возможен случай, кода последняя запись
            // будет иметь неполные остатки
            boolean isDropLast = resultRawPojo.getData().size() == 50;
            if(isDropLast) {
                records.remove(records.size() - 1);
            }

            resultPojo = new SearchSoftCheckPojoResult();
            resultPojo.setIsDone(resultRawPojo.getIsDone());
            resultPojo.setMessage(resultRawPojo.getMessage());
            resultPojo.setSubdivision(resultRawPojo.getSubdivision());
            resultPojo.setRecords(records);
        }

        return resultPojo;
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

    @Override
    public Command<SearchSoftCheckPojo> lastCommand() {
        return lastCommand;
    }
}
