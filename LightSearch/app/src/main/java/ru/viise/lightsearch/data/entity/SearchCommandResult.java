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

import java.util.List;

import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.SearchRecordImpl;
import ru.viise.lightsearch.data.SearchRecordList;
import ru.viise.lightsearch.data.Subdivision;
import ru.viise.lightsearch.data.SubdivisionImpl;
import ru.viise.lightsearch.data.SubdivisionList;
import ru.viise.lightsearch.data.SubdivisionListImpl;
import ru.viise.lightsearch.data.pojo.ProductPojo;
import ru.viise.lightsearch.data.pojo.SearchPojo;
import ru.viise.lightsearch.data.pojo.SearchPojoRawResult;
import ru.viise.lightsearch.data.pojo.SearchPojoResult;

public class SearchCommandResult implements CommandResult<SearchPojo, SearchPojoResult> {

    private final Command<SearchPojo> lastCommand;
    private final SearchPojoRawResult resultRawPojo;
    private SearchPojoResult resultPojo;

    public SearchCommandResult(Command<SearchPojo> lastCommand, SearchPojoRawResult resultRawPojo) {
        this.lastCommand = lastCommand;
        this.resultRawPojo = resultRawPojo;
    }

    public SearchCommandResult(SearchPojoRawResult resultRawPojo) {
        this.lastCommand = null;
        this.resultRawPojo = resultRawPojo;
    }

    @Override
    public boolean isDone() {
        return resultRawPojo.getIsDone();
    }

    @Override
    public SearchPojoResult data() {
        if(resultPojo == null) {
            List<SearchRecord> records = new SearchRecordList();
            if(resultRawPojo.getIsDone()) {
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

                    records.add(new SearchRecordImpl(name, barcode, price, amountUnit, subdivisions));
                }
            }

            // Убрать последнюю запись постольку, поскольку возможен случай, кода последняя запись
            // будет иметь неполные остатки
            boolean isDropLast = resultRawPojo.getData().size() == 50;
            if(isDropLast) {
                records.remove(records.size() - 1);
            }

            resultPojo = new SearchPojoResult();
            resultPojo.setIsDone(resultRawPojo.getIsDone());
            resultPojo.setMessage(resultRawPojo.getMessage());
            resultPojo.setSubdivision(resultRawPojo.getSubdivision());
            resultPojo.setRecords(records);
        }

        return resultPojo;
    }

    @Override
    public Command<SearchPojo> lastCommand() {
        return lastCommand;
    }
}
