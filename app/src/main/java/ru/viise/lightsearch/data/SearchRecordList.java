/*
 * Copyright 2019 ViiSE.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.viise.lightsearch.data;

import java.util.ArrayList;

public class SearchRecordList extends ArrayList<SearchRecord> {

    public SearchRecordList() {
        super();
    }

    @Override
    public boolean add(SearchRecord searchRecord) {
        for(int i = 0; i < super.size(); i++) {
            if(super.get(i).barcode().equals(searchRecord.barcode())) {
                super.get(i).subdivisions().addSubdivision(searchRecord.getSubdivision(0));
                return true;
            }
        }
        return super.add(searchRecord);
    }
}
