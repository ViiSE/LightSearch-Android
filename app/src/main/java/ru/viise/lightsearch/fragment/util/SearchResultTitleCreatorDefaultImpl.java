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

package ru.viise.lightsearch.fragment.util;

public class SearchResultTitleCreatorDefaultImpl implements SearchResultTitleCreator {

    private final String subdivision;
    private final String id;

    public SearchResultTitleCreatorDefaultImpl(String subdivision, String id) {
        this.subdivision = subdivision;
        this.id = id;
    }

    @Override
    public String createTitle() {
        if(subdivision.indexOf('(') != -1)
            return subdivision.substring(0, subdivision.indexOf('(')) + " " + id;
        else
            return subdivision + " " + id;
    }
}
