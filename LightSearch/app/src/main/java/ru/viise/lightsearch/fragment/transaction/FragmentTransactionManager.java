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

package ru.viise.lightsearch.fragment.transaction;

import java.util.List;

import ru.viise.lightsearch.cmd.result.BindCommandResult;
import ru.viise.lightsearch.cmd.result.UnbindCommandResult;
import ru.viise.lightsearch.data.SearchRecord;
import ru.viise.lightsearch.data.SoftCheckRecord;

public interface FragmentTransactionManager {
    void doAuthorizationFragmentTransaction();
    void doContainerFragmentTransaction(String[] skladArray, String[] TKArray);
    void doResultSearchFragmentTransaction(String title, List<SearchRecord> searchRecords);
    void doCartFragmentTransaction(List<SoftCheckRecord> cartRecords);
    void doContainerFragmentTransactionFromCart();

    void doResultBindFragmentTransaction(String title, BindCommandResult bindCmdRes);
    void doBindingContainerFragmentTransaction(String[] skladArray, String[] TKArray);
    void doBindingContainerFragmentTransactionFromResultBind();
    void doResultUnbindFragmentTransaction(String title, UnbindCommandResult unbindCmdRes);
}
