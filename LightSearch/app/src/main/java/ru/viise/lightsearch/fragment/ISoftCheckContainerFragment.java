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

package ru.viise.lightsearch.fragment;

import java.util.List;

import ru.viise.lightsearch.data.SoftCheckRecord;
import ru.viise.lightsearch.find.Findable;

public interface ISoftCheckContainerFragment extends Findable {
    void switchToSoftCheckFragment();
    void switchToOpenSoftCheckFragment();
    void switchToSoftCheckFragment(SoftCheckRecord record);
    void addSoftCheckRecord(SoftCheckRecord record);
    void showResultSearchSoftCheckFragment(List<SoftCheckRecord> records);
    void setCardCode(String cardCode);
    void setSoftCheckBarcode(String barcode);
    void switchToCartFragment(List<SoftCheckRecord> records);
    void refreshCartRecords(List<SoftCheckRecord> cartRecords);
//    void switchToResultSearchSoftCheckFragment(List<SoftCheckRecord> records);
}
